package com.ty.tsvetilian.a21connect.Services;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.telephony.TelephonyManager;

import com.ty.tsvetilian.a21connect.Models.Device;
import com.ty.tsvetilian.a21connect.Utility.ConnectionProperties;
import com.ty.tsvetilian.a21connect.Utility.DeviceName;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.realm.Realm;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CallReceivedService extends JobIntentService {

    private Socket socket;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String callerNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String callerName = getCallerName(callerNumber);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            try {
                emit(callerName, callerNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getCallerName(String number) {
        String returnName = null;
        ContentResolver getCr = getApplicationContext().getContentResolver();
        String[] wantedData = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Uri crUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = getCr.query(crUri, wantedData, null, null, null);

        if (cursor == null) return null;

        if (cursor.moveToFirst())
            returnName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));

        if (cursor != null && !cursor.isClosed()) cursor.close();

        return returnName;
    }

    private void emit(String callerName, String callerNumber) throws URISyntaxException, JSONException {
        Realm db = Realm.getDefaultInstance();

        Device currentDevice = db.where(Device.class).findFirst();

        if (currentDevice == null || !currentDevice.isSyncEnabled()) {
            return;
        }

        String validName;

        if (callerName == null) {
            validName = callerNumber;
        } else validName = callerName;


        JSONObject dataObject = new JSONObject();
        dataObject.put("number", validName);
        dataObject.put("device", DeviceName.getDeviceName());

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.query = "auth=" + currentDevice.getAuthToken();
        opts.reconnection = false;
        opts.timeout = 15000;

        socket = IO.socket(ConnectionProperties.PROTOCOL + currentDevice.getIp() + ConnectionProperties.PORT, opts);

        socket.emit("incomingCall", dataObject).once("done", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.disconnect();
            }
        });
        socket.connect();

    }
}

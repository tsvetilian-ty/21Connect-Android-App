package com.ty.tsvetilian.a21connect.Services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.util.Log;

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


public class CustomNotificationService extends JobIntentService {

    private Socket socket;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String notificationPkg = intent.getExtras().getString("notification.pkg");
        String notificationTitle = intent.getExtras().getString("notification.title");
        String notificationDescription = intent.getExtras().getString("notification.description");

        try {
            emit(notificationPkg ,notificationTitle, notificationDescription);
        } catch (Exception e) {
            Log.e("CNS", "Can't emit event");
        }
    }

    private void emit(String pkg, String title, String description) throws URISyntaxException, JSONException {
        Realm db = Realm.getDefaultInstance();
        Device currentDevice = db.where(Device.class).findFirst();

        if (currentDevice == null || !currentDevice.isSyncEnabled()) {
            return;
        }

        JSONObject dataObject = new JSONObject();
        dataObject.put("pkg", pkg);
        dataObject.put("title", title);
        dataObject.put("content", description);
        dataObject.put("device", DeviceName.getDeviceName());

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.query = "auth=" + currentDevice.getAuthToken();
        opts.reconnection = false;
        opts.timeout = 5000;

        socket = IO.socket(ConnectionProperties.PROTOCOL + currentDevice.getIp() + ConnectionProperties.PORT, opts);
        socket.emit("newNotification", dataObject);
        socket.once("done", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.disconnect();
            }
        });
        socket.connect();
    }
}

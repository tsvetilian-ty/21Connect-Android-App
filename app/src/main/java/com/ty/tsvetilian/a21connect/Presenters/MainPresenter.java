package com.ty.tsvetilian.a21connect.Presenters;

import com.ty.tsvetilian.a21connect.Contracts.MainContract;
import com.ty.tsvetilian.a21connect.Models.Device;
import com.ty.tsvetilian.a21connect.Utility.ConnectionProperties;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.realm.Realm;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mContextView;
    private Device currentDevice;
    private Realm DB;
    private Socket socket;

    @Override
    public void attach(MainContract.View view) {
        mContextView = view;
        DB = Realm.getDefaultInstance();
    }

    @Override
    public void detach() {
        mContextView = null;
        DB = null;
    }

    @Override
    public Device getDevice() {

        if (currentDevice == null) {
            currentDevice = DB.where(Device.class).findFirst();
        }

        return currentDevice;
    }

    @Override
    public void addDevice(String host, final String authToken) {
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.query = "auth=" + authToken;
            opts.reconnection = false;
            opts.timeout = 15000;

            socket = IO.socket(ConnectionProperties.PROTOCOL + host + ConnectionProperties.PORT, opts);
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                }
            }).on("info", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    final JSONObject obj = (JSONObject) args[0];
                    try {
                        addToDb(new Device(obj.getString("hash"),
                                obj.getString("host_name"),
                                obj.getString("connection_ip"),
                                obj.getInt("system_memory_gb"),
                                obj.getString("system_cpu"),
                                obj.getString("os"),
                                authToken,
                                true));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }).once("done", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    socket.disconnect();
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mContextView.addedDeviceResult(null);
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDevice() {
        Realm db = Realm.getDefaultInstance();

        db.beginTransaction();
        Device tmpDevice = db.where(Device.class).findFirst();
        tmpDevice.deleteFromRealm();
        db.commitTransaction();
    }

    @Override
    public void setDeviceSyncStatus(boolean status) {
        Realm db = Realm.getDefaultInstance();

        db.beginTransaction();
        Device tmpDevice = db.where(Device.class).findFirst();
        tmpDevice.setSyncEnabled(status);
        db.insertOrUpdate(tmpDevice);
        db.commitTransaction();
    }

    private void addToDb(final Device device) {
        Realm db = Realm.getDefaultInstance();

        db.beginTransaction();
        db.copyToRealm(device);
        db.commitTransaction();

        mContextView.addedDeviceResult(device);

    }
}

package com.ty.tsvetilian.a21connect.Presenters;

import com.ty.tsvetilian.a21connect.Contracts.MainContract;
import com.ty.tsvetilian.a21connect.Models.Device;

import io.realm.Realm;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mContextView;
    private Device currentDevice;
    private Realm DB;

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
        mContextView.addedDeviceResult(new Device("hash", "host", "ip", 16, "cpu", "win", "auth", true));
    }

    @Override
    public void deleteDevice() {
        //TODO
    }

    @Override
    public void setDeviceSyncStatus(boolean status) {
        //TODO
    }
}

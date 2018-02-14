package com.ty.tsvetilian.a21connect.Contracts;

import com.ty.tsvetilian.a21connect.Models.Device;

public interface MainContract {
    interface View extends BaseContract.View<Presenter> {
        // Return null if device can't be added
        void addedDeviceResult(Device device);
    }

    interface Presenter extends BaseContract.Presenter<View> {
        Device getDevice();
        void addDevice(String host, String authToken);

        void deleteDevice();

        void setDeviceSyncStatus(boolean status);
    }
}

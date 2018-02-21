package com.ty.tsvetilian.a21connect.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ty.tsvetilian.a21connect.Services.ChargingBatteryService;

public class ChargingBatteryReceiver extends BroadcastReceiver {

    private final int JOB_ID = 63;

    @Override
    public void onReceive(Context context, Intent intent) {
        ChargingBatteryService.enqueueWork(context, ChargingBatteryService.class, JOB_ID, intent);
    }
}

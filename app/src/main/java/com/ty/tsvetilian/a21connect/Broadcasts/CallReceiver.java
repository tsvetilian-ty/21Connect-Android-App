package com.ty.tsvetilian.a21connect.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ty.tsvetilian.a21connect.Services.CallReceivedService;

public class CallReceiver extends BroadcastReceiver{

    private static final int JOB_ID = 90;

    @Override
    public void onReceive(Context context, Intent intent) {
        CallReceivedService.enqueueWork(context, CallReceivedService.class, JOB_ID, intent);
    }
}

package com.ty.tsvetilian.a21connect.Broadcasts;


import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.ty.tsvetilian.a21connect.Services.CustomNotificationService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NotificationUtilityService extends NotificationListenerService {

    private static final int JOB_ID = 19;

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Bundle sbnExtras = sbn.getNotification().extras;
        String pkgName = sbn.getPackageName();
        String notificationTitle = sbnExtras.getString("android.title");
        String notificationDescription = sbnExtras.getString("android.text");

        Set<String> disabledPkgs = new HashSet<>(Arrays.asList("com.android.dialer", "android", "com.android.providers.downloads"));

        if(disabledPkgs.contains(pkgName)){
            return;
        }

        if (notificationTitle == null) {
            if (sbnExtras.get("android.title.big") != null) {
                notificationTitle = sbnExtras.get("android.title.big").toString();
            } else {
                return;
            }
        }

        if (notificationDescription == null) {
            if(sbnExtras.get("android.bigText") != null) {
                notificationDescription = sbnExtras.get("android.bigText").toString();
            } else {
                if (sbnExtras.get("android.summaryText") != null) {
                    notificationDescription = sbnExtras.get("android.summaryText").toString();
                } else {
                    return;
                }
            }
        }


        Intent mCustomNotification = new Intent();
        mCustomNotification.putExtra("notification.pkg", pkgName);
        mCustomNotification.putExtra("notification.title", notificationTitle);
        mCustomNotification.putExtra("notification.description", notificationDescription);

        CustomNotificationService.enqueueWork(getApplicationContext(), CustomNotificationService.class, JOB_ID, mCustomNotification);
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        return;
    }
}

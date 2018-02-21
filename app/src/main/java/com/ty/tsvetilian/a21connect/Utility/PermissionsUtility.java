package com.ty.tsvetilian.a21connect.Utility;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.Toast;

import com.ty.tsvetilian.a21connect.Views.MainActivity;

public class PermissionsUtility {

    public static void checkNotificationListener(final MainActivity mainActivity) {
        boolean haveNotificationPermissions = false;
        for (String service : NotificationManagerCompat.getEnabledListenerPackages(mainActivity)) {
            if (service.equals(mainActivity.getPackageName()))
                haveNotificationPermissions = true;
        }
        if (!haveNotificationPermissions) {
            AlertDialog.Builder missingNotificationPermissions = new AlertDialog.Builder(mainActivity)
                    .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent notificationIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                            mainActivity.startActivity(notificationIntent);
                        }
                    })
                    .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(mainActivity, "You denied access to the notifications :(", Toast.LENGTH_SHORT).show();
                        }
                    });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                missingNotificationPermissions.setMessage(Html.fromHtml("Allow " + "<b>" + "21Connect" + "</b>" + "  to access your notifications?", Html.FROM_HTML_MODE_LEGACY));
            } else {
                missingNotificationPermissions.setMessage(Html.fromHtml("Allow " + "<b>" + "21Connect" + "</b>" + "  to access your notifications?"));
            }

            missingNotificationPermissions.show();
        }
    }

    public static void checkPermissions(MainActivity mainActivity, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mainActivity.checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED &&
                    mainActivity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                mainActivity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE
                }, permissionCode);
            }
        } else {
            return;
        }
    }
}

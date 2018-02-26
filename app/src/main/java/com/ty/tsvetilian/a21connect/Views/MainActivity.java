package com.ty.tsvetilian.a21connect.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ty.tsvetilian.a21connect.Contracts.FragmentEventListener;
import com.ty.tsvetilian.a21connect.Contracts.MainContract;
import com.ty.tsvetilian.a21connect.Models.Device;
import com.ty.tsvetilian.a21connect.Presenters.MainPresenter;
import com.ty.tsvetilian.a21connect.R;
import com.ty.tsvetilian.a21connect.Utility.ApplicationEvents;
import com.ty.tsvetilian.a21connect.Utility.PermissionsUtility;
import com.ty.tsvetilian.a21connect.Views.Fragments.DeviceFragment;
import com.ty.tsvetilian.a21connect.Views.Fragments.NoDeviceFragment;


public class MainActivity extends AppCompatActivity implements MainContract.View, FragmentEventListener {

    private MainContract.Presenter mainPresenter;

    private final int QR_ACTIVATION_CODE = 100;
    private final int CAMERA_PERMISSION_CODE = 200;
    private final int CALL_PERMISSION_CODE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter();
        mainPresenter.attach(this);
        initializeFragment();
        PermissionsUtility.checkPermissions(this, CALL_PERMISSION_CODE);
        PermissionsUtility.checkNotificationListener(this);
    }

    private void initializeFragment() {
        Fragment fragmentLoader;

        if (mainPresenter.getDevice() == null) {
            fragmentLoader = new NoDeviceFragment();
        } else {
            fragmentLoader = new DeviceFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_activity_view, fragmentLoader)
                .commit();
    }

    @Override
    public void fragmentEvents(ApplicationEvents eventId) {
        switch (eventId) {
            case CONNECT_TO_DEVICE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        showScanner();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
                    }
                } else {
                    showScanner();
                }
                break;
            case DEVICE_SYNC_ENABLE:
                mainPresenter.setDeviceSyncStatus(true);
                break;
            case DEVICE_SYNC_DISABLE:
                mainPresenter.setDeviceSyncStatus(false);
                break;
            case DEVICE_DISCONNECT:
                mainPresenter.deleteDevice();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.main_activity_view, new NoDeviceFragment())
                        .commit();
                break;
        }
    }

    private void showScanner() {
        Intent startQrScanner = new Intent(MainActivity.this, CodeScannerActivity.class);
        startActivityForResult(startQrScanner, QR_ACTIVATION_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == QR_ACTIVATION_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                mainPresenter.addDevice(data.getData().getHost(), data.getData().getQueryParameter("token"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showScanner();
            } else {
                Toast.makeText(this, "You denied the use of camera :(", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                Toast.makeText(this, "You denied access to contacts :(", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addedDeviceResult(Device device) {
        if (device == null) {
            AlertDialog cantConnectDialog = new AlertDialog.Builder(this)
                    .setTitle("Can't add this device")
                    .setMessage("Check your internet connection")
                    .setPositiveButton("OK", null).show();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_view, new DeviceFragment())
                    .commit();
        }
    }
}

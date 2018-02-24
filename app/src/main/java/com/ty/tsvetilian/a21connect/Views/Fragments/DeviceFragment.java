package com.ty.tsvetilian.a21connect.Views.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ty.tsvetilian.a21connect.Contracts.FragmentEventListener;
import com.ty.tsvetilian.a21connect.Models.Device;
import com.ty.tsvetilian.a21connect.R;
import com.ty.tsvetilian.a21connect.Utility.ApplicationEvents;
import com.ty.tsvetilian.a21connect.Utility.ConnectionProperties;

import io.realm.Realm;

public class DeviceFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    private FragmentEventListener eventListener;
    private Context activityContext;
    private Device currentDevice;

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityContext = context;
        eventListener = (FragmentEventListener) activityContext;
        currentDevice = getCurrentDevice();
    }

    private Device getCurrentDevice() {
        Realm db = Realm.getDefaultInstance();
        return db.where(Device.class).findFirst();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View deviceView = inflater.inflate(R.layout.fragment_device, container, false);

        bindViews(deviceView);

        return deviceView;
    }


    private void bindViews(View deviceView) {
        BottomNavigationView bottomActions = deviceView.findViewById(R.id.bottom_controls);
        bottomActions.setOnNavigationItemSelectedListener(this);

        MenuItem syncItem = bottomActions.getMenu().findItem(R.id.sync);

        if (getCurrentDevice().isSyncEnabled()) {
            syncItem.setIcon(R.drawable.ic_enable_sync_24dp);
            syncItem.setTitle(R.string.sync_enabled);
        } else {
            syncItem.setIcon(R.drawable.ic_disable_sync_24dp);
            syncItem.setTitle(R.string.sync_disabled);
        }

        TextView deviceNameView = deviceView.findViewById(R.id.device_name_ram);
        String nameSetup = String.format(getString(R.string.device_name_and_ram), currentDevice.getHostName(), currentDevice.getRam());
        deviceNameView.setText(nameSetup);

        ImageView deviceImg = deviceView.findViewById(R.id.device_os);

        Picasso.with(activityContext)
                .load(String.format("%s%s%s/screen",
                        ConnectionProperties.PROTOCOL,
                        currentDevice.getIp(),
                        ConnectionProperties.PORT))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .placeholder(R.drawable.ic_os_default)
                .error(R.drawable.ic_os_default)
                .into(deviceImg);

        TextView deviceCpu = deviceView.findViewById(R.id.device_cpu);
        String cpuSetup = String.format(getString(R.string.device_cpu), currentDevice.getCpu());
        deviceCpu.setText(cpuSetup);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                if (getCurrentDevice().isSyncEnabled()) {
                    item.setIcon(R.drawable.ic_disable_sync_24dp);
                    item.setTitle(R.string.sync_disabled);
                    Toast.makeText(activityContext, R.string.sync_disabled_message, Toast.LENGTH_SHORT).show();
                    eventListener.fragmentEvents(ApplicationEvents.DEVICE_SYNC_DISABLE);
                } else {
                    item.setIcon(R.drawable.ic_enable_sync_24dp);
                    item.setTitle(R.string.sync_enabled);
                    Toast.makeText(activityContext, R.string.sync_enabled_message, Toast.LENGTH_SHORT).show();
                    eventListener.fragmentEvents(ApplicationEvents.DEVICE_SYNC_ENABLE);
                }
                break;
            case R.id.disconnect:
                AlertDialog disconnectDialog = new AlertDialog.Builder(activityContext)
                        .setTitle(R.string.title_disconnect_dialog)
                        .setMessage(R.string.message_disconnect_dialog)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventListener.fragmentEvents(ApplicationEvents.DEVICE_DISCONNECT);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
        }
        return false;
    }
}

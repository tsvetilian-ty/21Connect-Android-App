package com.ty.tsvetilian.a21connect.Views.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ty.tsvetilian.a21connect.Contracts.FragmentEventListener;
import com.ty.tsvetilian.a21connect.R;
import com.ty.tsvetilian.a21connect.Utility.ApplicationEvents;


public class NoDeviceFragment extends Fragment {

    private final int CAMERA_PERMISSION_CODE = 200;

    private Button connectToDevice;
    private FragmentEventListener eventListener;

    public NoDeviceFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        eventListener = (FragmentEventListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View noDeviceView = inflater.inflate(R.layout.fragment_no_device, container, false);

        connectToDevice = noDeviceView.findViewById(R.id.connectBtn);

        connectToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventListener.fragmentEvents(ApplicationEvents.CONNECT_TO_DEVICE);
            }
        });

        return noDeviceView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

package com.ty.tsvetilian.a21connect.Views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ty.tsvetilian.a21connect.Contracts.MainContract;
import com.ty.tsvetilian.a21connect.Models.Device;
import com.ty.tsvetilian.a21connect.Presenters.MainPresenter;
import com.ty.tsvetilian.a21connect.R;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private MainContract.Presenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter();
        mainPresenter.attach(this);
        mainPresenter.addDevice("test", "token_test");
    }

    @Override
    public void addedDeviceResult(Device device) {
        Log.d("DEVICE", device.getHostName());
    }
}

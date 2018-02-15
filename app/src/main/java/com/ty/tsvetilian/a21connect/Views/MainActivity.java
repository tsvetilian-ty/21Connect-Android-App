package com.ty.tsvetilian.a21connect.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        Button scanBtn = findViewById(R.id.button);
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startQrScanner = new Intent(MainActivity.this, CodeScannerActivity.class);
                startActivityForResult(startQrScanner, 123);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 123) {
            if (resultCode == RESULT_OK && data != null) {
                mainPresenter.addDevice(data.getData().getHost(), data.getData().getQueryParameter("token"));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void addedDeviceResult(Device device) {
        Log.d("DEVICE", device.getHostName());
    }
}

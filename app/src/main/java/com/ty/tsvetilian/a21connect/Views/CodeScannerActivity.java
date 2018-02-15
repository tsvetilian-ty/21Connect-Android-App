package com.ty.tsvetilian.a21connect.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.ty.tsvetilian.a21connect.Contracts.CodeScannerContract;
import com.ty.tsvetilian.a21connect.Presenters.CodeScannerPresenter;
import com.ty.tsvetilian.a21connect.R;

import java.io.IOException;


public class CodeScannerActivity extends AppCompatActivity implements CodeScannerContract.View {

    private CodeScannerContract.Presenter mPresenter;
    private Uri mQrConnectionCode;
    private AlertDialog mInformationDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_scanner);
        mPresenter = new CodeScannerPresenter();
        mPresenter.attach(this);
        initQrScanner();
        mInformationDialog = new AlertDialog.Builder(this)
                .setTitle("Scan the code")
                .setPositiveButton("OK", null)
                .show();
    }

    private void initQrScanner() {
        final SurfaceView qrCodeView = findViewById(R.id.qr_camera);

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        final CameraSource cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedFps(15.0f)
                .setRequestedPreviewSize(720, 576)
                .build();

        qrCodeView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(qrCodeView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA ERROR CAN'T SCAN", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> detectedQrCodes = detections.getDetectedItems();

                if (detectedQrCodes.size() != 0) {
                    mQrConnectionCode = Uri.parse(detectedQrCodes.valueAt(0).displayValue);
                    String scheme = mQrConnectionCode.getScheme();
                    mPresenter.checkScheme(scheme);
                }
            }
        });

    }

    @Override
    public void onSuccessfulScan() {
        mInformationDialog.dismiss();
        returnResult();
    }

    private void returnResult() {
        Intent resultData = new Intent();
        resultData.setData(mQrConnectionCode);
        mPresenter.detach();
        setResult(RESULT_OK, resultData);
        finish();
    }

    @Override
    public void onInvalidScan() {
        mInformationDialog.dismiss();
        mPresenter.detach();
        finish();
    }

    @Override
    protected void onResume() {
        mPresenter.attach(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        mPresenter = null;
        super.onDestroy();
    }
}

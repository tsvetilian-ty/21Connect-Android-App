package com.ty.tsvetilian.a21connect.Presenters;

import com.ty.tsvetilian.a21connect.Contracts.CodeScannerContract;


public class CodeScannerPresenter implements CodeScannerContract.Presenter {


    private CodeScannerContract.View mContext;

    @Override
    public void attach(CodeScannerContract.View view) {
        mContext = view;
    }

    @Override
    public void detach() {
        mContext = null;
    }

    @Override
    public void checkScheme(String scheme) {
        if(scheme.equals("connect21")){
            mContext.onSuccessfulScan();
        } else {
            mContext.onInvalidScan();
        }
    }
}

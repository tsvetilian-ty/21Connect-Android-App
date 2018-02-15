package com.ty.tsvetilian.a21connect.Contracts;


public interface CodeScannerContract {
    interface View extends BaseContract.View<Presenter>{
        void onSuccessfulScan();
        void onInvalidScan();
    }
    interface Presenter extends BaseContract.Presenter<View>{
        void checkScheme(String scheme);
    }
}

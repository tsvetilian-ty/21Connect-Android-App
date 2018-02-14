package com.ty.tsvetilian.a21connect;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
//        RealmConfiguration config = new RealmConfiguration.Builder().name("connect21.realm").build();
        RealmConfiguration config = new RealmConfiguration.Builder().inMemory().name("connect21.realm").build();
        Realm.setDefaultConfiguration(config);
    }
}
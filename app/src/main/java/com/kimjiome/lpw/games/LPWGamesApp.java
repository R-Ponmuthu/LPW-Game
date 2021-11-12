package com.kimjiome.lpw.games;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class LPWGamesApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
    }
}

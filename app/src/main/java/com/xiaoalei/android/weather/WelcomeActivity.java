package com.xiaoalei.android.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.xiaoalei.android.weather.base.BaseActivity;
import com.xiaoalei.android.library.util.system.StatusBarHelper;
import com.xiaoalei.android.weather.feature.home.MainActivity;
import com.xiaoalei.android.weather.data.db.CityDatabaseHelper;
import com.xiaoalei.android.weather.data.preference.PreferenceHelper;
import com.xiaoalei.android.weather.data.preference.WeatherSettings;

import java.io.InvalidClassException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WelcomeActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarHelper.statusBarLightMode(this);

        Observable.just(initAppData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> gotoMainPage());
    }

    private void gotoMainPage() {
        //跳转太快了 等两秒
        SystemClock.sleep(2000);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化应用数据
     */
    private String initAppData() {
        PreferenceHelper.loadDefaults();
        //TODO 测试，待删除
        if (PreferenceHelper.getSharedPreferences().getBoolean(WeatherSettings.SETTINGS_FIRST_USE.getId(), false)) {
            try {
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_CURRENT_CITY_ID, "101020100");
                PreferenceHelper.savePreference(WeatherSettings.SETTINGS_FIRST_USE, false);
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
        CityDatabaseHelper.importCityDB();
        return null;
    }
}

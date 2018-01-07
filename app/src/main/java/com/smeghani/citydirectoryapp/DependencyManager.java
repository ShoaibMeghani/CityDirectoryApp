package com.smeghani.citydirectoryapp;

import android.content.Context;

import com.google.gson.Gson;
import com.smeghani.citydirectoryapp.repository.CityListRepository;

/**
 * Created by shoaibmeghani on 07/01/2018.
 *
 * This class is to manage dependencies across the application. Since the app has only 1 module, this class
 * will suffice.
 */

public class DependencyManager {

    private Context applicationContext;
    private Gson gson;
    private CityListRepository cityListRepository;

    private static DependencyManager instance;

    private DependencyManager() {

    }

    public static DependencyManager getInstance() {
        if (instance == null)
            instance = new DependencyManager();

        return instance;
    }

    public void setApplicationContext(Context context) {
        this.applicationContext = context;
    }

    public Context provideApplicationContext() {
        return this.applicationContext;
    }

    public Gson provideGson(){
        if (gson == null)
            gson = new Gson();

        return this.gson;
    }

    public CityListRepository provideCityListRepo(){
        if (cityListRepository == null)
            cityListRepository = new CityListRepository(this);

        return this.cityListRepository;
    }


}

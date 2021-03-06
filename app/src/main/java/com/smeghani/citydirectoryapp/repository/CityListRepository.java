package com.smeghani.citydirectoryapp.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.smeghani.citydirectoryapp.DependencyManager;
import com.smeghani.citydirectoryapp.model.City;
import com.smeghani.citydirectoryapp.util.JsonReaderHelper;
import com.smeghani.citydirectoryapp.view.OnCityListDataListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public class CityListRepository {

    private Context context;
    private Gson gson;
    private List<City> cityList;

    public CityListRepository(DependencyManager manager) {
        context = manager.provideApplicationContext();
        gson = manager.provideGson();
    }

    public void fetchCityList(final OnCityListDataListener listener, final String fileName) {

        //check if data already cached, no need to fetch
        if (cityList != null) {
            listener.onCityListInitialized(cityList);
        } else {
            //fetching and preparing the data in background to keep app responsive.
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    String data = JsonReaderHelper.loadJsonFromAssets(context, fileName);

                    prepareDataFromJson(data);

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    listener.onCityListInitialized(cityList);
                }
            }.execute();
        }

    }

    public void prepareDataFromJson(String data) {


        JsonParser parser = new JsonParser();
        JsonElement tradeElement = parser.parse(data);
        JsonArray cityJsonArr = tradeElement.getAsJsonArray();

        data = null;
        tradeElement = null;
        parser = null;

        cityList = new ArrayList<>(cityJsonArr.size());

        for (JsonElement element : cityJsonArr) {
            JsonObject cityJsonObj = element.getAsJsonObject();
            City city = gson.fromJson(cityJsonObj, City.class);
            cityList.add(city);
        }

        sortList(cityList);

    }

    /**
     * This methods sorts the list ignoring case.
     */
    private void sortList(List<City> cityList) {

        Collections.sort(cityList, new Comparator<City>() {
            @Override
            public int compare(City s1, City s2) {
                int compareVal = s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());

                if (compareVal == 0)
                    compareVal = s1.getCountry().compareTo(s2.getCountry());

                return compareVal;
            }
        });

    }

    public List<City> filterCityList(int startIndex, int endIndex) {
        return cityList.subList(startIndex, endIndex + 1);
    }

    public List<City> getCityList() {
        return cityList;
    }


}

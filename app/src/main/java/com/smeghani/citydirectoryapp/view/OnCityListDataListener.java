package com.smeghani.citydirectoryapp.view;

import com.smeghani.citydirectoryapp.model.City;

import java.util.List;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public interface OnCityListDataListener {
    void onCityListInitialized(List<City> cityList);
    void onCityListFiltered(List<City> filteredCityList);
}

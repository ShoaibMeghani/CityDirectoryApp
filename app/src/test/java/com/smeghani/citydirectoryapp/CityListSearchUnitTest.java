package com.smeghani.citydirectoryapp;


import com.smeghani.citydirectoryapp.controller.CityListController;
import com.smeghani.citydirectoryapp.model.City;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public class CityListSearchUnitTest {

    private List<City> cityList;
    private CityListController cityListController;


    public CityListSearchUnitTest(){
        cityListController = new CityListController(DependencyManager.getInstance());

        String data = "[\n" +
                "    {\n" +
                "        \"country\": \"US\",\n" +
                "        \"name\": \"Arizona\",\n" +
                "        \"_id\": 707860,\n" +
                "        \"coord\": {\n" +
                "            \"lon\": 34.283333,\n" +
                "            \"lat\": 44.549999\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"country\": \"US\",\n" +
                "        \"name\": \"Sydney\",\n" +
                "        \"_id\": 519188,\n" +
                "        \"coord\": {\n" +
                "            \"lon\": 37.666668,\n" +
                "            \"lat\": 55.683334\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"country\": \"US\",\n" +
                "        \"name\": \"anaheim\",\n" +
                "        \"_id\": 1283378,\n" +
                "        \"coord\": {\n" +
                "            \"lon\": 84.633331,\n" +
                "            \"lat\": 28\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"country\": \"US\",\n" +
                "        \"name\": \"Alabama\",\n" +
                "        \"_id\": 1270260,\n" +
                "        \"coord\": {\n" +
                "            \"lon\": 76,\n" +
                "            \"lat\": 29\n" +
                "        }\n" +
                "    },\n" +
                "    {\n" +
                "        \"country\": \"AU\",\n" +
                "        \"name\": \"Albuquerque\",\n" +
                "        \"_id\": 1270260,\n" +
                "        \"coord\": {\n" +
                "            \"lon\": 76,\n" +
                "            \"lat\": 29\n" +
                "        }\n" +
                "    }\n" +
                "]";


        DependencyManager.getInstance().provideCityListRepo().prepareDataFromJson(data);
        cityList = DependencyManager.getInstance().provideCityListRepo().getCityList();
        cityListController.createIndexData((ArrayList<City>) cityList);
    }



    @Test
    public void checkCaseSensitivity(){
        List<City> list = cityListController.filterList("a");

        int sizeResult = list.size();
        assertEquals(sizeResult,4);


    }

    @Test
    public void checkInCompleteNameSearch(){
        List<City> list = cityListController.filterList("S");

        int sizeResult = list.size();
        assertEquals(sizeResult,1);

        City city =list.get(0);
        assertEquals(city.getName(),"Sydney");

    }

    @Test
    public void checkEmptyQueryStringSearch(){
        //should return all values
        List<City> list = cityListController.filterList("");
        int sizeResult = list.size();
        assertEquals(sizeResult,5);
    }


    @Test
    public void checkNotExistingQuerySearch(){
        cityListController.filterList("A");
        cityListController.filterList("As");
        cityListController.filterList("Aso");


        List<City> list = cityListController.filterList("Aslo");

        assertEquals(list,null);
    }

    @Test
    public void checkCompleteNameSearch(){

        cityListController.filterList("A");
        cityListController.filterList("Al");
        cityListController.filterList("Ala");
        cityListController.filterList("Alab");
        cityListController.filterList("Alaba");
        cityListController.filterList("Alabam");

        List<City> list = cityListController.filterList("Alabama, US");

        int sizeResult = list.size();
        assertEquals(sizeResult,1);

        City city =list.get(0);
        assertEquals(city.getName(),"Alabama");
    }
}

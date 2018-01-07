package com.smeghani.citydirectoryapp.controller;

import com.smeghani.citydirectoryapp.DependencyManager;
import com.smeghani.citydirectoryapp.model.City;
import com.smeghani.citydirectoryapp.model.Index;
import com.smeghani.citydirectoryapp.repository.CityListRepository;
import com.smeghani.citydirectoryapp.view.OnCityListDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by shoaibmeghani on 07/01/2018.
 */

public class CityListController implements OnCityListDataListener {

    private CityListRepository cityListRepository;
    private OnCityListDataListener cityListDataListener;
    private HashMap<Character, Index> indexData;
    private Stack<Index> indexStack;

    public CityListController(DependencyManager dependencyManager) {
        cityListRepository = dependencyManager.provideCityListRepo();
        indexStack = new Stack<>();
    }

    public void setCityListDataListener(OnCityListDataListener cityDataListener) {
        cityListDataListener = cityDataListener;
    }

    public void initializeCityListData() {
        cityListRepository.fetchCityList(this,"cities.json");
    }

    public void filterCityList(String query) {
        onCityListFiltered(filterList(query));
    }


    public List<City> filterList(String query) {
        if (query.length() == 0) {

            return cityListRepository.getCityList();
        }

        if (query.length() < indexStack.size()) {

            while (query.length() < indexStack.size()) {
                indexStack.pop();
            }

        }

        if (query.length() == 1) {
            indexStack.push(indexData.get(query.charAt(0)));
        } else {
            Index i = findIndexOfQuery(query);

            if (i != null) {
                indexStack.push(i);
            } else {
                return null;
            }
        }

        if (indexStack.peek().start == indexStack.peek().end) {
            ArrayList<City> list = new ArrayList<City>();
            list.add(cityListRepository.getCityList().get(indexStack.peek().start));

            return list;
        } else {
            Index index = findIndexOfQuery(query);

            return cityListRepository.filterCityList(index.start, index.end);
        }
    }

    @Override
    public void onCityListInitialized(List<City> cityList) {
        createIndexData((ArrayList<City>) cityList);

        if (cityListDataListener != null) {
            cityListDataListener.onCityListInitialized(cityList);
        } else {
            throw new RuntimeException("cityListDataListener is not set");
        }

    }

    @Override
    public void onCityListFiltered(List<City> filteredCityList) {
        if (cityListDataListener != null) {
            cityListDataListener.onCityListFiltered(filteredCityList);
        } else {
            throw new RuntimeException("cityListDataListener is not set");
        }
    }


    /**
     * This method will create map that holds information of all prefix (1st char) used in the list.
     * This will optimise filtering for first character by iterating on chunk of list instead of all the records.
     */
    public void createIndexData(ArrayList<City> list) {
        indexData = new HashMap<>();
        Character lastChar = list.get(0).getName().charAt(0);
        int tempIndex = 0;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().charAt(0) != lastChar) {
                Index index = new Index();
                index.start = tempIndex;
                index.end = i - 1;
                //Log.d("create index data", lastChar + " -> start: " + index.start + " end: " + index.end );
                indexData.put(lastChar, index);
                tempIndex = i;
                lastChar = list.get(i).getName().charAt(0);
            }
        }

        //For edge case when last item has unique prefix
        if (!indexData.containsKey(lastChar)) {
                Index index = new Index();
                index.start = tempIndex;
                index.end = list.size() - 1;
                indexData.put(lastChar,index);
        }
    }

    /**
     * This method finds the start and end indexes in sub-array for given query. It is stored in stack
     * so that it keeps the track of previous query strings.
     */
    private Index findIndexOfQuery(String query) {

        boolean wordFound = false;
        Index index = null;

        for (int i = indexStack.peek().start; i <= indexStack.peek().end; i++) {

            if (!wordFound && cityListRepository.getCityList().get(i).getCityDisplayedName().startsWith(query)) {
                index = new Index();
                index.start = i;
                wordFound = true;
            }

            if (wordFound && !cityListRepository.getCityList().get(i).getCityDisplayedName().startsWith(query)) {
                index.end = i - 1;
                break;
            }

            //if end is reached and all items matched
            if (index != null && i == (indexStack.peek().end) && index.end == 0) {
                index.end = i;
            }
        }
        return index;
    }
}

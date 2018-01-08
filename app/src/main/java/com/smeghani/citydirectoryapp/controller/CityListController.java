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
 * <p>
 * This class is responsible for managing search functionality.
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
        cityListRepository.fetchCityList(this, "cities.json");
    }

    public void filterCityList(String query) {
        onCityListFiltered(filterList(query));
    }

    /**
     * This method keeps track of character indexes in stack
     * and fetches the sublist based on the ranges.
     */
    public List<City> filterList(String query) {

        query = query.toLowerCase();
        if (query.length() == 0) {
            return cityListRepository.getCityList();
        }
        //if user removes a character from query string, pop the ranges of that character from stack
        if (query.length() < indexStack.size()) {
            while (query.length() < indexStack.size()) {
                indexStack.pop();
            }
        }
        if (query.length() == 1) {
            if (indexData.containsKey(query.charAt(0))) {
                indexStack.push(indexData.get(query.charAt(0)));
            } else {
                return null;
            }
        } else {
            Index i = findIndexOfQuery(query);
            if (i != null) {
                indexStack.push(i);
            } else {
                return null;
            }
        }
        //if there is only one value in the list, start and end will be same
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
     * This method will create map that holds index ranges of all prefix (1st char) used in the list.
     * This will optimise filtering for first character by iterating on chunk of list instead of all the records.
     */
    public void createIndexData(ArrayList<City> list) {
        indexData = new HashMap<>();
        char lastChar = list.get(0).getName().charAt(0);
        int tempIndex = 0;

        for (int i = 0; i < list.size(); i++) {
            if (Character.toLowerCase(list.get(i).getName().charAt(0)) != Character.toLowerCase(lastChar)) {
                Index index = new Index();
                index.start = tempIndex;
                index.end = i - 1;
                indexData.put(Character.toLowerCase(lastChar), index);
                tempIndex = i;
                lastChar = list.get(i).getName().charAt(0);
            }
        }

        //For edge case when last item has unique prefix
        if (!indexData.containsKey(Character.toLowerCase(lastChar))) {
            Index index = new Index();
            index.start = tempIndex;
            index.end = list.size() - 1;
            indexData.put(Character.toLowerCase(lastChar), index);
        }
    }

    /**
     * This method finds the start and end indexes in sub-array for given query. It is stored in stack
     * so that it keeps the track of previous query strings.
     */
    private Index findIndexOfQuery(String query) {

        boolean wordFound = false;
        Index index = null;
        query = query.toLowerCase();

        for (int i = indexStack.peek().start; i <= indexStack.peek().end; i++) {

            if (!wordFound && cityListRepository.getCityList().get(i).getCityDisplayedName().toLowerCase().startsWith(query)) {
                index = new Index();
                index.start = i;
                wordFound = true;
            }

            if (wordFound && !cityListRepository.getCityList().get(i).getCityDisplayedName().toLowerCase().startsWith(query)) {
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

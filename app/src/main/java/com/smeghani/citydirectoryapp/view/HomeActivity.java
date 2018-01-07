package com.smeghani.citydirectoryapp.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.smeghani.citydirectoryapp.BaseApplication;
import com.smeghani.citydirectoryapp.R;
import com.smeghani.citydirectoryapp.model.City;
import com.smeghani.citydirectoryapp.repository.CityListRepository;

public class HomeActivity extends AppCompatActivity implements CityListFragment.OnCityListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        if (savedInstanceState == null) {

            CityListFragment fragment = CityListFragment
                    .newInstance(((BaseApplication) getApplication()).getDependencyManager());

            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right)
                    .add(R.id.fragment_container, fragment, CityListFragment.TAG).commit();
        }
    }

    @Override
    public void onCityItemClick(City item) {
        showCityLocation(item);
    }

    public void showCityLocation(City city) {
        LatLng location = new LatLng(city.getCoord().getLat(), city.getCoord().getLon());
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right)
                .add(R.id.fragment_container, MapsFragment.newInstance(location, city.getCityDisplayedName()))
                .addToBackStack(MapsFragment.TAG).commit();

        hideKeyboard();
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
    }
}

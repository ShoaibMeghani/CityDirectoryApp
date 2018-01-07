package com.smeghani.citydirectoryapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, CityListFragment.TAG).commit();
        }
    }

    @Override
    public void onCityItemClick(City item) {

    }
}

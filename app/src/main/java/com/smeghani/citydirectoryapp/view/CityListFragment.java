package com.smeghani.citydirectoryapp.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.smeghani.citydirectoryapp.DependencyManager;
import com.smeghani.citydirectoryapp.R;
import com.smeghani.citydirectoryapp.controller.CityListController;
import com.smeghani.citydirectoryapp.model.City;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCityListItemClickListener}
 * interface.
 */
public class CityListFragment extends Fragment implements OnCityListDataListener {


    public final static String TAG = "CityListFragment";
    private OnCityListItemClickListener mListener;
    private CityListController cityListController;
    private RecyclerView cityListView;
    private EditText etSearch;
    private ProgressBar pb;
    private TextView lblNoData;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CityListFragment() {


    }

    /**
     * Use this method to initiate a fragment.
     */
    public static CityListFragment newInstance(DependencyManager dependencyManager) {
        CityListFragment fragment = new CityListFragment();

        fragment.cityListController = dependencyManager.provideCityListController();
        fragment.cityListController.setCityListDataListener(fragment);

        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cityListController.initializeCityListData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCityListItemClickListener) {
            mListener = (OnCityListItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCityListItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCityListInitialized(List<City> cityList) {

        initView();
        Context context = cityListView.getContext();
        cityListView.setLayoutManager(new LinearLayoutManager(context));
        cityListView.setAdapter(new MyCityRecyclerViewAdapter(cityList, mListener));
        pb.setVisibility(View.GONE);
    }

    @Override
    public void onCityListFiltered(List<City> filteredCityList) {
        if (filteredCityList != null) {
            ((MyCityRecyclerViewAdapter) cityListView.getAdapter()).setList(filteredCityList);
            lblNoData.setVisibility(View.GONE);
            cityListView.scrollToPosition(0);
            cityListView.setVisibility(View.VISIBLE);
        } else {
            cityListView.setVisibility(View.GONE);
            lblNoData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This listener must be implemented by the container activity
     */
    public interface OnCityListItemClickListener {
        void onCityItemClick(City item);
    }

    public void initView() {
        View view = getView();

        cityListView = view.findViewById(R.id.list);
        etSearch = view.findViewById(R.id.et_search);
        pb = view.findViewById(R.id.pb);
        lblNoData = view.findViewById(R.id.lbl_nodata);

        //hide keyboard if user scrolls the list.
        cityListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 1){
                    ((HomeActivity)getActivity()).hideKeyboard();
                }
            }
        });



        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cityListController.filterCityList(etSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}

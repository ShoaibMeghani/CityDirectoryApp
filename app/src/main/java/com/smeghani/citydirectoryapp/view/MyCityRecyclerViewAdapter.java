package com.smeghani.citydirectoryapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smeghani.citydirectoryapp.R;
import com.smeghani.citydirectoryapp.model.City;
import com.smeghani.citydirectoryapp.view.CityListFragment.OnCityListItemClickListener;

import java.util.List;


public class MyCityRecyclerViewAdapter extends RecyclerView.Adapter<MyCityRecyclerViewAdapter.ViewHolder> {

    private List<City> mValues;
    private final OnCityListItemClickListener mListener;

    MyCityRecyclerViewAdapter(List<City> items, CityListFragment.OnCityListItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getCityDisplayedName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onCityItemClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mContentView;
        private City mItem;

         ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.city_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void clearAll(){
        mValues.clear();
        notifyDataSetChanged();
    }

    public void setList(List<City> list){
        mValues = list;
        notifyDataSetChanged();

    }
}

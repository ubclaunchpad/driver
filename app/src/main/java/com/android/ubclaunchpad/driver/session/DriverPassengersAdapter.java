package com.android.ubclaunchpad.driver.session;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;

import com.android.ubclaunchpad.driver.user.User;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by grigorii on 14/07/17.
 */

public class DriverPassengersAdapter
        extends RecyclerView.Adapter<DriverPassengersAdapter.DriverAssignmentViewHolder> {

    private static final String TAG = DriverPassengersAdapter.class.getSimpleName();
    private Context context;
    private List<User> passengers;


    public DriverPassengersAdapter(Context context, List<User> passengers) {
        this.context = context;
        this.passengers = passengers;
    }

    // Custom ViewHolder
    static class DriverAssignmentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.passenger_name)
        TextView passengerName;
        @BindView(R.id.passenger_destination)
        TextView passengerDestination;

        DriverAssignmentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public DriverAssignmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.passenger_item, parent, false);
        return new DriverAssignmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DriverAssignmentViewHolder holder, int position) {
        final User currentPassenger = passengers.get(position);
        holder.passengerName.setText(currentPassenger.getName());
        holder.passengerDestination.setText(R.string.needs_a_ride + currentPassenger.getDestinationLatLngStr());
    }

    public void updatePassengers(List<User> newPassengers) {
        passengers = newPassengers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }
}

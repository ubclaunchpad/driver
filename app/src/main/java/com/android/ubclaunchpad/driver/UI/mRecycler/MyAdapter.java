package com.android.ubclaunchpad.driver.UI.mRecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.mDataObject.Passenger;

import java.util.ArrayList;

/**
 * Created by Noor on 7/31/2016.
 */

/**
 * Inflates each individual cardview in the listview. Populates their individual item values,
 * dismisses selected passengers.
 */
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Passenger> passengers;
    MyHolder myHolder;
    ArrayList<Passenger> selectedPassengers = new ArrayList<>();
    CheckBox yesorno;
    View v;


    public MyAdapter(Context c, ArrayList<Passenger> passengers) {
        this.c = c;
        this.passengers = passengers;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, parent, false);
        myHolder = new MyHolder(v);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        // TODO
        // Replace all these variable settings with actual data-grabbers once bluetooth is set up

        holder.mname.setText(passengers.get(position).getPName());
        holder.mprofPic.setImageResource(passengers.get(position).getIconID());
        holder.mdropOff.setText(passengers.get(position).getDropOffLocation());
        holder.mpickUp.setText(passengers.get(position).getPickUpLocation());

        /**
         * CheckBox click listener. Adds selected passengers to a list that will be removed
         * upon selecting "Remove Passengers" from FAB.
         */
        yesorno = (CheckBox) v.findViewById(R.id.yesorno);
        yesorno.setTag(new Integer(position));
        yesorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selectedPassengers.add(passengers.get(((Integer) v.getTag())));
                } else {
                    selectedPassengers.remove(passengers.get(((Integer) v.getTag())));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return passengers.size();
    }


    //DISMISS PASSENGERS WHEN SWIPED (CALLED FROM SWIPEHELPER)
    public void dismissPassenger(int position) {
        passengers.remove(position);
        this.notifyDataSetChanged();
    }

    //DISMISS PASSENGERS WHEN "REMOVE PASSENGERS" FAB IS SELECTED, AND THEY ARE CHECKED
    public void dismissSelectedPassenger(ArrayList<Passenger> passengersSelected) {

        for (Passenger p: passengersSelected) {
            passengers.remove(p);
        }

        this.notifyDataSetChanged();

    }


    public ArrayList<Passenger> getSelectedPassengers() {
        return selectedPassengers;
    }


}
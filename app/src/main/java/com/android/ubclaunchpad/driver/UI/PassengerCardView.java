package com.android.ubclaunchpad.driver.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.mDataObject.Passenger;
import com.android.ubclaunchpad.driver.UI.mDataObject.PassengerCollection;
import com.android.ubclaunchpad.driver.UI.mRecycler.MyAdapter;
import com.android.ubclaunchpad.driver.UI.mSwipe.SwipeHelper;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * RecyclerView fragment for all passengers. Responsible for attaching adapter to the recyclerview.
 * Driver can remove passengers they don't wish to take by either swiping left on that passenger,
 * or selecting their checkbox and clicking the remove passengers button in the FAB.
 */

public class PassengerCardView extends Fragment {

    static public RecyclerView rv;
    MyAdapter myAdapter;
    FloatingActionButton bRemovePassengers;
    ArrayList<Passenger> checkBoxSelectedPassengers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.sm_cardview,container,false);

    }


    /**
     * Attaches custom MyAdapter to recyclerview, as well as cutom SwipeHelper.
     * Communicated with MyAdapter class to remove all the selected passengers.
     */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);


        rv.setHasFixedSize(true);

        LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        myAdapter = new MyAdapter(getActivity(), PassengerCollection.getPassengers());

        rv.setAdapter(myAdapter);

        ItemTouchHelper.Callback callback = new SwipeHelper(myAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);


        bRemovePassengers = (FloatingActionButton) getView().findViewById(R.id.remove_passengers_btn);
        bRemovePassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkBoxSelectedPassengers = myAdapter.getSelectedPassengers();

                myAdapter.dismissSelectedPassenger(checkBoxSelectedPassengers);



            }
        });

    }





}
package com.android.ubclaunchpad.driver.UI.mSwipe;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.android.ubclaunchpad.driver.UI.mRecycler.MyAdapter;

/**
 * Created by Noor on 7/31/2016.
 */

/**
 * Custom swipe helper. Removes cardview upon swiping left.
 */
public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    MyAdapter adapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    public SwipeHelper(MyAdapter adapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        adapter.dismissPassenger(viewHolder.getAdapterPosition());
    }
}

package com.android.ubclaunchpad.driver.UI.mRecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;

/**
 * Created by Noor on 8/29/2016.
 */
// Holds and assigns item values in recyclerview model
public class MyHolder extends RecyclerView.ViewHolder {

    ImageView mprofPic;
    TextView mname;
    TextView mpickUp;
    TextView mdropOff;

    public MyHolder(View itemView) {
        super(itemView);

        this.mname = (TextView) itemView.findViewById(R.id.mName);
        this.mprofPic = (ImageView) itemView.findViewById(R.id.profPic);
        this.mpickUp = (TextView) itemView.findViewById(R.id.mPickUpLocation);
        this.mdropOff = (TextView) itemView.findViewById(R.id.mDropOffLocation);



    }



}
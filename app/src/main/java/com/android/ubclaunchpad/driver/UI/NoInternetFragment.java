package com.android.ubclaunchpad.driver.UI;

import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.ubclaunchpad.driver.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sherryuan on 2017-07-08.
 */

public class NoInternetFragment extends Fragment {

    public static String TAG = "NO_INTERNET_FRAGMENT";

    @OnClick(R.id.try_again_button)
    public void connectToInternet() {
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        if (connection != null && connection.isConnectedOrConnecting()){
            getActivity().getFragmentManager().popBackStack();
        }}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_internet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}

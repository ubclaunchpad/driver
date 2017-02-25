package com.android.ubclaunchpad.driver.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.util.SessionObj;

import java.util.List;

/**
 * Created by sherryuan on 2017-02-18.
 */

// RecyclerView.Adapter class for the RecyclerView in SessionActivity
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {
    private List<SessionModel> sessions;

    // Custom ViewHolder
    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public TextView sessionName;

        public SessionViewHolder(View view) {
            super(view);
            sessionName = (TextView) view.findViewById(R.id.session_list_name);
        }
    }

    public SessionAdapter(List<SessionModel> sessionModelList) {
        this.sessions = sessionModelList;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_list_row, parent, false);

        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        SessionModel sessionModel = sessions.get(position);
        holder.sessionName.setText(sessionModel.getName());
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }
}

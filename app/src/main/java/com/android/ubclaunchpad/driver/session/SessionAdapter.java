package com.android.ubclaunchpad.driver.session;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.UI.sessionInfoActivity;
import com.android.ubclaunchpad.driver.models.SessionModel;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by sherryuan on 2017-02-18.
 */

// RecyclerView.Adapter class for the RecyclerView in SessionActivity
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private Context context;
    private List<SessionModel> sessions;

    // Custom ViewHolder
    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public TextView sessionName;
        public TextView sessionDriverInfo;
        public TextView sessionPassengerInfo;
        public Button sessionJoinButton;

        public SessionViewHolder(View view) {
            super(view);
            sessionName = (TextView) view.findViewById(R.id.session_list_name);
            sessionDriverInfo = (TextView) view.findViewById(R.id.session_driver_info);
            sessionPassengerInfo = (TextView) view.findViewById(R.id.session_passenger_info);
            sessionJoinButton = (Button) view.findViewById(R.id.session_join_button);
        }
    }

    public SessionAdapter(Context context, List<SessionModel> sessionModels) {
        this.context = context;
        this.sessions = sessionModels;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_list_row, parent, false);

        return new SessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        final SessionModel sessionModel = sessions.get(position);
        holder.sessionName.setText(sessionModel.getName());
        holder.sessionDriverInfo.setText(sessionModel.getDrivers().size() + " driver(s)");
        holder.sessionPassengerInfo.setText(sessionModel.getPassengers().size() + " passenger(s)");

        holder.sessionJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sessionInfoIntent = new Intent(context, sessionInfoActivity.class);
                sessionInfoIntent.putExtra(context.getString(R.string.session_name), sessionModel.getName());
                context.startActivity(sessionInfoIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public void updateSessions(List<SessionModel> sessionModels) {
        sessions = sessionModels;
        notifyDataSetChanged();
    }

    //TODO delete user from previous sessions they have joined
    private void deleteFromPrevSessions(){
        FirebaseUtils.getDatabase().child("Session group")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot sessionSnapshot : dataSnapshot.getChildren()){
                            for(DataSnapshot driver : sessionSnapshot
                                    .child(StringUtils.FirebaseSessionDriverEndpoint)
                                    .getChildren()){

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}

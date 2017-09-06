package com.android.ubclaunchpad.driver.session;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.ubclaunchpad.driver.R;
import com.android.ubclaunchpad.driver.session.models.SessionModel;
import com.android.ubclaunchpad.driver.util.FirebaseUtils;
import com.android.ubclaunchpad.driver.util.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sherryuan on 2017-02-18.
 */

// RecyclerView.Adapter class for the RecyclerView in SessionActivity
public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private static final String TAG = SessionAdapter.class.getSimpleName();
    private Context context;
    private List<SessionModel> sessions;

    // Custom ViewHolder
    static class SessionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.session_list_name)
        TextView sessionName;
        @BindView(R.id.session_driver_info)
        TextView sessionDriverInfo;
        @BindView(R.id.session_passenger_info)
        TextView sessionPassengerInfo;
        @BindView(R.id.session_join_button)
        ImageButton sessionJoinButton;

        SessionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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
                deleteFromPrevSessions(sessionModel.getName());
                Intent sessionInfoIntent = new Intent(context, SessionInfoActivity.class);
                sessionInfoIntent.putExtra(StringUtils.SESSION_NAME, sessionModel.getName());
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

    /**
     * Delete the user's id from all sessions' driver and passenger
     * lists except for the target session
     *
     * @param targetSessionName name of the session to join
     */
    private void deleteFromPrevSessions(final String targetSessionName) {
        FirebaseUtils.getDatabase().child(StringUtils.FirebaseSessionEndpoint)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                            if (!sessionSnapshot.getKey().equals(targetSessionName)) {
                                SessionModel session = sessionSnapshot.getValue(SessionModel.class);
                                List<String> drivers = session.getDrivers();
                                List<String> passengers = session.getPassengers();
                                String UID = FirebaseUtils.getFirebaseUser().getUid();
                                if (drivers.contains(UID)) {
                                    drivers.remove(UID);
                                    FirebaseUtils.getDatabase()
                                            .child(StringUtils.FirebaseSessionEndpoint)
                                            .child(sessionSnapshot.getKey())
                                            .child(StringUtils.FirebaseSessionDriverEndpoint)
                                            .setValue(drivers);
                                    Log.v(TAG, "removed " + UID + "from drivers list in session " + session.getName());
                                }
                                if (passengers.contains(UID)) {
                                    passengers.remove(UID);
                                    FirebaseUtils.getDatabase()
                                            .child(StringUtils.FirebaseSessionEndpoint)
                                            .child(sessionSnapshot.getKey())
                                            .child(StringUtils.FirebaseSessionPassengerEndpoint)
                                            .setValue(passengers);
                                    Log.v(TAG, "removed " + UID + "from passengers list in session " + session.getName());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}

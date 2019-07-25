package com.example.dpashchenko.catfeeder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CatFeederActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_feeder);

        final Button btnFeed = findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference status = database.getReference("actions/feed/status");

                status.setValue(1);
            }
        });

        ValueEventListener lastFedListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long timestamp = dataSnapshot.getValue(Long.class);

                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                sdf.setTimeZone(tz);

                String lastUpdateDate = sdf.format(new Date(timestamp));

                TextView textView = (TextView) findViewById(R.id.txtLastFed);
                textView.setText(lastUpdateDate);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        DatabaseReference updated = database.getReference("actions/feed/updated");
        updated.addValueEventListener(lastFedListener);
    }
}

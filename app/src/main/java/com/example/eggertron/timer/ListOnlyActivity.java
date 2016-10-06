package com.example.eggertron.timer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.view.View.INVISIBLE;

public class ListOnlyActivity extends AppCompatActivity {

    Timer timer;

    private ControlFragment.OnFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make sure we are in landscape mode.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //finish();
            //return;
        }

        setContentView(R.layout.fragment_list);

        //Get the extra data from the MainActivity
        Intent intent = getIntent();

        //Display it on the textview
        TextView textView = (TextView)findViewById(R.id.listTimes);
        textView.setText(intent.getStringExtra(MainActivity.LIST));

        timer = new Timer(intent.getIntExtra(MainActivity.SEC, 0),
                intent.getIntExtra(MainActivity.MIN, 0),
                intent.getIntExtra(MainActivity.HOURS, 0),
                intent.getStringExtra(MainActivity.LIST),
                intent.getBooleanExtra(MainActivity.THREAD_STATUS, false),
                intent.getIntExtra(MainActivity.INDEX, 0));
    }

}

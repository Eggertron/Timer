package com.example.eggertron.timer;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        ControlFragment.OnFragmentInteractionListener, ListFragment.OnFragmentInteractionListener, View.OnClickListener {

    //Create Finals
    public final static String LIST = "LIST",
        MIN = "MIN", INDEX = "INDEX",
        SEC = "SEC", HOURS = "HOURS",
                THREAD_STATUS="THREAD_STATUS";

    //create global variables.
    Button startBtn;
    Button listBtn;
    Button backBtn;
    TextView cmdTxt;
    Timer stopWatch;
    TimerAsyncTask timerAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup global variables.
        initialize();
        timerAsyncTask = new TimerAsyncTask();
        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listFrag);
        //Check if a two pane configuration being displayed?
        if (listFragment != null &&
                listFragment.isInLayout()) {
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            listBtn.setVisibility(View.GONE);
            backBtn.setVisibility(View.GONE);
        }
        else {
            listFragment.getView().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                stopWatch = new Timer(data.getIntExtra(SEC, 0),
                        data.getIntExtra(MIN, 0),
                        data.getIntExtra(HOURS, 0),
                        data.getStringExtra(LIST),
                        data.getBooleanExtra(THREAD_STATUS, false),
                        data.getIntExtra(INDEX, 0));
                if (stopWatch.getList() != null) {
                    onButtonClicked(-1); // just reload
                }
                if(stopWatch.isStarted) {
                    timerAsyncTask = new TimerAsyncTask();
                    timerAsyncTask.execute();
                }
            }
        }
        System.out.println(stopWatch.isStarted);
    }

    @Override
    public void onButtonClicked(int buttonID) {
        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listFrag);
        if (buttonID == 0) {
            //start stop button
            if (stopWatch.isStarted) {
                stopWatch.stop();
                startBtn.setText("START");
                timerAsyncTask.cancel(true);
                timerAsyncTask = null;
            }
            else {
                stopWatch.start();
                startBtn.setText("STOP");
                if (timerAsyncTask != null) {
                    timerAsyncTask.cancel(true);
                }
                timerAsyncTask = new TimerAsyncTask();
                timerAsyncTask.execute();
            }
        }
        else if (buttonID == 1) {
            //lap button
            stopWatch.index++;
            stopWatch.lap();
        }
        else if (buttonID == 2) {
            //reset button
            initialize();
        }
        else if (buttonID == 4) {
            listFragment.getView().setVisibility(View.GONE);
        }
        //Check if the ListFragment exists in this layout

        //Check if a two pane configuration being displayed?
        if (listFragment != null &&
                listFragment.isInLayout()) {
            //informationFragment exists, two panes
            if (buttonID == 3) {
                //Next page button.
                //informationFragment doesn't exist, one pane
                Intent intent = new Intent(this, ListOnlyActivity.class);
                intent.putExtra(SEC, stopWatch.seconds);
                intent.putExtra(LIST, stopWatch.getList());
                intent.putExtra(MIN, stopWatch.mins);
                intent.putExtra(HOURS, stopWatch.hours);
                intent.putExtra(INDEX, stopWatch.index);
                intent.putExtra(THREAD_STATUS, stopWatch.isStarted);
                listFragment.getView().setVisibility(View.VISIBLE);
            }

            listFragment.setText(stopWatch.getList());
        } else {

        }
        if (stopWatch.isStarted) {

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        stopWatch = new Timer(savedInstanceState.getInt(SEC),
                savedInstanceState.getInt(MIN),
                savedInstanceState.getInt(HOURS),
                savedInstanceState.getString(LIST),
                savedInstanceState.getBoolean(THREAD_STATUS),
                savedInstanceState.getInt(INDEX));
        if(stopWatch.isStarted) {
            timerAsyncTask = new TimerAsyncTask();
            timerAsyncTask.execute();
        }
        if (stopWatch.getList() != null) {
            onButtonClicked(-1); // just reload
        }
        cmdTxt.setText(stopWatch.getTime() );
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (timerAsyncTask!=null && timerAsyncTask.getStatus()==AsyncTask.Status.RUNNING) {
            outState.putBoolean(THREAD_STATUS, stopWatch.isStarted);
            timerAsyncTask.cancel(true);
        }
        else
            outState.putBoolean(THREAD_STATUS, false);

        outState.putString(LIST, stopWatch.getList());
        outState.putInt(SEC, stopWatch.seconds);
        outState.putInt(MIN, stopWatch.mins);
        outState.putInt(HOURS, stopWatch.hours);
        outState.putInt(INDEX, stopWatch.index);
        super.onSaveInstanceState(outState);
    }

    private void initialize() {
        // reset everything
        if (timerAsyncTask != null) {
            timerAsyncTask.cancel(true);
            timerAsyncTask = new TimerAsyncTask();
        }
        stopWatch = new Timer();
        startBtn = (Button)findViewById(R.id.startButton);
        cmdTxt = (TextView)findViewById(R.id.controlText);
        cmdTxt.setText("00:00:00");
        startBtn.setText("START");
        listBtn = (Button)findViewById(R.id.nextFrag);
        backBtn = (Button)findViewById(R.id.backButn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println("test");
    }

    @Override
    public void onClick(View v) {
        ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentById(R.id.listFrag);
        listFragment.getView().setVisibility(View.GONE);
    }

    private class TimerAsyncTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private void setCmdTxt(String time) {
            cmdTxt.setText(time);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            setCmdTxt(stopWatch.getTime());
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Integer... params) {
            // update the timer text view here.
            //cmdTxt.setText(stopWatch.getTime());
            while (!isCancelled()) {
                publishProgress();
                try {
                    Thread.sleep(50);// sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopWatch.addMil();
            }
            return null;
        }
    }

}

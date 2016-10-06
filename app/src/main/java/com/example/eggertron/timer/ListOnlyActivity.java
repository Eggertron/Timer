package com.example.eggertron.timer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ListOnlyActivity extends AppCompatActivity {

    Timer timer;
    TimerAsyncTask timerAsyncTask;
    private ControlFragment.OnFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Make sure we are in landscape mode.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
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

        timerAsyncTask = new TimerAsyncTask();
        if (timer.isStarted) {
            timerAsyncTask.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (timerAsyncTask != null && timerAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            timerAsyncTask.cancel(true);
        }
        Intent i = new Intent();
        i.putExtra(MainActivity.SEC, timer.seconds);
        i.putExtra(MainActivity.LIST, timer.getList());
        i.putExtra(MainActivity.MIN, timer.mins);
        i.putExtra(MainActivity.HOURS, timer.hours);
        i.putExtra(MainActivity.INDEX, timer.index);
        i.putExtra(MainActivity.THREAD_STATUS, true);
        setResult(RESULT_OK, i);
        //super.onBackPressed();
        finish();
    }

    private class TimerAsyncTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        private void setCmdTxt(String time) {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Integer... params) {
            // update the timer text view here.
            //cmdTxt.setText(stopWatch.getTime());
            while (!isCancelled()) {
                timer.addSec();
                try {
                    Thread.sleep(1000);// sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}

package com.hfad.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    private Chronometer stopwatch;
    private boolean running = false;
    private long offset = 0;

    //KEYS for use with the bundle
    public static final String OFFSET_KEY = "offset";
    public static final String RUNNING_KEY = "running";
    public static final String BASE_KEY = "base";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //stopwatch is a field defined above so that it can be
        // accessed throughout all methods
        stopwatch = findViewById(R.id.stopwatch);

        Button btnStart = findViewById(R.id.start_button);
        Button btnPause = findViewById(R.id.pause_button);
        Button btnReset = findViewById(R.id.reset_button);

        //Restore the previous state: If savedInstanceState is not null,
        //Then there are properties in the bundle that we need to restore.

        if (savedInstanceState != null) {
            //Restore the values from the bundle for offset/running
            offset = savedInstanceState.getLong(OFFSET_KEY);
            running = savedInstanceState.getBoolean(RUNNING_KEY);

            //If the stopwatch is in the middle of running,
            // we need to directly set the base time to whatever it was
            // at the start of the run and start the clock.
            //Otherwise, we can just call the setBaseTime() method like we usually do.
            if (running) {
                stopwatch.setBase(savedInstanceState.getLong(BASE_KEY));
                stopwatch.start();
            }
            else {
                setBaseTime();
            }
        }

        //The start button starts the stopwatch if it is not already running
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running){
                    //Make sure the stopwatch starts from the correct time
                    //and then start it.
                    setBaseTime();
                    stopwatch.start();
                    running = true;

                    //Dr R included these for testing.
                    //Comment them out when you understand
                    //running, offset, stopwatch.getBase()
                    //System.out.println("START:");

                    //System.out.println("\trunning: " + running);
                    //System.out.println("\toffset: " + offset);
                    //System.out.println("\tbase: " + stopwatch.getBase());
                }
            }
        });
        //The pause button pauses the stopwatch if it is running
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running){
                    //save the time on the stopwatch and
                    //stop it from running
                    saveOffset();
                    stopwatch.stop();
                    running = false;
                    //Dr R included these for testing.
                    //Comment them out when you understand
                    //running, offset, stopwatch.getBase()
                    //System.out.println("PAUSE:");
                    //System.out.println("\trunning: " + running);
                    // System.out.println("\toffset: " + offset);
                    // System.out.println("\tbase: " + stopwatch.getBase());
                }
            }
        });
        //The reset button sets the offset and stopwatch back to 0
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Set the stopwatch time back to 0
                offset = 0;
                setBaseTime();
                //Dr R included these for testing.
                //Comment them out when you understand
                //running, offset, stopwatch.getBase()
                //System.out.println("RESET:");
                //System.out.println("\trunning: " + running);
                //System.out.println("\toffset: " + offset);
                //System.out.println("\tbase: " + stopwatch.getBase());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (running) {
            saveOffset();
            stopwatch.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (running){
            setBaseTime();
            stopwatch.start();
            offset = 0;
        }
    }





    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        //call the parent method and let it do its thing.
        //I get a warning if I do not do this.
        super.onSaveInstanceState(savedInstanceState);

        //Save the state of our activities' special properties
        savedInstanceState.putLong(OFFSET_KEY, offset);
        savedInstanceState.putBoolean(RUNNING_KEY, running);
        savedInstanceState.putLong(BASE_KEY, stopwatch.getBase());
    }
/**********************************
 *******HELPER METHODS*************
 ***********************************/

    /**
     * Update the stopwatch.base time allowing for any offset
     */
    public void setBaseTime()
    {
        stopwatch.setBase(SystemClock.elapsedRealtime() - offset);
    }

    /**
     * Record the offset
     */
    public void saveOffset(){
        offset = SystemClock.elapsedRealtime() - stopwatch.getBase();
    }
}

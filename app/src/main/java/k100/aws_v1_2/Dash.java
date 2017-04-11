package k100.aws_v1_2;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TextView;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class Dash extends AppCompatActivity {

    // VARIABLES

    ProgressBar temp_bar;
    ProgressBar pres_bar;
    ProgressBar humid_bar;
    ProgressBar alt_bar;

    TextView temp_text;
    TextView pres_text;
    TextView humid_text;
    TextView alt_text;
    TextView indicator;

    public int i;   // Random indexer
    public int exit=0; // Exit Code Variable
    public String Server_URL = ""; // Hostname
    public String[] response_seperated;
    public int[] data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        // Variable References
        temp_bar = (ProgressBar) findViewById(R.id.temp_progressBar);
        pres_bar = (ProgressBar) findViewById(R.id.pres_progressBar);
        humid_bar = (ProgressBar) findViewById(R.id.humid_progressBar);
        alt_bar = (ProgressBar) findViewById(R.id.alt_progressBar);

        temp_text = (TextView) findViewById(R.id.temp_text);
        pres_text = (TextView) findViewById(R.id.pres_text);
        humid_text = (TextView) findViewById(R.id.humid_text);
        alt_text = (TextView) findViewById(R.id.alt_text);
        indicator = (TextView) findViewById(R.id.indicator);

        //temp_bar.setProgress((int)100.0);

        // Start Updating
        Thread thread = new Thread() {
            public void run()   {
                while (true)    {
                    update();
                }
            }
        };

        thread.start();



    } // End On Create

    // METHODS

    public void update() {




            // Poll Database & Update
            new HTTP_request().execute();

            // Delay system (wait)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }








    } // End Update


    // CLASSES

    // HTTP Request Class (Handles both GET & POST all in one wowieee)
    public class HTTP_request extends AsyncTask<String, Void, Void> {
        public Void doInBackground(String... params) {
            try {
                // DEFINE POST or GET components:
                URL url = new URL(Server_URL);

                // Open HTTP Connection
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET"); // POST or GET

                // Variables for storing output response
                String response = "";
                BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream())); ;
                //final TextView outputView = (TextView) findViewById(R.id.textView_results); // Reference variable for output textview

                // Get output response
                String str = "";
                while ((str = buff.readLine()) != null) {
                    response = response + str;
                }
                buff.close();

                // Parse data into array
                response_seperated = response.split(":");

                Dash.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        // Formatting
                        DecimalFormat format = new DecimalFormat();
                        format.setDecimalSeparatorAlwaysShown(false);

                        temp_bar.setProgress((int) Double.parseDouble(response_seperated[7]));
                        pres_bar.setProgress((int) Double.parseDouble(response_seperated[9])/10000);
                        humid_bar.setProgress((int) Double.parseDouble(response_seperated[7]));
                        alt_bar.setProgress((int) Double.parseDouble(response_seperated[10])*-1 );


                        // Update Progress Text
                        temp_text.setText("" + response_seperated[7] + "C");
                        pres_text.setText("" + response_seperated[9] + "x10^-7 Pa");
                        humid_text.setText("" + response_seperated[7] + "%");
                        alt_text.setText("" + response_seperated[10] + "m");

                    }
                });

            } catch (IOException e) {
            } return null;
        }
    } // End of HTTP Request Class

}

/**     APPENDIX

//final String finalResponse = response_seperated[1]; // Year
//final String finalResponse = response_seperated[2]; // Month
//final String finalResponse = response_seperated[3]; // Date

//final String finalResponse = response_seperated[4]; // Hour
//final String finalResponse = response_seperated[5]; // Minute
//final String finalResponse = response_seperated[6]; // Second

//final String finalResponse = response_seperated[7]; // Temp
//final String finalResponse = response_seperated[8]; // Humidity
//final String finalResponse = response_seperated[9]; // Pressure
//final String finalResponse = response_seperated[10]; // Altitude
//final String finalResponse = response_seperated[11]; // Air Quality

 **/
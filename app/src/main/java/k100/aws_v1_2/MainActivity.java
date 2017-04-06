package k100.aws_v1_2;

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

public class MainActivity extends AppCompatActivity {

    public TextView outputView;
    public static Button get_button;
    public static Button post_button;

    public String Request_Type;
    public String Field_1 = "F1";
    public String Field_2 = "F2";

    // PLACE TOMCAT SERVER OR TUNNLED ADDRESS HERE!!!
    // DONT FORGET "http://" OR YOU MAY HAVE ISSUES
    public String Server_URL = "http://pc.rm2710.pagekite.me/midp/data"; // or your hostname


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Define Variables
        outputView = (TextView)findViewById(R.id.textView_results);
        get_button = (Button) findViewById(R.id.button_get);
        post_button = (Button) findViewById(R.id.button_post);

    }

    //                  METHODS

    // Manual OnClick handles (See buttons in layout xml)

    public void sendGetRequest(View v) {
        Request_Type = "GET";
        new HTTP_request().execute();
    }

    public void sendPostRequest(View v) {
        Field_2 = "F1 Test";
        Field_2 = "F2 Test";
        Request_Type = "POST";
        new HTTP_request().execute();
    }


    // HTTP Request Class (Handles both GET & POST all in one wowieee)
    public class HTTP_request extends AsyncTask<String, Void, Void> {
        public Void doInBackground(String... params) {
            try {
                // DEFINE POST or GET components:
                //  - Http POST format: Server_URL ? first_fieldname = "string value 1" + second_filedname = "string value 2"
                //  - Http GET format: Server_URL
                URL url = new URL(Server_URL + "?time=" + Field_1 + "&event=" + Field_2);

                // Open HTTP Connection
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod(Request_Type); // POST or GET

                // Variables for storing output response
                String response = "";
                BufferedReader buff = new BufferedReader(new InputStreamReader(connection.getInputStream())); ;
                final TextView outputView = (TextView) findViewById(R.id.textView_results); // Reference variable for output textview

                // Get output response
                String str = "";
                while ((str = buff.readLine()) != null) {
                    response = response + str;
                }
                buff.close();

                final String finalResponse = response;
                // Display output response on separate thread (UI Thread)
                MainActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() { outputView.setText(finalResponse); }
                                                }
                );

            }
            catch (IOException e) {
            }
            return null;
        }
    } // End of HTTP Request Class


    // --------------------------------------- GESTURE METHODS ---------------------------------------



         /*
                Field_1 = timeStamp;
                Field_2 = "EARTHQUAKE";
                Request_Type = "POST";
                new HTTP_request().execute();
                Event.setText(Field_2);
         */

} // END OF MAIN



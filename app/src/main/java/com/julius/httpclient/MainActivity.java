package com.julius.httpclient;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TimerTask myTimerTask;
    final Handler myHandler = new Handler();
    Timer myTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("main");
        // setTimer();
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnSms).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myTimerTask != null) {
            myTimerTask.cancel();
            Log.d("TIMER", "timer canceled");
        }
        Log.d("TIMER", "application set on pause");
    }

    public static void connect(String url) {
        HttpURLConnection urlConnection = null;
        Log.d("TIMER", "HTTP read");
        try {
            // uses default GET method
            // URLConnection connection = new URL(url + "?" + query).openConnection();
            URL myUrl = new URL(url);
            urlConnection = (HttpURLConnection) myUrl.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // readStream(in);
            String result = convertStreamToString(in);
            System.out.println(result);
        } catch (Exception e) {
            //AM: below line produce app has stopped
            //System.err.println(e.getMessage());

            Log.d("my error", e.getMessage());
        } finally {
            urlConnection.disconnect();
        }

    }

    public void setTimer() {

        myTimerTask = new TimerTask() {
            @Override
            public void run() {
                connect("http://short.org/");
            }
        };
        myTimer.schedule(myTimerTask, 1000, 1000);
        /*myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connect("");
            }
        },1000,1000);
        */
        /*myTimerTask = new TimerTask() {
            public void run() {
                myHandler.post(new Runnable() {
                    public void run() {
                        connect("");
                        //Log.d("TIMER", "TimerTask run");
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay ms, long period ms)
        myTimer.schedule(myTimerTask, 1000, 1000);*/
        Log.d("TIMER", "TimerTask set");
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                setTimer();
                Log.d("TIMER", "application timer set");
                break;
            case R.id.btnStop:
                if (myTimerTask != null) {
                    myTimerTask.cancel();
                    Log.d("TIMER", "timer canceled");
                }
                break;
            case R.id.btnSms:
                sendSms();
                Log.d("TIMER", "sms tried to send");
                break;
        }
    }

    public void sendSms() {
        //String phoneNo = textPhoneNo.getText().toString();
        //String sms = textSMS.getText().toString();

        String phoneNo = "89165733757";
        String sms = "java text";

        try {
            // imported just smsmanager, not gsm.smsmanager
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}


/*            HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url);

        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("http c", response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }


        } catch (Exception e) {}*/
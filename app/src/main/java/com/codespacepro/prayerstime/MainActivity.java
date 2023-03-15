package com.codespacepro.prayerstime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String fajrTime,dhuhrTime,asrTime,maghribTime,ishaTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://aladhan.p.rapidapi.com/timingsByCity?country=<REQUIRED>&city=<REQUIRED>")
                .get()
                .addHeader("X-RapidAPI-Key", "0bb1bec246msh35d21a378e92661p15ab45jsn456ec09c3b26")
                .addHeader("X-RapidAPI-Host", "aladhan.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                String responseData = response.body().string();
                JSONObject json = null;
                try {
                    json = new JSONObject(responseData);
                    JSONObject data = json.getJSONObject("data");
                    JSONObject timings = data.getJSONObject("timings");
                     fajrTime = timings.getString("Fajr");
                     dhuhrTime = timings.getString("Dhuhr");
                     asrTime = timings.getString("Asr");
                     maghribTime = timings.getString("Maghrib");
                     ishaTime = timings.getString("Isha");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                // Display the prayer times
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView fajrTextView = findViewById(R.id.fajrTextView);
                        TextView dhuhrTextView = findViewById(R.id.dhuhrTextView);
                        TextView asrTextView = findViewById(R.id.asrTextView);
                        TextView maghribTextView = findViewById(R.id.maghribTextView);
                        TextView ishaTextView = findViewById(R.id.ishaTextView);

                        fajrTextView.setText("Fajr: " + fajrTime);
                        dhuhrTextView.setText("Dhuhr: " + dhuhrTime);
                        asrTextView.setText("Asr: " + asrTime);
                        maghribTextView.setText("Maghrib: " + maghribTime);
                        ishaTextView.setText("Isha: " + ishaTime);
                    }
                });
            }
        });

    }
}
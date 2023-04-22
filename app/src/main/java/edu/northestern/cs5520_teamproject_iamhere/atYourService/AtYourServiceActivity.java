package edu.northestern.cs5520_teamproject_iamhere.atYourService;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import edu.northestern.cs5520_teamproject_iamhere.MainActivity;
import edu.northestern.cs5520_teamproject_iamhere.R;
import edu.northestern.cs5520_teamproject_iamhere.adapter.PollutantAdapter;
import edu.northestern.cs5520_teamproject_iamhere.entity.Pollutant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AtYourServiceActivity extends AppCompatActivity {

    private static final int RESPONSE_CODE = 200;
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "2343b78b8c23033f2983f5dd6a27a437";

    public static String lat;
    public static String lon;
    private TextView weatherData;
    ProgressDialog progressDialog;
    private RecyclerView.LayoutManager rLayoutManager;

    private List<Pollutant> pollutantList = new ArrayList<>();
    private PollutantAdapter adaptor;
    private RecyclerView rView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_your_service);
        // init(savedInstanceState);
        findViewById(R.id.getButton).setOnClickListener(v -> getCurrentData());
        createRecyclerView();
        Button buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> backToMainActivity());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(AtYourServiceActivity.this, "Delete View", Toast.LENGTH_SHORT).show();
                int position = viewHolder.getLayoutPosition();
                pollutantList.remove(position);
                adaptor.notifyItemRemoved(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(rView);
    }


    private void createRecyclerView() {
        rLayoutManager = new LinearLayoutManager(this);
        rView = findViewById(R.id.item_recycler_view);
        rView.setHasFixedSize(true);
        adaptor = new PollutantAdapter(pollutantList);
        rView.setAdapter(adaptor);
        rView.setLayoutManager(rLayoutManager);
    }


    void getCurrentData() {

        adaptor.clearData();

        EditText editLat = findViewById(R.id.enterLatitude);
        lat = editLat.getText().toString();

        EditText editLon = findViewById(R.id.enterLongitude);
        lon = editLon.getText().toString();

        boolean isLegal = true;
        double latDouble = 0.0, lonDouble = 0.0;
        if (lat.isEmpty() || lon.isEmpty()) {
            isLegal = false;
        } else {
            try {
                latDouble = Double.parseDouble(lat);
                lonDouble = Double.parseDouble(lon);
            } catch (java.lang.NumberFormatException e) {
                isLegal = false;
            }
        }
        if (latDouble < -90 || latDouble > 90 || lonDouble < -180 || lonDouble > 180) {
            isLegal = false;
        }

        if (isLegal) {
            lat = String.valueOf(latDouble);
            lon = String.valueOf(lonDouble);
            progressDialog = new ProgressDialog(AtYourServiceActivity.this);
            progressDialog.setMessage("getting data ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AirPollutionService service = retrofit.create(AirPollutionService.class);
            Call<AirPollutionResponse> call = service.getCurrentWeatherData(lat, lon, AppId);
            call.enqueue(new Callback<AirPollutionResponse>() {
                @Override
                public void onResponse(@NonNull Call<AirPollutionResponse> call, @NonNull Response<AirPollutionResponse> response) {
                    if (response.code() == RESPONSE_CODE) {
                        AirPollutionResponse airPollutionResponse = response.body();
                        assert airPollutionResponse != null;

                        int no2index, pm10index, o3index, pm2_5index;
                        if (airPollutionResponse.list.get(0).components.no2 < 50) {
                            no2index = 1;
                        } else if (airPollutionResponse.list.get(0).components.no2 < 100) {
                            no2index = 2;
                        } else if (airPollutionResponse.list.get(0).components.no2 < 200) {
                            no2index = 3;
                        } else if (airPollutionResponse.list.get(0).components.no2 < 400) {
                            no2index = 4;
                        } else {
                            no2index = 5;
                        }
                        if (airPollutionResponse.list.get(0).components.pm10 < 25) {
                            pm10index = 1;
                        } else if (airPollutionResponse.list.get(0).components.pm10 < 50) {
                            pm10index = 2;
                        } else if (airPollutionResponse.list.get(0).components.pm10 < 90) {
                            pm10index = 3;
                        } else if (airPollutionResponse.list.get(0).components.pm10 < 180) {
                            pm10index = 4;
                        } else {
                            pm10index = 5;
                        }
                        if (airPollutionResponse.list.get(0).components.o3 < 60) {
                            o3index = 1;
                        } else if (airPollutionResponse.list.get(0).components.o3 < 120) {
                            o3index = 2;
                        } else if (airPollutionResponse.list.get(0).components.o3 < 180) {
                            o3index = 3;
                        } else if (airPollutionResponse.list.get(0).components.o3 < 240) {
                            o3index = 4;
                        } else {
                            o3index = 5;
                        }
                        if (airPollutionResponse.list.get(0).components.pm2_5 < 15) {
                            pm2_5index = 1;
                        } else if (airPollutionResponse.list.get(0).components.pm2_5 < 30) {
                            pm2_5index = 2;
                        } else if (airPollutionResponse.list.get(0).components.pm2_5 < 55) {
                            pm2_5index = 3;
                        } else if (airPollutionResponse.list.get(0).components.pm2_5 < 110) {
                            pm2_5index = 4;
                        } else {
                            pm2_5index = 5;
                        }

                        long overall_index = Math.round((no2index+pm10index+o3index+pm2_5index)/4.0);
                        pollutantList.add(0, new Pollutant("no2", no2index));
                        adaptor.notifyItemInserted(0);
                        pollutantList.add(1, new Pollutant("pm10", pm10index));
                        adaptor.notifyItemInserted(1);
                        pollutantList.add(2, new Pollutant("o3", o3index));
                        adaptor.notifyItemInserted(2);
                        pollutantList.add(3, new Pollutant("pm2.5", pm2_5index));
                        adaptor.notifyItemInserted(3);
                        pollutantList.add(4, new Pollutant("overall", (int)overall_index));
                        adaptor.notifyItemInserted(4);
                        progressDialog.dismiss();
                        View parentLayout = findViewById(android.R.id.content);
                        Snackbar.make(parentLayout, R.string.pollutant_add_success, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AirPollutionResponse> call, @NonNull Throwable t) {

                }
            });
        } else {

            Toast.makeText(this, "Please enter correct coordinates!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void backToMainActivity () {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Exiting Activity")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", null)
                .show();
    }
}

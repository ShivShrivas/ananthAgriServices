package com.project.aas.ui.home;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.project.aas.R;
import com.project.aas.adapter.WeatherRVAdapter;
import com.project.aas.model.WeatherRVModal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Weather extends AppCompatActivity {


    private RelativeLayout homeRL;
    private ProgressBar loadingPB;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private TextInputEditText cityEdit;
    private ImageView backIV,iconIV,searchIV;
    private RecyclerView weatherRV;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE=1;
    private String cityName;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

                homeRL=findViewById(R.id.idRLHome);
                loadingPB=findViewById(R.id.idPBLoading);
                cityNameTV=findViewById(R.id.idTVCityName);
                temperatureTV=findViewById(R.id.idTVTemperature);
                conditionTV=findViewById(R.id.idTVCondition);
                cityEdit=findViewById(R.id.idEdtcity);
                weatherRV=findViewById(R.id.idRVWeather);
                backIV=findViewById(R.id.idIVBack);
                iconIV=findViewById(R.id.idIVIcon);
                searchIV=findViewById(R.id.idIVSearch);
                weatherRVModalArrayList=new ArrayList<>();
                weatherRVAdapter=new WeatherRVAdapter(this,weatherRVModalArrayList);
                weatherRV.setAdapter(weatherRVAdapter);

                locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location=task.getResult();
                            if(location!=null){
                                Geocoder geocoder=new Geocoder(Weather.this, Locale.getDefault());
                                try {
                                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    cityName=getCityName(location.getLongitude(),location.getLatitude());
                                    cityNameTV.setText(cityName);
                                    getWeatherInfo(cityName);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });
                }
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Weather.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
                }

                searchIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String city=cityEdit.getText().toString();
                        if(city.isEmpty()){
                            Toast.makeText(Weather.this,"please Enter city Name",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Weather.this, "City Entered", Toast.LENGTH_SHORT).show();
                            //cityNameTV.setText(cityName);
                            getWeatherInfo(city);
                        }
                    }
                });
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                if(requestCode==PERMISSION_CODE){
                    if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            private String getCityName(double longitute, double latitute){
                String cityName="Not Found";
                Geocoder gcd=new Geocoder(getBaseContext(), Locale.getDefault());
                try {
                    List<Address> addresses=gcd.getFromLocation(latitute,longitute,10);
                    for (Address adr:addresses){
                        if(adr!=null){
                            String city = adr.getLocality();
                            if(city!=null && !city.equals("")){
                                cityName=city;
                            }
                        }
                    }
                }catch(IOException e) {
                    e.printStackTrace();
                }
                return cityName;

            }

            private void getWeatherInfo(String cityName){
                String url="https://api.weatherapi.com/v1/forecast.json?key=94f950b37a5f4f268c282447210108&q="+cityName+"&days=1&aqi=yes&alerts=yes";
                cityNameTV.setText(cityName);
                RequestQueue requestQueue = Volley.newRequestQueue(Weather.this);

                //ERROR

                //Log.i("Error","error begin");
                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        homeRL.setVisibility(View.VISIBLE);
                        weatherRVModalArrayList.clear();
                        try {
                            String temperature=response.getJSONObject("current").getString("temp_c");
                            temperatureTV.setText(temperature+"°C");
                            int isDay=response.getJSONObject("current").getInt("is_day");
                            String condition=response.getJSONObject("current").getJSONObject("condition").getString("text");
                            String conditionIcon=response.getJSONObject("current").getJSONObject("condition").getString("icon");
                            Picasso.get().load("https:".concat(conditionIcon)).into(iconIV);
                            conditionTV.setText(condition);
                            if(isDay==1){
                                Picasso.get().load("https://i.pinimg.com/originals/11/02/6d/11026d6e10cf4b647ffb5631e02f2d2e.png").into(backIV);
                            }else{
                                Picasso.get().load("https://i.pinimg.com/originals/11/02/6d/11026d6e10cf4b647ffb5631e02f2d2e.png").into(backIV);
                            }
                            JSONObject forecastObj=response.getJSONObject("forecast");
                            JSONObject forcast0=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                            JSONArray hourArray=forcast0.getJSONArray("hour");
                            for(int i=0;i<hourArray.length();i++){
                                JSONObject hourObj=hourArray.getJSONObject(i);
                                String time = hourObj.getString("time");
                                String temper = hourObj.getString("temp_c");
                                String icon= hourObj.getJSONObject("condition").getString("icon");
                                String wind = hourObj.getString("wind_kph");
                                weatherRVModalArrayList.add(new WeatherRVModal(time,temper,icon,wind));
                            }
                            weatherRVAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Weather.this, "Please enter valid city name..", Toast.LENGTH_SHORT).show();

                    }
                });
                requestQueue.add(jsonObjectRequest);

            }

        }
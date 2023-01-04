package com.example.coronatracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements  onItemClick{
    public RequestQueue queue ;
    public static DecimalFormat df = new DecimalFormat("0.00");
    public ArrayList<countrystructure> countrystructureArrayList = new ArrayList<>();
    countryAdapter countAdapter;
    TextView casetype ;

    RecyclerView countryRecycler;
    public TextView total;
    public TextView casesCount;
    public TextView country;
    TextView newcases;
    Button activebtn;
    Button recoveredbtn;
    Button deadbtn;
    ConstraintLayout circle ;
    TextView percent;
    int position=1;




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            this.getSupportActionBar().hide();
        }catch (Exception e){}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.custom, this.getTheme()));

        }

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        if(connected == false){
            TextView connection = findViewById(R.id.noconnection);
            connection.setText("No Connection Restart App");
        }
        else {
            extractIntent();
            casetype = findViewById(R.id.caseType);
            circle = findViewById(R.id.constraintLayout4);
            total = findViewById(R.id.totalcases);
            casesCount = findViewById(R.id.casesCount);
            country = findViewById(R.id.country);
            activebtn = findViewById(R.id.activebtn);
            recoveredbtn = findViewById(R.id.recoveredbtn);
            deadbtn = findViewById(R.id.deadbtn);
            percent = findViewById(R.id.percent);
            newcases = findViewById(R.id.newcases);


            queue = Volley.newRequestQueue(this);
            jsonParse();


        }





    }
    public void jsonParse(){
        String url = "https://api.covid19api.com/summary";

        JsonObjectRequest object = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {



                        try {
                            JSONArray country = response.getJSONArray("Countries");

                            for(int i =0 ; i<country.length(); i++){
                                JSONObject count  = country.getJSONObject(i);
                                String desh = count.getString("Country");
                                int active = count.getInt("TotalConfirmed");
                                int newActive = count.getInt("NewConfirmed");
                                int recovered = count.getInt("TotalRecovered");
                                int newRecovered = count.getInt("NewRecovered");
                                int death = count.getInt("TotalDeaths");
                                int newDeath= count.getInt("NewDeaths");
                                countrystructureArrayList.add(new countrystructure(desh,active,recovered,death,newActive,newRecovered,newDeath));


                            }
                            ConstraintLayout gif = findViewById(R.id.gif);
                            gif.setVisibility(View.INVISIBLE);
                            ImageView img = findViewById(R.id.img);
                            img.setVisibility(View.INVISIBLE);

                            sortCountry();
                            setCountryRecyclerView(countrystructureArrayList);
                            setViews();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof TimeoutError){
                    jsonParse();
                }
            }
        });
        queue.add(object);
    }
    public void extractIntent(){
        Intent intent = getIntent();
        int position = intent.getIntExtra("position",-1);
        if(position>=0)

        country.setText(""+position);
    }
    public void setViews(){

        country.setText(countrystructureArrayList.get(position).getCountryName());
        total.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getTotalCases()));
        casesCount.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getActivecases()));
        activebtn.setBackgroundColor(Color.parseColor("#adc6be"));
        deadbtn.setBackgroundColor(Color.WHITE);
        recoveredbtn.setBackgroundColor(Color.WHITE);
        casetype.setText("Active Cases");


        if(countrystructureArrayList.get(position).getNewActive()>=0)
            newcases.setText("+"+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewActive()));
        else
            newcases.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewActive()));

        float per= ((float)countrystructureArrayList.get(position).getActivecases()/(float)countrystructureArrayList.get(position).getTotalCases())*100;

        percent.setText(""+df.format(per)+"%");



    }

    public void activeButton(View v){


        activebtn.setBackgroundColor(Color.parseColor("#adc6be"));
        deadbtn.setBackgroundColor(Color.WHITE);
        recoveredbtn.setBackgroundColor(Color.WHITE);
        casetype.setText("Active Cases");
        casesCount.setText(""+ NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getActivecases()));
        if(countrystructureArrayList.get(position).getNewActive()>=0)
        newcases.setText("+"+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewActive()));
        else
            newcases.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewActive()));


        float per= ((float)countrystructureArrayList.get(position).getActivecases()/(float)countrystructureArrayList.get(position).getTotalCases())*100;
        percent.setText(""+df.format(per)+"%");

    }
    public void deadButton(View v){


        deadbtn.setBackgroundColor(Color.parseColor("#adc6be"));

        recoveredbtn.setBackgroundColor(Color.WHITE);
        activebtn.setBackgroundColor(Color.WHITE);
        casetype.setText("Dead Cases");
        casesCount.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getTotalDeaths()));
        if(countrystructureArrayList.get(position).getNewActive()>=0)
            newcases.setText("+"+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewDeaths()));
        else
            newcases.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewDeaths()));

        float per= ((float)countrystructureArrayList.get(position).getTotalDeaths()/(float)countrystructureArrayList.get(position).getTotalCases())*100;
        percent.setText(""+df.format(per)+"%");



    }

    public void recoveredButton(View v){
        recoveredbtn.setBackgroundColor(Color.parseColor("#adc6be"));

        activebtn.setBackgroundColor(Color.WHITE);
        deadbtn.setBackgroundColor(Color.WHITE);
        casetype.setText("Recovered ");
        casesCount.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getRecoveredCases()));
        if(countrystructureArrayList.get(position).getNewActive()>=0)
            newcases.setText("+"+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewRecovered()));
        else
            newcases.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countrystructureArrayList.get(position).getNewRecovered()));

        float per= ((float)countrystructureArrayList.get(position).getRecoveredCases()/(float)countrystructureArrayList.get(position).getTotalCases())*100;

        percent.setText(""+df.format(per)+"%");


    }


    public void setCountryRecyclerView(List<countrystructure> countriesList){
        countryRecycler = findViewById(R.id.countryrecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        countryRecycler.setLayoutManager(layoutManager);

        countAdapter = new countryAdapter(this,countriesList,this);
        countryRecycler.setAdapter(countAdapter);

    }

    public void sortCountry(){
        List<countrystructure> sortedCountries  = (ArrayList<countrystructure>)countrystructureArrayList.clone();
        Collections.sort(countrystructureArrayList);

    }


    @Override
    public void onClick(int value) {
        position=value;
        setViews();
        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();



    }
}

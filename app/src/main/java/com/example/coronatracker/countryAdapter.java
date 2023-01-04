package com.example.coronatracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class countryAdapter extends RecyclerView.Adapter<countryAdapter.CountryViewHolder> {
    Context context;
    List<countrystructure> countryList;
    onItemClick mcallback;
    public countryAdapter(Context context,List<countrystructure> countryList,onItemClick listener){
        this.context=context;
        this.countryList=countryList;
        this.mcallback= listener;
    }



    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.countryactivity,parent ,false);

        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        holder.country.setText(countryList.get(position).getCountryName());
        holder.totalCases.setText(""+NumberFormat.getNumberInstance(Locale.US).format(countryList.get(position).getTotalCases()));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcallback.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public static final class CountryViewHolder extends RecyclerView.ViewHolder{
        TextView country;
        TextView totalCases;
        ConstraintLayout parent;
        public CountryViewHolder(View itemView){
            super(itemView);
            country = itemView.findViewById(R.id.countryadapter);
            totalCases = itemView.findViewById(R.id.totalcasesadapter);
            parent = itemView.findViewById(R.id.countryadapteractivity);


        }

    }
}

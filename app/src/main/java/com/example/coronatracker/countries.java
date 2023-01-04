package com.example.coronatracker;

public class countries {
    String country;
    int totalCases;
    countries(String country,int totalCases){
        this.country= country;
        this.totalCases=totalCases;
    }

    public String getCountry() {
        return country;
    }

    public int getTotalCases() {
        return totalCases;
    }
}

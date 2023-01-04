package com.example.coronatracker;

import java.io.Serializable;

public class countrystructure implements  Comparable<countrystructure>, Serializable {
    String countryName;
    int totalCases;
    int activecases;
    int newActive;
    int recoveredCases;
    int newRecovered;
    int totalDeaths;
    int newDeaths;
    countrystructure(String countryName,int totalCases,int recoveredCases,int totalDeaths,int newActive,int newRecovered, int newDeaths){
        this.countryName = countryName;
        this.totalCases = totalCases;
        this.recoveredCases = recoveredCases;
        this.totalDeaths = totalDeaths;
        this.activecases = totalCases - recoveredCases - totalDeaths;
        this.newDeaths = newDeaths;
        this.newRecovered = newRecovered;
        this.newActive = newActive-newDeaths-newRecovered;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public int getActivecases() {
        return activecases;
    }

    public int getRecoveredCases() {
        return recoveredCases;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getNewActive() {
        return newActive;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    @Override
    public int compareTo(countrystructure o) {
        int compareCases = ((countrystructure)o).getTotalCases();
        return compareCases-this.totalCases;
    }
}

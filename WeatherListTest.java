package com.example.weatherapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class WeatherListTest {
    String city="Serres";
    String date="Mon,25 Dec 2019 23:00";
    String temp="24";
    String weather="Βροχερός";
    String wind="7";

    public WeatherListTest(int i, int i1, String expected, String date, String temp, String weather, String wind) {
    }

    @Test
    public void getId() {
    }

    @Test
    public void getOldWeatherID() {
    }

    @Test
    public void getCity() {
        String expected="Serres";
        WeatherListTest weatherList = new WeatherListTest(1,1,expected,date,temp,weather,wind);
        assertSame("Check your WeatherList.java or your expected here.",city);
    }

    @Test
    public void getDt() {
        String expected="Mon,25 Dec 2019 23:00";
        WeatherListTest weatherList = new WeatherListTest(1,1,"Serres",expected,temp,weather,wind);
        assertSame("Check your WeatherList.java or your expected here.",date);
    }
    @Test
    public void getMain() {
        String expected="24";
        WeatherListTest weatherList = new WeatherListTest(1,1,"Serres",date,expected,weather,wind);
        assertSame("Check your WeatherList.java or your expected here.",temp);
    }
    @Test
    public void getWeather() {
        String expected="Βροχερός";
        WeatherListTest weatherList = new WeatherListTest(1,1,"Serres",date,temp,expected,wind);
        assertSame("Check your WeatherList.java or your expected here.",weather);
    }
    @Test
    public void getWind() {
        String expected="7";
        WeatherListTest weatherList = new WeatherListTest(1,1,"Serres",date,temp,weather,expected);
        assertSame("Check your WeatherList.java or your expected here.",wind);
    }
}
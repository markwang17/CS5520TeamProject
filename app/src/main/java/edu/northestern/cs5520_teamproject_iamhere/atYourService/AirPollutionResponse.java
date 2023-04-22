package edu.northestern.cs5520_teamproject_iamhere.atYourService;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AirPollutionResponse {
    @SerializedName("coord")
    public Coord coord;

    @SerializedName("list")
    ArrayList<List> list = new ArrayList<List>();

}

class List {
    @SerializedName("dt")
    public long dt;
    @SerializedName("main")
    public Main main;
    @SerializedName("components")
    public Components components;

}


class Main {
    @SerializedName("aqi")
    public float aqi;

}

class Components {
    @SerializedName("no2")
    public float no2;
    @SerializedName("o3")
    public float o3;
    @SerializedName("pm2_5")
    public float pm2_5;
    @SerializedName("pm10")
    public float pm10;
}

class Coord {
    @SerializedName("lon")
    public float lon;
    @SerializedName("lat")
    public float lat;

}

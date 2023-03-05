package edu.northestern.cs5520_teamproject_iamhere.entity;

public class Pollutant {
    private final String name;
    private final int index;

    public Pollutant(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}

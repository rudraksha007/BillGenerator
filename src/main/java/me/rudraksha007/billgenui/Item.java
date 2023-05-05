package me.rudraksha007.billgenui;

import java.util.*;

public class Item {

    private String name;
    private String brand;
    private Map<String, Integer> sizes;


    public Item(String name, String brand, Map<String,Integer>sizes){
        this.name = name;
        this.brand = brand;
        this.sizes = sizes;
    }

    public Item (String serializedData){
        String[] data = serializedData.split(":");
        this.name = data[0].split(",")[0];
        this.brand = data[0].split(",")[1];
        this.sizes = new HashMap<>();
        for (int i = 1; i < data.length; i++) {
         sizes.put(data[i].split(",")[0], Integer.parseInt(data[i].split(",")[1]));
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,Integer> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Integer> sizes) {
        this.sizes = sizes;
    }

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void addSize(String size, int cost){
        sizes.put(size, cost);
    }

    public String toString(){
        StringBuilder s = new StringBuilder(name+","+brand);
        for (String size: sizes.keySet()){
            s.append(":").append(size).append(",").append(sizes.get(size));
        }
        return s.toString();
    }
}

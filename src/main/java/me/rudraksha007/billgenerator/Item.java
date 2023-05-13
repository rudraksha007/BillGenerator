package me.rudraksha007.billgenerator;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String name;
    private String brand;
    private String hsn;
    private Map<String, Float> sizes;


    public Item(String name, String brand,String hsn, Map<String,Float>sizes){
        this.name = name;
        this.brand = brand;
        this.sizes = sizes;
        this.hsn = hsn;
    }

    public Item (String serializedData){
        String[] data = serializedData.split(":");
        this.name = data[0].split(",")[0];
        this.brand = data[0].split(",")[1];
        this.hsn = data[0].split(",")[2].split(",")[0];
        this.sizes = new HashMap<>();
        for (int i = 1; i < data.length; i++) {
            sizes.put(data[i].split(",")[0], Float.parseFloat(data[i].split(",")[1]));
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,Float> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, Float> sizes) {
        this.sizes = sizes;
    }

    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public void addSize(String size, float cost){
        sizes.put(size, cost);
    }

    public String toString(){
        StringBuilder s = new StringBuilder(name+","+brand+","+hsn);
        for (String size: sizes.keySet()){
            s.append(":").append(size).append(",").append(sizes.get(size));
        }
        return s.toString();
    }
}

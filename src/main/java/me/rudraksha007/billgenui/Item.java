package me.rudraksha007.billgenui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Item {

    private String name;
    private List<String>sizes;

    public Item(String name, List<String>sizes){
        this.name = name;
        this.sizes = sizes;
    }

    public Item (String serializedData){
        String[] data = serializedData.split(":");
        this.name = data[0];
        this.sizes = new ArrayList<>();
        sizes.addAll(Arrays.asList(data).subList(1, data.length));

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public void addSize(String size){
        sizes.add(size);
    }

    public String toString(){
        StringBuilder s = new StringBuilder(name);
        for (String size: sizes){
            s.append(":").append(size);
        }
        return s.toString();
    }
}

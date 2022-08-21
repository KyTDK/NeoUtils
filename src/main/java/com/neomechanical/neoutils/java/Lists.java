package com.neomechanical.neoutils.java;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static <K, G> List<K> cast(List<G> list) {
        ArrayList<K> newlyCastedArrayList = new ArrayList<>();
        for (G listObject : list) {
            //Check cast
            newlyCastedArrayList.add((K) listObject);

        }
        return newlyCastedArrayList;
    }
}

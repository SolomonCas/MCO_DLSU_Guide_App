package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


public class DataHelper {
    public static Directory[] initializeData() {
        String[] directoryNames = {"McDonald's", "Jollibee", "Chowking", "KFC", "Burger King"};
        String[] locations = {"2399 Taft Ave, Malate, Manila, 1004 Metro Manila",
                "855 Taft Avenue, cor Ocampo St, Malate, Manila City, Metro Manila",
                "Level 1, D'Student's Place, Commercial Center, 2488 Taft Ave, Malate, Manila, 1004 Metro Manila",
                "University Mall, 2057 Taft Ave, Malate, Manila, 1004 Metro Manila",
                "1017 Taft Ave, Malate, Manila, 1017 Metro Manila"};
        Integer[] images = {R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3,
                R.drawable.image4,
                R.drawable.image5};
        String tags = "Fast-Food Restaurant";

        Directory[] data = new Directory[directoryNames.length];
        for(int i = 0; i < directoryNames.length; i++){
            data[i] = new Directory(directoryNames[i], locations[i], images[i], tags);

        }
        return data;
    }
}

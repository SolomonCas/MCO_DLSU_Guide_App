package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        Review[] reviews = initializeReviewData();

        Directory[] data = new Directory[directoryNames.length];
        for(int i = 0; i < directoryNames.length; i++){
            data[i] = new Directory(directoryNames[i], locations[i], images[i], tags);

        }

        data[0].addReview(reviews[0]);
        data[0].addReview(reviews[0]);
        data[0].addReview(reviews[1]);
        data[0].addReview(reviews[1]);
        data[0].addReview(reviews[2]);
        data[0].addReview(reviews[2]);

        data[1].addReview(reviews[0]);
        data[1].addReview(reviews[1]);
        data[1].addReview(reviews[1]);
        data[1].addReview(reviews[2]);

        data[2].addReview(reviews[0]);
        data[2].addReview(reviews[0]);

        return data;
    }
    public static Review[] initializeReviewData() {
        String[] reviewContent = {"Yesterday is history, Tomorrow is a mystery, but today is a gift. " +
                "That is why it is called the present.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris lacinia, risus eget " +
                        "lobortis volutpat, justo risus sodales eros, nec aliquam neque ante nec nulla. " +
                        "Nullam pretium tempus massa, non consequat tellus tristique nec.",
                "Nice"};
        Integer[] images = {R.drawable.image1,
                null,
                R.drawable.image2};
        float[] rates = {2.5f, 1.0f, 3.0f};
        String[] dateStrings = {"November 17, 2023", "November 15, 2023", "November 5, 2023"};
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        Date[] dates = new Date[dateStrings.length];
        for (int i = 0; i < dateStrings.length; i++) {
            try {
                dates[i] = dateFormat.parse(dateStrings[i]);
            } catch (ParseException e) {
                e.printStackTrace(); // Handle parsing exception here
            }
        }

        User[] users = initializeUserData();

        Review[] data = new Review[reviewContent.length];
        for(int i = 0; i < reviewContent.length; i++){
            data[i] = new Review(reviewContent[i], images[i], rates[i], users[i].getUserID(), dates[i]);
        }
        return data;
    }
    public static User[] initializeUserData() {
        String[] userNames = {"user1", "user2", "user3", "user4", "user5"};
        String[] emails = {"solomon_castillo@dlsu.edu.ph",
                "alma_merrill@dlsu.edu.ph",
                "ashleigh_ware@dlsu.edu.ph",
                "daniella_deleon@dlsu.edu.ph",
                "rafferty_durham@dlsu.edu.ph"};
        int[] idNo = {12072303,
                12072304,
                12072305,
                12072306,
                12072307};
        String passwords = "12345";

        User[] data = new User[userNames.length];
        for(int i = 0; i < userNames.length; i++){
            data[i] = new User(userNames[i], emails[i], passwords, idNo[i]);
        }

        return data;
    }
}

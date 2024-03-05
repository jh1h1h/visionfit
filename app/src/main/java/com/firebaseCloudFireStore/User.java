package com.firebaseCloudFireStore;

import java.util.List;

public class User {

    public String country;
    public int streak;
    public int repsToday;
    public int repsAllTime;




    public User() {
        // Must have a public no-argument constructor
    }

    // Initialize all fields of a city
    public User(

            String country,
            int streak,
            int repsToday,
            int repsAllTime) {
        this.country=country;
        this.streak=streak;
        this.repsToday=repsToday;
        this.repsAllTime=repsAllTime;
    }
}

package Team10_VisionFit.Backend.firebaseCloudFireStore;

public class User {

    public String country;
    public int streak;
    public int repsToday;
    public int repsAllTime;




    public User() {
        // Must have a public no-argument constructor
        this.repsAllTime=-1;//dummy user
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

package Team10_VisionFit.Backend.firebaseAuthentication;

public class User {

    public String country;
    public String username;
    public String dob;
    public int streak;
    //pushup,situp,squats,weightlift
    public int pushupToday;
    public int pushupAllTime;
    public int situpToday;
    public int situpAllTime;
    public int squatToday;
    public int squatAllTime;
    public int weightliftToday;
    public int weightliftAllTime;




    public User() {
        // Must have a public no-argument constructor
        this.streak=-1;//dummy user
    }

    // Initialize all fields of a city
    public User(
            String username,
            String country,
            String dob,
            int streak,

                int pushupToday,
            int pushupAllTime,
            int situpToday,
            int situpAllTime,
            int squatToday,
            int squatAllTime,
            int weightliftToday,
            int weightliftAllTime
    ) {
        this.country=country;
        this.streak=streak;
        this.username=username;
        this.dob=dob;
        this.pushupToday=pushupToday;
        this.pushupAllTime=pushupAllTime;
        this.situpToday=situpToday;
        this.situpAllTime=situpAllTime;
        this.squatToday=squatToday;
        this.squatAllTime=squatAllTime;
        this.weightliftToday=weightliftToday;
        this.weightliftAllTime=weightliftAllTime;
    }
}

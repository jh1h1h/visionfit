package Team10_VisionFit.Backend.firebaseAuthentication;

public class User {

    public String country;
    public String username;
    public String dob;
    public boolean streakChange;
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
    public int numSquatsChallengeCompleted;
    public int numPushupsChallengeCompleted;
    public boolean hasCompletedSquatsChallengeToday;
    public boolean hasCompletedPushupsChallengeToday;
    public int current_points;
    public int lifetime_points;
    public int num_rewards1;
    public int num_rewards2;
    public int num_rewards3;
    public int num_rewards4;




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
            int weightliftAllTime,
            int numSquatsChallengeCompleted,
            int numPushupsChallengeCompleted,
            boolean hasCompletedSquatsChallengeToday,
            boolean hasCompletedPushupsChallengeToday,
            boolean streakChange,
            int current_points,
            int lifetime_points,
            int num_rewards1,
            int num_rewards2,
            int num_rewards3,
                    int num_rewards4
    ) {
        this.country=country;
        this.streakChange=streakChange;
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
        this.numSquatsChallengeCompleted=numSquatsChallengeCompleted;
        this.numPushupsChallengeCompleted=numPushupsChallengeCompleted;
        this.hasCompletedSquatsChallengeToday=hasCompletedSquatsChallengeToday;
        this.hasCompletedPushupsChallengeToday=hasCompletedPushupsChallengeToday;
        this.current_points=current_points;
        this.lifetime_points=lifetime_points;
        this.num_rewards1=num_rewards1;
        this.num_rewards2=num_rewards2;
        this.num_rewards3=num_rewards3;
        this.num_rewards4=num_rewards4;

    }
}

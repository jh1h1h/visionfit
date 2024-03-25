VisionFit 2.0

⭐New features
✅Profile Page (Dummy ver)

✅Countdown for exercises working (refer to video demo) --> once users press start, countdown will begin and once the countdown ends they will be redirected back to daily challenges page AUTOMATICALLY

✅When logging out, users wil be prompted a confirmation (I totally didnt log out by accident too many times)

✅Added scrollbars to all pages with scrollview

✅Privacy Policy Page --> added fluff onto edgar's fluff

✅Check Pose/ Camera button added to nav bar  --> help me test if can launch from every page when yall pull

✅Changed icons to more consistent looking ones

✅Buttons in navbar will light up if you at the particular page ( i didnt do this though so idk how)

✅All buttons have a logcat feature tagged to "Button Check" to detect if its being clicked or not --> go to logcat and search for "Button Check" for testing



📖Renaming and Directory Changing
Java
⚪All Java classes are now in src/main/java/Team10_VisionFit and within this package
⚪all app UI in "UI" package
⚪all firebase stuff in "Backend" package
⚪all ML kit stuff in "PoseDetector" package

⚪merged Edgar's firebase's mainactivity into MainActivity, now we only have 1 MainActivity (no duplicates)
⚪deleted all of the og ML kit stuffs like SettingsActivity (we have one too but now no more duplicates)

Res
⚪deleted all old ML kit UI stuff in res as well as all the default stuff when a new project is created --> should have no traces of ML kit UI
⚪ deleted all duplcates of icons and logos
⚪ visionfit logo should only be found in mipmap
⚪renamed all icons to start with "icon"
⚪ renamed all exercise icons to start with "exercise"

❌Missing Features + Things we need discuss
⚪Leaderboard (to show daily and cumulative)--> work in progress
⚪Profile Page havent link to firebase
⚪Reward System How? --> need discuss
⚪About Us page --> not important but maybe put a pic of 7 of us


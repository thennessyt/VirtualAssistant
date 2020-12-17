package Template;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;

public class Main {


    static View v;
    static Character c;
    static ArrayList<ApplicationData> appData = new ArrayList<>();


    static String headings;

    static Calendar cal;
    static boolean hadToday = false;
    static String todayString = "";

    static Timer t;

    static Process process;

    static int currentDay = 0;




    static Runtime runtime;

    static File activityFile = new File("activity.csv");


    static String appScript = "tell application \"System Events\" to get the name of every process whose background only is false";

    //INIT FUNCTION
    /*

    CREATES CHARACTER OBJECT(s), CHECKS SAVE FILE, ETC.



     */
    public static void init(){

        //CHARACTER INIT
        c = new Character("Hexia", true, 3);

        //VIEW INIT
        v = new View(c);

        //get today
        cal = Calendar.getInstance();

        Date today = Calendar.getInstance().getTime();

        String monthString = today.toString();
        monthString = monthString.substring(monthString.indexOf(" ") + 1);
        String yearString = monthString.substring(monthString.lastIndexOf(" ") + 1);
        String dayString = monthString.substring(monthString.indexOf(" ") + 1);
        dayString = dayString.substring(0, monthString.indexOf(" "));
        monthString = monthString.substring(0, monthString.indexOf(" "));

        todayString = dayString + monthString + " " + yearString;

    }


    public static void main(String[] args){

        init();

    }

    //begins tracking, which is going to be a lot of work
    //I suppose it should gather a list of applications
    //  that are running. Then, it will compare that list
    //  to a text list kept in a file (this list will be
    //  empty on a first run.)
    //Then, it just
    //File is formatted as follows: CSV
    // COLUMN HEADINGS: (Application Name), (Date 1), (Date 2), (Date3)
    // (example) | Google Chrome, (usage on D1), (usage on D2), etc
    //if I have time:
    // (active time) | ACTIVE, (active on D1), (active on D2), etc
    // (blank row)
    //the active window time row comes after the application it refers to,
    // and its "application name" heading is always ACTIVE
    public static void Start_Tracking(){

        appData = new ArrayList<ApplicationData>();

        String nextLine;

        String windowname;

        String applicationName = "";

        String minutesForDay;

        //ApplicationData application;




        //open the file
        try {
            //if it opens
            FileReader fileReader = new FileReader(activityFile);

            //read in the lines
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            headings = bufferedReader.readLine();

            for (int i = 0; i < headings.length(); i++){
                if(headings.charAt(i) == ','){
                    currentDay++;
                }
            }
            currentDay--;

            //if today's date is not in the headings yet, put it at the end
            if(!(headings.contains(todayString))){
                headings = headings + ", " + todayString;
                hadToday = true;
            }

            int count = -1;
            //read in the next line
            nextLine = bufferedReader.readLine();
            while(nextLine != null){

                //if there is a comma (data line) do this:
                if (nextLine.contains(",")){

                    appData.add(new ApplicationData((nextLine.substring(0, nextLine.indexOf(","))).trim()));

                    count++;

                    //take off app name
                    nextLine = (nextLine.substring(nextLine.indexOf(",") + 1)).trim();

                    //loop through the numbers in nextLine
                    while (nextLine.length()>0){
                        //System.out.println("THIS TIME:_" + nextLine);
                        if(nextLine.contains(",")){
                            minutesForDay = nextLine.substring(0, nextLine.indexOf(","));
                            nextLine = nextLine.substring(nextLine.indexOf(",") + 1);
                        } else {
                            minutesForDay = nextLine;
                            nextLine = "";
                        }
                        //add the day of data
                        appData.get(count).addDayOfData(Integer.valueOf(minutesForDay));
                        nextLine = nextLine.trim();
                    }

                    while(appData.get(count).getWhichDay() != currentDay){
                        appData.get(count).addDayOfData(0);
                    }

                    nextLine = bufferedReader.readLine();

                } else {

                    nextLine = bufferedReader.readLine();

                }


            }

        //if there was no file no need to read so just make one
        } catch (FileNotFoundException e) {
            //c.setNewText("Sorry! I could not open the file. Please close the program.");

            headings = "Program, " + todayString;
            currentDay = 0;

            //create the new file
            try {
                activityFile.createNewFile();
            } catch (IOException ex) {
                c.setNewText("Sorry! I could not write the activity file. Please close the program.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            c.setNewText("Sorry! I could not open or create the file. Please close the program.");
        }



        //timer to check
        t = new Timer("app checker", true);

        TimerTask check = new TimerTask() {
            @Override
            public void run() {
                CheckApplications();
            }
        } ;

        t.scheduleAtFixedRate(check,0, 60000);

    }


    public static void Stop_Tracking(){

        c.setNewText("Tracking stopped! Click \"View Tracking\" to view it!");

        //stop timer task
        t.cancel();

        //write to activity file
        Write_File();


    }

    public static void Write_File(){

        if (appData != null) {
            try {
                PrintWriter fileWriter = new PrintWriter(activityFile);

                fileWriter.println(headings);

                for (int i = 0; i < appData.size(); i++) {
                    fileWriter.println(appData.get(i).getName() + ", " +
                            appData.get(i).getAppDataString());
                }

                fileWriter.flush();

                appData = null;

            } catch (IOException e) {
                c.setNewText("Sorry! I could not write the activity file. Please close the program.");

            }
        }


    }

    public static boolean CheckApplications(){
        runtime = Runtime.getRuntime();

        String[] args = { "osascript", "-e", appScript};
        try
        {
            process = runtime.exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String apps = in.readLine();
            ArrayList<String> appList = new ArrayList<>();

            while(apps.length() > 0){
                apps = apps.trim();
                if(apps.indexOf(",") == -1){
                    appList.add(apps);
                    apps = "";
                } else {
                    appList.add(apps.substring(0, apps.indexOf(",")));
                    apps = apps.substring(apps.indexOf(",") + 1);
                }
            }


            for (int i = 0; i < appData.size(); i++){
                for (int j = 0; j < appList.size(); j++){
                    if (appData.get(i).getName().equalsIgnoreCase(appList.get(j))){
                        appList.remove(j);
                        appData.get(i).addToLastDay(1);
                    }
                }
            }

            for (int i = 0; i < appList.size(); i++){
                appData.add(new ApplicationData(appList.get(i)));
                appData.get(appData.size()-1).addToLastDay(1);
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            c.setNewText("I can't see your applications, you have to enable access for assisted devices!");

            return false;
        }

    }




}//CLOSE MAIN CLASS

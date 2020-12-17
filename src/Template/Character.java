


package Template;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Character {



    static String name;
    static String dialogue = "";
    static boolean isMain = true;
    static double happiness;
    static boolean isStartup = true;
    static boolean isFirstTime = true;
    static View v = null;



    /*

    CREATES A NEW CHARACTER OBJECT

    The character

     */
    Character(String n, boolean main, int happy){

        this.name = n;
        this.isMain = main;
        this.happiness = happy;

    }




    //add whatever is passed in here,
    public double adjustHappiness(double x){
        happiness = happiness + x;

        //check and make sure it won't go over 5 or under 0
        if (happiness < 0){
            happiness = 0;
        } else if (happiness > 5){
            happiness = 5;
        }

        //return this since the image should update with that
        return happiness;
    }//close adjustHappiness



    static String updateInitialText(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        System.out.println(  );
        String timeStr = cal.getTime().toString();

        int time = Integer.valueOf(sdf.format(cal.getTime()));

        int timeMin = Integer.valueOf(timeStr.substring(timeStr.indexOf(':') + 1, timeStr.indexOf(':') +3));

        if (isStartup){
            isStartup = false;
            if (time <= 4){
                return "Ah, hello! It's so late! (or maybe it's early?)\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            } if (time <= 9){
                return "Oh, Good morning! It's great to see you today!\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            } if (time <= 13){
                return "Hello! I hope your day has been good so far!\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            } if (time <= 17){
                return "Good afternoon!\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            } if (time <= 21){
                return "Good evening!\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            } else {
                return "Hello! It's getting late isn't it?\n\nIt's currently " + time
                        + ":" + String.format("%02d", timeMin);
            }
        } else {
            return dialogue;
        }
    }


    public static void setNewText(String newDialogue){

        dialogue = newDialogue;

        v.text.setText(Character.updateInitialText());

    }


    String getName(){
        return this.name;
    }



} //close CLASS CHARACTER
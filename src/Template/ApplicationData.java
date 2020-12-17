package Template;

import java.util.ArrayList;

public class ApplicationData {

     String name = "";
     int count = 0;
     ArrayList<Integer> minutes = new ArrayList<>();


    //creates new ApplicationData Object
    ApplicationData(String appName){

        name = appName.toUpperCase();

    }


    //puts a new day at the end, increases the count
    public void addDayOfData(int data){

        minutes.add(data);

        count++;

    }

    public void addToLastDay(int mins){

        if (minutes.size() == 0){
            addDayOfData(1);
        } else {
            minutes.set(count - 1, minutes.get(count - 1) + mins);
        }

    }

    public String getName(){
        return name;
    }

    public String getAppDataString(){
        String mins = "";

        for (int i = 0; i < minutes.size(); i++){
            if (i == 0){
                mins = Integer.toString(minutes.get(i));
            } else {
                mins = mins + ", " + Integer.toString(minutes.get(i));
            }
        }
        return mins;
    }

    public int getWhichDay(){
        return (count - 1);
    }

}

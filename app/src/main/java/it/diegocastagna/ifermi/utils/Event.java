package it.diegocastagna.ifermi.utils;


import java.util.Comparator;

/**
 * Class that represents an Event
 */
public class Event implements Comparable<Event> {

    private String description;
    private String time;

    /**
     * @return description as a String
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return time as a String
     */
    public String getTime() {
        return time;
    }

    /**
     * Contructor class
     * @param description of the event
     * @param time of the event
     */
    public Event(String description, String time) {
        this.description = description;
        String timeAdjustedToTimeZone = time.replace(time.substring(0,time.indexOf(":")),String.valueOf(Integer.valueOf(time.substring(0,time.indexOf(":")))+1));
        this.time = normalizeTime(timeAdjustedToTimeZone);
    }

    /**
     * Method that normalizes the time to (GMT+2) timezone
     * @param time
     * @return String representing time
     */
    private String normalizeTime(String time){
        if(time.length()<5 && Integer.valueOf(time.substring(0,time.indexOf(":")))<10)
            time = "0" + time;
        if(time.length()<5 && Integer.valueOf(time.substring(time.indexOf(":")+1))<10)
            time = time.substring(0,time.indexOf(":")+1) + "0" + time.substring(time.indexOf(":")+1);
        return time;
    }

    /**
     * Compares to another event
     * @param e, represents another event
     * @return time difference between the two events
     */
    @Override
    public int compareTo(Event e) {

        int h1 = Integer.valueOf(time.substring(0,time.indexOf(":")));
        int h2 = Integer.valueOf(e.getTime().substring(0,time.indexOf(":")));
        if(h1!=h2)
            return h1-h2;
        int m1 = Integer.valueOf(time.substring(time.indexOf(":")+1));
        int m2 = Integer.valueOf(e.getTime().substring(time.indexOf(":")+1));
        return m1-m2;

    }

}

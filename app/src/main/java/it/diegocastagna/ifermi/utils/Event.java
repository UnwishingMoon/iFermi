package it.diegocastagna.ifermi.utils;


public class Event {

    private String description;
    private String time;

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }


    public Event(String description, String time) {
        this.description = description;
        String timeAdjustedToTimeZone = time.replace(time.substring(0,time.indexOf(":")),String.valueOf(Integer.valueOf(time.substring(0,time.indexOf(":")))+1));
        this.time = normalizeTime(timeAdjustedToTimeZone);
    }

    private String normalizeTime(String time){
        if(time.length()<5 && Integer.valueOf(time.substring(0,time.indexOf(":")))<10)
            time = "0" + time;
        if(time.length()<5 && Integer.valueOf(time.substring(time.indexOf(":")+1))<10)
            time = time.substring(0,time.indexOf(":")+1) + "0" + time.substring(time.indexOf(":")+1);
        return time;
    }
}

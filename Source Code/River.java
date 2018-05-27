/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riverDataIA;

import java.util.Calendar;
import java.text.DateFormat;
import java.util.Date;
import java.sql.Time;

public class River {
    int id;
    int location_id;
    Date date_collected;
    Time time_collected;
    String monitor_names;
    String observations;
    int e_coli_sample_1;
    int e_coli_sample_2;
    String abundance;
    String abundance_2;
    
    public  River(){
      
    }
    public void setID(int id_loc){
        id = id_loc;
    }
    public void setLocation(int loc){
        location_id = loc;
    }
    public void setDate(Date date){
        date_collected = date;
    }
    public void setTime(Time time){
        time_collected = time;
    }
    public void setMonitorNames(String mon){
        monitor_names = mon;
    }
    public void setObservations(String ob){
        observations = ob;
    }
    public void setEColi1(int a){
        e_coli_sample_1 = a;
    }
    public void setEColi2(int b){
        e_coli_sample_2 = b;
    }
    public void setAbundance(String a){
        abundance = a;
    }
    public void setAbundance2(String b){
        abundance_2 = b;
    }
    public String getMonitorNames(){
        return monitor_names;
    }
    public String getObservations(){
        return observations;
    }
    public int getEColi1(){
        return e_coli_sample_1;
    }
    public int getEColi2(){
        return e_coli_sample_2;
    }
    public String getAbundance(){
        return abundance;
    }
    public String getAbundance2(){
        return abundance_2;
    }
    public int getID(){
        return id;
    }
    public int getLocation(){
        return location_id;
    }
    public Date getDate(){
        return date_collected;
    }
    public Time getTime(){
        return time_collected;
    }
}

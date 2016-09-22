package app.fevermeter.org.Model;

import java.util.Date;

/**
 * Created by iam on 9/16/16.
 */
public class Fever {

    private double temperature;
    private long feverDate;
    private int feverTime;

    public Fever(double temperature, long feverDate,int feverTime) {
        this.temperature = temperature;
        this.feverDate = feverDate;
        this.feverTime = feverTime;
    }

    public long getFeverDate() {

        return feverDate;
    }

    public void setFeverDate(long feverDate) {
        this.feverDate = feverDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getFeverTime() {
        return feverTime;
    }

    public void setFeverTime(int feverTime) {
        this.feverTime = feverTime;
    }

    public Fever() {
    }


}

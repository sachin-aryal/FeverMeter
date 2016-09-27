package app.fevermeter.org.Model;

import java.util.Date;

/**
 * Created by iam on 9/16/16.
 */
public class Fever {

    private double temperature;
    private long feverDate;

    public Fever(double temperature, long feverDate) {
        this.temperature = temperature;
        this.feverDate = feverDate;
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

    public Fever() {
    }


}

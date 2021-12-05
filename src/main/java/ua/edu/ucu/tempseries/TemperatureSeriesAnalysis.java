package ua.edu.ucu.tempseries;

import java.util.Arrays;
import java.util.InputMismatchException;


public class TemperatureSeriesAnalysis {

    private double[] temperatureSeries;
    private int absoluteMinus = -273;
    private int maxTemp = 1000;

    public TemperatureSeriesAnalysis(double[] temperatureSeries) {
        this.temperatureSeries = Arrays.copyOf(temperatureSeries,
                temperatureSeries.length);
    }

    public double average() {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }

        double sum = 0;
        for (int i = 0; i < temperatureSeries.length; i++) {
            sum += temperatureSeries[i];
        }
        return sum/temperatureSeries.length;
    }

    public double deviation() {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }

        double sum = 0;
        double sqDiff = 0;

        for (int i = 0; i < temperatureSeries.length; i++) {
            sum += temperatureSeries[i];
        }

        double mean = (double) sum/(double) temperatureSeries.length;

        for (int i = 0; i < temperatureSeries.length; i++) {
            sqDiff += (temperatureSeries[i] - mean)
                    * (temperatureSeries[i] - mean);
        }

        return Math.sqrt((double) sqDiff / temperatureSeries.length);
    }

    public double min() {
        return findTempClosestToValue(absoluteMinus);
    }

    public double max() {
        return findTempClosestToValue(maxTemp);
    }

    public double findTempClosestToZero() {
        return findTempClosestToValue(0);
    }

    public double findTempClosestToValue(double tempValue) {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }

        double diff = Math.abs(temperatureSeries[0] - tempValue);
        double closest = temperatureSeries[0];

        for (int i = 0; i < temperatureSeries.length; i++) {
            double difference = Math.abs(temperatureSeries[i] - tempValue);
            if (difference < diff) {
                diff = difference;
                closest = temperatureSeries[i];
            }
            else if (difference == diff) {
                closest = Math.max(closest, temperatureSeries[i]);
            }
        }
        return closest;
    }

    public double[] findTempsLessThen(double tempValue) {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }

        int size = helper(tempValue, 1);
        double[] lessArray = new double[size];

        int i = 0;
        int j = 0;

        while (i < temperatureSeries.length) {

            if (temperatureSeries[i] < tempValue) {
                lessArray[j] = temperatureSeries[i];
                j++;
            }
            i++;
        }

        return lessArray;
    }

    public int helper(double tempValue, int indicator) {
        int counter = 0;
        for (int i = 0; i < temperatureSeries.length; i++) {
            if (tempValue > temperatureSeries[i]) {
                counter++;
            }
        }
        if (indicator == 1) {
            return counter;
        }
        return temperatureSeries.length - counter;
    }

    public double[] findTempsGreaterThen(double tempValue) {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }

        int size = helper(tempValue, 0);
        double[] greaterArray = new double[size];

        int i = 0, j = 0;

        while (i < temperatureSeries.length) {

            if (temperatureSeries[i] >= tempValue) {
                greaterArray[j] = temperatureSeries[i];
                j++;
            }
            i++;
        }

        return greaterArray;
    }


    public final TempSummaryStatistics summaryStatistics() {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }


        double avg = this.average();
        double dev = this.deviation();
        double min = this.min();
        double max = this.max();

        return new TempSummaryStatistics(avg, dev, min, max);

    }

    public int addTemps(double... temps) {

        if (temperatureSeries.length == 0) {
            throw new IllegalArgumentException("temperatureSeries is empty");
        }
        for (double temp : temps) {
            if (temp < min()) {
                throw new InputMismatchException(
                        "The temperature exceeds minimum value!");
            }
        }
        int capacity = temperatureSeries.length;

        while (capacity < temperatureSeries.length + temps.length) {
            capacity *= 2;
        }
        double[] newTemperatureSeries = new double[capacity];

        for (int i = 0; i < temperatureSeries.length; ++i) {
            newTemperatureSeries[i] = temperatureSeries[i];
        }
        for (int i = temperatureSeries.length; i < temperatureSeries.length
                + temps.length; ++i) {
            newTemperatureSeries[i] = temps[i - temperatureSeries.length];
        }
        this.temperatureSeries = newTemperatureSeries;
        return temperatureSeries.length;
    }

}

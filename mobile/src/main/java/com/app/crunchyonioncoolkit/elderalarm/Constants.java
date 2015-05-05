package com.app.crunchyonioncoolkit.elderalarm;

/**
 * Created by David on 2015-04-14.
 */
public class Constants {

    // Threshold to determine peak time
    public static double PEAK_TIME_THRESHOLD = 11;
    // Samples after peak when SMV < PEAK_TIME_THRESHOLD
    public static int TIME_WITHOUT_PEAKS = 2000;
    // Sliding window time size (ms)
    public static int WINDOW_WIDTH = 4000;
    // Interval to search for impact end (ms)
    public static int IMPACT_END_INTERVAL = 1000;
    // Magnitude of impact
    public static double IMPACT_END_MAGNITUDE_THRESHOLD = 4;
    // Interval to search for impact start (ms)
    public static int IMPACT_START_INTERVAL = 1900;
    // Magnitude after impact
    public static double IMPACT_START_LOW_THRESHOLD = 3;
    // Win interval
    public static int WIN_INTERVAL = 1000;
    // AAMV
    public static double AAMV_THRESHOLD = 0.52;
    public static int IDI_THRESHOLD = 320;
    // Maximum acceleration
    public static double MAXIMUM_PEAK_THRESHOLD = 34.1;
    // MVI start interval
    public static int MVI_Interval = 500;
    // MVI average magnitude interval
    public static double MVI_AVERAGE_MAGNITUDE_LOW = 0.24;
    // MVI average magnitude interval
    public static double MVI_AVERAGE_MAGNITUDE_HIGH = 0.9;
    // PDI magnitude
    public static int PDI_INTERVAL = 1291;
    public static double PDI_THRESHOLD = 0.8;

    public static int ARI_INTERVAL = 700;
    public static double ARI_LOW = 0.85;
    public static double ARI_HIGH = 1.3;
    public static double ARI_THRESHOLD = 0.6; // Kolla skillnad mellan fall och slå sensorn

    public static double FFI_THRESHOLD = 0.8;
    public static int FFI_INTERVAL = 200;
    public static double FFI_MINIMUM_FALL_THRESHOLD = 0.6;


    public static int MPI_SCORE = 5;
    public static int AAMV_SCORE = 5;
    public static int MVI_SCORE = 5;
    public static int PDI_SCORE = 5;
    public static int ARI_SCORE = 5;
    public static int FFI_SCORE = 5;

    // Resualt treshold for decision maker to alarm
    public static int RESUALT_THRESHOLD = 20;



    //Simple detection

    //MPI Score for simple detection
    public static int MPI_SIMPLE_SCORE = 5;
    public static int MAXIMUM_GYROSCOPE_PEAK_THRESHOLD = 5;
    public static int MGI_SIMPLE_SCORE = 5;
    public static int FALL_SCORE_RESUALT_THRESHOLD = 10;

    // Heart rate
    public static double MINIMUM_PULSE_THRESHOLD = 1.15;
    public static int MHRI_SIMPLE_SCORE= 10;
    public static int CARDIAC_ARREST_LIMIT = 40;
    public static double CARDIAC_ARREST_RATIO = 0.8;
}
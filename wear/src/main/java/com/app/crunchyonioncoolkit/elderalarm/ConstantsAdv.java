package com.app.crunchyonioncoolkit.elderalarm;

/**
 * Created by Jack on 2015-05-05.
 */
public class ConstantsAdv {

    // Threshold to determine peak time
    public static double PEAK_TIME_THRESHOLD = 15;
    // Samples after peak when SMV < PEAK_TIME_THRESHOLD
    public static int TIME_WITHOUT_PEAKS = 2000;
    // Sliding window time size (ms)
    public static int WINDOW_WIDTH = 6000;
    // Interval to search for impact end (ms)
    public static int IMPACT_END_INTERVAL = 800;
    // Magnitude of impact
    public static double IMPACT_END_MAGNITUDE_THRESHOLD = 7;
    // Interval to search for impact start (ms)
    public static int IMPACT_START_INTERVAL = 1000;
    // Magnitude after impact
    public static double IMPACT_START_LOW_THRESHOLD = 6;
    // Win interval
    public static int WIN_INTERVAL = 1000;
    // AAMV
    public static double AAMV_THRESHOLD = 0.12;  //hade 0.35
    public static int IDI_THRESHOLD = 400;                                  //Sätt övre gräns också
    // Maximum acceleration
    public static double MAXIMUM_PEAK_THRESHOLD = 25;

    // MVI start interval
    public static int MVI_Interval = 500;
    // MVI average magnitude interval
    public static double MVI_AVERAGE_MAGNITUDE_LOW = 0.95;
    // MVI average magnitude interval
    public static double MVI_AVERAGE_MAGNITUDE_HIGH = 3;
    // PDI magnitude
    public static int PDI_INTERVAL = 150;
    //public static int PDI_MAX = 800;
    public static double PDI_THRESHOLD = 5;

    public static int ARI_INTERVAL = 700;
    public static double ARI_LOW = 0.85;
    public static double ARI_HIGH = 1.3;
    public static double ARI_THRESHOLD = 0.065; // Kolla skillnad mellan fall och slå sensorn

    public static double FFI_THRESHOLD = 0.8;
    public static int FFI_INTERVAL = 200;
    public static double FFI_MINIMUM_FALL_THRESHOLD = 0.6;


    public static int MPI_SCORE = 5;
    public static int IDI_SCORE = 5;
    public static int AAMV_SCORE = 10;
    public static int MVI_SCORE = 5;
    public static int PDI_SCORE = 5;
    public static int ARI_SCORE = 5;
    public static int FFI_SCORE = -5;

    // Resualt treshold for decision maker to alarm
    public static int RESUALT_THRESHOLD = 15;


    //Simple detection
    public static int SMA_THRESHOLD = 3;
    public static int SMA_SIMPLE_SCORE = 5;
    //MPI Score for simple detection
    public static int MPI_SIMPLE_SCORE = 5;
    public static int MAXIMUM_GYROSCOPE_PEAK_THRESHOLD = 5;
    public static int MGI_SIMPLE_SCORE = 5;
    public static int FALL_SCORE_RESUALT_THRESHOLD = 10;

    // Heart rate
    public static double MINIMUM_PULSE_THRESHOLD = 1.15;
    public static int MHRI_SIMPLE_SCORE = 10;
    public static int CARDIAC_ARREST_LIMIT = 40;
    public static double CARDIAC_ARREST_RATIO = 0.8;


    // IDI DATA
    public static double IDI_AVG = 627.9;
    public static double IDI_MAX = 960;
    public static double IDI_MIN = 375;
    // AAMV DATA
    public static double AAMV_AVG = 0.275;
    public static double AAMV_MAX = 0.448;
    public static double AAMV_MIN = 0.096;
    // MPI DATA
    public static double MPI_AVG = 34.18;
    public static double MPI_MAX = 71.37;
    public static double MPI_MIN = 16.59;
    // MVI DATA
    public static double MVI_AVG = 2.807;
    public static double MVI_MAX = 4.862;
    public static double MVI_MIN = 0.625;
    // PDI DATA
    public static double PDI_AVG = 297.3;
    public static double PDI_MAX = 838;
    public static double PDI_MIN = 130;
    // ARI DATA
    public static double ARI_AVG = 0.06593;
    public static double ARI_MAX = 0.08143;
    public static double ARI_MIN = 0.04857;


    //PATTERN, UP = IDI>700. DOWN = IDI<700
    public static double IDI_AAMV_UP_MAX = 0.225;
    public static double IDI_AAMV_UP_MIN = 0.125;
    public static double IDI_AAMV_DOWN_MAX = 0.448;
    public static double IDI_AAMV_DOWN_MIN = 0.096;


}

package com.app.crunchyonioncoolkit.elderalarm;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by David on 2015-04-16.
 */
public class Vibration {

    private static Vibrator vibrator;

    private static final long VIBRATION_DURATION = 1000;
    private static final long WAIT_DURATION = 1000;

    public static void startVibration(Context context) {
        if (vibrator == null)
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (vibrator.hasVibrator()) {
            long[] pattern = new long[2];
            pattern[0] = WAIT_DURATION;
            pattern[1] = VIBRATION_DURATION;
            vibrator.vibrate(pattern, 1);
        }
    }

    public static void stopVibration() {
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }
}

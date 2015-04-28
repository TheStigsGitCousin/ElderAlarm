package com.app.crunchyonioncoolkit.elderalarm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2015-04-27.
 */
public class SendData {

    public static String[] getAccelerationData() {
        return splitData("ACC.txt");
    }

    public static String[] getGyroscopeData() {
        return splitData("GYR.txt");
    }

    public static String[] getPulseData() {
        return splitData("PUL.txt");
    }

    private static String[] splitData(String path) {
        String data = DataOut.readFromFile(path);
        List<String> list = splitEqually(data, 90000);
        return list.toArray(new String[list.size()]);
    }

    public static List<String> splitEqually(String text, int size) {
        // Give the list the right capacity to start with. You could use an array
        // instead if you wanted.
        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
}

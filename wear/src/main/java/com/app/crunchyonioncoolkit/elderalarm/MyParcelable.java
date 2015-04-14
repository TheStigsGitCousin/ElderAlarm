package com.app.crunchyonioncoolkit.elderalarm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 2015-04-14.
 */
public class MyParcelable implements Parcelable {

    int priority;
    String type;

    public MyParcelable(int priority, String type) {
        this.priority = priority;
        this.type = type;
    }

    public void setPriority(int prio) {
        this.priority = prio;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                Integer.toString(priority),
                type});
    }

    public static final Parcelable.Creator<MyParcelable> CREATOR
            = new Parcelable.Creator<MyParcelable>() {
        public MyParcelable createFromParcel(Parcel in) {
            return new MyParcelable(in);
        }

        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }
    };

    private MyParcelable(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);

        priority = Integer.parseInt(data[0]);
        type = data[1];
    }
}

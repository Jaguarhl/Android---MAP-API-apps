package ru.dmitry.kartsev.maptracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * Class describes data of aPoint array (track points).
 */

public class APoint implements Parcelable {
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("altitude")
    @Expose
    private double altitude;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("pulse")
    @Expose
    private int pulse;
    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("course")
    @Expose
    private int course;

    public APoint() {}

    protected APoint(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        altitude = in.readDouble();
        time = in.readString();
        pulse = in.readInt();
        speed = in.readDouble();
        course = in.readInt();
    }

    public static final Creator<APoint> CREATOR = new Creator<APoint>() {
        @Override
        public APoint createFromParcel(Parcel in) {
            return new APoint(in);
        }

        @Override
        public APoint[] newArray(int size) {
            return new APoint[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeDouble(altitude);
        parcel.writeString(time);
        parcel.writeInt(pulse);
        parcel.writeDouble(speed);
        parcel.writeInt(course);
    }
}

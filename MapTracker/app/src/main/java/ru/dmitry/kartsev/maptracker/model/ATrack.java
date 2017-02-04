package ru.dmitry.kartsev.maptracker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * This class was generated to work with Jason, but not used right now in app.
 */

public class ATrack {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("dt_start")
    @Expose
    private String dtStart;
    @SerializedName("dt_end")
    @Expose
    private String dtEnd;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("id_track")
    @Expose
    private String idTrack;
    @SerializedName("sp_avg")
    @Expose
    private String spAvg;
    @SerializedName("sp_max")
    @Expose
    private String spMax;
    @SerializedName("calories")
    @Expose
    private String calories;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("access")
    @Expose
    private String access;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("cardio")
    @Expose
    private String cardio;
    @SerializedName("hr_max")
    @Expose
    private int hrMax;
    @SerializedName("hr_avg")
    @Expose
    private int hrAvg;
    @SerializedName("var_max")
    @Expose
    private String varMax;
    @SerializedName("var_min")
    @Expose
    private String varMin;
    @SerializedName("status")
    @Expose
    private boolean status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDtStart() {
        return dtStart;
    }

    public void setDtStart(String dtStart) {
        this.dtStart = dtStart;
    }

    public String getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(String dtEnd) {
        this.dtEnd = dtEnd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIdTrack() {
        return idTrack;
    }

    public void setIdTrack(String idTrack) {
        this.idTrack = idTrack;
    }

    public String getSpAvg() {
        return spAvg;
    }

    public void setSpAvg(String spAvg) {
        this.spAvg = spAvg;
    }

    public String getSpMax() {
        return spMax;
    }

    public void setSpMax(String spMax) {
        this.spMax = spMax;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCardio() {
        return cardio;
    }

    public void setCardio(String cardio) {
        this.cardio = cardio;
    }

    public int getHrMax() {
        return hrMax;
    }

    public void setHrMax(int hrMax) {
        this.hrMax = hrMax;
    }

    public int getHrAvg() {
        return hrAvg;
    }

    public void setHrAvg(int hrAvg) {
        this.hrAvg = hrAvg;
    }

    public String getVarMax() {
        return varMax;
    }

    public void setVarMax(String varMax) {
        this.varMax = varMax;
    }

    public String getVarMin() {
        return varMin;
    }

    public void setVarMin(String varMin) {
        this.varMin = varMin;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}

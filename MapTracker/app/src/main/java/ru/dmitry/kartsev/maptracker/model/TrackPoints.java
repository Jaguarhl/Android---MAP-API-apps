package ru.dmitry.kartsev.maptracker.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*
 * This class was generated to work with Jason, but not used right now in app.
 */

public class TrackPoints {

    @SerializedName("aWaypoints")
    @Expose
    private List<Object> aWaypoints = null;
    @SerializedName("aTrack")
    @Expose
    private ATrack aTrack;
    @SerializedName("aPoints")
    @Expose
    private List<APoint> aPoints = null;
    @SerializedName("sMsg")
    @Expose
    private String sMsg;
    @SerializedName("sMsgTitle")
    @Expose
    private String sMsgTitle;
    @SerializedName("bStateError")
    @Expose
    private boolean bStateError;
    @SerializedName("min_id")
    @Expose
    private Object minId;

    public List<Object> getAWaypoints() {
        return aWaypoints;
    }

    public void setAWaypoints(List<Object> aWaypoints) {
        this.aWaypoints = aWaypoints;
    }

    public ATrack getATrack() {
        return aTrack;
    }

    public void setATrack(ATrack aTrack) {
        this.aTrack = aTrack;
    }

    public List<APoint> getAPoints() {
        return aPoints;
    }

    public void setAPoints(List<APoint> aPoint) {
        this.aPoints = aPoint;
    }

    public String getSMsg() {
        return sMsg;
    }

    public void setSMsg(String sMsg) {
        this.sMsg = sMsg;
    }

    public String getSMsgTitle() {
        return sMsgTitle;
    }

    public void setSMsgTitle(String sMsgTitle) {
        this.sMsgTitle = sMsgTitle;
    }

    public boolean isBStateError() {
        return bStateError;
    }

    public void setBStateError(boolean bStateError) {
        this.bStateError = bStateError;
    }

    public Object getMinId() {
        return minId;
    }

    public void setMinId(Object minId) {
        this.minId = minId;
    }

}

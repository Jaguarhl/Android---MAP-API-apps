package ru.dmitry.kartsev.maptracker.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lombok.Getter;
import ru.dmitry.kartsev.maptracker.MapsActivity;
import ru.dmitry.kartsev.maptracker.model.APoint;

/**
 * Json helper. Loading Json file from remote server with Volley async. Better, than use of aSync Task,
 * that may be aborted by changing device orientation.
 */

public class Json {
    public static final String LOG_TAG = "JSON";
    public static final String JASON_ARRAY_APOINTS = "aPoints";
    private Context mContext;
    private MapsActivity mapsActivity;
    @Getter
    private ArrayList<APoint> trackPoints;
    private RequestQueue requestQueue;

    public Json(MapsActivity mapsActivity, ArrayList<APoint> points) {
        this.mContext = mapsActivity.getBaseContext();
        this.trackPoints = points;
        this.mapsActivity = mapsActivity;
        requestQueue = Volley.newRequestQueue(mContext);
    }

    public void fetchData(String url) {
        // let's start to fetch data from Json
        StringRequest request = new StringRequest(Request.Method.GET, url,
                onPostsLoaded, onPostsError);

        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d(LOG_TAG, response);
            JSONObject dataJsonObj = null;
            trackPoints = new ArrayList<APoint>();

            try {
                dataJsonObj = new JSONObject(response);
                JSONArray points = dataJsonObj.getJSONArray(JASON_ARRAY_APOINTS);
                Log.d(LOG_TAG, "aPoints size: " + points.length());
                // Loading data from aPoints to array trackPoints
                for (int i = 0; i < points.length(); i++) {
                    JSONArray aPoint = points.getJSONArray(i);
                    APoint point = new APoint();
                    point.setLatitude(aPoint.getDouble(0));
                    point.setLongitude(aPoint.getDouble(1));
                    point.setAltitude(aPoint.getDouble(2));
                    point.setTime(aPoint.getString(3));
                    point.setPulse(aPoint.getInt(4));
                    point.setSpeed(aPoint.getLong(5));
                    point.setCourse(aPoint.getInt(6));
                    trackPoints.add(point);
                }
                // updating trackPoints for main activity
                mapsActivity.setTrackPoints(trackPoints);
                // displaying track
                mapsActivity.drawTrack();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(LOG_TAG, error.toString());
        }
    };
}

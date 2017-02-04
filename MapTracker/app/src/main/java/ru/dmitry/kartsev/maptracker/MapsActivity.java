package ru.dmitry.kartsev.maptracker;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import ru.dmitry.kartsev.maptracker.helpers.Json;
import ru.dmitry.kartsev.maptracker.model.APoint;

/*
 * MainActivity of app
 */

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String LOG_TAG = "MapActivity";
    public static final String SHOWING_TRACK = "SHOWING_TRACK";
    public static final String TRACK_ARRAY = "TRACK_ARRAY";
    public static final double MOSCOW_LAT = 55.751244;
    public static final double MOSCOW_LNG = 37.618423;
    public static final int LATLNG_BOUNDS = 100;
    private GoogleMap mMap;
    private Button btnShowTrack;
    private ImageButton btnMapOptions;
    @Getter
    private static String trackUrl = "http://avionicus.com/android/track_v0649.php?avkey=1M1TE9oeWTDK6gFME9JYWXqpAGc%3D&hash=58ecdea2a91f32aa4c9a1d2ea010adcf2348166a04&track_id=36131&user_id=22";
    private Json jsonTask;
    private ArrayList<APoint> trackPoints = null;
    private boolean showTrack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();
        setButtonsBehavior();
        if(savedInstanceState != null) {
            showTrack = savedInstanceState.getBoolean(SHOWING_TRACK);
            trackPoints = savedInstanceState.getParcelableArrayList(TRACK_ARRAY);
        } else {
            trackPoints = new ArrayList<APoint>();
            // creating Json helper and starting to load data
            jsonTask = (Json) new Json(this, trackPoints);
            trackPoints = jsonTask.getTrackPoints();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOWING_TRACK, showTrack);
        outState.putParcelableArrayList(TRACK_ARRAY,  trackPoints);
    }

    private void initViews() {
        btnShowTrack = (Button) findViewById(R.id.btnShowTrack);
        btnMapOptions = (ImageButton) findViewById(R.id.btnMapOptions);
    }

    private void setButtonsBehavior() {
        btnShowTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trackPoints != null) {
                    showTrack = true;
                    drawTrack();
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.error_no_points_data), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnMapOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "btnMapOptions clicked");
                showPopupMenu(view);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.mnu_mapmode_normal:
                        if ((mMap != null) & (mMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL)) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                        return true;
                    case R.id.mnu_mapmode_satellite:
                        if ((mMap != null) & (mMap.getMapType() != GoogleMap.MAP_TYPE_SATELLITE)) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        }
                        return true;
                    case R.id.mnu_mapmode_hybrid:
                        if ((mMap != null) & (mMap.getMapType() != GoogleMap.MAP_TYPE_TERRAIN)) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    private void drawTrack() {
        // checking track is filled. If not - get it.
        if (trackPoints.size() < 1) {
            trackPoints = jsonTask.getTrackPoints();
        }
        List<LatLng> routePoints = new ArrayList<>();
        /*Polyline route;*/
        mMap.clear();
        if(trackPoints.size() > 0 && showTrack){
            for (int i = 0; i < trackPoints.size(); ++i) {
                String color = this.getResources().getString(R.string.colorFastRun);
                if (trackPoints.get(i).getSpeed() >= 0 && trackPoints.get(i).getSpeed() < 5) {
                    color = this.getResources().getString(R.string.colorWalk);
                } else if (trackPoints.get(i).getSpeed() >= 5 && trackPoints.get(i).getSpeed() < 10) {
                    color = this.getResources().getString(R.string.colorFastWalk);
                } else if (trackPoints.get(i).getSpeed() >= 10 && trackPoints.get(i).getSpeed() < 20) {
                    color = this.getResources().getString(R.string.colorRun);
                }
                routePoints.add(new LatLng(trackPoints.get(i).getLatitude(), trackPoints.get(i).getLongitude()));
                if(i > 0) {
                    /*route = */mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(trackPoints.get(i - 1).getLatitude(), trackPoints.get(i - 1).getLongitude()),
                                    new LatLng(trackPoints.get(i).getLatitude(), trackPoints.get(i).getLongitude()))
                            .color(Color.parseColor(color))
                            .width(7.5f));
                }
                if(i == 0) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(trackPoints.get(i).getLatitude(),
                            trackPoints.get(i).getLongitude())).title(getResources().getString(R.string.point_a)));
                }
                if(i == trackPoints.size() - 1) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(trackPoints.get(i).getLatitude(),
                            trackPoints.get(i).getLongitude())).title(getResources().getString(R.string.point_b)));
                }
            }
            // let's view all track on one screen
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(LatLng latLng: routePoints) {
                builder.include(latLng);
            }
            final LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, LATLNG_BOUNDS);
            mMap.animateCamera(cu);
        } else {
            // if we now ready for drawing track, than letting user to see track next time
            showTrack = false;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(showTrack) {
            drawTrack();
        } else {
            LatLng Moscow = new LatLng(MOSCOW_LAT, MOSCOW_LNG);
            mMap.addMarker(new MarkerOptions().position(Moscow).title(getResources().getString(R.string.first_marker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Moscow));
        }
    }
}

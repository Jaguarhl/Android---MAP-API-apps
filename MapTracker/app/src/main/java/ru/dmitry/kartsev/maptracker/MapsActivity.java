package ru.dmitry.kartsev.maptracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import ru.dmitry.kartsev.maptracker.helpers.Json;
import ru.dmitry.kartsev.maptracker.model.APoint;

import static android.app.PendingIntent.getActivity;

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
    public static final int MAXIMUM_TRACK_POINTS_TO_VIEW = 500;
    private GoogleMap mMap;
    private Button btnShowTrack;
    private ImageButton btnMapOptions;
    private ProgressBar progressBar;
    private static String trackUrl = "http://avionicus.com/android/track_v0649.php?avkey=1M1TE9oeWTDK6gFME9JYWXqpAGc%3D&hash=58ecdea2a91f32aa4c9a1d2ea010adcf2348166a04&track_id=";
    private static String trackUrlPartTwo = "&user_id=22";
    private static String trackNumber = "36131";
    private Json jsonTask;
    @Setter
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
        if (savedInstanceState != null) {
            showTrack = savedInstanceState.getBoolean(SHOWING_TRACK);
            trackPoints = savedInstanceState.getParcelableArrayList(TRACK_ARRAY);
        } else {
            trackPoints = new ArrayList<APoint>();
            // creating Json helper and starting to load data
            jsonTask = (Json) new Json(this, trackPoints);
            jsonTask.fetchData(trackUrl + trackNumber + trackUrlPartTwo);
            trackPoints = jsonTask.getTrackPoints();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOWING_TRACK, showTrack);
        outState.putParcelableArrayList(TRACK_ARRAY, trackPoints);
    }

    private void initViews() {
        btnShowTrack = (Button) findViewById(R.id.btnShowTrack);
        btnMapOptions = (ImageButton) findViewById(R.id.btnMapOptions);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void setButtonsBehavior() {
        btnShowTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mDialog = new AlertDialog.Builder(MapsActivity.this);
                LayoutInflater inflater = MapsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_enter_track_number, null);
                final EditText dlgTrackNumber = (EditText) dialogView.findViewById(R.id.editTrackNumber);
                mDialog.setView(dialogView).setMessage(MapsActivity.this.getResources()
                        .getString(R.string.dialog_track_number_title))
                        .setPositiveButton(MapsActivity.this.getResources().getString(R.string.dialog_btn_show),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (dlgTrackNumber.getText().length() > 3) {
                                            String url = trackUrl + dlgTrackNumber.getText().toString() + trackUrlPartTwo;
                                            progressBar.setVisibility(ProgressBar.VISIBLE);
                                            if(jsonTask == null) {
                                                jsonTask = (Json) new Json(MapsActivity.this, trackPoints);
                                            }
                                            jsonTask.fetchData(url);
                                            showTrack = true;
                                        } else {
                                            Toast.makeText(getBaseContext(), getResources()
                                                    .getString(R.string.error_number_must_contain), Toast.LENGTH_LONG)
                                                    .show();
                                        }
                                    }
                                })
                        .setNegativeButton(MapsActivity.this.getResources().getString(R.string.dialog_btn_cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                mDialog.create().show();
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
                switch (item.getItemId()) {
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

    public void drawTrack() {
        if (trackPoints.size() != 0) {
            List<LatLng> routePoints = new ArrayList<>();
            List<LatLng> trackPart = new ArrayList<>();
            if (trackPoints.size() > 0 && showTrack) {
                // clear map from previous objects
                mMap.clear();
                int trackSpeedPart, trackSpeedPartLast = 0;
                for (int i = 0; i < trackPoints.size(); i ++) {
                    int clr = MapsActivity.this.getResources().getColor(R.color.colorFastRun);
                    boolean addTrackPart = false;
                    if (trackPoints.get(i).getSpeed() >= 0 && trackPoints.get(i).getSpeed() < 5) {
                        trackSpeedPart = 5;
                        if(trackSpeedPart != trackSpeedPartLast) {
                            clr = MapsActivity.this.getResources().getColor(R.color.colorWalk);
                            addTrackPart = true;
                        }
                    } else if (trackPoints.get(i).getSpeed() >= 5 && trackPoints.get(i).getSpeed() < 10) {
                        trackSpeedPart = 6;
                        if(trackSpeedPart != trackSpeedPartLast) {
                            clr = MapsActivity.this.getResources().getColor(R.color.colorFastWalk);
                            addTrackPart = true;
                        }
                    } else if (trackPoints.get(i).getSpeed() >= 10 && trackPoints.get(i).getSpeed() < 20) {
                        trackSpeedPart = 7;
                        if(trackSpeedPart != trackSpeedPartLast) {
                            clr = MapsActivity.this.getResources().getColor(R.color.colorRun);
                            addTrackPart = true;
                        }
                    } else {
                        trackSpeedPart = 8;
                        if(trackSpeedPart != trackSpeedPartLast) {
                            addTrackPart = true;
                        }
                    }
                    trackSpeedPartLast = trackSpeedPart;
                    routePoints.add(new LatLng(trackPoints.get(i).getLatitude(),
                            trackPoints.get(i).getLongitude()));
                    trackPart.add(new LatLng(trackPoints.get(i).getLatitude(),
                            trackPoints.get(i).getLongitude()));
                    if(addTrackPart) {
                        trackPart = addNewPartToTrack(i, trackPart, clr);
                    }
                }
                addStartEndMarkers();
                showWholeTrackOnOneScreen(routePoints);
            } else {
                // if we now ready for drawing track, than letting user to see track next time
                showTrack = false;
            }
        } else {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.error_no_points_data),
                    Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    private void showWholeTrackOnOneScreen(List<LatLng> routePoints) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : routePoints) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, LATLNG_BOUNDS);
        mMap.moveCamera(cu);
    }

    private void addStartEndMarkers() {
        if(trackPoints.size() > 1) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(trackPoints.get(0).getLatitude(),
                    trackPoints.get(0).getLongitude())).title(getResources().getString(R.string.point_a)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(trackPoints.get(trackPoints.size() - 1).getLatitude(),
                    trackPoints.get(trackPoints.size() - 1).getLongitude())).title(getResources()
                    .getString(R.string.point_b)));
        }
    }

    private List<LatLng> addNewPartToTrack(int i, List<LatLng> trackPart, int clr) {
        if (i > 0) {
            mMap.addPolyline(new PolylineOptions()
                    .addAll(trackPart)
                    .color(clr)
                    // we are store width size of stroke in resources string
                    .width(Float.parseFloat(getResources().getString(R.string.stroke_width))));
        }
        trackPart = new ArrayList<>();
        // adding last point to avoid spaces between track parts
        trackPart.add(new LatLng(trackPoints.get(i).getLatitude(),
                trackPoints.get(i).getLongitude()));
        return trackPart;
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (showTrack) {
            drawTrack();
        } else {
            LatLng Moscow = new LatLng(MOSCOW_LAT, MOSCOW_LNG);
            mMap.addMarker(new MarkerOptions().position(Moscow).title(getResources().getString(R.string.first_marker)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Moscow));
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }
}

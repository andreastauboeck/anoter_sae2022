package at.ac.fhwn.locationapp;

import android.location.Location;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import at.ac.fhwn.lib.LocationClientService;
import at.ac.fhwn.lib.SaePoint;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import at.ac.fhwn.locationapp.databinding.ActivityMapsBinding;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private LocationClientService mLocationClientService;
    private List<LatLng> mLocations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationClientService = new LocationClientService(42);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this tutorial, we add polylines and polygons to represent routes and areas on the map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add polylines to the map.
        // Polylines are useful to show a route or some other connection between points.
        mMap = googleMap;

        initMapLine();


//        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-35.016, 143.321),
//                        new LatLng(-34.747, 145.592),
//                        new LatLng(-34.364, 147.891),
//                        new LatLng(-33.501, 150.217),
//                        new LatLng(-32.306, 149.248),
//                        new LatLng(-32.491, 147.309)));
//
//
//        Polyline polyline2 = googleMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(-29.501, 119.700),
//                        new LatLng(-27.456, 119.672),
//                        new LatLng(-25.971, 124.187),
//                        new LatLng(-28.081, 126.555),
//                        new LatLng(-28.848, 124.229),
//                        new LatLng(-28.215, 123.938)));


        // Position the map's camera near Alice Springs in the center of Australia,
        // and set the zoom factor so most of Australia shows on the screen.
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocations.get(mLocations.size() - 1), 20));

        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                new Thread(() -> {
                        SaePoint point = mLocationClientService.getLocation(0);
                        LatLng location = new LatLng(point.getLatitude(), point.getLongitude());
                        MapsActivity.this.runOnUiThread(() -> {
                            mLocations.add(location);
                            googleMap.clear();
                            mMap.addPolyline(new PolylineOptions().clickable(false).addAll(
                                    mLocations
                            ));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                        });
                }).start();
            }
        };

        Timer timer = new Timer("timerTask");

        timer.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    private void initMapLine() {
        new Thread(()->{
            List<SaePoint> points = mLocationClientService.getLocations();

            MapsActivity.this.runOnUiThread(()->{
                mLocations = points.stream().map(point ->
                        new LatLng(
                                point.getLatitude(),
                                point.getLongitude()
                        )).collect(Collectors.toList());

                mMap.addPolyline(new PolylineOptions().clickable(false).addAll(
                        mLocations
                ));
            });
        }).start();


    }
}
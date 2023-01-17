package at.ac.fhwn.locationtransmitter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.*;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import at.ac.fhwn.lib.LocationClientService;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements LocationListener, OnNmeaMessageListener {

    public static final int PERMISSION_REQUEST_CODE = 10; // code you want

    private NmeaReader mNmeaReader;
    private LocationClientService locationClientService;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialization of android location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //In order to get location user has to grand permissions or reject. In case of rejection,there is  no possibility to get nmea data and location.
        //That is how we check if app has  ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            //In case not -> we have to request it. This show to the user a dialog asking for granting location permission
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            //In case  we have already permissions -> we add listener for location updates and nmea and it starts listening every 5000 ms
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
            locationManager.addNmeaListener(this, null);
        }
    }

    //This is called when user accepts or rejects permission in the dialog.
    @SuppressLint("MissingPermission") //by adding this annoying red line is disappering, we can do it because we have a check of permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        //Checking if all permissions in permissionsList has grantResult = PERMISSION_GRANTED
        boolean areAllPermissionGranted = false;
        if (PERMISSION_REQUEST_CODE == requestCode && permissionsList.length == 2) {
            for (int i = 0; i < permissionsList.length; i++) {
                areAllPermissionGranted = PERMISSION_GRANTED == grantResults[i];
                if (!areAllPermissionGranted) {
                    Toast.makeText(this, "Nothing works, because location permission is not granted by the user.", Toast.LENGTH_SHORT).show();
                    break; // no point to check other permissions
                }
            }
        }
        //If all permissions granted -> we add listener for location updates and nmea
        if (areAllPermissionGranted) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
            locationManager.addNmeaListener(this, null);
        }
    }

    //When Activity is destroying we need to release the updates from the location manager
    @Override
    protected void onDestroy() {
        locationManager.removeUpdates(this);
        locationManager.removeNmeaListener(this);
        super.onDestroy();
    }

    //Callback from LocationListener
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("LOCATION1", location.toString());
    }

    //Callback from NMEA listener
    @Override
    public void onNmeaMessage(String nmea, long timestamp) {
        Log.d("NMEA1", nmea);
    }
}
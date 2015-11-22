package exception.overdose.stack.devhacksapp.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.utils.PrefUtils;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;

/**
 * Created by sony on 03/10/2015.
 */
public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback {

    /**
     * The google map
     */
    private GoogleMap googleMaps;

    /**
     * The info about the location
     */
    private String location = "";
    /**
     * The coordinates
     */
    private String coordinates;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initLayout();
        initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_maps_toolbar);
        this.setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(this, this.getResources().getString(R.string.app_name));
    }

    private void initLayout() {
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        googleMaps = mapFragment.getMap();
        googleMaps.setOnMapClickListener(this);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.menu_maps_action_save) {
            onSaveButtonClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSaveButtonClicked() {
        Intent intent = new Intent();
        intent.putExtra(Constants.LOCATION, location);
        intent.putExtra(Constants.COORDINATES, coordinates);
        intent.putExtra("location", location);
        intent.putExtra("longitude", coordinates.split(" ")[0]);
        intent.putExtra("latitude", coordinates.split(" ")[1]);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions options = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .title(getAddress(latLng.latitude, latLng.longitude));
        options.position(latLng);
        googleMaps.clear();
        googleMaps.addMarker(options);
        location = getAddress(latLng.latitude, latLng.longitude);
        coordinates = latLng.latitude + " " + latLng.longitude;
    }

    /**
     * Get address from given coordinates
     *
     * @param lat
     * @param lng
     * @return
     */
    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();

            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
            return add;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double latitude = Double.parseDouble(PrefUtils.getStringFromPrefs(this, Constants.PREF_USER_LOCATION_LATITUDE, "0"));
        double longitude = Double.parseDouble(PrefUtils.getStringFromPrefs(this, Constants.PREF_USER_LOCATION_LONGITUDE, "0"));
        if (!(latitude == 0 && longitude == 0)){
            googleMaps.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(getAddress(latitude, longitude)));
        }
        coordinates = longitude + " " + latitude;
        location = getAddress(latitude, longitude);
    }
}

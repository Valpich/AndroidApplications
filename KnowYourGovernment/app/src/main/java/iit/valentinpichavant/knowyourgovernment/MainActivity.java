package iit.valentinpichavant.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String DEFAULT_LOCATION = "No Data For Location";
    private static final String TAG = "MainActivity";
    private List<Office> officesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OfficeAdapter mAdapter;
    private String lastLocation;
    private Locator locator;
    private MainActivity mainActivity;
    private String address = null;
    private TextView textViewLocation;
    private ConstraintLayout noNetworkLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);
        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        textViewLocation.setText(DEFAULT_LOCATION);
        locator = new Locator(this);
        mAdapter = new OfficeAdapter(officesList, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noNetworkLayout = (ConstraintLayout) findViewById(R.id.constraintNoNetwork);
        if (isNetworkOn()) {
            noNetworkLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            locator.determineLocation();
        } else {
            noNetworkLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
        if (address != null) {
            new OfficialsParserAsyncTask(mainActivity, address).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_help:
                Intent intentAboutActivity = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAboutActivity);
                return true;
            case R.id.menu_search:
                askNewLocationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void setData(double lat, double lon) {
        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        String address = doAddress(lat, lon);
        ((TextView) findViewById(R.id.textViewLocation)).setText(address);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }


    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);

        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

                    sb.append(ad.getAddressLine(1));
                }
                if (locator != null) {
                    locator.shutdown();
                }
                this.address = sb.toString();
                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
        if (locator != null) {
            locator.shutdown();
        }
        Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (locator != null) {
            locator.shutdown();
        }
        super.onDestroy();
    }

    public void setOfficesList(List<Office> officesList) {
        this.officesList.clear();
        for (Office office : officesList) {
            this.officesList.add(office);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
        ((TextView) findViewById(R.id.textViewLocation)).setText(this.lastLocation);
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        intent.putExtra("office", this.officesList.get(pos));
        intent.putExtra("address", this.address);
        startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        onClick(v);
        return true;
    }

    private void askNewLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a City, State or a Zip Code:");
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.dialog, null);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText editTestSymbol = (EditText) view.findViewById(R.id.textTypedAsk);
                new OfficialsParserAsyncTask(mainActivity, editTestSymbol.getText().toString()).execute();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean isNetworkOn() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

package com.st.pizzame.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.st.pizzame.PizzaMeApplication;
import com.st.pizzame.R;
import com.st.pizzame.adapter.AddressListAdapter;
import com.st.pizzame.model.LocationPresenter;
import com.st.pizzame.model.LocationView;
import com.st.pizzame.model.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by sumit.thakur on 5/11/18.
 */
public class MainActivity extends AppCompatActivity implements LocationView, AddressListAdapter.ViewHolderClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 101;
    private static final int CALL_PERMISSION_REQUEST_CODE = 102;
    private RecyclerView mRecyclerView;
    private TextView errorView;
    private AddressListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LocationManager mLocationManager;
    private FrameLayout progressBar;
    @Inject
    LocationPresenter locationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((PizzaMeApplication) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        errorView = findViewById(R.id.error_text);

        progressBar = findViewById(R.id.loadingBar);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AddressListAdapter(new ArrayList(), this);
        mRecyclerView.setAdapter(mAdapter);

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        errorView.setVisibility(View.GONE);
        locationPresenter.onResume(this);
        locationPresenter.requestLocation(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationPresenter.onPause();
    }

    @Override
    public void onDataLoaded(JSONArray storeLocations) {
        progressBar.setVisibility(View.GONE);
        showResults(storeLocations);
    }

    @Override
    public void onError(String errorMessage) {
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        //Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPresenter.requestLocation(MainActivity.this);
                } else {
                    Toast.makeText(this, "Permission denied to read your location.", Toast.LENGTH_SHORT).show();
                }

                break;
            case CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Permission denied to read your location.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showResults(JSONArray storeLocations) {
        final List<Result> results = new ArrayList<>();

        for (int i = 0; i < storeLocations.length(); i++) {
            try {
                JSONObject location = storeLocations.getJSONObject(i);
                Result result = new Result();
                result.setTitle(location.getString("Title"));
                result.setAddress(location.getString("Address"));
                result.setCity(location.getString("City"));
                result.setState(location.getString("State"));
                result.setMapUrl(location.getString("MapUrl"));
                result.setPhone(location.getString("Phone"));
                result.setDistance(location.getString("Distance"));
                results.add(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (results.size() > 0) {
            mAdapter.setStoreLocation(results);
        }
    }

    @Override
    public void onCall(String phoneNumber) {
        makeCall(phoneNumber);
    }

    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
            }
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onDirectionClick(String url) {
        Intent mapsIntent = new Intent(MainActivity.this, WebViewActivity.class);
        mapsIntent.putExtra(WebViewActivity.EXTRA_MAP_URL, url);
        startActivity(mapsIntent);
    }
}

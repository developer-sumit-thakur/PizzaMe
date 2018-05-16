package com.st.pizzame.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.st.pizzame.model.LocationPresenter;
import com.st.pizzame.model.LocationView;
import com.st.pizzame.retrofit.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.Context.LOCATION_SERVICE;
import static com.st.pizzame.ui.MainActivity.LOCATION_PERMISSION_REQUEST_CODE;

/**
 * Created by sumit.thakur on 5/15/18.
 */

public class LocationPresenterImpl implements LocationPresenter {
    private static final String TAG = LocationPresenterImpl.class.getSimpleName();
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;
    private LocationView locationView;
    private Context context;
    private LocationManager mLocationManager;

    public LocationPresenterImpl(Context context) {
        this.context = context;
    }

    @Override
    public void onResume(LocationView locationView) {
        this.locationView = locationView;
        this.mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    @Override
    public void onPause() {
        this.locationView = null;
    }

    @Override
    public void getData(String zipCode) {
        Log.d(TAG, "requested data for zipcode : " + zipCode);
        LocationService locationService = LocationService.getInstance();
        locationService.initService();
        locationService.getLocations(zipCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe.");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.d(TAG, "onNext : " + responseBody.toString());
                        JSONObject response = null;
                        try {
                            response = new JSONObject(responseBody.string());
                            JSONObject object = (JSONObject) response.get("query");
                            JSONObject results = (JSONObject) object.get("results");
                            JSONArray storeLocations = results.getJSONArray("Result");
                            if (storeLocations.length() > 0) {
                                if (locationView != null) locationView.onDataLoaded(storeLocations);
                            } else {
                                if (locationView != null)
                                    locationView.onError("no data available.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete.");
                    }
                });

    }

    @Override
    public void requestLocation(Activity activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location permission not granted.");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.d(TAG, "Location permission granted.");
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, mLocationListener);
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                    getData(addresses.get(0).getPostalCode());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
}

package com.st.pizzame.retrofit;

import android.util.Log;

import java.net.URLEncoder;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumit.thakur on 5/12/18.
 */

public class LocationService {
    private static final String TAG = LocationService.class.getSimpleName();
    private static final String baseUrl = "https://query.yahooapis.com/v1/public/";
    private static final String QUERY = "select * from local.search where";
    private static final String ZIP_PARAM = " zip=";
    private static final String QUERY_PARAM = " and query=";
    private static final String CONSTANT_QUERY = "&format=json&diagnostics=true&callback=";

    private DataService dataService;
    private static LocationService instance;

    public static LocationService getInstance() {
        if (instance == null) instance = new LocationService();
        return instance;
    }

    public void initService() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        dataService = retrofit.create(DataService.class);
    }

    public Observable<ResponseBody> getLocations(String zipCode) {
        Observable<ResponseBody> retVal = null;

        try {
            retVal = dataService.getStoreLocation("yql?q=" + getDataUrl(zipCode) + CONSTANT_QUERY);
        } catch (Exception ex) {
            Log.d(TAG, "API Error : " + ex.getMessage());
        }

        return retVal;
    }

    public static String getDataUrl(String zipCode) {
        try {
            String query = QUERY + ZIP_PARAM + String.valueOf(zipCode) + QUERY_PARAM + "\"pizza\"";
            return URLEncoder.encode(query, "UTF-8").replace("+", "%20");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

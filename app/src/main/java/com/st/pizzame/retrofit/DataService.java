package com.st.pizzame.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by sumit.thakur on 5/12/18.
 */

public interface DataService {
    @GET
    Observable<ResponseBody> getStoreLocation(@Url String url);
}

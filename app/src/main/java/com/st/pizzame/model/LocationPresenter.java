package com.st.pizzame.model;

import android.app.Activity;

/**
 * Created by sumit.thakur on 5/15/18.
 */

public interface LocationPresenter {

    void onResume(LocationView locationView);
    void onPause();
    void getData(String zipCode);
    void requestLocation(Activity activity);
}

package com.st.pizzame.model;

import org.json.JSONArray;

/**
 * Created by sumit.thakur on 5/12/18.
 */

public interface LocationView {
    void onDataLoaded(JSONArray storeLocations);

    void onError(String errorMessage);
}

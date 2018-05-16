package com.st.pizzame.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import com.st.pizzame.model.LocationPresenter;
import com.st.pizzame.viewmodel.LocationPresenterImpl;

/**
 * Created by sumit.thakur on 5/15/18.
 */

@Module
public class PresenterModule {

    @Provides
    public LocationPresenter provideUpsellPresenter(Context context) {
        return new LocationPresenterImpl(context);
    }
}

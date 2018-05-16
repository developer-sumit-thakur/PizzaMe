package com.st.pizzame.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumit.thakur on 5/12/18.
 */

@Module
public class AppModule {
    Application application;

    public AppModule(Application appContext) {
        this.application = appContext;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }
}

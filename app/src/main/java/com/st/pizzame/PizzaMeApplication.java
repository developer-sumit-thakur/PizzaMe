package com.st.pizzame;

import android.app.Application;

import com.st.pizzame.dagger.AppComponent;
import com.st.pizzame.dagger.AppModule;
import com.st.pizzame.dagger.DaggerAppComponent;

/**
 * Created by sumit.thakur on 5/12/18.
 */

public class PizzaMeApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = createMyComponent();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent createMyComponent() {
        return DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
}

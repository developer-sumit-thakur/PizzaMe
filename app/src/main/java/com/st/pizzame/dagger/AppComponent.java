package com.st.pizzame.dagger;

/**
 * Created by sumit.thakur on 5/12/18.
 */

import com.st.pizzame.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
}

package com.esoxjem.movieguide;

import android.app.Application;
import android.os.StrictMode;

import com.esoxjem.movieguide.details.DetailsComponent;
import com.esoxjem.movieguide.details.DetailsModule;
import com.esoxjem.movieguide.favorites.FavoritesModule;
import com.esoxjem.movieguide.listing.ListingComponent;
import com.esoxjem.movieguide.listing.ListingModule;
import com.esoxjem.movieguide.network.NetworkModule;

import sdk.pendo.io.*;
import io.realm.Realm;
import java.util.HashMap;
import java.util.Map;
/**
 * @author arun
 */
public class BaseApplication extends Application {
    private AppComponent appComponent;
    private DetailsComponent detailsComponent;
    private ListingComponent listingComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        initRealm();
        appComponent = createAppComponent();

        Pendo.PendoInitParams pendoParams = new Pendo.PendoInitParams();
        pendoParams.setVisitorId("Caner NAYKI");
        pendoParams.setAccountId("Pendo New");



////send Visitor Level Data
//        Map<String, String> userData = new HashMap<>();
//        userData.put("age", "27");
//        userData.put("country", "USA");
//        pendoParams.setUserData(userData,);
//
////send Account Level Data
//        Map<String, String> accountData = new HashMap<>();
//        accountData.put("Tier", "1");
//        accountData.put("Size", "Enterprise");
//        pendoParams.setAccountData(accountData);



        Pendo.initSDK(
                this,
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiI2NzhlZDdkNjkxOTliOTcyN2ExOTU3NDAwMDg2MGM3MDFjYmZhYzE4NDFhNjJjNzdhNzhhYjFmNWM3ODA5MDUyOTczZTIxY2MzNjlkY2MwNWM0MmE1ZTVhOGYzNDRiMWQ1ZmY3OGY0ODU4OTc1NTE1OWJmZjQxMWViZDhhODMzOGY1ZmNjNGJkMDk3NjZmNjg3YzMyNjQzYmMwOTcyNWUzLjVmNTNjOWRmZjAxNWZhM2ExNDk3MjczZjA5ZTczMmMwLmEyM2I5NGRhODU1ODRmZDlmMjNhNGZjODM5ODA3YzdmOWI0YmEwZjhmNDYwNzI3NmZkOWI1ZTQwNmFjMTJkMmUifQ.N_iAlnMwNpVRktk92RjhRe1JKVvF5kyWumEuthg1YyZuEAq5n2vYDltZSg7fthW-gSOSI05yEP52aA2Qg0Q3qHRnM36cp7zYrNuKj4JAbKc7MCjZ3oKsacaCLJZL9jGYRAhTnamgTR3bOHUll2mAyUIx0J30JG0sTcMCf_szhvA",
                pendoParams);   // call initSDK with initParams as a fourth parameter (this can be `null`).
    }

    private AppComponent createAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .favoritesModule(new FavoritesModule())
                .build();
    }

    private void initRealm() {
        Realm.init(this);
    }

    public DetailsComponent createDetailsComponent() {
        detailsComponent = appComponent.plus(new DetailsModule());
        return detailsComponent;
    }

    public void releaseDetailsComponent() {
        detailsComponent = null;
    }

    public ListingComponent createListingComponent() {
        listingComponent = appComponent.plus(new ListingModule());
        return listingComponent;
    }

    public void releaseListingComponent() {
        listingComponent = null;
    }

    public ListingComponent getListingComponent() {
        return listingComponent;
    }
}

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



//send Visitor Level Data
        Map<String, String> userData = new HashMap<>();
        userData.put("age", "27");
        userData.put("country", "USA");
 //       pendoParams.setUserData(userData,);

//send Account Level Data
        Map<String, String> accountData = new HashMap<>();
        accountData.put("Tier", "1");
        accountData.put("Size", "Enterprise");
//        pendoParams.setAccountData();
//


        Pendo.initSDK(
                this,
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiJkMWI1YTNjOTEyYWNjNTc0NzliMjQ4NzliMGFjZTAwZmM3OTQ4ODg1OTI4YTYyOGQ4MmY3OTdlOGYxMGNjNDYzMzc0ZmUyMzUwNDAxZjUxMGIwZjA0ZjZjNzVlMTBlYjE3YmE5ZTRjYjFkMzE3YzQ5NmRmM2JjNTEyZTYxMjBhMTljOWIxN2YwNzU2MTQzYTQwMjY1NTE1NTNmNjBmYzQ5LmUwY2JmYjEyN2I3M2I5YzIzNmU4YzkxYzNjM2ZiN2FhLjQ0MWU0YzJkODdjNTk1NWQ3ZGRhNDllNGNmZGE2MjRjZjhkYTQxOWJmODRkNTVlMzIzMzM3NzhmZDM0ZDg0OGYifQ.XbTQwnRSSWLS0-UXTGXehurXwPJyGSY_xt96V71qA4v5AxETSAw6P_yrAH24BI3ToopSHvpQgfRY4FIIxI5-8Xmw92VvkciLMPxxzko0Emc2qTF2JM-1MauYCcU9Gf1WDOokjsW5Hck6TBOnL1LWwUI3QoYvFZOCYDiYwinKvCo",
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

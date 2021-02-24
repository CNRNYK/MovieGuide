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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiIxZDQwOWE2ZmYwMzlhZTIwN2U0MTc1NGQyYzZjMjcyNTc0YzAzMTFmMjMwMjI4Nzk4Y2YxYTc3ZDc1NjJkNTI0MWEyODVjOWU2MzliNzNiMjJiNWE3MmQwODkxNmU5YmNlYTFjYmY3ZDA2OGUyYzMxYWRhODNiOThiZTIwZjViOWY1NDMzY2Y4NTFmY2FhMzE1YjdmYWM0MzdjOWIxNDEyLmQxOWZlMDZiM2FlMTdiMjkzODk2NGJhOTBjZTUzNDRmLjFkZTA1ZDEwMDQzMDA4Mzc3NGE3YWRiYmMzMzI5MTVhMzM1ZjEyNzZlYTkwNjQyYmQ2N2YwYTg3MzM0OGRmNWQifQ.tHSiR-eYAXbHGUksrHZ5XJhcmwfW5FJ5nh01aT9vkYvX6HXOLrP08oNZCO3CPaomvpUJKVsF_cCYFPe4nsnxPQv6LaGrYQm6udJvNPgODA4jxlY8TYRmfnO8aJ3AzLbmXK76kKvX-VkojlM_wiuhw6GPPj6brmGoa1CWvI0MSdg",
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

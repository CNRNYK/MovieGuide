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
        pendoParams.setVisitorId("John Smith");
        pendoParams.setAccountId("Acme Inc");

//send Visitor Level Data
        Map<String, String> userData = new HashMap<>();
        userData.put("age", "27");
        userData.put("country", "USA");
        pendoParams.setUserData(userData);

//send Account Level Data
        Map<String, String> accountData = new HashMap<>();
        accountData.put("Tier", "1");
        accountData.put("Size", "Enterprise");
        pendoParams.setAccountData(accountData);


        Pendo.initSDK(
                this,
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiJlZTlhMWFkY2FmMTU3OTM5MDNmODAzNTM0NDAyMmRkYjdiYzg3ODMzYThkMDk5Mzg3NjI5YjNjN2QzNjg3NTM5NmUzZTBjNzY1YWU5YTA3N2UyMDRkODcxYmFlY2RiNTZhMDUyYTA5ZmEzMjc1YTkzMzdmYzYxYTliZWZmMjYzNTU4ODE5YzJiMTU4NjYzZGMzY2EyZmZlMmM1NGMxODVkLjQzOGU3ODE4NjZmMjE2YmU0NWVjM2RkYWZiNjRiNGQzLjVhZWU4MWU2MjE5YTAxOWE1YmJkMzQyMGQ4ZTNmN2JjMjUwYzgzYTg4MGVlMTg0ZWNiZGRlZjJkYjVkZTI2ZWQifQ.nEPbqCCeaTYy6-TYNIxXUU7He4O_9EPvVaY6XlgHZEwj90KTHk0GDl6pcAV1Z_KHOOtJ5ID6qBPTdLFYMR0rj7volzNBvoWlWSPkZ18ahV-f78-FiX3KUaJryX8AzKxQ6jcz3P8SMULrjibvZ6ae0sDouKDNYWatL9dH6bXj3_E",
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

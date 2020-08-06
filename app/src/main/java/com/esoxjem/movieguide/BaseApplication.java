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
        pendoParams.setAccountId("Caner Account ID");
/*
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
*/

        Pendo.initSDK(
                this,
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiJlNDliN2VmNmYzNGY3OTAxZTJmOGQ3NzJiZTEwNmRhYTAzZTQ0Y2EwMjA2ZDE2NzBhZGY5NTk2ZmM4Mzg3MzQ0MjgyNmRiODZkNDZhMzg1ZTQxMWUwNWRhZTU4NzhiOTgyNmRkMWY5YWU5ZDkxZjAyY2YwY2M0M2U2ZGM3ZmRkODE5MzY5NWY3ZGE3ZTNhMGUzYmM2NzZhNjAyYTBkMTU0LmU2MjQ4ZmZmMDk5MTE1YzdlNjk4NWRiZDYwNjA2Y2Y2LmUyNzM3ODE4YjIxNWRlYzM0ZDQ0MjY2Mzk3NjRjMWY1Mzg1NTIzZTA5YTg4ZWZjYzhiNTY1MTU3ZDc5N2RiNjUifQ.QOYEKelYCYgEorjJaaVDouQS0sSEdQP7tlV7Ps6Eqt5L-c_HX5WlsLYklWeiA2bIiWQ0HT5OR55IJdx7GPPwsVuVRp75G03zVNDVEIPqC-_aqJXoaNDTawZc1nhjq1y6LySAulNakBbkthKi5_W_eGxn1DNL6n2NbBeiQGPcGFQ",
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

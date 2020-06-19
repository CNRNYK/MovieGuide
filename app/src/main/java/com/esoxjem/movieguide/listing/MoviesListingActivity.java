package com.esoxjem.movieguide.listing;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.test.espresso.IdlingResource;

import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.movieguide.details.MovieDetailsActivity;
import com.esoxjem.movieguide.details.MovieDetailsFragment;
import com.esoxjem.movieguide.util.EspressoIdlingResource;
import com.esoxjem.movieguide.util.RxUtils;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import sdk.pendo.io.Pendo;


public class MoviesListingActivity extends AppCompatActivity implements MoviesListingFragment.Callback {
    public static final String DETAILS_FRAGMENT = "DetailsFragment";
    private boolean twoPaneMode;
    private Disposable searchViewTextSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();

        Pendo.PendoInitParams pendoParams = new Pendo.PendoInitParams();
        pendoParams.setVisitorId("Caner Android");
        pendoParams.setAccountId("Acme Inc");
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
                "eyJhbGciOiJSUzI1NiIsImtpZCI6IiIsInR5cCI6IkpXVCJ9.eyJkYXRhY2VudGVyIjoidXMiLCJrZXkiOiJhNjg2YjQxYTFjZmQ1NGM4OTUzYjcxYzI4NjVkMmViYTRjMTQxZDc3MzNmMDM1YzllZDFiNTk5MDEyMWE4OGU5Yzc1MTU4MjMwZjE3Yzc4Mzk1MWU4NzE0MTViMmY5NjgxYTFkOWMwNzRmYzkxN2U5MTk4NzczY2Y5Mzg2Mjk4ZTg5MTFiMWQyMzc4NzE5YTQ2MTY4YWM5ZDRhYmIyYTQ5LmE3Yjg0YzRiNWQ4M2I3NTE4ZWIyNWZlMGJlMjJmYmM5LjhjOTU3ZGFmMGRiZmJiN2Q5YmEwMThjODBjNDlmMmUxYTFiYzkwZGJkMjIxYzYzODM4ODk1MGEwYTI5ZmRjYmMifQ.RFf8kk-2lvugNFvNCE9-bO4qqkCsE1GbOI6kGmOsHJ7wY_j0kLeIxVLN89lCSYpp5-2f8yMZonQKGhTeUyOBVKe9I4fm8I-pKIHh11kVag55q6vbulIayWwcaJMEjAbcB15xDAFhmQMTtuQEzwDIdp4Oh8QTG_9GsBvZDirUHq0",
                pendoParams);   // call initSDK with initParams as a fourth parameter (this can be `null`).
        if (findViewById(R.id.movie_details_container) != null) {
            twoPaneMode = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_details_container, new MovieDetailsFragment())
                        .commit();
            }
        } else {
            twoPaneMode = false;
        }
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        final MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                MoviesListingFragment mlFragment = (MoviesListingFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_listing);
                mlFragment.searchViewBackButtonClicked();
                return true;
            }
        });

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {
                        mlFragment.searchViewClicked(charSequence.toString());
                    }
                });

        return true;
    }

    @Override
    public void onMoviesLoaded(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {
            // Do not load in single pane view
        }
    }

    @Override
    public void onMovieClicked(Movie movie) {
        if (twoPaneMode) {
            loadMovieFragment(movie);
        } else {
            startMovieActivity(movie);
        }
    }

    private void startMovieActivity(Movie movie) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(Constants.MOVIE, movie);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadMovieFragment(Movie movie) {
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(movie);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, movieDetailsFragment, DETAILS_FRAGMENT)
                .commit();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }

    @Override
    protected void onDestroy() {
        RxUtils.unsubscribe(searchViewTextSubscription);
        super.onDestroy();
    }
}

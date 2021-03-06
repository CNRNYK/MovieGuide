package com.esoxjem.movieguide.details;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

//Test
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esoxjem.movieguide.Api;
import com.esoxjem.movieguide.BaseApplication;
import com.esoxjem.movieguide.Constants;
import com.esoxjem.movieguide.Movie;
import com.esoxjem.movieguide.R;
import com.esoxjem.*;
import com.esoxjem.movieguide.Review;
import com.esoxjem.movieguide.Video;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kotlin.random.URandomKt;
import sdk.pendo.io.Pendo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class MovieDetailsFragment extends Fragment implements MovieDetailsView, View.OnClickListener {
    @Inject
    MovieDetailsPresenter movieDetailsPresenter;

    @BindView(R.id.movie_poster)
    ImageView poster;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.movie_name)
    TextView title;
    @BindView(R.id.movie_year)
    TextView releaseDate;
    @BindView(R.id.movie_rating)
    TextView rating;
    @BindView(R.id.movie_description)
    TextView overview;
    @BindView(R.id.trailers_label)
    TextView label;
    @BindView(R.id.trailers)
    LinearLayout trailers;
    @BindView(R.id.trailers_container)
    HorizontalScrollView horizontalScrollView;
    @BindView(R.id.reviews_label)
    TextView reviews;
    @BindView(R.id.reviews)
    LinearLayout reviewsContainer;
    @BindView(R.id.favorite)
    FloatingActionButton favorite;
    @BindView(R.id.toolbar)
    @Nullable
    Toolbar toolbar;

    private Movie movie;
    private Unbinder unbinder;

    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private Object List;


    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    public static MovieDetailsFragment getInstance(@NonNull Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.MOVIE, movie);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseApplication) getActivity().getApplication()).createDetailsComponent().inject(this);






/*
        Button User1 = (Button) findViewById(R.id.user_1);
        User1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User 1");

                Map<String, String> newUserData = new HashMap<>();
                newUserData.put("RR1", "RR1");

                Map<String, String> newAccountData = new HashMap<>();
                newAccountData.put("RR1", "RR1");

                Pendo.switchVisitor(
                        "RR1",
                        "RR1",
                        newUserData,
                        newAccountData);

            }
        });

        Button User2 = (Button) getView().findViewById(R.id.user_2);
        User2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User 2");

                Map<String, String> newUserData = new HashMap<>();
                newUserData.put("RR2", "RR2");

                Map<String, String> newAccountData = new HashMap<>();
                newAccountData.put("RR2", "RR2");

                Pendo.switchVisitor(
                        "RR2",
                        "RR2",
                        newUserData,
                        newAccountData);
            }
        });

        Button User3 = (Button) findViewById(R.id.user_3);
        User3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User 3");

                Map<String, String> newUserData = new HashMap<>();
                newUserData.put("RR3", "RR3");

                Map<String, String> newAccountData = new HashMap<>();
                newAccountData.put("RR3", "RR3");

                Pendo.switchVisitor(
                        "RR3",
                        "RR3",
                        newUserData,
                        newAccountData);
            }
        });
*/

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        setToolbar();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Movie movie = (Movie) getArguments().get(Constants.MOVIE);
            if (movie != null) {
                this.movie = movie;
                movieDetailsPresenter.setView(this);
                movieDetailsPresenter.showDetails((movie));
                movieDetailsPresenter.showFavoriteButton(movie);
            }
        }
    }

    private void setToolbar() {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    @Override
    public void showDetails(Movie movie) {
        Glide.with(getContext()).load(Api.getBackdropPath(movie.getBackdropPath())).into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
        overview.setText(movie.getOverview());
        movieDetailsPresenter.showTrailers(movie);
        movieDetailsPresenter.showReviews(movie);




    }

    @Override
    public void showTrailers(List<Video> trailers) {
        if (trailers.isEmpty()) {
            label.setVisibility(View.GONE);
            this.trailers.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);

        } else {
            label.setVisibility(View.VISIBLE);
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150);

            for (Video trailer : trailers) {
                View thumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView thumbView = thumbContainer.findViewById(R.id.video_thumb);
                thumbView.setTag(R.id.glide_tag, Video.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                Glide.with(requireContext())
                        .load(Video.getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    @Override
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            this.reviews.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else {
            this.reviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();
            LayoutInflater inflater = getActivity().getLayoutInflater();
            for (Review review : reviews) {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = reviewContainer.findViewById(R.id.review_author);
                TextView reviewContent = reviewContainer.findViewById(R.id.review_content);




                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);





            }
        }


    }

    @Override
    public void showFavorited() {
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_white_24dp));
    }

    @Override
    public void showUnFavorited() {
        favorite.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite_border_white_24dp));
    }

    @OnClick(R.id.Clear)
    public void clearEvent(View view){
        Pendo.clearVisitor();
    }

    @OnClick(R.id.Change)
    public void changeVisitor(View view){
        Random rand = new Random();
        int x = 25;
        int j= rand.nextInt(25);
        Pendo.PendoInitParams pendoParams = new Pendo.PendoInitParams();
        pendoParams.setVisitorId("New setted caner"+j);
        pendoParams.setAccountId("New Setted caner");
    }


    @OnClick(R.id.Switch)
    public void switchVisitor(View view) {
        HashMap insertAttributes = new HashMap();
        insertAttributes.put("UserGender", "Female");
        Random rand = new Random();
        int x = 25;
        int j= rand.nextInt(25);

        Pendo.switchVisitor(

                "New Visitor ID"+j,
                "\"For both of the above API's (SetVisitor and SwitchVisitor) try to break the api by use the following: \n" +
                        "Empty strings/extremely long strings, numbers, values in other languages, special chars, space, null, send part of the parameters that the api requires\"",insertAttributes,insertAttributes);
    }

    @OnClick(R.id.favorite)
    public void onClick(View view) {

//
//                HashMap<String, String> properties = new HashMap<>();
//                properties.put("key1", "value1");
//                properties.put("key2", "value2");
//                Pendo.track("Track Event", properties);
        HashMap insertAttributes = new HashMap();
        HashMap<String, String> properties = new HashMap<>();
        properties.put("item", view.toString());
        properties.put("index", String.valueOf(1));
        Pendo.track("item_selected",insertAttributes);


        switch (view.getId()) {
            case R.id.video_thumb:
                onThumbnailClick(view);
                break;

            case R.id.review_content:
                onReviewClick((TextView) view);
                break;

            case R.id.favorite:
                onFavoriteClick();
                break;

            default:
                break;
        }
    }

    private void onReviewClick(TextView view) {
        if (view.getMaxLines() == 5) {
            view.setMaxLines(500);
        } else {
            view.setMaxLines(5);
        }
    }

    private void onThumbnailClick(View view) {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    private void onFavoriteClick() {
        movieDetailsPresenter.onFavoriteClick(movie);



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        movieDetailsPresenter.destroy();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((BaseApplication) getActivity().getApplication()).releaseDetailsComponent();



    }
}

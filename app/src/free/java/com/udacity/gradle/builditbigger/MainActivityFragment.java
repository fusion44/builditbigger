package com.udacity.gradle.builditbigger;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.lang.String;

import xyz.tripcannon.jokesdisplay.JokesDisplayActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements FetchJokesAsyncTask.JokeListener {

    private static final int REQUEST_CODE_FETCH_JOKE = 1337;
    IntentFilter filter = new IntentFilter(FetchJokesService.ACTION_FETCH_JOKE_READY);
    InterstitialAd mInterstitialAd;
    private View mRoot = null;
    private Dialog loadingIndicator = null;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(FetchJokesService.ACTION_FETCH_JOKE_READY)) {
                showAd(intent.getStringExtra(FetchJokesService.JOKE_TEXT));
            }
        }
    };

    public MainActivityFragment() {
    }

    @Override public void onResume() {
        getActivity().registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_main, container, false);

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                fetchJoke();
            }
        });

        requestNewInterstitial();

        Button tellJokeBtn = (Button) mRoot.findViewById(R.id.tellJokeButton);
        tellJokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                fetchJoke();
            }
        });

        AdView mAdView = (AdView) mRoot.findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
        return mRoot;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("2FEECB7CA9795735FEC1713A9F194D34")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public void fetchJoke() {
        if (loadingIndicator == null) {
            loadingIndicator = new Dialog(getContext());
            loadingIndicator.setTitle(R.string.loading_indicator);
            loadingIndicator.setContentView(R.layout.progress_view);
        }
        loadingIndicator.show();

        //FetchJokesService.startActionFetchJoke(getContext());
        new FetchJokesAsyncTask().execute(
                new Pair<FetchJokesAsyncTask.JokeListener, String>(
                        this, getString(R.string.jokes_service_url)));
    }

    public void showAd(String jokeText) {
        if (loadingIndicator != null) {
            loadingIndicator.dismiss();
        }

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            tellJoke(jokeText);
        }
    }

    public void tellJoke(String jokeText) {
        Intent i = new Intent(getContext(), JokesDisplayActivity.class);
        i.putExtra(JokesDisplayActivity.KEY_JOKE_TO_DISPLAY, jokeText);
        startActivity(i);
    }

    @Override public void onFinished(String joke) {
        showAd(joke);
    }
}

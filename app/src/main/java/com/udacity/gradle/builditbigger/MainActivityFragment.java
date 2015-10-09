package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import xyz.tripcannon.jokes.JokesRepository;
import xyz.tripcannon.jokesdisplay.JokesDisplayActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View mRoot;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_main, container, false);

        Button tellJokeBtn = (Button) mRoot.findViewById(R.id.tellJokeButton);
        tellJokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                tellJoke();
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

    public void tellJoke() {
        Intent i = new Intent(getContext(), JokesDisplayActivity.class);
        i.putExtra(JokesDisplayActivity.KEY_JOKE_TO_DISPLAY, JokesRepository.getJoke());
        startActivity(i);
    }

}

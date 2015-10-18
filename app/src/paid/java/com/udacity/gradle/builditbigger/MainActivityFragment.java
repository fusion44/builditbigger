package com.udacity.gradle.builditbigger;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.tripcannon.jokesdisplay.JokesDisplayActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements FetchJokesAsyncTask.JokeListener {

    private static final int REQUEST_CODE_FETCH_JOKE = 1337;
    IntentFilter filter = new IntentFilter(FetchJokesService.ACTION_FETCH_JOKE_READY);
    private View mRoot = null;
    private Dialog loadingIndicator = null;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(FetchJokesService.ACTION_FETCH_JOKE_READY)) {
                tellJoke(intent.getStringExtra(FetchJokesService.JOKE_TEXT));
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

        Button tellJokeBtn = (Button) mRoot.findViewById(R.id.tellJokeButton);
        tellJokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                fetchJoke();
            }
        });

        return mRoot;
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

    public void tellJoke(String jokeText) {
        if (loadingIndicator != null) {
            loadingIndicator.dismiss();
        }

        Intent i = new Intent(getContext(), JokesDisplayActivity.class);
        i.putExtra(JokesDisplayActivity.KEY_JOKE_TO_DISPLAY, jokeText);
        startActivity(i);
    }

    @Override public void onFinished(String joke) {
        tellJoke(joke);
    }
}

package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.util.Pair;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import xyz.tripcannon.backend.myApi.MyApi;

class FetchJokesAsyncTask extends AsyncTask<Pair<FetchJokesAsyncTask.JokeListener, String>, Void, String> {
    private MyApi myApiService = null;
    private JokeListener listener;

    @SafeVarargs @Override
    protected final String doInBackground(Pair<JokeListener, String>... params) {
        if (myApiService == null) {  // Only do this once
            listener = params[0].first;
            String url = params[0].second;

            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl(url)
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        listener.onFinished(result);
    }

    public interface JokeListener {
        void onFinished(String joke);
    }
}
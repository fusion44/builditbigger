package com.udacity.gradle.builditbigger;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import xyz.tripcannon.backend.myApi.MyApi;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class FetchJokesService extends IntentService {
    public static final String ACTION_FETCH_JOKE = "com.udacity.gradle.builditbigger.action.fetchjoke";
    public static final String ACTION_FETCH_JOKE_READY = "com.udacity.gradle.builditbigger.action.fetchjokeready";
    public static final String JOKE_TEXT = "com.udacity.gradle.builditbigger.extra.joketxt";

    private static MyApi api = null;

    public FetchJokesService() {
        super("FetchJokesService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFetchJoke(Context context) {
        Intent intent = new Intent(context, FetchJokesService.class);
        intent.setAction(ACTION_FETCH_JOKE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FETCH_JOKE.equals(action)) {
                handleActionFetchJoke();
            }
        }
    }

    private void handleActionFetchJoke() {
        if (api == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl(getString(R.string.jokes_service_url))
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            api = builder.build();
        }

        String jokeText;
        try {
            jokeText = api.getJoke().execute().getData();
            Intent i = new Intent(ACTION_FETCH_JOKE_READY);
            i.putExtra(JOKE_TEXT, jokeText);

            // simulate network latency
            // delay(5000);

            sendBroadcast(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

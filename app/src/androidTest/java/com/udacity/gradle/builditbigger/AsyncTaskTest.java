package com.udacity.gradle.builditbigger;

import android.support.v4.util.Pair;
import android.test.InstrumentationTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
    Idea from here: https://gist.github.com/he9lin/2195897
 */
public class AsyncTaskTest extends InstrumentationTestCase {
    private static String joke;

    protected void setUp() throws Exception {
        super.setUp();
        joke = "";
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testSuccessfulFetch() throws Throwable {
        // create  a signal to let us know when our task is done.
        final CountDownLatch signal = new CountDownLatch(1);

        // Execute the async task on the UI thread! THIS IS KEY!
        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                new FetchJokesAsyncTask().execute(new Pair<FetchJokesAsyncTask.JokeListener, String>(
                        new FetchJokesAsyncTask.JokeListener() {
                            @Override public void onFinished(String fin_joke) {
                                joke = fin_joke;
                            }
                        },
                        "http://10.0.3.2:8080/_ah/api/"
                ));
            }
        });

	    /* The testing thread will wait here until the UI thread releases it
         * above with the countDown() or 30 seconds passes and it times out.
	     */
        signal.await(5, TimeUnit.SECONDS);
        assertEquals(joke, "Put funny joke here!");
    }
}
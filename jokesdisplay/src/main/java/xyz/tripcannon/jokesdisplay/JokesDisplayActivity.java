package xyz.tripcannon.jokesdisplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokesDisplayActivity extends AppCompatActivity {
    public static final String KEY_JOKE_TO_DISPLAY = "joke_to_display";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes_display);
        String joke = "";
        if (getIntent().hasExtra(KEY_JOKE_TO_DISPLAY)) {
            joke = getIntent().getStringExtra(KEY_JOKE_TO_DISPLAY);
        }

        TextView displayJokeView = (TextView) findViewById(R.id.joke_display_text_view);
        if (displayJokeView != null) {
            displayJokeView.setText(joke);
        }
    }
}

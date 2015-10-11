package xyz.tripcannon.backend;

/**
 * The object model for the data we are sending through endpoints
 */
public class Joke {

    private String jokeData;

    public String getData() {
        return jokeData;
    }

    public void setData(String data) {
        jokeData = data;
    }
}
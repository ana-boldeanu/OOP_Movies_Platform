package user;

import java.util.ArrayList;
import java.util.Map;

/**
 * Information about a User
 */
public class User {
    /**
     * Username of the user
     */
    private final String username;
    /**
     * Subscription Type
     */
    private String subscriptionType;
    /**
     * History of watched Shows
     */
    private Map<String, Integer> history;
    /**
     * List of favourite Shows
     */
    private ArrayList<String> favoriteShows;

    /**
     * The number of ratings the user has given so far
     */
    private int numberOfRatings;


    public User(final String username, String subscriptionType,
                Map<String, Integer> history,
                ArrayList<String> favoriteMovies) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteShows = favoriteMovies;
    }

    public String getUsername() {
        return username;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteShows;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }
}

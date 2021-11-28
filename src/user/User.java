package user;

import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;

import java.util.ArrayList;
import java.util.HashMap;
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
     * The Movies that have been rated so far
     */
    private ArrayList<String> givenMovieRatings;
    /**
     * The Serials (title + number of Season) that have been rated so far
     */
    private Map<String, ArrayList<Integer>> givenSerialRatings;
    /**
     * Number of ratings given so far
     */
    private int noRatings;

    public User(final String username, String subscriptionType,
                Map<String, Integer> history,
                ArrayList<String> favoriteShows) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.history = history;
        this.favoriteShows = favoriteShows;
        this.givenSerialRatings = new HashMap<>();
        this.givenMovieRatings = new ArrayList<>();
        this.noRatings = 0;
    }

    public String addFavourite(String title) {
        if (favoriteShows.contains(title)) {
            return "error -> " + title + " is already in favourite list";
        }

        if (history.containsKey(title)) {
            favoriteShows.add(title);
            return "success -> " + title + " was added as favourite";

        } else {
            return "error -> " + title + " is not seen";
        }
    }

    public String addView(String title) {
        if (history.containsKey(title)) {
            history.put(title, history.get(title) + 1);

        } else {
            history.put(title, 1);
        }
        return "success -> " + title + " was viewed with total views of " + history.get(title);
    }

    public String addRating(Movie movie, double rating) {
        if (history.containsKey(movie.getTitle())) {
            if (givenMovieRatings.contains(movie.getTitle())) {
                return "error -> " + movie.getTitle() + " has been already rated";
            }
            movie.receiveRating(rating);
            givenMovieRatings.add(movie.getTitle());
            return "success -> " + movie.getTitle() + " was rated with " + rating + " by " + this.getUsername();

        } else {
            return "error -> " + movie.getTitle() + " is not seen";
        }
    }

    public String addRating(Serial serial, double rating, int season) {
        if (history.containsKey(serial.getTitle())) {
            if (givenSerialRatings.containsKey(serial.getTitle())) {
                if (givenSerialRatings.get(serial.getTitle()).contains(season)) {
                    return "error -> " + serial.getTitle() + " has been already rated";
                }
                ArrayList<Integer> seasonRatings = givenSerialRatings.get(serial.getTitle());
                seasonRatings.add(season);
                givenSerialRatings.put(serial.getTitle(), seasonRatings);

            } else {
                ArrayList<Integer> seasonRatings = new ArrayList<>();
                seasonRatings.add(season);
                givenSerialRatings.put(serial.getTitle(), seasonRatings);
            }

            serial.receiveRating(rating, season);
            return "success -> " + serial.getTitle() + " was rated with " + rating + " by " + this.getUsername();

        } else {
            return "error -> " + serial.getTitle() + " is not seen";
        }
    }

    public void computeNoRatings() {
        noRatings += givenMovieRatings.size();
        noRatings += givenSerialRatings.size();
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

    public ArrayList<String> getFavoriteShows() {
        return favoriteShows;
    }

    public int getNoRatings() {
        return noRatings;
    }

    public void setNoRatings(int noRatings) {
        this.noRatings = noRatings;
    }

    @Override
    public String toString() {
        return username;
    }
}

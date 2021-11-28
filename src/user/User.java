package user;

import entertainment.Movie;
import entertainment.Serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Information about a User
 */
public final class User {
    /**
     * Username of the user
     */
    private final String username;
    /**
     * Subscription Type
     */
    private final String subscriptionType;
    /**
     * History of watched Shows
     */
    private final Map<String, Integer> watchHistory;
    /**
     * List of favourite Shows
     */
    private final ArrayList<String> favoriteShows;
    /**
     * The Movies that have been rated so far
     */
    private final ArrayList<String> givenMovieRatings;
    /**
     * The Serials (title + number of Season) that have been rated so far
     */
    private final Map<String, ArrayList<Integer>> givenSerialRatings;
    /**
     * Number of ratings given so far
     */
    private int noRatings;

    public User(final String username, final String subscriptionType,
                final Map<String, Integer> watchHistory, final ArrayList<String> favoriteShows) {
        this.username = username;
        this.subscriptionType = subscriptionType;
        this.watchHistory = watchHistory;
        this.favoriteShows = favoriteShows;
        this.givenSerialRatings = new HashMap<>();
        this.givenMovieRatings = new ArrayList<>();
        this.noRatings = 0;
    }

    public User() {
        this.username = null;
        this.subscriptionType = null;
        this.watchHistory = null;
        this.favoriteShows = null;
        this.givenMovieRatings = null;
        this.givenSerialRatings = null;
        this.noRatings = 0;
    }

    /**
     * Adds a new Show to the list of favoriteShows.
     * @param title The title to be added in favoriteShows
     * @return success if the title could be added, error if the title
     *         exists in the list already or if it is not in watchHistory
     */
    public String addFavourite(final String title) {
        if (favoriteShows.contains(title)) {
            return "error -> " + title + " is already in favourite list";
        }

        if (watchHistory.containsKey(title)) {
            favoriteShows.add(title);
            return "success -> " + title + " was added as favourite";

        } else {
            return "error -> " + title + " is not seen";
        }
    }

    /**
     * Adds a new Show to the map watchHistory, with key = title and value = 1
     * If the Show is already in watchHistory, increment its value.
     * @param title Title of the Show to be added
     * @return The number of total views for the show (value of the key title
     *         from watchHistory)
     */
    public String addView(final String title) {
        if (watchHistory.containsKey(title)) {
            watchHistory.put(title, watchHistory.get(title) + 1); // Add a view

        } else {
            watchHistory.put(title, 1); // Add the Show for the first time
        }
        return "success -> " + title + " was viewed with total views of " + watchHistory.get(title);
    }

    /**
     * Gives rating to a Movie in watchHistory. The rating can only be given
     * once. The Movie also updates its ratingsList with the new rating.
     * @param movie The Movie that receives this rating
     * @param rating Rating received
     * @return error if the Movie wasn't seen or was already rated, success
     *         otherwise.
     */
    public String addRating(final Movie movie, final double rating) {
        if (watchHistory.containsKey(movie.getTitle())) {
            if (givenMovieRatings.contains(movie.getTitle())) {
                return "error -> " + movie.getTitle() + " has been already rated";
            }
            movie.receiveRating(rating);
            givenMovieRatings.add(movie.getTitle());
            return "success -> " + movie.getTitle() + " was rated with " + rating + " by "
                    + this.getUsername();

        } else {
            return "error -> " + movie.getTitle() + " is not seen";
        }
    }

    /**
     * Gives rating to one Season of a Serial in watchHistory. A season can
     * only be rated once. The Serial also updates the ratingsList of the Season
     * with the new rating.
     * @param serial The serial that is rated
     * @param rating Given rating
     * @param season The number of the season of this serial
     * @return error if the Serial wasn't seen or if the Season was already
     *         rated, success otherwise.
     */
    public String addRating(final Serial serial, final double rating, final int season) {
        if (watchHistory.containsKey(serial.getTitle())) {
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
            return "success -> " + serial.getTitle() + " was rated with " + rating + " by "
                    + this.getUsername();

        } else {
            return "error -> " + serial.getTitle() + " is not seen";
        }
    }

    /**
     * Updates the numberOfRatings that the User has given so far. The
     * noRatings must always be computed before using its getter. After use,
     * it must be reset to 0 (So that it will not stack if we need to update
     * it later).
     */
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

    public Map<String, Integer> getWatchHistory() {
        return watchHistory;
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

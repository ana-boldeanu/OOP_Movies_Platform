package actions;

import entertainment.Movie;
import entertainment.Serial;
import user.User;

/**
 * Contains the types of commands that a User can give.
 */
public class CommandAction {
    /**
     * Current Database
     */
    private final DataContainer data;

    public CommandAction(final DataContainer data) {
        this.data = data;
    }

    /**
     * Command to add a new favourite Show to the User's list of favouriteShows
     * @param user The User that wants to add a new favourite Show
     * @param title Title of the Show to be added
     * @return the result message of the User.addFavourite function
     */
    public String addFavourite(final User user, final String title) {
        if (user.getUsername() != null) {
            return user.addFavourite(title);
        }
        return "error -> user does not exist";
    }

    /**
     * Command to add one view to a Show in the User's watchHistory
     * @param user The User that watched a Show
     * @param title Title of the watched Show
     * @return the result message of the User.addView function
     */
    public String addView(final User user, final String title) {
        if (user.getUsername() != null) {
            return user.addView(title);
        }
        return "error -> user does not exist";
    }

    /**
     * Command to give rating to a Show in the User's watchHistory
     * @param user The User that gives this rating
     * @param title Title of the rated Show
     * @param rating The given rating
     * @param season if the Show is a Serial, this is the number of the Season
     *               that is rated
     * @return the result message of the User.addRating function, which is
     *         overloaded according to the type of the Show
     */
    public String addRating(final User user, final String title, final double rating,
                            final int season) {
        // Check if the Show with this title is a Movie
        for (Movie movie : data.getMoviesList()) {
            if (movie.getTitle().equals(title)) {
                // It is a Movie, so use addRating for Movies
                return user.addRating(movie, rating);
            }
        }

        // Otherwise, check if it is a Serial
        for (Serial serial : data.getSerialsList()) {
            if (serial.getTitle().equals(title)) {
                // It is a Serial, so use addRating for Serials
                return user.addRating(serial, rating, season);
            }
        }
        return "error -> show does not exist";
    }
}

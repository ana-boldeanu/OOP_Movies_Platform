package entertainment;

import actions.DataContainer;
import user.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * General information about a Show (Movie or Serial)
 */
public abstract class Show {
    /**
     * Title of the show
     */
    protected final String title;
    /**
     * Year of release
     */
    protected final int year;
    /**
     * List of genres for the show
     */
    protected final ArrayList<String> genres;
    /**
     * The cast playing in the show
     */
    protected final ArrayList<String> cast;
    /**
     * Final calculated rating of the show
     */
    protected double finalRating;
    /**
     * How many users added this show to their favouriteShows list
     */
    protected int timesFavorite;
    /**
     * How many users viewed this show
     */
    protected int timesViewed;
    /**
     * Total duration (for serials, add the duration of each season)
     */
    protected int totalDuration;

    public Show(final String title, final int year,
                final ArrayList<String> genres, final ArrayList<String> cast) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.cast = cast;
        this.finalRating = 0;
        this.timesFavorite = 0;
        this.timesViewed = 0;
        this.totalDuration = 0;
    }

    /**
     * Updates the value of timesFavourite for the current instance of Show.
     * The value of timesFavourite must always be computed before using its
     * getter. After use, timesFavourite must always be reset to 0 using setter.
     * (So that if we need to update it again, it will not stack)
     * @param data The current database
     */
    public void computeTimesFavorite(final DataContainer data) {
        for (User user : data.getUsersList()) {
            if (user.getFavoriteShows().contains(title)) {
                timesFavorite++;
            }
        }
    }

    /**
     * Updates the value of timesViewed for the current instance of Show.
     * The value of timesViewed must always be computed before using its
     * getter. After use, timesViewed must always be reset to 0 using setter.
     * (So that if we need to update it again, it will not stack)
     * @param data The current database
     */
    public void computeTimesViewed(final DataContainer data) {
        for (User user : data.getUsersList()) {
            for (Map.Entry<String, Integer> showEntry : user.getWatchHistory().entrySet()) {
                if (showEntry.getKey().equals(title)) {
                    timesViewed += showEntry.getValue();
                }
            }
        }
    }

    /**
     * Updates the value of finalRating for the current instance of Show.
     * The value of finalRating must always be computed before using its
     * getter. After use, finalRating must always be reset to 0 using setter.
     * (So that if we need to update it again, it will not stack)
     */
    public abstract void computeRating();

    /**
     * Updates the value of totalDuration for the current instance of Show.
     * The value of totalDuration must always be computed before using its
     * getter. After use, totalDuration must always be reset to 0 using setter.
     * (So that if we need to use it later again, it will not stack)
     */
    public abstract void computeTotalDuration();

    /**
     * Get the title of a Movie or Show
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the year of release of a Movie or Show
     */
    public int getYear() {
        return year;
    }

    /**
     * Get the list of genres of a Movie or Show
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * Get the finalRating of a Movie or Serial. Must only be used after the function
     * computeRating(). After use, finalRating must be reset.
     */
    public double getFinalRating() {
        return finalRating;
    }

    /**
     * Get timesFavorite of a Movie or Serial. Must only be used after the function
     * computeTimesFavorite(). After use, timesFavorite must be reset.
     */
    public int getTimesFavorite() {
        return timesFavorite;
    }

    /**
     * Get timesViewed of a Movie or Serial. Must only be used after the function
     * computeTimesViewed()
     * After use, timesViewed must be reset.
     */
    public int getTimesViewed() {
        return timesViewed;
    }

    /**
     * Get totalDuration of a Movie or Serial. Must only be used after the function
     * computeTotalDuration(). After use, totalDuration must be reset.
     */
    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * Use only to reset finalRating to value 0.
     */
    public void setFinalRating(double finalRating) {
        this.finalRating = finalRating;
    }

    /**
     * Use only to reset timesFavorite to value 0.
     */
    public void setTimesFavorite(int timesFavorite) {
        this.timesFavorite = timesFavorite;
    }

    /**
     * Use only to reset timesViewed to value 0.
     */
    public void setTimesViewed(int timesViewed) {
        this.timesViewed = timesViewed;
    }

    /**
     * Use only to reset totalDuration to value 0.
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    /**
     * Used to print the title of the Movie or Serial
     */
    @Override
    public String toString() {
        return title;
    }
}

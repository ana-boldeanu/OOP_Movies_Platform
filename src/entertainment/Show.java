package entertainment;

import actions.DataContainer;
import user.User;

import java.lang.reflect.Array;
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
    protected ArrayList<String> genres;
    /**
     * The cast playing in the show
     */
    protected ArrayList<String> cast;
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

    public void computeTimesFavorite(DataContainer data) {
        for (User user : data.getUsersList()) {
            if (user.getFavoriteShows().contains(title)) {
                timesFavorite++;
            }
        }
    }

    public void computeTimesViewed(DataContainer data) {
        for (User user : data.getUsersList()) {
            for (Map.Entry<String, Integer> showEntry : user.getHistory().entrySet()) {
                if (showEntry.getKey().equals(title)) {
                    timesViewed += showEntry.getValue();
                }
            }
        }
    }

    public abstract void computeRating();

    public abstract void computeTotalDuration();

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public double getFinalRating() {
        return finalRating;
    }

    public int getTimesFavorite() {
        return timesFavorite;
    }

    public int getTimesViewed() {
        return timesViewed;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setFinalRating(double finalRating) {
        this.finalRating = finalRating;
    }

    public void setTimesFavorite(int timesFavorite) {
        this.timesFavorite = timesFavorite;
    }

    public void setTimesViewed(int timesViewed) {
        this.timesViewed = timesViewed;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Override
    public String toString() {
        return title;
    }
}

package entertainment;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * General information about a Show (Movie or Serial)
 */
public abstract class Show {
    /**
     * Title of the show
     */
    private final String title;
    /**
     * Year of release
     */
    private final int year;
    /**
     * List of genres for the show
     */
    private final ArrayList<Genre> genres;
    /**
     * The cast playing in the show
     */
    private final ArrayList<String> cast;
    /**
     * Rating of the show
     */
    private int rating;
    /**
     * How many users added this show to their favouriteShows list
     */
    private int timesFavorite;
    /**
     * How many users viewed this show
     */
    private int timesViewed;
    /**
     * Total duration (for serials, add the duration of each season)
     */
    private int totalDuration;

    // TODO Astea 4 de sus au nevoie de metode care sa le actualizeze
    // o metoda abstracta cu override pt totalDuration?

    public Show(final String title, final int year,
                final ArrayList<Genre> genres, final ArrayList<String> cast) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.cast = cast;
        this.rating = 0;
        this.timesFavorite = 0;
        this.timesViewed = 0;
        this.totalDuration = 0;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public ArrayList<String> getCast() {
        return cast;
    }

    public int getRating() {
        return rating;
    }

    // TODO Metoda abstracta gen receiveRating(User ?) care actualizeaza ratingul mereu
    // numara cati useri i-au dat rating pana acum?
}

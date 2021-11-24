package entertainment;

import java.util.ArrayList;

/**
 * Extra/specific information about a Movie
 */
public class Movie extends Show {
    /**
     * Duration of the movie
     */
    private final int duration;


    public Movie(final String title, final int year, final int duration,
                 final ArrayList<Genre> genres, final ArrayList<String> cast,
                 int rating) {
        super(title, year, genres, cast);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }


}

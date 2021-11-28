package entertainment;

import java.util.ArrayList;

/**
 * Extra/specific information about a Movie
 */
public final class Movie extends Show {
    /**
     * Duration of the movie
     */
    private final int duration;
    /**
     * List of ratings received so far
     */
    private final ArrayList<Double> ratingsList;

    public Movie(final String title, final int year, final int duration,
                 final ArrayList<String> genres, final ArrayList<String> cast) {
        super(title, year, genres, cast);
        this.duration = duration;
        this.ratingsList = new ArrayList<>();
    }

    /**
     * Updates the list of ratings that have been received so far.
     * @param rating The new rating to be added
     */
    public void receiveRating(double rating) {
        ratingsList.add(rating);
    }

    /**
     * Computes finalRating as average of ratings received so far.
     */
    @Override
    public void computeRating() {
        if (ratingsList.size() > 0) {
            double sum = 0;
            for (Double rating : ratingsList) {
                sum += rating;
            }
            this.finalRating = sum / ratingsList.size();

        } else {
            this.finalRating = 0;
        }
    }

    /**
     * Computes the totalDuration of the Movie, which is the same as its
     * duration.
     */
    @Override
    public void computeTotalDuration() {
        totalDuration = duration;
    }
}

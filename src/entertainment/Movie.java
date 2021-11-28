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
    /**
     * List of ratings received so far
     */
    private ArrayList<Double> ratingsList;


    public Movie(final String title, final int year, final int duration,
                 final ArrayList<String> genres, final ArrayList<String> cast) {
        super(title, year, genres, cast);
        this.duration = duration;
        this.ratingsList = new ArrayList<>();
    }

    public void receiveRating(double rating) {
        ratingsList.add(Double.valueOf(rating));
    }

    @Override
    public void computeRating() {
        if (ratingsList.size() > 0) {
            double sum = 0;
            for (Double rating : ratingsList) {
                sum += rating.doubleValue();
            }
            this.finalRating = sum / ratingsList.size();

        } else {
            this.finalRating = 0;
        }
    }

    @Override
    public void computeTotalDuration() {
        totalDuration = duration;
    }
}

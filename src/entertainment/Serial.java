package entertainment;

import java.util.ArrayList;

/**
 * Extra/specific information about a Serial
 */
public final class Serial extends Show {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * List of seasons
     */
    private final ArrayList<Season> seasons;

    public Serial(final String title, final int year, final int numberOfSeasons,
                  final ArrayList<String> genres, final ArrayList<String> cast,
                  final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    /**
     * Updates the list of ratings that have been received so far by one of
     * the Seasons of this Serial.
     * @param rating The rating to be added
     * @param seasonNumber The number of the season that received the rating
     */
    public void receiveRating(final double rating, final int seasonNumber) {
        for (Season season : seasons) {
            if (season.getCurrentSeason() == seasonNumber) {
                season.getRatings().add(rating);
            }
        }
    }

    /**
     * Computes finalRating as average of the ratings of each Season for this
     * Serial (if at least one Season has been rated so far).
     */
    @Override
    public void computeRating() {
        double sum = 0;
        boolean hasOne = false;
        for (Season season : seasons) {
            if (season.getRatings().size() > 0) {
                for (Double rating : season.getRatings()) {
                    sum += rating;
                }
                hasOne = true; // At least one season was rated
            }
        }
        if (hasOne) {
            this.finalRating = sum / numberOfSeasons;

        } else {
            this.finalRating = 0;
        }
    }

    /**
     * Computes totalDuration of the Serial as sum of the durations of its
     * Seasons.
     */
    @Override
    public void computeTotalDuration() {
        for (Season season : seasons) {
            totalDuration += season.getDuration();
        }
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }
}

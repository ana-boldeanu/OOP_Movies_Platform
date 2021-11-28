package entertainment;

import actions.DataContainer;
import user.User;

import java.util.ArrayList;
import java.util.Map;

/**
 * Extra/specific information about a Serial
 */
public class Serial extends Show {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * List of seasons
     */
    private ArrayList<Season> seasons;

    public Serial(final String title, final int year, final int numberOfSeasons,
                  final ArrayList<String> genres, final ArrayList<String> cast,
                  final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public void receiveRating(double rating, int seasonNumber) {
        for (Season season : seasons) {
            if (season.getCurrentSeason() == seasonNumber) {
                season.getRatings().add(Double.valueOf(rating));
            }
        }
    }

    @Override
    public void computeRating() {
        double sum = 0;
        boolean OK = false;
        for (Season season : seasons) {
            if (season.getRatings().size() > 0) {
                for (Double rating : season.getRatings()) {
                    sum += rating.doubleValue();
                }
                OK = true;
            }
        }
        if (OK) {
            this.finalRating = sum / numberOfSeasons;

        } else {
            this.finalRating = 0;
        }
    }

    @Override
    public void computeTotalDuration() {
        for (Season season : seasons) {
            totalDuration += season.getDuration();
        }
    }



    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

}

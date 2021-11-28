package actor;

import entertainment.Movie;
import entertainment.Serial;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Information about an Actor
 */
public final class Actor {
    /**
     * Name of the actor
     */
    private final String name;
    /**
     * Description of the actor's career
     */
    private final String careerDescription;
    /**
     * List of the Movies that the actor played in
     */
    private final ArrayList<Movie> moviesFilmography;
    /**
     * List of the Movies that the actor played in
     */
    private final ArrayList<Serial> serialsFilmography;
    /**
     * Map of awards received by the actor (and their number)
     */
    private final Map<ActorsAwards, Integer> awards;
    /**
     * Number of received awards
     */
    private int numberOfAwards;
    /**
     * Rating of the actor, given by the average of the ratings of the shows
     * in filmography.
     */
    private double rating;

    public Actor(final String name, final Map<ActorsAwards, Integer> awards,
                 final ArrayList<Movie> moviesFilmography, final String careerDescription,
                 final ArrayList<Serial> serialsFilmography) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.moviesFilmography = moviesFilmography;
        this.serialsFilmography = serialsFilmography;
        this.awards = awards;
        this.rating = 0;
    }

    /**
     * Calculates the rating of the Actor as average of the ratings of all
     * the Shows from their filmography. The rating must always be computed
     * before using its getter. After use, the rating must be reset to 0
     * with its setter (So that if we need to update it later, its value
     * will not stack).
     */
    public void computeRating() {
        double sum = 0;
        int noShows = 0;

        // The movies they played in
        for (Movie movie : moviesFilmography) {
            movie.computeRating();
            if (movie.getFinalRating() != 0) {
                sum += movie.getFinalRating();
                noShows++;
            }
            movie.setFinalRating(0);
        }

        // The serials they played in
        for (Serial serial : serialsFilmography) {
            serial.computeRating();
            if (serial.getFinalRating() != 0) {
                sum += serial.getFinalRating();
                noShows++;
            }
            serial.setFinalRating(0);
        }

        if (noShows > 0) {
            rating = sum / noShows;
        } else {
            rating = 0;
        }
    }

    /**
     * Check if the Actor has received *all* the awards from the list of awards
     * given as parameter.
     * @param wantedAwards The awards we search for
     * @return false if at least one award does not exist in the Actor's awards
     *         list, true otherwise.
     */
    public boolean hasAwards(final List<String> wantedAwards) {
        for (String award : wantedAwards) {
            if (!awards.containsKey(Utils.stringToAwards(award))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compute the total numberOfAwards that the Actor has received so far.
     * The numberOfAwards must always be computed before using its getter.
     * After use, the numberOfAwards must be reset to 0 with its setter (So that
     * if we need to use it again later, it will not stack its value)
     */
    public void computeNoAwards() {
        for (Map.Entry<ActorsAwards, Integer> awardEntry : awards.entrySet()) {
            numberOfAwards += awardEntry.getValue();
        }
    }

    /**
     * Uses regex to check if the Actor's careerDescription contains *all* the
     * keyWords from the list of keyWords given as parameter.
     * @param keyWords List of keyWords that we look for
     * @return true if all the keyWords have been found
     */
    public boolean hasKeyWords(final List<String> keyWords) {
        for (String keyWord : keyWords) {

            Pattern pattern = Pattern.compile("[ .,-]" + keyWord + "[ .,-]",
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(careerDescription);

            boolean matchFound = matcher.find();
            if (matchFound) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfAwards() {
        return numberOfAwards;
    }

    public double getRating() {
        return rating;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public void setNumberOfAwards(int numberOfAwards) {
        this.numberOfAwards = numberOfAwards;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name;
    }
}

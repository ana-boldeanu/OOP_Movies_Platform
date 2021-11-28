package actor;

import entertainment.Movie;
import entertainment.Serial;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Information about an Actor
 */
public class Actor {
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
    private ArrayList<Movie> moviesFilmography;
    /**
     * List of the Movies that the actor played in
     */
    private ArrayList<Serial> serialsFilmography;
    /**
     * Map of awards received by the actor (and their number)
     */
    private Map<ActorsAwards, Integer> awards;
    /**
     * Number of received awards
     */
    private int numberOfAwards;
    /**
     * Rating of the actor, given by the average of the ratings of the shows
     * in filmography.
     */
    private double rating;

    public Actor(final String name, Map<ActorsAwards, Integer> awards,
                 ArrayList<Movie> moviesFilmography, ArrayList<Serial> serialsFilmography,
                 final String careerDescription) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.moviesFilmography = moviesFilmography;
        this.serialsFilmography = serialsFilmography;
        this.awards = awards;
        this.rating = Double.valueOf(0);
    }

    public void computeRating() {
        double sum = 0;
        int noShows = 0;
        for (Movie movie : moviesFilmography) {
            movie.computeRating();
            if (movie.getFinalRating() != 0) {
                sum += movie.getFinalRating();
                noShows++;
            }
        }
        for (Serial serial : serialsFilmography) {
            serial.computeRating();
            if (serial.getFinalRating() != 0) {
                sum += serial.getFinalRating();
                noShows++;
            }
        }
        this.rating = sum;
    }

    public boolean hasAwards(List<String> wantedAwards) {
        for (String award : wantedAwards) {
            if (!awards.containsKey(Utils.stringToAwards(award))) {
                return false;
            }
        }
        return true;
    }

    public void computeNoAwards() {
        for (Map.Entry<ActorsAwards, Integer> awardEntry : awards.entrySet()) {
            numberOfAwards += awardEntry.getValue();
        }
    }

    public boolean hasKeyWords(List<String> keyWords) {
        for (String keyWord : keyWords) {
            if (!careerDescription.contains(keyWord)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
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

    @Override
    public String toString() {
        return name;
    }
}

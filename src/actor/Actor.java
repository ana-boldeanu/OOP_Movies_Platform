package actor;

import java.util.ArrayList;
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
     * List of the shows that the actor played in
     */
    private ArrayList<String> filmography;
    // TODO: Sa-mi dau seama de unde iau lista asta
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
     * in rolesList.
     */
    private int averageRating;

    public Actor(final String name, Map<ActorsAwards, Integer> awards,
                 ArrayList<String> filmography, final String careerDescription) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }
}

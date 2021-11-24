package entertainment;

import java.util.ArrayList;

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
    private final ArrayList<Season> seasons;

    public Serial(final String title, final int year, final int numberOfSeasons,
                  final ArrayList<Genre> genres, final ArrayList<String> cast,
                  final ArrayList<Season> seasons) {
        super(title, year, genres, cast);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

}

package actions;

import entertainment.Genre;
import entertainment.Show;
import user.User;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Contains the types of Recommendations that a User can ask for.
 */
public class RecommendAction {
    /**
     * Current Database
     */
    private final DataContainer data;

    public RecommendAction(final DataContainer data) {
        this.data = data;
    }

    /**
     * Gives the User a Standard recommendation.
     * @param user User that required this recommendation
     * @return The title of the first Show from our Database that the User
     *         hasn't watched yet.
     */
    public String getStandard(final User user) {
        // List of all the Shows from our current Database
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        for (Show show : shows) {
            if (!user.getWatchHistory().containsKey(show.getTitle())) {
                // Return first unseen Show
                return "StandardRecommendation result: " + show.getTitle();
            }
        }

        return "StandardRecommendation cannot be applied!";
    }

    /**
     * Gives the User a BestRatedUnseen recommendation. First, sorts all Shows
     * by their finalRatings in descending order.
     * @param user User that required this recommendation
     * @return The title of the best rated Show from our Database that the User
     *         hasn't watched yet.
     */
    public String getBestUnseen(final User user) {
        // List of all the Shows from our current Database
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        // Sort them by their rating, in descending order
        for (Show show : shows) {
            // Compute rating for each Show. It must be reset after use.
            show.computeRating();
        }
        Collections.sort(shows, new Comparator<Show>() {
            @Override
            public int compare(final Show o1, final Show o2) {
                return Double.compare(o2.getFinalRating(), o1.getFinalRating());
            }
        });
        for (Show show : shows) {
            // Reset rating for each Show
            show.setFinalRating(0);
        }

        for (Show show : shows) {
            if (!user.getWatchHistory().containsKey(show.getTitle())) {
                // Return first unseen Show from the sorted list
                return "BestRatedUnseenRecommendation result: " + show.getTitle();
            }
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    /**
     * Gives the User a Popular Recommendation. First, computes a list of all
     * genres, sorted by their popularity (i.e. number of views) in descending
     * order.
     * @param user The User that required this recommendation (must be PREMIUM)
     * @return The title of the first Show that has the most popular genre
     *         and hasn't been watched yet by the User.
     */
    public String getPopular(final User user) {
        // Check that the user is PREMIUM
        if (!user.getSubscriptionType().equals("PREMIUM")) {
            return "PopularRecommendation cannot be applied!";
        }

        // List of all the Shows from our current Database
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        /**
         * A local class used to sort Genres by their popularity
         */
        class GenrePopularity {
            /**
             * Name of the genre
             */
            private final String name;
            /**
             * Popularity of the genre (number of watches)
             */
            private final int popularity;

            GenrePopularity(final String name, final int popularity) {
                this.name = name;
                this.popularity = popularity;
            }

            public String getName() {
                return name;
            }

            public int getPopularity() {
                return popularity;
            }
        }

        // The list of genres that will be sorted by most popular
        List<GenrePopularity> genres = new ArrayList<>();

        // Compute popularity for each Genre
        for (Genre genre : Genre.values()) {
            // The Shows that have this Genre
            List<Show> filteredShows = new ArrayList<>();
            for (Show show : shows) {
                if (show.getGenres().contains(Utils.genreToString(genre))) {
                    filteredShows.add(show);
                }
            }
            // The popularity is the sum of timesViewed of the Shows that have
            // this genre
            int popularity = 0;
            for (Show show : filteredShows) {
                show.computeTimesViewed(data); // Compute timesViewed
                popularity += show.getTimesViewed();
                show.setTimesViewed(0); // Reset timesViewed (for later use)
            }

            genres.add(new GenrePopularity(Utils.genreToString(genre), popularity));
        }
        Collections.sort(genres, new Comparator<GenrePopularity>() {
            @Override
            public int compare(final GenrePopularity o1, final GenrePopularity o2) {
                return Integer.compare(o1.popularity, o2.popularity);
            }
        });
        Collections.reverse(genres);

        // For the most popular Genre, get the first unseen Show for this User
        for (GenrePopularity genre : genres) {
            for (Show show : shows) {
                if (show.getGenres().contains(genre.name)) {
                    if (!user.getWatchHistory().containsKey(show.getTitle())) {
                        return "PopularRecommendation result: " + show.getTitle();
                    }
                }
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    /**
     * Gives the User a Favourite Recommendation.
     * @param user The User that required this recommendation (must be PREMIUM)
     * @return The title of the first Show that has been added to the list
     *         of favouriteShows by most Users.
     */
    public String getFavourite(final User user) {
        // Check if the User is PREMIUM
        if (!user.getSubscriptionType().equals("PREMIUM")) {
            return "FavoriteRecommendation cannot be applied!";
        }

        // List of all the Shows in our current Database
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        // Sort these Shows by timesFavorite, in descending order
        for (Show show : shows) {
            // Compute timesFavorite for each Show
            show.computeTimesFavorite(data);
        }
        Collections.sort(shows, new Comparator<Show>() {
            @Override
            public int compare(final Show o1, final Show o2) {
                return Integer.compare(o2.getTimesFavorite(), o1.getTimesFavorite());
            }
        });

        // Needs to use variable found in order to iterate through the entire
        // list of Shows before returning result (in order to reset timesFavorite)
        boolean found = false;
        String result = "";

        for (Show show : shows) {
            if (show.getTimesFavorite() != 0) {
                show.setTimesFavorite(0); // Reset timesFavorite for each Show

                if (!user.getWatchHistory().containsKey(show.getTitle())) {
                    if (!found) {
                        found = true;
                        result = "FavoriteRecommendation result: " + show.getTitle();
                    }
                }
            }
        }

        if (found) {
            return result;
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * Executes a Search Recommendation. Returns a list of all the Shows of
     * the desired Genre, sorted by their Rating in ascending order.
     * @param user The User that required this recommendation (must be PREMIUM)
     * @param genre The desired Genre, used as filter
     * @return All the Shows that have this Genre
     */
    public String getSearch(final User user, final String genre) {
        // Check if the User is PREMIUM
        if (!user.getSubscriptionType().equals("PREMIUM")) {
            return "SearchRecommendation cannot be applied!";
        }

        // List of all the Shows in our current Database
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        // Build list of the Shows that have this Genre
        List<Show> filteredShows = new ArrayList<>();
        for (Show show : shows) {
            if (show.getGenres().contains(genre)) {
                filteredShows.add(show);
                // Compute the rating for each Show (must be reset after use)
                show.computeRating();
            }
        }
        // Sort the Shows by their rating
        Collections.sort(filteredShows, new Comparator<Show>() {
            @Override
            public int compare(final Show o1, final Show o2) {
                if (o1.getFinalRating() == o2.getFinalRating()) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                return Double.compare(o1.getFinalRating(), o2.getFinalRating());
            }
        });

        for (Show show :shows) {
            // Reset rating for each Show
            show.setFinalRating(0);
        }

        // This will be the result of the recommendation
        ArrayList<String> recommendedShows = new ArrayList<>();
        for (Show show : filteredShows) {
            if (!user.getWatchHistory().containsKey(show.getTitle())) {
                // Add this Show if it hasn't been watched yet
                recommendedShows.add(show.getTitle());
            }
        }

        if (!recommendedShows.isEmpty()) {
            return "SearchRecommendation result: " + recommendedShows.toString();
        }

        return "SearchRecommendation cannot be applied!";
    }
}

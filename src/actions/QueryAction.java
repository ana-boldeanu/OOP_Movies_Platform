package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;
import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Contains the types of Queries that a User can ask for.
 */
public class QueryAction {
    /**
     * Current Database
     */
    private final DataContainer data;

    public QueryAction(final DataContainer data) {
        this.data = data;
    }

    /**
     * Executes a query on Actors, depending on the given parameters.
     * @param number How many actors we want to return.
     * @param filters A list of filters where filters[0] is a list that contains
     *                the year of release for a Show, filters[1] is a list of
     *                genres of a Show, filters[2] is a list of keyWords that
     *                an Actor's careerDescription may contain and finally
     *                filters[3] is a list of Awards that an Actor may receive.
     *                Used for different types of criteria.
     * @param sortType "asc" if the resulting list of the query should be sorted
     *                 in ascending order, "desc" for descending order.
     * @param criteria The type of query that will be executed on the ActorsList
     * @return The result of the query, which is a list of Actors which respect
     *         the desired criteria.
     */
    public List<Actor> queryActors(final int number, final List<List<String>> filters,
                                   final String sortType, final String criteria) {
        // The List of Actors from Database
        List<Actor> actors = data.getActorsList();

        // This will be the result of this Query
        ArrayList<Actor> sortedActors = new ArrayList<>();

        switch (criteria) {
            case "average":
                for (Actor actor : actors) {
                    // Compute the rating of this Actor
                    actor.computeRating();
                }

                Collections.sort(actors, new Comparator<Actor>() {
                    @Override
                    public int compare(final Actor o1, final Actor o2) {
                        if (o1.getRating() == o2.getRating()) {
                            return o1.getName().compareTo(o2.getName());
                        }
                        return Double.compare(o1.getRating(), o2.getRating());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(actors);
                }
                int actorsToAdd = number;
                for (Actor actor : actors) {
                    if (actor.getRating() > 0) {
                        sortedActors.add(actor);
                        actorsToAdd--;
                    }
                    if (actorsToAdd == 0) {
                        break;
                    }
                }

                for (Actor actor : actors) {
                    // After use, we have to reset each rating
                    actor.setRating(0);
                }

                break;

            case "awards":
                for (Actor actor : actors) {
                    // Compute the number of awards of this Actor
                    actor.computeNoAwards();
                    if (actor.hasAwards(filters.get(3))) {
                        sortedActors.add(actor);
                    }
                }

                Collections.sort(sortedActors, new Comparator<Actor>() {
                    @Override
                    public int compare(final Actor o1, final Actor o2) {
                        if (o1.getNumberOfAwards() == o2.getNumberOfAwards()) {
                            return o1.getName().compareTo(o2.getName());
                        }
                        return Integer.compare(o1.getNumberOfAwards(), o2.getNumberOfAwards());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(sortedActors);
                }
                for (Actor actor : actors) {
                    // After use, we have to reset each number of awards
                    actor.setNumberOfAwards(0);
                }
                break;

            case "filter_description":
                for (Actor actor : actors) {
                    if (actor.hasKeyWords(filters.get(2))) {
                        sortedActors.add(actor);
                    }
                }
                Collections.sort(sortedActors, new Comparator<Actor>() {
                    @Override
                    public int compare(final Actor o1, final Actor o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(sortedActors);
                }
                break;

            default:
                break;
        }
        return sortedActors;
    }

    /**
     * Executes a query on Shows, depending on the given parameters. This
     * function works the same way for Movies and Serials. It uses parameter
     * showType to determine the type of the Show, in order to build the list
     * of Movies/Serials that needs to be queried.
     * @param number How many Movies/Serials we want to return.
     * @param filters A list of filters where filters[0] is a list that contains
     *                the year of release for a Show, filters[1] is a list of
     *                genres of a Show, filters[2] is a list of keyWords that
     *                an Actor's careerDescription may contain and finally
     *                filters[3] is a list of Awards that an Actor may receive.
     *                Used for different types of criteria.
     * @param sortType "asc" if the resulting list of the query should be sorted
     *                 in ascending order, "desc" for descending order.
     * @param criteria The type of query that will be executed on the ActorsList.
     * @param showType The type of Shows that we apply this query on.
     * @return The result of the query, which is a list of Shows which respect
     *         the desired criteria.
     */
    public List<Show> queryShows(final int number, final List<List<String>> filters,
                                 final String sortType, final String criteria,
                                 final String showType) {
        // List of Movies from our Database
        List<Movie> movies = data.getMoviesList();

        // List of Serials from our Database
        List<Serial> serials = data.getSerialsList();

        // List of Shows that will be filtered
        List<Show> showsToFilter = new ArrayList<>();

        // List of Shows that have been filtered so far
        List<Show> filteredShows = new ArrayList<>();

        // This will be the result of the query
        List<Show> sortedShows = new ArrayList<>();

        // Build the list of Shows that will be queried
        if (showType.equals("movies")) {
            showsToFilter.addAll(movies);
        } else {
            showsToFilter.addAll(serials);
        }

        if (filters != null) {
            // Apply First filter (for year)
            List<String> years = filters.get(0);

            if (years.get(0) != null) {
                int year = Integer.parseInt(years.get(0));
                for (Show show : showsToFilter) {
                    if (show.getYear() == year) {
                        filteredShows.add(show);
                    }
                }
            } else {
                // There is no year filter
                filteredShows.addAll(showsToFilter);
            }

            // Apply Second filter (for genres)
            List<String> genres = filters.get(1);

            if (genres.get(0) != null) {
                // Reset lists used for filtering
                showsToFilter.clear();
                showsToFilter.addAll(filteredShows);
                filteredShows.clear();

                for (String genre : genres) {
                    for (Show show : showsToFilter) {
                        if (show.getGenres().contains(genre)) {
                            filteredShows.add(show);
                        }
                    }
                }
            }
        }

        // Compute all the data we need for any kind of query on Shows. We
        // must reset this data after use.
        for (Show show : filteredShows) {
            show.computeTimesFavorite(data);
            show.computeTimesViewed(data);
            show.computeTotalDuration();
            show.computeRating();
        }

        switch (criteria) {
            case "ratings":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(final Show o1, final Show o2) {
                        if (o1.getFinalRating() == o2.getFinalRating()) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                        return Double.compare(o1.getFinalRating(), o2.getFinalRating());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                int showsToAdd = number;
                for (Show show : filteredShows) {
                    if (show.getFinalRating() > 0) {
                        sortedShows.add(show);
                        showsToAdd--;
                    }
                    if (showsToAdd == 0) {
                        break;
                    }
                }
                break;

            case "favorite":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(final Show o1, final Show o2) {
                        if (o1.getTimesFavorite() == o2.getTimesFavorite()) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                        return Integer.compare(o1.getTimesFavorite(), o2.getTimesFavorite());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                showsToAdd = number;
                for (Show show : filteredShows) {
                    if (show.getTimesFavorite() != 0) {
                        sortedShows.add(show);
                        showsToAdd--;
                        if (showsToAdd == 0) {
                            break;
                        }
                    }
                }
                break;

            case "longest":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(final Show o1, final Show o2) {
                        if (o1.getTotalDuration() == o2.getTotalDuration()) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                        return Integer.compare(o1.getTotalDuration(), o2.getTotalDuration());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                showsToAdd = number;
                for (Show show : filteredShows) {
                    sortedShows.add(show);
                    showsToAdd--;
                    if (showsToAdd == 0) {
                        break;
                    }
                }
                break;

            case "most_viewed":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(final Show o1, final Show o2) {
                        if (o1.getTimesViewed() == o2.getTimesViewed()) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                        return Integer.compare(o1.getTimesViewed(), o2.getTimesViewed());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                showsToAdd = number;
                for (Show show : filteredShows) {
                    if (show.getTimesViewed() > 0) {
                        sortedShows.add(show);
                        showsToAdd--;
                        if (showsToAdd == 0) {
                            break;
                        }
                    }
                }
                break;

            default:
                break;
        }

        // Reset the computed data, so that we can update/reuse it later
        for (Show show : filteredShows) {
            show.setFinalRating(0);
            show.setTimesFavorite(0);
            show.setTimesViewed(0);
            show.setTotalDuration(0);
        }

        return sortedShows;
    }

    /**
     * Executes a query on Users, depending on the given criteria.
     * @param number The number of Users that we want to return.
     * @param sortType "asc" if the resulting list of the query should be sorted
     *                 in ascending order, "desc" for descending order.
     * @param criteria The criteria we use for this query.
     * @return A list of Users that resulted after applying the criteria.
     */
    public List<User> queryUsers(final int number, final String sortType, final String criteria) {
        // The Users in our current Database
        List<User> users = data.getUsersList();

        // This will be the result of the query
        List<User> sortedUsers = new ArrayList<>();

        for (User user : users) {
            // Compute the numberOfRatings that each user has given so far
            user.computeNoRatings();
        }

        if (criteria.equals("num_ratings")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(final User o1, final User o2) {
                    if (o1.getNoRatings() == o2.getNoRatings()) {
                        return o1.getUsername().compareTo(o2.getUsername());
                    }
                    return Integer.compare(o1.getNoRatings(), o2.getNoRatings());
                }
            });
            if (sortType.equals("desc")) {
                Collections.reverse(users);
            }
            int usersToAdd = number;
            for (User user : users) {
                if (user.getNoRatings() > 0) {
                    sortedUsers.add(user);
                    usersToAdd--;
                    if (usersToAdd == 0) {
                        break;
                    }
                }
            }
        }

        for (User user : users) {
            // Reset the numberOfRatings for each User (so that we can recompute
            // it later, with new information)
            user.setNoRatings(0);
        }

        return sortedUsers;
    }
}

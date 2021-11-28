package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;
import fileio.SerialInputData;
import user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * General information about an Action (command) that users ask for
 */
public class Action {
    private DataContainer data;

    public Action(DataContainer data) {
        this.data = data;
    }

    public String commandFavourite(String username, String title) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                return user.addFavourite(title);
            }
        }
        return "error -> " + username + " does not exist";
    }

    public String commandView(String username, String title) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                return user.addView(title);
            }
        }
        return "error -> " + username + " does not exist";
    }

    public String commandRating(String username, String title, double rating, int season) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (Movie movie : data.getMoviesList()) {
                    if (movie.getTitle().equals(title)) { // If it's a movie
                        return user.addRating(movie, rating);
                    }
                }
                for (Serial serial : data.getSerialsList()) {
                    if (serial.getTitle().equals(title)) { // Else it's a series
                        return user.addRating(serial, rating, season);
                    }
                }
            }
        }
        return "error -> " + username + " does not exist";
    }

    public List<Actor> queryActors(int number, List<List<String>> filters, String sortType, String criteria) {
        List<Actor> actors = data.getActorsList();
        ArrayList<Actor> sortedActors = new ArrayList<>();

        switch (criteria) {
            case "average":
                for (Actor actor : actors) {
                    actor.computeRating();
                }
                Collections.sort(actors, new Comparator<Actor>() {
                    @Override
                    public int compare(Actor actor1, Actor actor2) {
                        if (actor1.getRating() == actor2.getRating()) {
                            return actor1.getName().compareTo(actor2.getName());
                        }
                        return Double.compare(actor1.getRating(), actor2.getRating());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(actors);
                }
                for (Actor actor : actors) {
                    if (actor.getRating() > 0) {
                        sortedActors.add(actor);
                        number--;
                    }
                    if (number == 0) {
                        break;
                    }
                }
                break;

            case "awards":
                for (Actor actor : actors) {
                    actor.computeNoAwards(filters.get(3));
                    if (actor.hasAwards(filters.get(3))) {
                        sortedActors.add(actor);
                    }
                }
                Collections.sort(sortedActors, new Comparator<Actor>() {
                    @Override
                    public int compare(Actor actor1, Actor actor2) {
                        if (actor1.getNumberOfAwards() == actor2.getNumberOfAwards()) {
                            return actor1.getName().compareTo(actor2.getName());
                        }
                        return Integer.compare(actor1.getNumberOfAwards(), actor2.getNumberOfAwards());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(sortedActors);
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
                    public int compare(Actor actor1, Actor actor2) {
                        return actor1.getName().compareTo(actor2.getName());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(sortedActors);
                }
                break;
        }
        return sortedActors;
    }

    public List<Show> yearFilter(List<Show> shows, int year) {
        List<Show> filteredShows = new ArrayList<>();
        for (Show show : shows) {
            if (show.getYear() == year) {
                filteredShows.add(show);
            }
        }
        return filteredShows;
    }

    public List<Show> genreFilter(List<Show> shows,  List<String> genres) {
        List<Show> filteredShows = new ArrayList<>();
        for (String genre : genres) {
            for (Show show : shows) {
                if (show.getGenres().contains(genre)) {
                    filteredShows.add(show);
                }
            }
        }
        return filteredShows;
    }

    public List<Show> queryShows(int number, List<List<String>> filters, String sortType, String criteria) {
        List<Movie> movies = data.getMoviesList();
        List<Serial> serials = data.getSerialsList();
        List<Show> filteredShows = new ArrayList<>();
        List<Show> sortedShows = new ArrayList<>();

        filteredShows.addAll(movies);
        filteredShows.addAll(serials);

        if (filters.get(0) != null) {
            if (filters.get(0).get(0) != null)
            filteredShows = yearFilter(filteredShows, Integer.parseInt(filters.get(0).get(0)));
        }
        filteredShows = genreFilter(filteredShows, filters.get(1));

        for (Show show : filteredShows) {
            show.computeTimesFavorite(data);
            show.computeTimesViewed(data);
            show.computeTotalDuration();
            show.computeRating();
        }

        switch(criteria) {
            case "ratings":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(Show o1, Show o2) {
                        o1.computeRating();
                        o2.computeRating();
                        return Double.compare(o1.getFinalRating(), o2.getFinalRating());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                for (Show show : filteredShows) {
                    if (show.getFinalRating() > 0) {
                        sortedShows.add(show);
                        number--;
                    }
                    if (number == 0) {
                        break;
                    }
                }
                break;

            case "favorite":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(Show o1, Show o2) {
                        return Integer.compare(o1.getTimesFavorite(), o2.getTimesFavorite());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                for (Show show : filteredShows) {
                    if (show.getTimesFavorite() != 0) {
                        sortedShows.add(show);
                        number--;
                        if (number == 0) {
                            break;
                        }
                    }
                }
                break;

            case "longest":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(Show o1, Show o2) {
                        return Integer.compare(o1.getTotalDuration(), o2.getTotalDuration());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                for (Show show : filteredShows) {
                    sortedShows.add(show);
                    number--;
                    if (number == 0) {
                        break;
                    }
                }
                break;

            case "most_viewed":
                Collections.sort(filteredShows, new Comparator<Show>() {
                    @Override
                    public int compare(Show o1, Show o2) {
                        return Integer.compare(o1.getTimesViewed(), o2.getTimesViewed());
                    }
                });
                if (sortType.equals("desc")) {
                    Collections.reverse(filteredShows);
                }
                for (Show show : filteredShows) {
                    if (show.getTimesViewed() > 0) {
                        sortedShows.add(show);
                        number--;
                        if (number == 0) {
                            break;
                        }
                    }
                }
                break;
        }

        return sortedShows;
    }

    public List<User> queryUsers(int number, String sortType, String criteria) {
        List<User> users = data.getUsersList();
        List<User> sortedUsers = new ArrayList<>();

        for (User user : users) {
            user.computeNoRatings();
        }

        if (criteria.equals("num_ratings")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return Integer.compare(o1.getNoRatings(), o2.getNoRatings());
                }
            });
            if (sortType.equals("desc")) {
                Collections.reverse(users);
            }
            for (User user : users) {
                if (user.getNoRatings() > 0) {
                    sortedUsers.add(user);
                    number--;
                    if (number == 0) {
                        break;
                    }
                }
            }
        }

        return sortedUsers;
    }
}

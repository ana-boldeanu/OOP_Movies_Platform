package actions;

import actor.Actor;
import entertainment.Genre;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;
import fileio.SerialInputData;
import user.User;
import utils.Utils;

import java.util.*;

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
                    actor.computeNoAwards();
                    if (actor.hasAwards(filters.get(3))) {
                        sortedActors.add(actor);
                    }
                }
                Collections.sort(sortedActors, new Comparator<Actor>() {
                    @Override
                    public int compare(Actor o1, Actor o2) {
                        if (o1.getNumberOfAwards() == o2.getNumberOfAwards()) {
                            return o1.getName().compareTo(o2.getName());
                        }
                        return Integer.compare(o1.getNumberOfAwards(), o2.getNumberOfAwards());
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

    public List<Show> queryShows(int number, List<List<String>> filters, String sortType, String criteria, String showType) {
        List<Movie> movies = data.getMoviesList();
        List<Serial> serials = data.getSerialsList();
        List<Show> showsToFilter = new ArrayList<>();
        List<Show> filteredShows = new ArrayList<>();
        List<Show> sortedShows = new ArrayList<>();

        if (showType.equals("movies")) {
            showsToFilter.addAll(movies);

        } else {
            showsToFilter.addAll(serials);
        }

        if (filters != null) {
            // Apply First filter
            List<String> years = filters.get(0);

            if (years.get(0) != null) {
                int year = Integer.parseInt(years.get(0));
                for (Show show : showsToFilter) {
                    if (show.getYear() == year) {
                        filteredShows.add(show);
                    }
                }
            }
            else {
                filteredShows.addAll(showsToFilter);
            }

            // Apply Second filter
            List<String> genres = filters.get(1);

            if (genres.get(0) != null) {
                // Reset lists for filtering
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
                    if (o1.getNoRatings() == o2.getNoRatings()) {
                        return o1.getUsername().compareTo(o2.getUsername());
                    }
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

    public String recommendStandard(String username) {
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (Show show : shows) {
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        return "StandardRecommendation result: " + show.getTitle();
                    }
                }
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    public String recommendBestUnseen(String username) {
        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        for (Show show : shows) {
            show.computeRating();
        }

        Collections.sort(shows, new Comparator<Show>() {
            @Override
            public int compare(Show o1, Show o2) {
                return Double.compare(o2.getFinalRating(), o1.getFinalRating());
            }
        });

        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (Show show : shows) {
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        return "BestRatedUnseenRecommendation result: " + show.getTitle();
                    }
                }
            }
        }
        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    public String recommendPopular(String username) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                if (!user.getSubscriptionType().equals("PREMIUM")) {
                    return "PopularRecommendation cannot be applied!";
                }
            }
        }

        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        class GenrePopularity {
            String name;
            int popularity;

            public GenrePopularity(String name, int popularity) {
                this.name = name;
                this.popularity = popularity;
            }
        }

        List<GenrePopularity> genres = new ArrayList<>();

        for (Genre genre : Genre.values()) {
            List<Show> filteredShows = new ArrayList<>();
            for (Show show : shows) {
                if (show.getGenres().contains(Utils.genreToString(genre))) {
                    filteredShows.add(show);
                }
            }
            int rating = 0;
            for (Show show : filteredShows) {
                show.computeTimesViewed(data);
                rating += show.getTimesViewed();
            }

            genres.add(new GenrePopularity(Utils.genreToString(genre), rating));
        }

        Collections.sort(genres, new Comparator<GenrePopularity>() {
            @Override
            public int compare(GenrePopularity o1, GenrePopularity o2) {
                return Integer.compare(o1.popularity, o2.popularity);
            }
        });
        Collections.reverse(genres);

        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (GenrePopularity genre : genres) {
                    for (Show show : shows) {
                        if (show.getGenres().contains(genre.name)) {
                            if (!user.getHistory().containsKey(show.getTitle())) {
                                return "PopularRecommendation result: " + show.getTitle();
                            }
                        }
                    }
                }
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    public String recommendFavourite(String username) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                if (!user.getSubscriptionType().equals("PREMIUM")) {
                    return "FavoriteRecommendation cannot be applied!";
                }
            }
        }

        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        for (Show show : shows) {
            show.computeTimesFavorite(data);
        }

        Collections.sort(shows, new Comparator<Show>() {
            @Override
            public int compare(Show o1, Show o2) {
                return Integer.compare(o1.getTimesFavorite(), o2.getTimesFavorite());
            }
        });
        Collections.reverse(shows);

        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (Show show : shows) {
                    if (show.getTimesFavorite() != 0) {
                        if (!user.getHistory().containsKey(show.getTitle())) {
                            return "FavoriteRecommendation result: " + show.getTitle();
                        }
                    }
                }
            }
        }

         return "FavoriteRecommendation cannot be applied!";
    }

    public String recommendSearch(String username, String genre) {
        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                if (!user.getSubscriptionType().equals("PREMIUM")) {
                    return "SearchRecommendation cannot be applied!";
                }
            }
        }

        List<Show> shows = new ArrayList<>();
        shows.addAll(data.getMoviesList());
        shows.addAll(data.getSerialsList());

        List<Show> filteredShows = new ArrayList<>();

        for (Show show : shows) {
            if (show.getGenres().contains(genre)) {
                filteredShows.add(show);
                show.computeRating();
            }
        }

        Collections.sort(filteredShows, new Comparator<Show>() {
            @Override
            public int compare(Show o1, Show o2) {
                if (o1.getFinalRating() == o2.getFinalRating()) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                return Double.compare(o2.getFinalRating(), o1.getFinalRating());
            }
        });

        ArrayList<String> recommendedShows = new ArrayList<>();

        for (User user : data.getUsersList()) {
            if (user.getUsername().equals(username)) {
                for (Show show : filteredShows) {
                    if (!user.getHistory().containsKey(show.getTitle())) {
                        recommendedShows.add(show.getTitle());
                    }
                }
            }
        }

        if (!recommendedShows.isEmpty()) {
            return "SearchRecommendation result: " + recommendedShows.toString();
        }

        return "SearchRecommendation cannot be applied!";
    }
}

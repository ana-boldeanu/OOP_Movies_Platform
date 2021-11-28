package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import fileio.Input;
import fileio.SerialInputData;
import fileio.MovieInputData;
import fileio.ActorInputData;
import fileio.UserInputData;
import user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the Lists of Actors, Users, Movies and Serials from our current
 * Database.
 */
public final class DataContainer {
    /**
     * List of Actors that exist in this Database
     */
    private List<Actor> actorsList = new ArrayList<>();
    /**
     * List of Users that exist in this Database
     */
    private List<User> usersList = new ArrayList<>();
    /**
     * List of Movies that exist in this Database
     */
    private List<Movie> moviesList = new ArrayList<>();
    /**
     * List of Serials that exist in this Database
     */
    private List<Serial> serialsList = new ArrayList<>();

    /**
     * This constructor makes a deep copy of every list of entities that are
     * given to us as input. For Actors, it also builds their moviesFilmography
     * and serialsFilmography lists.
     * @param input The input from our Database
     */
    public DataContainer(final Input input) {
        for (UserInputData user : input.getUsers()) {
            usersList.add(new User(user.getUsername(), user.getSubscriptionType(),
                    user.getHistory(), user.getFavoriteMovies()));
        }
        for (MovieInputData movie : input.getMovies()) {
            moviesList.add(new Movie(movie.getTitle(), movie.getYear(),
                    movie.getDuration(), movie.getGenres(), movie.getCast()));
        }
        for (SerialInputData serial : input.getSerials()) {
            serialsList.add(new Serial(serial.getTitle(), serial.getYear(),
                    serial.getNumberSeason(), serial.getGenres(),
                    serial.getCast(), serial.getSeasons()));
        }
        for (ActorInputData actor : input.getActors()) {
            ArrayList<Movie> moviesFilmography = new ArrayList<>();
            ArrayList<Serial> serialsFilmography = new ArrayList<>();

            for (Movie movie : moviesList) {
                // If this movie is in his filmography
                if (actor.getFilmography().contains(movie.getTitle())) {
                    moviesFilmography.add(movie);
                }
            }
            for (Serial serial : serialsList) {
                // If this serial is in his filmography
                if (actor.getFilmography().contains(serial.getTitle())) {
                    serialsFilmography.add(serial);
                }
            }

            actorsList.add(new Actor(actor.getName(), actor.getAwards(),
                    moviesFilmography, actor.getCareerDescription(), serialsFilmography));
        }
    }

    public List<Actor> getActorsList() {
        return actorsList;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public List<Serial> getSerialsList() {
        return serialsList;
    }
}

package actions;

import actor.Actor;
import entertainment.Movie;
import entertainment.Serial;
import fileio.*;
import user.User;

import java.util.ArrayList;
import java.util.List;

public class DataContainer {
    private List<Actor> actorsList = new ArrayList<>();
    private List<User> usersList = new ArrayList<>();
    private List<Movie> moviesList = new ArrayList<>();
    private List<Serial> serialsList = new ArrayList<>();

    public DataContainer(Input input) {
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

            for (String show : actor.getFilmography()) {
                for (Movie movie : moviesList) {
                    if (movie.getTitle().equals(show)) { // If the movie is in his filmography
                        moviesFilmography.add(movie);
                    }
                }
                for (Serial serial : serialsList) {
                    if (serial.getTitle().equals(show)) { // If the serial is in his filmography
                        serialsFilmography.add(serial);
                    }
                }
            }

            actorsList.add(new Actor(actor.getName(), actor.getAwards(),
                    moviesFilmography, serialsFilmography, actor.getCareerDescription()));
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

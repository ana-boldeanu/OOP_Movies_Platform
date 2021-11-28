package main;

import actions.CommandAction;
import actions.DataContainer;
import actions.QueryAction;
import actions.RecommendAction;
import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implementation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1, final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        // Entry point
        DataContainer data = new DataContainer(input); // Transform input into my own input classes
        String resultMessage = "";

        for (ActionInputData command : input.getCommands()) {
            /**
             * The user that executes a Command or asks for a Recommendation
             */
            User actionUser = new User();
            for (User user : data.getUsersList()) {
                if (user.getUsername().equals(command.getUsername())) {
                    actionUser = user;
                }
            }

            switch (command.getActionType()) {
                case "command" -> {
                    CommandAction commandAction = new CommandAction(data);

                    switch (command.getType()) {
                        case "favorite":
                            resultMessage = commandAction.addFavourite(actionUser,
                                    command.getTitle());
                            break;

                        case "view":
                            resultMessage = commandAction.addView(actionUser,
                                    command.getTitle());
                            break;

                        case "rating":
                            resultMessage = commandAction.addRating(actionUser,
                                    command.getTitle(), command.getGrade(),
                                    command.getSeasonNumber());
                            break;

                        default:
                            break;
                    }
                }

                case "query" -> {
                    QueryAction queryAction = new QueryAction(data);
                    resultMessage = "Query result: ";

                    switch (command.getObjectType()) {
                        case "actors":
                            List<Actor> actors = queryAction.queryActors(command.getNumber(),
                                    command.getFilters(), command.getSortType(),
                                    command.getCriteria());
                            resultMessage += actors;
                            break;

                        case "movies":
                            List<Show> movies = queryAction.queryShows(command.getNumber(),
                                    command.getFilters(), command.getSortType(),
                                    command.getCriteria(), "movies");
                            resultMessage += movies;
                            break;

                        case "shows":
                            List<Show> shows = queryAction.queryShows(command.getNumber(),
                                    command.getFilters(), command.getSortType(),
                                    command.getCriteria(), "shows");
                            resultMessage += shows;
                            break;

                        case "users":
                            List<User> users = queryAction.queryUsers(command.getNumber(),
                                    command.getSortType(), command.getCriteria());
                            resultMessage += users;
                            break;

                        default:
                            break;
                    }
                }

                case "recommendation" -> {
                    RecommendAction recommendAction = new RecommendAction(data);

                    switch (command.getType()) {
                        case "standard":
                            resultMessage = recommendAction.getStandard(actionUser);
                            break;

                        case "best_unseen":
                            resultMessage = recommendAction.getBestUnseen(actionUser);
                            break;

                        case "popular":
                            resultMessage = recommendAction.getPopular(actionUser);
                            break;

                        case "favorite":
                            resultMessage = recommendAction.getFavourite(actionUser);
                            break;

                        case "search":
                            resultMessage = recommendAction.getSearch(actionUser,
                                    command.getGenre());
                            break;

                        default:
                            break;
                    }
                }

                default -> {
                    break;
                }
            }

            arrayResult.add(fileWriter.writeFile(command.getActionId(),
                    "", resultMessage));
        }

        fileWriter.closeJSON(arrayResult);
    }
}

package main;

import actions.Action;
import actions.DataContainer;
import actor.Actor;
import checker.Checkstyle;
import checker.Checker;
import common.Constants;
import entertainment.Movie;
import entertainment.Serial;
import entertainment.Show;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.User;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        // TODO add here the entry point to your implementation
        DataContainer data = new DataContainer(input); // Transform input into my own input classes
        Action action = new Action(data);
        String resultMessage = new String();

        for (ActionInputData command : input.getCommands()) {
            if (command.getActionType().equals("command")) {
                switch (command.getType()) {
                    case "favorite":
                         resultMessage = action.commandFavourite(command.getUsername(),
                                 command.getTitle());
                        break;

                    case "view":
                        resultMessage = action.commandView(command.getUsername(),
                                command.getTitle());
                        break;

                    case "rating":
                        resultMessage = action.commandRating(command.getUsername(),
                                command.getTitle(), command.getGrade(),
                                command.getSeasonNumber());
                        break;
                }

            } else if (command.getActionType().equals("query")) {
                resultMessage = "Query result: ";

                switch (command.getObjectType()) {
                    case "actors":
                        List<Actor> actors = action.queryActors(command.getNumber(),
                                command.getFilters(), command.getSortType(),
                                command.getCriteria());
                        resultMessage += actors;
                        break;

                    case "movies":
                    case "shows":
                        List<Show> shows = action.queryShows(command.getNumber(),
                                command.getFilters(), command.getSortType(),
                                command.getCriteria());
                        resultMessage += shows;
                        break;

                    case "users":
                        List<User> users = action.queryUsers(command.getNumber(),
                                command.getSortType(), command.getCriteria());
                        resultMessage += users;
                        break;
                }

            } else if (command.getActionType().equals("recommendation")) {

            }

            arrayResult.add(fileWriter.writeFile(command.getActionId(),
                    "", resultMessage));
        }


        fileWriter.closeJSON(arrayResult);
    }
}

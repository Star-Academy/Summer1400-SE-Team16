import controller.AppController;
import exception.BaseDirectoryInvalidException;
import exception.SearchException;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        startSearchEngine();
    }

    private static void startSearchEngine() {
        AppController controller = new AppController();
        try {
            controller.init();
        } catch (BaseDirectoryInvalidException | IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        String input = getInput(scanner);
        try {
            controller.search(input);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    private static String getInput(Scanner scanner) {
        return scanner.nextLine();
    }
}

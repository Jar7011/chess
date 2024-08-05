package serverFacade;

import ui.PreloginUI;
import static ui.EscapeSequences.*;
import java.util.Scanner;

public class Repl {

    public PreloginUI menu;
    public static String username;
    public static String authToken;

    public Repl(String serverUrl) {
        menu = new PreloginUI(serverUrl);
    }

    public void run() {
        System.out.println(BLACK_KING + "Welcome to Chess Game. Sign in to start.");
        System.out.print(menu.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = menu.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}

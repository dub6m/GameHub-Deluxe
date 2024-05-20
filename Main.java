import java.util.Scanner;

public class Main implements GameManagementInterface {
    private static int userWin = 0;
    private static int userLoss = 0;
    private static int aiWin = 0;
    private static String gameChoice;
    private static RPS rps = new RPS();
    private static Hoarders hoarders = new Hoarders();

    @Override
    public String optionsDescription() {
        String description = "";
        if (gameChoice.equals("r")) {
            description = rps.optionsDescription();
        }
        else if (gameChoice.equals("h")) {
            description = hoarders.optionsDescription();
        }
        return description;
    }

    @Override
    public String actionDescription() {
        String description = "";
        if (gameChoice.equals("r")) {
            description = rps.actionDescription();
        }
        else if (gameChoice.equals("h")) {
            description = hoarders.actionDescription();
        }
        return description;
    }

    @Override
    public void initializeGame(String[] options) throws InitializationException {
        if (gameChoice.equals("r")) {
            rps.initializeGame(options);
        }
        else if (gameChoice.equals("h")) {
            hoarders.initializeGame(options);
        }
    }

    @Override
    public void resetGame() {
        if (gameChoice.equals("r")) {
            rps.resetGame();
        }
        else if (gameChoice.equals("h")) {
            hoarders.resetGame();
        }
    }

    @Override
    public boolean isInitialized() {
        boolean isInitialized = false;
        if (gameChoice.equals("r")) {
            isInitialized = rps.isInitialized();
        }
        else if (gameChoice.equals("h")) {
            isInitialized = hoarders.isInitialized();
        }
        return isInitialized;
    }

    @Override
    public boolean isGameOver() {
        if (gameChoice.equals("r")) {
            return rps.gameOver;
        }
        else if (gameChoice.equals("h")) {
            return hoarders.gameOver;
        }
        return false;
    }

    @Override
    public boolean isGameWon() {
        boolean isGameWon = false;
        if (gameChoice.equals("r")) {
            isGameWon = rps.isGameWon();
        }
        else if (gameChoice.equals("h")) {
            isGameWon = hoarders.isGameWon();
        }
        return isGameWon;
    }

    @Override
    public void printState() {
        if (gameChoice.equals("r")) {
            rps.printState();
        }
        else if (gameChoice.equals("h")) {
            hoarders.printState();
        }
    }

    public boolean performAction(char action, String[] options) {
        if (gameChoice.equals("r")) {
            return rps.performAction(action, options);
        }
        else if (gameChoice.equals("h")) {
            return hoarders.performAction(action, options);
        }
        return false;
    }
    public static void runHoarders(Scanner scanner, Main main) {
        scanner.nextLine();

        while (!main.isInitialized()) {
            System.out.print("File to initialize Hoarders ('x' to return to main menu): ");
            String fileName = scanner.nextLine();
            String[] file = {fileName};
            // Returns back to main menu if...
            if (fileName.equals("x")) {
                System.out.println();
                break;
            }
            // Continues to the game
            else {
                try {
                    main.initializeGame(file);
                    hoarders.printState();
                }
                catch (InitializationException e){}
            }
        }
        if (main.isInitialized()) {
            while (!main.isGameOver()) {
                main.isGameOver();
                System.out.print("What's your next action " + main.actionDescription() + ": ");
                String hoardersUserPlay = scanner.nextLine();
                // Quit mid-game
                if (hoardersUserPlay.equals("q")) {
                    System.out.print("Sure you want to quit? ('y' will end this game): ");
                    String confirm = scanner.next();
                    if (confirm.equals("y")) {
                        System.out.println("Quitting...\n");
                        break;
                    }
                    else {
                        System.out.println("I guess not then...\n");
                    }
                }
                // Reset mid-game
                else if (hoardersUserPlay.equals("r")) {
                    System.out.print("Sure you want to reset? ('y' will reset this game): ");
                    String confirm = scanner.next();
                    if (confirm.equals("y")) {
                        System.out.println("Resetting...\n");
                        break;
                    }
                    else {
                        System.out.println("I guess not then...\n");
                    }
                }
                // Peek action
                String[] entries = hoardersUserPlay.split(" ");
                if (entries.length == 3) {
                    char action = entries[0].charAt(0);
                    String[] options = {entries[1], entries[2]};
                    main.performAction(action, options);
                }
                else if (entries.length == 5) {
                    char action = entries[0].charAt(0);
                    String[] options = {entries[1], entries[2], entries[3], entries[4]};
                    main.performAction(action, options);
                }
                else {
                    scanner.nextLine();
                    if (!hoardersUserPlay.equals("r") && !hoardersUserPlay.equals("q")) {
                        System.out.println("Invalid input...\n");
                    }
                }
            }
            if (main.isGameWon()) {
                userWin += 1;
                System.out.println("Game over: true, Game won: true\n");
            }
            else {
                if (main.isInitialized()) {
                    userLoss += 1;
                }
                System.out.println("Game over: true, Game won: false\n");
            }
        }
        main.resetGame();
    }

    public static void runRPS(Scanner scanner, Main main) {
        boolean validNumToWin = false;
        String[] numToWinArr = new String[1];
        String numToWin = "";

        while (!validNumToWin) {
            System.out.print("Number of Games to win ('x' to return to main menu): ");
            numToWin = scanner.next();
            numToWinArr[0] = numToWin;
            if (numToWin.equals("x")) {
                break;
            }
            try {
                main.initializeGame(numToWinArr);
                validNumToWin = true;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        // Printing initialized stuff
        if (main.isInitialized()) {
            System.out.println("Needed to win the match: " + numToWin);
            System.out.println("Your wins: " + userWin);
            System.out.println("AI's wins: " + aiWin);
        }

        while (!main.isGameOver() && main.isInitialized()) {
            System.out.print("What's your next action [" + main.actionDescription() + "]: ");
            // Rock, paper or scissors
            String rpsUserPlay = scanner.next();

            if (rpsUserPlay.equals("q") || gameChoice.equals("x")) {
                System.out.print("Sure you want to quit? ('y' will end this game): ");
                String confirm = scanner.next();
                if (confirm.equals("y")) {
                    System.out.println("Quitting...\n");
                    break;
                }
                else {
                    System.out.println("I guess not then...");
                }
            }
            else if (rpsUserPlay.equals("r")) {
                System.out.print("Sure you want to reset? ('y' will reset this game): ");
                String confirm = scanner.next();
                if (confirm.equals("y")) {
                    System.out.println("Resetting...\n");
                    break;
                }
                else {
                    System.out.println("I guess not then...");
                }
            }
            else {
                main.performAction(rpsUserPlay.charAt(0), numToWinArr);
            }
            System.out.println("");
            main.printState();
            System.out.println("");
        }
        if (main.isGameWon() && main.isInitialized()) {
            userWin += 1;
            System.out.println("Game over: true, Game won: true\n");
        }
        else {
            if (main.isInitialized()) {
                userLoss += 1;
            }
            System.out.println("Game over: true, Game won: false\n");
        }
        main.resetGame();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;
        Main main = new Main();

        while (!quit) {
            // Pick game
            System.out.println("What would you like to play?");
            System.out.println("=> 'r' for RockPaperScissors");
            System.out.println("=> 'h' for Hoarders");
            System.out.print("=> 'q' to quit: ");
            gameChoice = scanner.next();
            System.out.println(); // Space stuff out...
            if (gameChoice.equals("r")) {
                System.out.println("Rock-Paper-Scissors it is!\n");
                runRPS(scanner, main);
                System.out.printf("So far you have %d win(s) and %d loss(es)!\n\n", userWin, userLoss);
            }
            else if (gameChoice.equals("h")) {
                System.out.println("Hoarders it is!\n");
                runHoarders(scanner, main);
                System.out.printf("So far you have %d win(s) and %d loss(es)!\n\n", userWin, userLoss);
            }
            else if (gameChoice.equals("q") || gameChoice.equals("x")) {
                System.out.print("Sure you want to quit? ('y' will end this program): ");
                String endProgram = scanner.next();
                if (endProgram.equals("y")) {
                    System.out.println("Ending program...\n");
                    quit = true;
                    break;
                }
                else {
                    System.out.println("I guess not then...\n");
                }
            }
            else {
                System.out.println("Please enter a valid option.\n");
            }
        }
    }
}

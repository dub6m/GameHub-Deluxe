import java.util.Random;

/**
 * A simple implemenation of a Rock, Paper, Scissors match.
 * 
 * Implements the GameManagerInterace to allow it to be used in an arcade/anthology style game 
 * collection. 
 */
public class RPS implements GameManagementInterface {
    // Game constants
    private final static char ROCK = 'k';
    private final static char PAPER = 'p';
    private final static char SCISSORS = 's';
    private final static int ROCK_IDX = 0;
    private final static int PAPER_IDX = 1;
    private final static int SCISSORS_IDX = 2;
    private final static String[] OPTION_LABELS = {"rock", "paper", "scissors"};

    // Outcome constants
    private final static int AI_WIN = -1;
    private final static int DRAW = 0;
    private final static int PLAYER_WIN = 1;

    // 2D array representing the outcomes. First index is player choice, second
    // index is AI choice.
    private final static int[][] OUTCOMES = {
            { DRAW, AI_WIN, PLAYER_WIN },
            { PLAYER_WIN, DRAW, AI_WIN },
            { AI_WIN, PLAYER_WIN, DRAW }
    };

    Random rdm;

    int numWinsRequired;
    int playerWins;
    int aiWins;
    boolean gameOver;
    boolean playerWon;

    public RPS() {
        rdm = new Random();
        numWinsRequired = -1;
        playerWins = 0;
        aiWins = 0;
        gameOver = false;
        playerWon = false;
    }

    @Override
    public String optionsDescription() {
        return "[num_wins_required]";
    }

    @Override
    public String actionDescription() {
        return "rock (k), paper (p), or scissors (s)";
    }

    @Override
    public void initializeGame(String[] options) throws InitializationException {
        if (options.length < 1) {
            throw new InitializationException("Invalid options for new match of RPS! " +
                    "Need: [num_wins_required]");
        } else {
            try {
                int value = Integer.parseInt(options[0]);
                if (value <= 0) {
                    throw new InitializationException("[num_wins_required] must be positive");
                }
                numWinsRequired = value;
            } catch (Exception e) {
                throw new InitializationException("Please enter a valid number of games.\n");
            }

            // Optional (hidden) option for specifying the randomization seed. Useful for testing,
            // or beating the "AI" every time if you're feeling cheat-y today. 
            if (options.length >= 2) {
                int seed = Integer.parseInt(options[1]);
                rdm = new Random(seed);
            }
        }
    }

    public void resetGame() {
        playerWins = 0;
        aiWins = 0;
        gameOver = false;
        playerWon = false;
    }

    public boolean isInitialized() {
        return numWinsRequired >= 0;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return playerWon;
    }

    public void printState() {
        if (gameOver) {
            if (playerWon) {
                System.out.println("You won the match!");
            } else {
                System.out.println("You lost the match...");
            }
        } else {
            System.out.printf("Needed to win the match: %d\nYour wins: %d\nAI's wins: %d\n",
                    numWinsRequired, playerWins, aiWins);
        }
    }

    // NOTE: This game doens't need the user to provide additional options for 
    // their actions, and therefore doesn't use the parameter, but other games might...
    public boolean performAction(char action, String[] options) {
        boolean valid = true;
        int playerChoiceIdx = -1;
        switch (action) {
            case ROCK:
                playerChoiceIdx = ROCK_IDX;
                break;
            case PAPER:
                playerChoiceIdx = PAPER_IDX;
                break;
            case SCISSORS:
                playerChoiceIdx = SCISSORS_IDX;
                break;
            default:
                System.out.println("Invalid action. Pick one of 'k', 'p', or 's'!");
                valid = false;
        }

        if (valid) {
            int aiChoiceIdx = rdm.nextInt(0, 3);
            System.out.printf("\nYou chose %s, the AI chose %s.\n", 
                    OPTION_LABELS[playerChoiceIdx], OPTION_LABELS[aiChoiceIdx]);
            int outcome = OUTCOMES[playerChoiceIdx][aiChoiceIdx];
            switch (outcome) {
                case PLAYER_WIN:
                    System.out.println("You won the round!");
                    playerWins++;
                    break;
                case DRAW:
                    System.out.println("The round was a draw.");
                    break;
                case AI_WIN:
                    System.out.println("You lost the round...");
                    aiWins++;
                    break;
            }

            if (playerWins >= numWinsRequired) {
                gameOver = true;
                playerWon = true;
            } else if (aiWins >= numWinsRequired) {
                gameOver = true;
                playerWon = false;
            }
        }

        return valid;
    }
}

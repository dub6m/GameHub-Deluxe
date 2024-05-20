import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Hoarders implements GameManagementInterface{
    int numSpecialMoves = 0;
    int numMoves = 0;
    int numRow = 0;
    int numColumn = 0;
    boolean isInitialized = false;
    boolean gameOver = false;
    LinkedItemPile[][] hoarderBoard;

    public String printBoard() throws Exception {
        String board = "";
        if (isInitialized()) {
            for (int i = 0; i < hoarderBoard.length; i++) {
                board += "[";
                for (int j = 0; j < hoarderBoard[i].length; j++) {
                    board += " " + hoarderBoard[i][j].toString() + " ";
                }
                board += "]\n";
            }
            return board;
        }
        else {
            throw new Exception("Hoarder initialization failed. No valid board exists.\n");
        }
    }

    public void printState() {
        String state = "";
        if (isInitialized()) {
            for (int i = 0; i < hoarderBoard.length; i++) {
                state += "[";
                for (int j = 0; j < hoarderBoard[i].length; j++) {
                    if (hoarderBoard[i][j].isEmpty()) {
                        state += " X ";
                    }
                    else {
                        if (hoarderBoard[i][j].isInIncreasingOrder()) {
                            state += " " + hoarderBoard[i][j].seeTop() + " ";
                        }
                        else {
                            state += " " + hoarderBoard[i][j].seeTop() + "* ";
                        }
                    }
                }
                state += "]\n";
            }
            state += "You have " + numMoves + " move(s) and " + numSpecialMoves + " special move(s) left.\n";
            System.out.println(state);
        }
    }

    @Override
    public String optionsDescription() {
        return "[name_of_file_with_board_data]";
    }

    @Override
    public String actionDescription() {
        return "[move (m), super move (s), or peek (p)]";
    }

    public void initializeGamehelper(String[] options) throws InitializationException {
        int line = 0;
        int row = 0;
        int column = 0;
        try { // options[0] is the file name
            FileReader fileReader = new FileReader(options[0] + ".txt");
            BufferedReader reader = new BufferedReader(fileReader);
            String texts = reader.readLine();
            String[] boardDimension = new String[2]; // Row & Coloumn
            // Continue here
            while (texts != null) {
                line += 1;
                if (line == 1) {
                    boardDimension = texts.split(" ");
                    numRow = Integer.parseInt(boardDimension[0]);
                    numColumn = Integer.parseInt(boardDimension[1]);
                    hoarderBoard = new LinkedItemPile[numRow][numColumn];

                }
                // first line is the boards dimension
                // Next row x column lines are data for each board entry
                //  So anything above row x column + 1 is the number of moves & special moves respectively
                else if (line == (numRow * numColumn) + 2) { 
                    numMoves = Integer.parseInt(texts);
                }
                else if (line == (numRow * numColumn) + 3) {
                    numSpecialMoves = Integer.parseInt(texts);
                }
                else {
                    // Make the pile
                    String[] pileValues = texts.split(" ");
                    LinkedItemPile pile = new LinkedItemPile();
                    for (int i = 0; i < pileValues.length; i++) {
                        pile.addToPile(Integer.parseInt(pileValues[i]));
                    }
                    // Place the pile in it's assigned position
                    hoarderBoard[row][column] = pile;
                    // Adjust the values for row & column
                    if (row < numRow) {
                        if (column + 1 == numColumn) {
                            column = 0;
                            row += 1;
                        }
                        else {
                            column += 1;
                        }
                    }
                }
                texts = reader.readLine();
            }
            fileReader.close();
            reader.close();
            isInitialized = true;
        }
        catch (IOException e) {
            System.out.println("Provided file could not be found.\n");
            throw new InitializationException(null);
        }
        catch (Exception e) {
            throw new InitializationException("There's been a problem with the initialization process."
            +"\nProbably due to some invalid entry in the board initialization file.\n");
        }
    }

    @Override
    public void initializeGame(String[] options) throws InitializationException {
        options = options[0].split("/");
        for (int i = 0; i < options.length; i++) {
            System.out.println(options[i]);
            try {
                String[] option = {options[i]};
                if (option[0].indexOf(".txt") == -1) {
                    initializeGamehelper(option);
                }
                else {
                    option[0] = option[0].substring(0, option[0].indexOf("."));
                    initializeGamehelper(option);
                }
                if (isInitialized) {
                    break;
                }
            }
            catch (InitializationException e) {
                if (i == options.length - 1) {
                    throw new InitializationException("");
                }
            }
        }
    }

    @Override
    public void resetGame() {
        numSpecialMoves = 0;
        numMoves = 0;
        numRow = 0;
        numColumn = 0;
        isInitialized = false;
        gameOver = false;
        hoarderBoard = null;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public boolean isGameOver() {
        if (numMoves == 0 || numSpecialMoves == 0) {
            gameOver = true;
        }
        else if (isGameWon()) {
            gameOver = true;
        }
        else {
            gameOver = false;
        }
        return gameOver;
    }

    @Override
    public boolean isGameWon() {
        if (isInitialized) {
            for (int i = 0; i < hoarderBoard.length; i++) {
                for (int j = 0; j < hoarderBoard[i].length; j++) {
                    if (!hoarderBoard[i][j].isInIncreasingOrder()) {
                        gameOver = false;
                        return false;
                    }
                }
            }
            gameOver = true;
            return true;
        }
        else {
            gameOver = false;
            return false;
        }
    }

    public boolean performAction(char action, String[] options) {
        // Handle if pile is empty
        if (action == 'p' && options.length == 2) {
            int peekRow = 0;
            int peekColumn = 0;
            try {
                peekRow = Integer.parseInt(options[0]);
                peekColumn = Integer.parseInt(options[1]);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid row/column passed");
                return false;
            }
            if (peekRow < 1 || peekColumn < 1 || peekRow > numRow || peekColumn > numColumn) {
                System.out.println("Invalid arguments. You can't peek out of bounds.\n");
                return false;
            }
            else {
                String pile = "";
                String pileReverse = "";
                if (hoarderBoard[peekRow - 1][peekColumn - 1].getSize() == 0) {
                    System.out.println("Row: " + (peekRow) + ", Column: " + (peekColumn) + " is an empty pile.\n");
                }
                Node current = hoarderBoard[peekRow - 1][peekColumn - 1].getHead();
                while (current != null) {
                    pile += current.toString();
                    current = current.next;
                }
                for (int k = pile.length() - 1; k >= 0; k--) {
                    if (k == 0) {
                        pileReverse += pile.charAt(k);
                    }
                    else {
                        pileReverse += pile.charAt(k) + ", ";
                    }
                }
                System.out.println("Row: " + (peekRow) + ", Column: " + (peekColumn) + " (bottom to top): " + pileReverse.toString() + "\n");
                printState();
                return true;
            }
        }
        else if (action == 'm' || action == 's' && options.length == 4) {
            int srcRow = 0;
            int srcColumn = 0;
            int destRow = 0;
            int destColumn = 0;
                try {
                    srcRow = Integer.parseInt(options[0]);
                    srcColumn = Integer.parseInt(options[1]);
                    destRow = Integer.parseInt(options[2]);
                    destColumn = Integer.parseInt(options[3]);
                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid row/column passed");
                    return false;
                }
                if (srcRow < 1 || srcColumn < 1 || srcRow > numRow || srcColumn > numColumn ||
                    destRow < 1 || destColumn < 1 || destRow > numRow || destColumn > numColumn) {
                    System.out.println("Invalid arguments. You can't move into or out of bounds.\n");
                    gameOver = isGameWon();
                    isGameOver();
                    return false;
                }
                else {
                    if ((srcRow == destRow && srcColumn != destColumn) || (srcRow != destRow && srcColumn == destColumn)) {
                        if (action == 'm' && numMoves > 0) {
                            hoarderBoard[destRow - 1][destColumn - 1].addToPile(hoarderBoard[srcColumn - 1][srcColumn - 1].seeTop());
                            hoarderBoard[srcColumn - 1][srcColumn - 1].takeFromPile();
                            numMoves -= 1;
                            printState();
                            gameOver = isGameOver();
                            //isGameOver();
                            return true;
                        }
                        else {
                            if (numSpecialMoves > 0) {
                                hoarderBoard[destRow - 1][destColumn - 1].pushDown(hoarderBoard[srcColumn - 1][srcColumn - 1].seeTop());
                                hoarderBoard[srcColumn - 1][srcColumn - 1].takeFromPile();
                                numSpecialMoves -= 1;
                                numMoves -= 1;
                                printState();
                                gameOver = isGameOver();
                                //isGameOver();
                                return true;
                            }
                            return false;
                        }
                    }
                    else {
                        // Two sets of rows and columns are not orthogonal
                        System.out.println("The two sets of rows and columns are not orthogonal.");
                        return false;
                    }
                }
            }
        else {
            System.out.println("Invalid action or insufficient options provided.");
            return false;
        }
    }
    
    public static void main(String[] args) {
        Hoarders h = new Hoarders();
        String[] name = {"board1"};
        String[] dims = {"1", "a"};
        try {
            h.initializeGame(name);
            //System.out.println(h.printBoard());
        } catch (InitializationException e) {
            System.out.println(e.getMessage());
        }
        // catch (Exception e) {
        //     System.out.println(e.getMessage());
        // }
        h.performAction('p', dims);
    }
}

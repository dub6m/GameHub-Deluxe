public interface GameManagementInterface {
    public String optionsDescription();

    public String actionDescription();
    
    public void initializeGame(String[] options) throws InitializationException;
    
    public void resetGame();

    public boolean isInitialized();

    public boolean isGameOver();

    public boolean isGameWon();

    public void printState();

    public boolean performAction(char action, String[] options);
}

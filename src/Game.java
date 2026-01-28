import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The Game class manages the main game loop, player
 * interaction, game time, UI, and stage transitions.
 * author Melih Efe Sonmez
 * since Date: 18.04.2025
 */
public class Game {

    // DATA FIELDS of the class
    private int stageIndex = 0;
    private ArrayList<Stage> stages;
    private int deathNumber = 0;
    private double gameTime = 0;
    private Player player;
    private Map map;
    private boolean isFinished = false;
    private int minutes = 0;
    private int seconds = 0;
    private int miliseconds = 0;
    private boolean helpPressed = false;
    private boolean isResetting = false;
    private double resetMessageTime = 0;


    /**
     * Constructs a new Game instance with a list of stages.
     *
     * @param stages A list of predefined Stage objects.
     */
    public Game(ArrayList<Stage> stages) {
        this.stages = stages;
    }


    // GETTER METHODS
    /**
     * @return the active Stage object based on the stage index.
     */
    public Stage getCurrentStage(){return stages.get(stageIndex);}


    // OTHER METHODS

    /**
     * Main game loop that controls gameplay, UI rendering,
     * stage transition, and input handling.
     */
    public void play() {
        // canvas size
        StdDraw.setCanvasSize(800, 600);
        StdDraw.setXscale(0, 800);
        StdDraw.setYscale(0, 600);

        // Create some objects by OOP
        Stage currentStage = getCurrentStage();
        player = new Player(130, 465);
        map = new Map(currentStage, player, this);

        double lastTime = System.currentTimeMillis() / 1000.0;
        double passedStageTime = 0;
        boolean stagePassed = false;

        // main game loop
        StdDraw.enableDoubleBuffering();
        while (true) {

            // Arrange time
            double currentTime = System.currentTimeMillis() / 1000.0;
            double deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            // Reset display
            if (isResetting) {
                resetMessageTime += deltaTime;

                // Draw reset message
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledRectangle(400, 275, 400, 75);
                StdDraw.setPenColor(StdDraw.WHITE);

                Font resetFont = new Font("Arial", Font.BOLD, 45);
                StdDraw.setFont(resetFont);
                StdDraw.text(400, 275, "RESETTING THE GAME...");

                if (resetMessageTime >= 2.0) { // Wait to seconds
                    isResetting = false;
                    resetMessageTime = 0;
                    resetGame();
                    resetHelp();
                    player = new Player(130, 465);
                    stageIndex = 0;
                    map = new Map(getCurrentStage(), player, this);
                }

                StdDraw.show();
                StdDraw.pause(20);
                continue;  // just show reset message
            }


            if (!stagePassed) {
                updateGameTime(deltaTime);
            }

            StdDraw.clear();

            // Check for Mouse presses (Help, Restart, Reset Game)
            checkMouseButtonClicks();

            handleInput();
            map.handleCollisions();

            // Check if stage is completed
            if (map.changeStage()) {
                // Check if this is the last stage
                if (stageIndex >= stages.size() - 1) {
                    // Go directly to end game
                    endGame();
                    if (isFinished) {
                        break;
                    }
                } else {
                    // Show passing stage banner
                    stagePassed = true;
                }
                continue;
            }

            // Update door and draw map
            map.updateDoor();
            map.draw();

            // bottom side of the game display
            StdDraw.setPenColor(new Color(56, 93, 172)); // Color of the area
            StdDraw.filledRectangle((map.getTimerArea()[2]-map.getTimerArea()[0]) / 2.0, (map.getTimerArea()[3]-map.getTimerArea()[1]) / 2.0,
                    (map.getTimerArea()[2]-map.getTimerArea()[0]) / 2.0, (map.getTimerArea()[3]-map.getTimerArea()[1]) / 2.0); // Drawing bottom part
            StdDraw.setPenColor(StdDraw.WHITE);

            Font defaultFont = new Font("SansSerif", Font.PLAIN, 16);
            StdDraw.setFont(defaultFont);

            StdDraw.text(250, 85, "Help");
            StdDraw.rectangle(250, 85, 40, 15); // Help button
            StdDraw.text(550, 85, "Restart");
            StdDraw.rectangle(550, 85, 40, 15); // Restart button
            StdDraw.text(400, 20, "RESET THE GAME");
            StdDraw.rectangle(400, 20, 80, 15); // Reset button
            StdDraw.text(700, 75, "Deaths: " + deathNumber);
            StdDraw.text(700, 50, "Stage: " + (stageIndex + 1));
            StdDraw.text(100, 50, String.format("%02d:%02d:%02d", minutes, seconds, miliseconds));
            StdDraw.text(100, 75, "Level: 1");

            // Display clue or help
            if (helpPressed) {
                StdDraw.text(400, 85, "Help:");
                StdDraw.text(400, 55, currentStage.getHelp());
            } else {
                StdDraw.text(400, 85, "Clue:");
                StdDraw.text(400, 55, currentStage.getClue());
            }

            // Passing stage banner
            if (stagePassed) {
                passedStageTime += deltaTime;

                // Draw passing stage banner
                StdDraw.setPenColor(StdDraw.GREEN);
                StdDraw.filledRectangle(400, 275, 400, 75);
                StdDraw.setPenColor(StdDraw.WHITE);

                Font passFont = new Font("Arial", Font.PLAIN, 24);
                StdDraw.setFont(passFont);
                StdDraw.text(400, 290, "You passed the stage");
                StdDraw.text(400, 260, "But is the level over?!");

                if (passedStageTime >= 2.0) { // Wait 2 seconds
                    stagePassed = false;
                    passedStageTime = 0;
                    stageIndex++;
                    resetHelp();

                    if (stageIndex >= stages.size()) {
                        endGame();
                        if (isFinished) {
                            break;
                        }
                    } else {
                        currentStage = getCurrentStage();
                        player.respawn(new double[]{130, 465});
                        map = new Map(currentStage, player, this);
                    }
                }
            }

            StdDraw.show();
            StdDraw.pause(20);
        }
    }

    /**
     * Checks for mouse clicks and triggers actions based on UI buttons.
     */
    private void checkMouseButtonClicks() {
        if (StdDraw.isMousePressed()) {
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();

            // Help button click
            if (mouseX >= 210 && mouseX <= 290 && mouseY >= 70 && mouseY <= 100) {
                helpPressed = true;
            }

            // Restart button click
            if (mouseX >= 510 && mouseX <= 590 && mouseY >= 70 && mouseY <= 100) {
                map.restartStage();
            }

            // Reset Game button click
            if (mouseX >= 320 && mouseX <= 480 && mouseY >= 5 && mouseY <= 35) {
                isResetting = true;
                resetMessageTime = 0;
            }
        }
    }

    /**
     * Displays the final end game screen with stats and
     * provides exit/replay options.
     */
    private void endGame() {
        StdDraw.clear();

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledRectangle(400, 275, 400, 75);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font endFont1 = new Font("Arial", Font.PLAIN, 24);
        StdDraw.setFont(endFont1);
        StdDraw.text(400, 295, "CONGRATULATIONS YOU FINISHED THE LEVEL");
        StdDraw.text(400, 266, "PRESS 'A' TO PLAY AGAIN!");

        Font endFont2 = new Font("Arial", Font.PLAIN, 20);
        StdDraw.setFont(endFont2);
        String timeStr = String.format("%02d : %02d : %02d", minutes, seconds, miliseconds);
        StdDraw.text(400, 240, "You finished with " + deathNumber + " deaths in " + timeStr);

        StdDraw.show();

        // keyboard inputs at the end game banner
        while (true) {
            if (StdDraw.isKeyPressed(KeyEvent.VK_Q)) { // Q for quitting
                isFinished = true;
                System.exit(0); // Close the tab
                break;

            } else if (StdDraw.isKeyPressed(KeyEvent.VK_A)) { // A for restarting
                resetHelp();
                resetGame();
                player = new Player(130, 465);
                map = new Map(getCurrentStage(), player, this);
                break;
            }

            StdDraw.pause(20);
        }
    }


    /**
     * Resets all game data to start from beginning.
     */
    private void resetGame() {
        stageIndex = 0;
        deathNumber = 0;
        gameTime = 0;
        minutes = 0;
        seconds = 0;
        miliseconds = 0;
    }

    /**
     * Handles user keyboard input for player movement.
     */
    private void handleInput() {
        if (StdDraw.isKeyPressed(getCurrentStage().getKeyCodes()[0])) {
            if (getCurrentStage().getStageNumber() == 1) {
                map.movePlayer('R', -1); // -1 for reversed key stage
            } else {
                map.movePlayer('R');
            }
        }
        if (StdDraw.isKeyPressed(getCurrentStage().getKeyCodes()[1])) {
            if (getCurrentStage().getStageNumber() == 1) {
                map.movePlayer('L', -1); // -1 for reversed key stage
            } else {
                map.movePlayer('L');
            }
        }
        if (StdDraw.isKeyPressed(getCurrentStage().getKeyCodes()[2])) {
            map.movePlayer('U');
        }
    }

    /**
     * Increments the death counter by one.
     */
    public void incrementDeath() {
        deathNumber++;
    }

    /**
     * Updates the internal game timer.
     *
     * @param delta Time passed since last update.
     */
    public void updateGameTime(double delta) {
        gameTime += delta;

        miliseconds = (int) (gameTime * 1000) % 100;
        seconds = (int) gameTime % 60;
        minutes = ((int) gameTime / 60) % 60;
    }

    /**
     * Resets helpPressed flag to false.
     */
    public void resetHelp() {
        helpPressed = false;
    }

}

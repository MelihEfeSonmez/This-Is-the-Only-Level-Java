import java.awt.Color;

/**
 * Represents the game map for a single stage, including obstacles, spikes,
 * doors, buttons, player interactions, and drawing the game display.
 * author Melih Efe Sonmez
 * since Date: 18.04.2025
 */
public class Map {

    // DATA FIELDS of the class
    private Stage stage;
    private Player player;
    private Game game;

    // Obstacles List (formant is int[] = {xLeftDown , yLeftDown, xRightUp, yRightUp}
    private int[][] obstacles = {
            new int[]{0,   120, 120, 270}, new int[]{0,   270, 168, 330},
            new int[]{0,   330, 30,  480}, new int[]{0,   480, 180, 600},
            new int[]{180, 570, 680, 600}, new int[]{270, 540, 300, 570},
            new int[]{590, 540, 620, 570}, new int[]{680, 510, 800, 600},
            new int[]{710, 450, 800, 510}, new int[]{740, 420, 800, 450},
            new int[]{770, 300, 800, 420}, new int[]{680, 240, 800, 300},
            new int[]{680, 300, 710, 330}, new int[]{770, 180, 800, 240},
            new int[]{0,   120, 800, 150}, new int[]{560, 150, 800, 180},
            new int[]{530, 180, 590, 210}, new int[]{530, 210, 560, 240},
            new int[]{320, 150, 440, 210}, new int[]{350, 210, 440, 270},
            new int[]{220, 270, 310, 300}, new int[]{360, 360, 480, 390},
            new int[]{530, 310, 590, 340}, new int[]{560, 400, 620, 430}};

    // Button Coordinates and some related fields
    private int[] button = new int[]{400, 390, 470, 410};
    private int[] originalButton = new int[4]; // Store original button positions
    private int buttonPressNum = 0;
    private boolean isButtonPressing = false;
    private boolean wasOffButton = true;

    // Button Floor Coordinates
    private int[] buttonFloor = new int[]{400, 390, 470, 400};

    // Start Pipe Coordinates for Drawing
    private int[][] startPipe = {
            new int[]{115, 450, 145, 480},
            new int[]{110, 430, 150, 450}};

    // Exit Pipe Coordinates for Drawing
    private int[][] exitPipe = {
            new int[]{720, 175, 740, 215},
            new int[]{740, 180, 770, 210}};

    // Coordinates of spike areas
    private int[][] spikes = {
            new int[]{30, 333,  50,  423}, new int[]{121, 150, 207, 170},
            new int[]{441, 150, 557, 170}, new int[]{591, 180, 621, 200},
            new int[]{750, 301, 769, 419}, new int[]{680, 490, 710, 510},
            new int[]{401, 550, 521, 570}};

    // Door Coordinates and some related fields
    private int[] door = new int[]{685, 180, 700, 240};
    private int[] originalDoor = new int[4]; // Store original door positions
    private boolean isDoorOpen = false;
    private boolean isDoorOpening = false;

    // Timer Area (Blue Area at the Bottom)
    private int[] timerArea = new int[]{0, 0, 800, 120};


    /**
     * Constructs a map with the given stage and player reference.
     *
     * @param stage The current stage configuration.
     * @param player The player object.
     * @param game The game object.
     */
    public Map(Stage stage, Player player, Game game){
        this.stage = stage;
        this.player = player;
        this.game = game;
        // Store original button and door positions
        for (int i = 0; i < 4; i++) {
            originalButton[i] = button[i];
            originalDoor[i] = door[i];
        }
    }


    // GETTER METHODS
    /**
     * @return An int array defining the timer area coordinates.
     */
    public int[] getTimerArea(){return timerArea;}


    // OTHER METHODS

    /**
     * Moves the player in the given direction using standard controls.
     *
     * @param direction Direction to move ('L', 'R', 'U')
     */
    public void movePlayer(char direction){

        if (direction == 'L') { // calls move left
            player.setIsFacingRight(false);
            player.moveLeft(stage.getVelocityX());
        } else if (direction == 'R') { // calls move right
            player.setIsFacingRight(true);
            player.moveRight(stage.getVelocityX());
        } else if (direction == 'U') { // calls jump
            player.jump(stage.getVelocityY());
        }

        // Bounce when on ground at stage 3
        if (stage.getStageNumber() == 2 && player.getIsOnGround()) {
            player.jump(stage.getVelocityY());
        }

    }
    /**
     * Overloaded movement method to support reversed control schemes.
     *
     * @param direction Direction to move.
     * @param isReversedKeys Just a trigger to choose reverse logic.
     */
    public void movePlayer(char direction, int isReversedKeys){

        if (direction == 'L') {
            player.setIsFacingRight(true);
            player.moveLeft(stage.getVelocityX());
        } else if (direction == 'R') {
            player.setIsFacingRight(false);
            player.moveRight(stage.getVelocityX());
        } else if (direction == 'U') {
            player.jump(stage.getVelocityY());
        }

    }

    /**
     * Checks collision between the player and a rectangle that
     * represents an obstacle, door, button, or a spike.
     *
     * @param x Player x position.
     * @param y Player y position.
     * @param width Player width.
     * @param height Player height.
     * @param collidedShape Rectangle to check.
     * @return true if they overlap, false otherwise.
     */
    public boolean checkCollision(double x, double y, double width, double height, int[] collidedShape) {
        double left   = x - width  / 2;
        double right  = x + width  / 2;
        double bottom = y - height / 2;
        double top    = y + height / 2;

        return right > collidedShape[0] && left < collidedShape[2] && top > collidedShape[1] && bottom < collidedShape[3];
    }

    /**
     * Handles all collisions including gravity, obstacles, spikes, buttons, and doors.
     */
    public void handleCollisions() {
        // Apply gravity if player is jumping
        if (player.getIsJumping()) {
            player.applyGravity(stage.getGravity());
        }

        // Check for spike collisions
        checkSpikeCollision();

        // Check for obstacle collisions (was or is it on the ground)
        boolean wasOnGround = player.getIsOnGround();
        boolean isOnGround = checkObstacleCollision();

        // Check door collision
        if (!isDoorOpen) {
            checkDoorCollision();
        }

        // Stop jumping
        if (isOnGround && !wasOnGround) {
            player.setIsJumping(false);

            // auto jump for stage 3
            if (stage.getStageNumber() == 2) {
                player.jump(stage.getVelocityY());
            }

        }

        // Start jumping
        if (!isOnGround && wasOnGround && !player.getIsJumping()) {
            player.setIsJumping(true);
        }

        // Update player's ground state
        player.setIsOnGround(isOnGround);

        // Check button interaction
        boolean wasPressing = isButtonPressing;
        isButtonPressing = false;
        checkButtonCollision();

        // Reset button position when player leaves the button
        if (wasPressing && !isButtonPressing) {
            resetButtonPosition();
        }

    }

    /**
     * Resets the button to its original height.
     */
    private void resetButtonPosition() {
        button[1] = originalButton[1];
        button[3] = originalButton[3];
    }

    /**
     * Checks if the player touches any spikes and restarts if so.
     */
    private void checkSpikeCollision() {
        for (int[] spike : spikes) {
            if (checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), spike)) {
                restartStage();
                break;
            }
        }
    }

    /**
     * Prevents player from passing through a closed door.
     */
    private void checkDoorCollision() {
        if (checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), door)) {
            double doorLeft = door[0];

            // collision from the door's left
            player.setX(doorLeft - player.getWidth() / 2);
            player.setVelocityX(0);
        }
    }

    /**
     * Checks and handles player collision with all obstacles.
     *
     * @return true if player is on the ground after calculation.
     */
    private boolean checkObstacleCollision() {
        boolean isOnGround = false;

        for (int[] obstacle : obstacles) {
            if (checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), obstacle)) {
                double playerLeft = player.getX() - player.getWidth() / 2;
                double playerRight = player.getX() + player.getWidth() / 2;
                double playerTop = player.getY() + player.getHeight() / 2;
                double playerBottom = player.getY() - player.getHeight() / 2;

                double obstacleLeft = obstacle[0];
                double obstacleRight = obstacle[2];
                double obstacleTop = obstacle[3];
                double obstacleBottom = obstacle[1];

                // Calculate penetration depths to find which side is the true collision
                double leftPenetration = playerRight - obstacleLeft;
                double rightPenetration = obstacleRight - playerLeft;
                double topPenetration = obstacleTop - playerBottom;
                double bottomPenetration = playerTop - obstacleBottom;

                // Find minimum penetration
                double minPenetration = Math.min( Math.min(leftPenetration, rightPenetration), Math.min(topPenetration, bottomPenetration) );

                // Prevent wrong collision detection due to high velocityY which causes confusion
                if( stage.getStageNumber() == 2 && leftPenetration > 5 && rightPenetration > 5) {
                    minPenetration = Math.min(bottomPenetration, topPenetration);
                }

                // Assign based on minimum penetration
                if (minPenetration == leftPenetration) {
                    // collision from the left
                    player.setX(obstacleLeft - player.getWidth() / 2);
                    player.setVelocityX(0);
                } else if (minPenetration == rightPenetration) {
                    // collision from the right
                    player.setX(obstacleRight + player.getWidth() / 2);
                    player.setVelocityX(0);
                } else if (minPenetration == topPenetration) {
                    // collision from the top (player is below obstacle)
                    player.setY(obstacleTop + player.getHeight() / 2);
                    player.setVelocityY(0);
                    isOnGround = true;
                    player.setIsJumping(false);
                } else if (minPenetration == bottomPenetration) {
                    // collision from the bottom (player is above obstacle)
                    player.setY(obstacleBottom - player.getHeight() / 2);
                    player.setVelocityY(0);
                    if (player.getVelocityY() > 0) {
                        player.setVelocityY(0);
                    }
                }
            }
        }
        return isOnGround;
    }

    /**
     * Handles logic for stepping on the button and triggering door animation.
     */
    private void checkButtonCollision() {
        // for understanding whether is player on the button
        boolean isCurrentlyOnButton = checkCollision(player.getX(), player.getY(), player.getWidth(), player.getHeight(), button);

        if (isCurrentlyOnButton) {
            if (wasOffButton) { // player arrives button
                pressButton();
                wasOffButton = false;
            }
            isButtonPressing = true;
        } else { // leaves button
            wasOffButton = true;
            isButtonPressing = false;
        }
    }

    /**
     * Checks if the player is inside the exit pipe and the door is open.
     *
     * @return true if stage should change.
     */
    public boolean changeStage(){
        boolean playerInExitPipe =
                        player.getX() > exitPipe[0][0] &&
                        player.getX() < exitPipe[0][2] &&
                        player.getY() > exitPipe[0][1] &&
                        player.getY() < exitPipe[0][3];

        return isDoorOpen && playerInExitPipe;
    }

    /**
     * Triggers a button press, updating coordinates and logic.
     */
    public void pressButton(){
        buttonPressNum++;

        if (stage.getStageNumber() != 3 || buttonPressNum >= 5) { // requires 5 presess for stage 4
            isDoorOpen = true;
            isDoorOpening = true;

            button[1] = buttonFloor[1]; // set y1 (bottom) to floor level
            button[3] = buttonFloor[3]; // set y2 (top) to floor level
        }
    }

    /**
     * Restarts the current stage by resetting all positions and counters.
     */
    public void restartStage(){
        player.respawn(new double[]{130, 465});
        buttonPressNum = 0;
        isDoorOpen = false;
        isDoorOpening = false;

        // Reset door and button positions using original values
        for (int i = 0; i < 4; i++) {
            door[i] = originalDoor[i];
            button[i] = originalButton[i];
        }

        game.resetHelp();
        game.incrementDeath();
    }

    /**
     * Animates the door opening by reducing its height.
     */
    public void updateDoor() {
        if (!isDoorOpening || !isDoorOpen) return;

        int doorSpeed = 3;
        if (door[3] > door[1]) {
            door[3] -= doorSpeed;
            if (door[3] <= door[1]) {
                isDoorOpening = false;
            }
        }
    }

    /**
     * Renders the entire game map including player, pipes, spikes, door, button, and obstacles.
     */
    public void draw(){

        // player
        player.draw();

        // obstacles
        StdDraw.setPenColor(stage.getColor());
        for (int[] obs : obstacles) {
            StdDraw.filledRectangle((obs[0]+obs[2])/2, (obs[1]+obs[3])/2,
                    (obs[2]-obs[0])/2, (obs[3]-obs[1])/2);
        }

        // spikes (according their locations)
        for (int[] spike : spikes) {
            if ((spike[3] + spike[1]) / 2.0 < 300.0) {
                StdDraw.picture((spike[2] + spike[0]) / 2.0, (spike[3] + spike[1]) / 2.0, "misc/Spikes.png", spike[2]-spike[0], spike[3]-spike[1]);
            } else if (200.0 < (spike[3] + spike[1]) / 2.0 && (spike[3] + spike[1]) / 2.0 < 400.0){
                if ((spike[2] + spike[0]) < 400.0){
                    StdDraw.picture((spike[2] + spike[0]) / 2.0, (spike[3] + spike[1]) / 2.0, "misc/Spikes.png", spike[3]-spike[1], spike[2]-spike[0], 270);
                } else{
                    StdDraw.picture((spike[2] + spike[0]) / 2.0, (spike[3] + spike[1]) / 2.0, "misc/Spikes.png", spike[3]-spike[1], spike[2]-spike[0],90);
                }
            } else {
                StdDraw.picture((spike[2] + spike[0]) / 2.0, (spike[3] + spike[1]) / 2.0, "misc/Spikes.png", spike[2]-spike[0], spike[3]-spike[1], 180);
            }
        }

        // start pipe
        for (int[] pipe : startPipe) {
            StdDraw.setPenColor(new Color(200,200,55));
            StdDraw.filledRectangle((pipe[0] + pipe[2]) / 2.0, (pipe[1] + pipe[3]) / 2.0,
                              (pipe[2] - pipe[0]) / 2.0, (pipe[3] - pipe[1]) / 2.0);
        }

        // exit pipe
        for (int[] pipe : exitPipe) {
            StdDraw.setPenColor(new Color(200,200,55));
            StdDraw.filledRectangle((pipe[0] + pipe[2]) / 2.0, (pipe[1] + pipe[3]) / 2.0,
                    (pipe[2] - pipe[0]) / 2.0, (pipe[3] - pipe[1]) / 2.0);
        }

        // door
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledRectangle((door[0] + door[2]) / 2.0, (door[1] + door[3]) / 2.0,
                (door[2] - door[0]) / 2.0, (door[3] - door[1]) / 2.0);

        // button
        if (!isButtonPressing) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledRectangle((button[0] + button[2]) / 2.0, (button[1] + button[3]) / 2.0,
                    (button[2] - button[0]) / 2.0, (button[3] - button[1]) / 2.0);
        }
        // button floor
        StdDraw.setPenColor(new Color(22,22,100));
        StdDraw.filledRectangle((buttonFloor[0] + buttonFloor[2]) / 2.0, (buttonFloor[1] + buttonFloor[3]) / 2.0,
                (buttonFloor[2] - buttonFloor[0]) / 2.0, (buttonFloor[3] - buttonFloor[1]) / 2.0);

    }

}

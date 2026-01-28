/**
 * Represents the player character in the game.
 * Stores position, velocity, direction, and state (jumping, grounded, face direction).
 * author Melih Efe Sonmez
 * since Date: 18.04.2025
 */
public class Player {

    // DATA FIELDS of the class
    private double x;
    private double y;
    private double width = 20;
    private double height = 20;
    private double velocityX = 0;
    private double velocityY = 0;
    private boolean isJumping = false;
    private boolean isFacingRight = true;
    private boolean isOnGround = true;


    /**
     * Constructs a player with x and y coordinates.
     *
     * @param x The x position of the player.
     * @param y The y position of the player.
     */
    public Player(double x, double y){
        this.x = x;
        this.y = y;
    }


    // GETTER METHODS
    /**
     * @return Current x position of the player.
     */
    public double getX(){return x;}
    /**
     * @return Current y position of the player.
     */
    public double getY(){return y;}
    /**
     * @return Height of the player (constant).
     */
    public double getHeight(){return height;}
    /**
     * @return Width of the player (constant).
     */
    public double getWidth(){return width;}
    /**
     * @return Whether the player is currently jumping.
     */
    public boolean getIsJumping(){return isJumping;}
    /**
     * @return Current vertical velocity.
     */
    public double getVelocityY() {return velocityY;}
    /**
     * @return Whether the player is currently on the ground.
     */
    public boolean getIsOnGround() {return isOnGround;}

    // SETTER METHODS
    /**
     * Sets the player's x position.
     * @param x New x coordinate.
     */
    public void setX(double x){this.x = x;}
    /**
     * Sets the player's y position.
     * @param y New y coordinate.
     */
    public void setY(double y){this.y = y;}
    /**
     * Sets whether the player is jumping.
     * @param isJumping Jumping state.
     */
    public void setIsJumping(boolean isJumping){this.isJumping = isJumping;}
    /**
     * Sets the player's horizontal velocity.
     * @param velocityX New horizontal velocity.
     */
    public void setVelocityX(double velocityX){this.velocityX = velocityX;}
    /**
     * Sets the player's vertical velocity.
     * @param velocityY New vertical velocity.
     */
    public void setVelocityY(double velocityY){this.velocityY = velocityY;}
    /**
     * Sets the direction the player is facing.
     * @param isFacingRight True if facing right, false if facing left.
     */
    public void setIsFacingRight(boolean isFacingRight){this.isFacingRight = isFacingRight;}
    /**
     * Sets whether the player is on the ground.
     * @param isOnGround Grounded state.
     */
    public void setIsOnGround(boolean isOnGround){this.isOnGround = isOnGround;}


    // OTHER METHODS

    /**
     * Respawns the player at the given coordinates and resets movement states.
     *
     * @param spawnPoint Array containing x and y respawn coordinates.
     */
    public void respawn(double[] spawnPoint){
        this.x = spawnPoint[0];
        this.y = spawnPoint[1];
        this.velocityX = 0;
        this.velocityY = 0;
        isJumping = false;
        isOnGround = true;
    }

    /**
     * Draws the player on screen depending on the direction.
     */
    public void draw(){
        if (isFacingRight) {
            StdDraw.picture(x, y, "misc/ElephantRight.png", 20, 20);
        } else {
            StdDraw.picture(x, y, "misc/ElephantLeft.png", 20, 20);
        }
    }

    /**
     * Makes the player jump with a given vertical velocity.
     *
     * @param jumpVelocity Velocity to apply when jumping.
     */
    public void jump(double jumpVelocity) {
        if (!isJumping) {
            velocityY = jumpVelocity;
            isJumping = true;
            isOnGround = false;
        }
    }

    /**
     * Moves the player to the left by a given speed.
     *
     * @param speedX Amount to move left.
     */
    public void moveLeft(double speedX) {
        velocityX = -speedX;
        x -= speedX;
    }

    /**
     * Moves the player to the right by a given speed.
     *
     * @param speedX Amount to move right.
     */
    public void moveRight(double speedX) {
        velocityX = speedX;
        x += speedX;
    }

    /**
     * Applies gravity to the player and updates vertical position accordingly.
     *
     * @param gravity The gravity acceleration value.
     */
    public void applyGravity(double gravity) {
        velocityY += gravity;
        y += velocityY;
    }

}

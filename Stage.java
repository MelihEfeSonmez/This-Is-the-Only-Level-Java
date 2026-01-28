import java.awt.Color;

/**
 * Represents a stage in the game.
 * Stores properties such as gravity, movement speeds, key codes,
 * clue/help text, stage number, and obstacle color.
 * author Melih Efe Sonmez
 * since Date: 18.04.2025
 */
public class Stage {

    // DATA FIELDS of the class
    private int stageNumber;
    private double gravity;
    private double velocityX;
    private double velocityY;
    private int rightCode;
    private int leftCode;
    private int upCode;
    private String clue;
    private String help;
    private Color color;


    /**
     * Constructs a new Stage with the specified parameters.
     * Assigns obstacle color except white.
     *
     * @param gravity     The gravity value for the stage (affects jumping/falling).
     * @param velocityX   The horizontal movement speed.
     * @param velocityY   The vertical jump speed.
     * @param stageNumber The identifier number of the stage.
     * @param rightCode   The key code for moving right.
     * @param leftCode    The key code for moving left.
     * @param upCode      The key code for jumping (up).
     * @param clue        A short clue shown during gameplay.
     * @param help        Detailed help text shown when the help button is clicked.
     */
    public Stage(double gravity, double velocityX, double velocityY,
                  int stageNumber, int rightCode, int leftCode,
                  int upCode, String clue, String help) {
        this.gravity = gravity;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.stageNumber = stageNumber;
        this.rightCode = rightCode;
        this.leftCode = leftCode;
        this.upCode = upCode;
        this.clue = clue;
        this.help = help;
        this.color = new Color((int)(Math.random()*256),
                               (int)(Math.random()*256),
                               (int)(Math.random()*256));
        // Prevent possibility of color being white
        while (color.equals(new Color(255,255,255))){
            color = new Color((int)(Math.random()*256),
                              (int)(Math.random()*256),
                              (int)(Math.random()*256)
            );
        }
    }


    // GETTER METHODS
    // primitive data type returns
    /**
     * @return The stage number.
     */
    public int getStageNumber(){return stageNumber;}
    /**
     * @return The gravity value.
     */
    public double getGravity(){return gravity;}
    /**
     * @return The horizontal movement speed.
     */
    public double getVelocityX(){return velocityX;}
    /**
     * @return The vertical jump speed.
     */
    public double getVelocityY(){return velocityY;}
    // reference data type returns
    /**
     * @return The key codes for right, left, and up movement.
     */
    public int[] getKeyCodes(){return new int[]{rightCode, leftCode, upCode};} // keyCodes are KeyEvent.VK_RIGHT, KeyEvent.VK_A etc.
    /**
     * @return The clue text for this stage.
     */
    public String getClue(){return clue;}
    /**
     * @return The help text for this stage.
     */
    public String getHelp(){return help;}
    /**
     * @return The obstacle color for this stage.
     */
    public Color getColor(){return color;}

}

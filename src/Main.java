import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The main class that starts the game.
 * It contains the main method and sets up all game stages.
 * author Melih Efe Sonmez
 * since Date: 18.04.2025
 */
public class Main {

    /**
     * Initializes all stages and starts the game by calling the game object's method.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args){

        int nullButton = -1; // Cancel "up button" for stage 3

        // Given Stages
        // normal game
        Stage s1 = new Stage(-0.45, 3.65,10,0, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,"Arrow keys are required",    "Arrow keys move player, press button and enter the second pipe");
        // reversed buttons
        Stage s2 = new Stage(-0.45, 3.65,10,1, KeyEvent.VK_LEFT,  KeyEvent.VK_RIGHT,KeyEvent.VK_UP,"Not always straight forward","Right and left buttons reversed");
        // bouncing
        Stage s3 = new Stage(-2,    3.65,24,2, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, nullButton,    "A bit bouncy here",          "You jump constantly");
        // multiple button presses
        Stage s4 = new Stage(-0.45, 3.65,10,3, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP,"Never gonna give you up",    "Press button 5 times ");
        // New stage (direction buttons are FTH)
        Stage s5 = new Stage(-0.45, 3.65,10,4, KeyEvent.VK_H,     KeyEvent.VK_F,    KeyEvent.VK_T,  "Center keyboarder",         "Use F T H buttons to move");

        // Add the stages to the arraylist
        ArrayList<Stage> stages = new ArrayList<Stage>();
        stages.add(s1);
        stages.add(s2);
        stages.add(s3);
        stages.add(s4);
        stages.add(s5);

        // Start the game
        Game game = new Game(stages);
        game.play();

    }
}

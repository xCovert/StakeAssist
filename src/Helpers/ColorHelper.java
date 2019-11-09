package Helpers;

import java.awt.*;
import java.util.Random;

public class ColorHelper {
    /**
     * Gets a random color (SPECIFIED RGB VALUES)
     * @return Color
     */
    public static Color getRandom() {
        Random rand = new Random();
        
        // Pick a random integer from 1-20 that represents the chosen color.
        int n = rand.nextInt(15) + 1;
        
        // Return the random color.
        switch (n) {
            case 1:
                return new Color(255,0, 0);
            case 2:
                return new Color(255, 255, 0);
            case 3:
                return new Color(128, 255, 0);
            case 4:
                return new Color(0, 255, 0);
            case 5:
                return new Color(0, 255, 255);
            case 6:
                return new Color(255, 51, 255);
            case 7:
                return new Color(255, 153, 255);
            case 8:
                return new Color(204, 255, 51);
            case 9:
                return new Color(0, 255, 204);
            case 10:
                return new Color(153, 204, 255);
            case 11:
                return new Color(227, 151, 252);
            case 12:
                return new Color(177, 255, 117);
            case 13:
                return new Color(51, 204, 51);
            case 14:
                return new Color(0, 153, 153);
            case 15:
                return new Color(102, 153, 255);
            default:
                return Color.green;
        }
    }

    /**
     * Gets an absolute random colour (ALL RGB VALUES)
     * @return Color
     */
    public Color getAbsoluteRandom() {
        Random rand = new Random();
        return new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }
}

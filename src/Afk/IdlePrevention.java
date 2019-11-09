package Afk;


import org.osbot.rs07.script.Script;

import java.util.Random;

public class IdlePrevention {
    /**
     * The elapsed time in milliseconds
     */
    public static long ELAPSED_TIME;

    /**
     * The amount of time elapsed in the previous loop.
     */
    private static long PREVIOUS_AFK_TIME;
    
    /**
     * The base interval at which the idle prevention will trigger 
     */
    private static final int AFK_INTERVAL = 240000;

    /**
     * The random interval offset at which the idle prevention will actually trigger
     */
    private static int RandomIntervalOffset;

    /**
     * The range of which the camera's pitch will move.
     */
    private static final int CAMERA_PITCH_MAX_MOVE = 2;

    /**
     * The rand of which the camera's yaw will move.
     */
    private static final int CAMERA_YAW_MAX_MOVE = 2;
    
    /**
     * Moves the camera slightly every (AFK_INTERVAL + RandomIntervalOffset) milliseconds.
     * @param script
     */
    public static void moveCamera(Script script) {
        // If we're deemed to be AFK, then go ahead and carry out the slight camera movement.
        if (ELAPSED_TIME - PREVIOUS_AFK_TIME >= AFK_INTERVAL + RandomIntervalOffset) {
            // Generate random pitch and yaw to move the camera.
            Random rng = new Random();
            int pitch = rng.nextInt(CAMERA_PITCH_MAX_MOVE + 1 - CAMERA_PITCH_MAX_MOVE) - CAMERA_PITCH_MAX_MOVE;
            int yaw = rng.nextInt(CAMERA_YAW_MAX_MOVE + 1 - CAMERA_YAW_MAX_MOVE) - CAMERA_YAW_MAX_MOVE;
            
            // Carry out slight camera movement.
            script.camera.movePitch(script.camera.getPitchAngle() + pitch);
            script.camera.moveYaw(script.camera.getYawAngle() + yaw);
            
            // Set new random interval offset
            RandomIntervalOffset = rng.nextInt(30000);
            
            // Set the new previous AFK time to the current time.
            PREVIOUS_AFK_TIME = ELAPSED_TIME;

            script.log("Idle Prevention handler executed!");
        }
    }
}

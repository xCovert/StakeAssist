import Afk.IdlePrevention;
import Friends.Friend;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import java.util.List;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(author = "covert.", info = "", name = "StakeAssist", version = 1, logo = "")
public class Main extends Script {
    /**
     * List of all the user's on this person's friend's list.
     */
    private List<Friend> FriendsList;
    
    /**
     * List of all the players in the user's area.
     */
	private List<Player> PlayersInArea;

    /**
     * Called once at the beginning of the script execution.
     */
    @Override
    public void onStart() {    	
    	// Get the user's friends list upon script start.
    	FriendsList = Friend.getAll(this);
    }

    /**
     * Called every frame.
     * @return
     */
    @Override
    public int onLoop() {
        // Only execute if logged in.
        if (myPlayer().isVisible()) {
            // Get the new list of friends and merge them.
            FriendsList = Friend.mergeFriends(FriendsList, Friend.getAll(this));

            // The the players in the current area.
            PlayersInArea = getPlayers().getAll();
            
            // Add to the idle prevention current time.
            IdlePrevention.ELAPSED_TIME = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
            IdlePrevention.moveCamera(this);
        }
        
        return random(200, 300);
    }

    /**
     * Called when exiting/unloading the script.
     */
    @Override
    public void onExit() {
    }

    /**
     * Called every frame used for drawing.
     * @param g
     */
    @Override
    public void onPaint(Graphics2D g) {
    	// Only execute if logged in.
        if (myPlayer().isVisible()) {
            g.setFont(new Font("Arial", Font.BOLD, 12));
            
            // Draw friends list on-screen.
            Friend.drawListOnScreen(this, g, FriendsList, PlayersInArea);
        }
    }
}

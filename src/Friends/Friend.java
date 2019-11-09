package Friends;
import java.awt.*;
import java.util.*;
import java.util.List;
import Helpers.BeepHelper;
import Helpers.ColorHelper;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.input.mouse.MiniMapTileDestination;
import org.osbot.rs07.script.Script;

public class Friend {
    /**
     * The username of the individual friend.
     */
    private String Username;

    /**
     * The username color when displayed on-screen.
     */
    private Color DrawColor;

    /**
     * Keeps track of if the user is newly in the area.
     */
    private boolean IsNewlyInArea;
    
    /**
     * The list of previous players that are in the area.
     */
    private static List<Player> PreviousPlayers;

    /**
     * The amount of time the script will beep + draw text on the screen when a user enters the area.
     */
    private static final int BEEP_INTERVAL = 150;

    /**
     * Keeps track of whether we are currently beeping / drawing the user's name on-screen.
     */
    private static boolean GlobalBeepInProgress = false;
    
    /**
     * Constructor - Creates a new Friend object.
     * @param username
     * @param color
     */
    private Friend(String username, Color color) {
        Username = username;
        DrawColor = color;
    }
    
	/*
	 * Gets all list of all people on the user's friend's list.
	 * 
	 * @return  List of friends.
	 */
	public static List<Friend> getAll(Script script)
	{
	    List<Friend> friendsList = new ArrayList<>();
		
		// Get the widget of the friend's list.
		RS2Widget friendsWidget = script.getWidgets().get(429, 9);
		
		if (friendsWidget != null) {
		    // Get all user widgets. 
			RS2Widget[] children = friendsWidget.getChildWidgets();
			
			if (children == null)
			    return friendsList;
			
			// Add each friend to the list.
			for (int i = 0; i < children.length; i++) {
                // Each child widget is setup as follows (username (space) Online Status). 
                // We use i % 3 here to get only the username.
				if (i % 3 == 0) {
					RS2Widget friend = children[i];

                    Friend f = new Friend(friend.getMessage(), ColorHelper.getRandom());
                    friendsList.add(f);
				}
			}	
		}
		return friendsList;
	}

    /**
     * Takes two lists of friends and merges them together. Keeps the old friends in tact.
     * 
     * @return list of merged friends
     */
	public static List<Friend> mergeFriends(List<Friend> oldFriends, List<Friend> newFriends) {
	    List<Friend> merged = new ArrayList<>();
	    
	    // Go through each of the new friends and see if they match the current 
	    for (Friend newFriend: newFriends) {
            boolean found = false;
            
            // Add old friends to the merged list, keeping their same username colour.
            for (Friend oldFriend: oldFriends) {
                if (oldFriend.Username.equals(newFriend.Username)) {
                    merged.add(oldFriend);
                    found = true;
                    break;
                }
            }
            
            // If the existing friend wasn't found, we'll consider this user new, so he should be added.
            if (!found)
                merged.add(newFriend);
        }
        
        return merged;
    }

    /**
     * Draws the user's friend's list to the screen.
     * @param script
     * @param g
     * @param friends
     * @param playersInArea
     */
    public static void drawListOnScreen(Script script, Graphics2D g, List<Friend> friends, List<Player> playersInArea)
    {
        // Go through every friend and player in the area and display them if it's a friend.
        for (Friend friend : friends) {
            for (Player player : playersInArea) {
                // Check if the username of our friend matches one that is contained in the area.
                if (friend.Username.equals(player.getName())) {
                    Position pos = player.getPosition();

                    // Draw username on game screen.
                    if (player.isOnScreen()) {
                        Polygon poly = pos.getPolygon(script.bot);
                        Rectangle rect = poly.getBounds();
                        g.setColor(friend.DrawColor);
                        g.setFont(new Font("Arial", Font.BOLD, 12));
                        g.drawString(player.getName(), (int) rect.getX(), (int) rect.getY());
                    }

                    // Draw username on mini-map
                    if (pos.isOnMiniMap(script.bot)) {
                        MiniMapTileDestination mmtd = new MiniMapTileDestination(script.getBot(), pos);
                        g.setColor(friend.DrawColor);
                        g.setFont(new Font("Arial", Font.BOLD, 12));
                        g.drawString(player.getName(), mmtd.getBoundingBox().x, mmtd.getBoundingBox().y);
                    }
                    
                    // If the specified player wasn't in the previous player's list, beep
                    if (playersInArea.contains(player) && !PreviousPlayers.contains(player) && PreviousPlayers != null) {
                        script.log(player.getName() + " has entered the area!");
                        friend.IsNewlyInArea = true;
                        playBeep(friends, friend);
                    }
                    
                    // Draws the username while beeping (if the friend is newly in the area.)
                    drawUsernameWhileBeeping(friends, g, friend);
                }
            }
        }
        
        PreviousPlayers = playersInArea;
    }

    /**
     * Draws a friend's username 
     * @param friend
     */
    private static void drawUsernameWhileBeeping(List<Friend> friends,  Graphics2D g, Friend friend) {
        if (!friend.IsNewlyInArea)
            return;

        // Get filtered list of friends that are new in the area.
        List<Friend> newInAreaFriends = new ArrayList<>(friends);
        newInAreaFriends.removeIf(x -> !x.IsNewlyInArea);
        
        // Get the index of the current friend, so we can get the y position accordingly.
        int index = newInAreaFriends.indexOf(friend);
        
        // Draw user's name in huge letters, in chat box.
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(friend.Username + " has entered the arena!", 14, 25 * index + 360);
    }
    
    /***
     * Plays a beeping noise when a 
     * Usually used when a new user enters the area.
     * 
     * @param friend
     */
    private static void playBeep(List<Friend> allFriends, Friend friend) {
        // Play beeping noise on a new thread.
        Thread thread = new Thread(() -> { 
            try {
                if (!GlobalBeepInProgress && friend.IsNewlyInArea) {
                    
                    GlobalBeepInProgress = true;
                    BeepHelper.play(1000, BEEP_INTERVAL, 0.15);
                    GlobalBeepInProgress = false;
                    
                    Thread.sleep(5000);
                    // Set all friends that are new in the area to false.
                    for (Friend f: allFriends)
                        f.IsNewlyInArea = false;
                }

            } catch (Exception e) { }
        });
        thread.start();
    }
}

package com.hotmail.AdrianSRJose.JoinMaster.BossBar;

import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.hotmail.AdrianSRJose.JoinMaster.JoinMaster;
import com.hotmail.AdrianSRJose.JoinMaster.BossBar.versions.v1_9;

/**
 * Allows plugins to safely set a health bar message.
 *
 * @author James Mortemore
 */

public class BarAPI implements Listener 
{

  private static HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
  private static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();

  private static BarAPI plugin;
  
  public static BarAPI getInstance()
  {
	  return plugin;
  }
  
  public HashMap<UUID, FakeDragon> getPlayers()
  {
	  return players;
  }
  
  public void clearTimers()
  {
	  timers.clear();
  }
  
  public HashMap<UUID, Integer> getTimers()
  {
	  return timers;
  }
  
  public void clearPlayers()
  {
	  players.clear();
  }

  private static boolean useSpigotHack = false;

  public static boolean useSpigotHack() {
    return useSpigotHack;
  }
  
  @EventHandler
  public void onJoin(PlayerJoinEvent event)
  {
	  Player p = event.getPlayer();
	  
	  ConfigurationSection bossbar = JoinMaster.getInstance().getConfig().getConfigurationSection("JoinMaster_Config.BossBar");
	  
	  if (bossbar != null)
	  {
		  boolean a = bossbar.getBoolean("Use");
		  
		  if (!a)return;
		  
		  String title = bossbar.getString("Title");
		  
		  if (title != null)
		  {
			  title = com.hotmail.AdrianSRJose.JoinMaster.Util.Util.VariableReplacer(p, title);
			  title = ChatColor.translateAlternateColorCodes('&', title);
			  
			  BossBarType bar = BossBarType.valueOf(bossbar.getString("Type"));
			  
			  if (bar == BossBarType.MOMENTARY)
			  {
				  int seconds = bossbar.getInt("Seconds");
				  setMessage(p, title, seconds);
			  }
			  else{
				  setMessage(p, title);
			  }
		  }
	  }
  }

  /**
   * Set a message for all players.<br>
   * It will remain there until the player logs off or another plugin overrides it.<br>
   * This method will show a full health bar and will cancel any running timers.
   *
   * @param message The message shown.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   *
   * @see BarAPI#setMessage(player, message)
   */
  @Deprecated
  public static void setMessage(String message) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      setMessage(player, message);
    }
  }

  /**
   * Set a message for the given player.<br>
   * It will remain there until the player logs off or another plugin overrides it.<br>
   * This method will show a full health bar and will cancel any running timers.
   *
   * @param player  The player who should see the given message.
   * @param message The message shown to the player.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   */
  @Deprecated
  public static void setMessage(Player player, String message) {
    if (hasBar(player))
      removeBar(player);
    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = dragon.getMaxHealth();

    cancelTimer(player);

    sendDragon(dragon, player);
  }
  
  @Deprecated
  public static void setNormalMessage(Player player, String message, float healt) {
    if (hasBar(player))
      removeBar(player);
    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);

    cancelTimer(player);

    sendDragon(dragon, player);
  }

  /**
   * Set a message for all players.<br>
   * It will remain there for each player until the player logs off or another plugin overrides it.<br>
   * This method will show a health bar using the given percentage value and will cancel any running timers.
   *
   * @param message The message shown to the player.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   * @param percent The percentage of the health bar filled.<br>
   *                This value must be between 0F (inclusive) and 100F (inclusive).
   *
   * @throws IllegalArgumentException If the percentage is not within valid bounds.
   * @see BarAPI#setMessage(player, message, percent)
   */
  @Deprecated
  public static void setMessage(String message, float percent, int seconds) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      setMessage(player, message, percent);
    }
  }

  /**
   * Set a message for the given player.<br>
   * It will remain there until the player logs off or another plugin overrides it.<br>
   * This method will show a health bar using the given percentage value and will cancel any running timers.
   *
   * @param player  The player who should see the given message.
   * @param message The message shown to the player.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   * @param percent The percentage of the health bar filled.<br>
   *                This value must be between 0F (inclusive) and 100F (inclusive).
   *
   * @throws IllegalArgumentException If the percentage is not within valid bounds.
   */
  @Deprecated
  public static void setMessage(Player player, String message, float percent) {
    Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);

    if (hasBar(player))
      removeBar(player);

    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = (percent / 100f) * dragon.getMaxHealth();

    cancelTimer(player);

    sendDragon(dragon, player);
  }


  /**
   * Set a message for all players.<br>
   * It will remain there for each player until the player logs off or another plugin overrides it.<br>
   * This method will use the health bar as a decreasing timer, all previously started timers will be cancelled.<br>
   * The timer starts with a full bar.<br>
   * The health bar will be removed automatically if it hits zero.
   *
   * @param message The message shown.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   * @param seconds The amount of seconds displayed by the timer.<br>
   *                Supports values above 1 (inclusive).
   *
   * @throws IllegalArgumentException If seconds is zero or below.
   * @see BarAPI#setMessage(player, message, seconds)
   */
  @Deprecated
  public static void setMessage(String message, int seconds) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      setMessage(player, message, seconds);
    }
  }

  /**
   * Set a message for the given player.<br>
   * It will remain there until the player logs off or another plugin overrides it.<br>
   * This method will use the health bar as a decreasing timer, all previously started timers will be cancelled.<br>
   * The timer starts with a full bar.<br>
   * The health bar will be removed automatically if it hits zero.
   *
   * @param player  The player who should see the given timer/message.
   * @param message The message shown to the player.<br>
   *                Due to limitations in Minecraft this message cannot be longer than 64 characters.<br>
   *                It will be cut to that size automatically.
   * @param seconds The amount of seconds displayed by the timer.<br>
   *                Supports values above 1 (inclusive).
   *
   * @throws IllegalArgumentException If seconds is zero or below.
   */
  @Deprecated
  public static void setMessage(final Player player, String message, int seconds) {
    Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);

    if (hasBar(player))
      removeBar(player);

    FakeDragon dragon = getDragon(player, message);

    dragon.name = cleanMessage(message);
    dragon.health = dragon.getMaxHealth();

    final float dragonHealthMinus = dragon.getMaxHealth() / seconds;

    cancelTimer(player);

    timers.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(JoinMaster.getInstance(), new Runnable() {

      @Override
      public void run() {
        FakeDragon drag = getDragon(player, "");
        drag.health -= dragonHealthMinus;

        if (drag.health <= 1) {
          removeBar(player);
          cancelTimer(player);
        } else {
          sendDragon(drag, player);
        }
      }

    }, 20L, 20L).getTaskId());

    sendDragon(dragon, player);
  }
  
  /**
   * Checks whether the given player has a bar.
   *
   * @param player The player who should be checked.
   *
   * @return True, if the player has a bar, False otherwise.
   */
  @Deprecated
  public static boolean hasBar(Player player) {
    return players.get(player.getUniqueId()) != null;
  }

  /**
   * Removes the bar from the given player.<br>
   * If the player has no bar, this method does nothing.
   *
   * @param player The player whose bar should be removed.
   */
  @Deprecated
  public static void removeBar(Player player) {
    if (!hasBar(player))
      return;

    FakeDragon dragon = getDragon(player, "");

    if (dragon instanceof v1_9) {
      ((v1_9) dragon).getBar().removePlayer(player);
    } else {
      Util.sendPacket(player, getDragon(player, "").getDestroyPacket());
    }

    players.remove(player.getUniqueId());

    cancelTimer(player);
  }

  /**
   * Modifies the health of an existing bar.<br>
   * If the player has no bar, this method does nothing.
   *
   * @param player  The player whose bar should be modified.
   * @param percent The percentage of the health bar filled.<br>
   *                This value must be between 0F and 100F (inclusive).
   */
  @Deprecated
  public static void setHealth(Player player, float percent) {
    if (!hasBar(player))
      return;

    FakeDragon dragon = getDragon(player, "");
    dragon.health = (percent / 100f) * dragon.getMaxHealth();

    cancelTimer(player);

    if (percent == 0) {
      removeBar(player);
    } else {
      sendDragon(dragon, player);
    }
  }

  /**
   * Get the health of an existing bar.
   *
   * @param player The player whose bar's health should be returned.
   *
   * @return The current absolute health of the bar.<br>
   * If the player has no bar, this method returns -1.
   */
  @Deprecated
  public static float getHealth(Player player) {
    if (!hasBar(player))
      return -1;

    return getDragon(player, "").health;
  }

  /**
   * Get the message of an existing bar.
   *
   * @param player The player whose bar's message should be returned.
   *
   * @return The current message displayed to the player.<br>
   * If the player has no bar, this method returns an empty string.
   */
  @Deprecated
  public static String getMessage(Player player) {
    if (!hasBar(player))
      return "";

    return getDragon(player, "").name;
  }

  public static String cleanMessage(String message) {
    if (message.length() > 64)
      message = message.substring(0, 63);

    return message;
  }

  public static void cancelTimer(Player player) {
    Integer timerID = timers.remove(player.getUniqueId());

    if (timerID != null) {
      Bukkit.getScheduler().cancelTask(timerID);
    }
  }

  public static void sendDragon(FakeDragon dragon, Player player) {
    if (dragon instanceof v1_9) {
      BossBar bar = ((v1_9) dragon).getBar();

      bar.addPlayer(player);
      bar.setProgress(dragon.health / dragon.getMaxHealth());
    } else {
      Util.sendPacket(player, dragon.getMetaPacket(dragon.getWatcher()));
      Util.sendPacket(player, dragon.getTeleportPacket(getDragonLocation(player.getLocation())));
    }
  }

  public static FakeDragon getDragon(Player player, String message) {
    if (hasBar(player)) {
      return players.get(player.getUniqueId());
    } else
      return addDragon(player, cleanMessage(message));
  }

  private static FakeDragon addDragon(Player player, String message) {
    FakeDragon dragon = Util.newDragon(message, getDragonLocation(player.getLocation()));

    if (dragon instanceof v1_9) {
      BossBar bar = ((v1_9) dragon).getBar();

      bar.addPlayer(player);
    } else {
      Util.sendPacket(player, dragon.getSpawnPacket());
    }

    players.put(player.getUniqueId(), dragon);

    return dragon;
  }

  private static FakeDragon addDragon(Player player, Location loc, String message) {
    FakeDragon dragon = Util.newDragon(message, getDragonLocation(loc));

    if (dragon instanceof v1_9) {
      BossBar bar = ((v1_9) dragon).getBar();

      bar.addPlayer(player);
    } else {
      Util.sendPacket(player, dragon.getSpawnPacket());
    }

    players.put(player.getUniqueId(), dragon);

    return dragon;
  }

  private static Location getDragonLocation(Location loc) {
    if (Util.isBelowGround) {
      loc.subtract(0, 300, 0);
      return loc;
    }

    float pitch = loc.getPitch();

    if (pitch >= 55) {
      loc.add(0, -300, 0);
    } else if (pitch <= -55) {
      loc.add(0, 300, 0);
    } else {
      loc = loc.getBlock().getRelative(getDirection(loc), JoinMaster.getInstance().getServer().getViewDistance() * 16).getLocation();
    }

    return loc;
  }

  private static BlockFace getDirection(Location loc) {
    float dir = Math.round(loc.getYaw() / 90);
    if (dir == -4 || dir == 0 || dir == 4)
      return BlockFace.SOUTH;
    if (dir == -1 || dir == 3)
      return BlockFace.EAST;
    if (dir == -2 || dir == 2)
      return BlockFace.NORTH;
    if (dir == -3 || dir == 1)
      return BlockFace.WEST;
    return null;
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void PlayerLoggout(PlayerQuitEvent event) {
    quit(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerKick(PlayerKickEvent event) {
    quit(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerTeleport(final PlayerTeleportEvent event) {
    handleTeleport(event.getPlayer(), event.getTo().clone());
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerTeleport(final PlayerRespawnEvent event) {
    handleTeleport(event.getPlayer(), event.getRespawnLocation().clone());
  }

  private void handleTeleport(final Player player, final Location loc) {

    if (!hasBar(player))
      return;

    final FakeDragon oldDragon = getDragon(player, "");

    if (oldDragon instanceof v1_9) return;

    Bukkit.getScheduler().runTaskLater(JoinMaster.getInstance(), new Runnable() {

      @Override
      public void run() {
        // Check if the player still has a dragon after the two ticks! ;)
        if (!hasBar(player))
          return;

        float health = oldDragon.health;
        String message = oldDragon.name;

        Util.sendPacket(player, getDragon(player, "").getDestroyPacket());

        players.remove(player.getUniqueId());

        FakeDragon dragon = addDragon(player, loc, message);
        dragon.health = health;

        sendDragon(dragon, player);
      }

    }, 2L);
  }

  public static void quit(Player player) 
  {
    removeBar(player);
  }
}

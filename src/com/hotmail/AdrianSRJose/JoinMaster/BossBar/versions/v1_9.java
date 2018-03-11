package com.hotmail.AdrianSRJose.JoinMaster.BossBar.versions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import com.hotmail.AdrianSRJose.JoinMaster.JoinMaster;
import com.hotmail.AdrianSRJose.JoinMaster.BossBar.FakeDragon;

/**
 * This is the FakeDragon class for BarAPI.
 * It is based on the code by SoThatsIt.
 * <p>
 * http://forums.bukkit.org/threads/tutorial-utilizing-the-boss-health-bar.158018/page-5#post-2053705
 *
 * @author James Mortemore
 */

public class v1_9 extends FakeDragon {

  private BossBar bar;

  public v1_9(String name, Location loc) {
    super(name, loc);

    bar = Bukkit.createBossBar(name, BarColor.PINK, BarStyle.SOLID);
    
    String color = JoinMaster.getInstance().getConfig().getString("JoinMaster_Config.BossBar.Color");
    
    if (color != null)
    	bar.setColor(BarColor.valueOf(color));
    
  }

  public BossBar getBar() {
    return bar;
  }

  @Override
  public Object getSpawnPacket() {
    return null;
  }

  @Override
  public Object getDestroyPacket() {
    return null;
  }

  @Override
  public Object getMetaPacket(Object watcher) {
    return null;
  }

  @Override
  public Object getTeleportPacket(Location loc) {
    return null;
  }

  @Override
  public Object getWatcher() {
    return null;
  }
}

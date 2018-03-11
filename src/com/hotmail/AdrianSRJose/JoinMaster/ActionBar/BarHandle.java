package com.hotmail.AdrianSRJose.JoinMaster.ActionBar;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hotmail.AdrianSRJose.JoinMaster.JoinMaster;
import com.hotmail.AdrianSRJose.JoinMaster.Util.Util;

public class BarHandle implements Listener 
{
	private BarPacket bar;
	
	public BarHandle()
    {
        try
        {
            String version = Util.getVersion();
            String className = "com.hotmail.AdrianSRJose.JoinMaster.ActionBar.versions."+"Bar"+version;
            Class<?> cl = Class.forName(className);
            Class<? extends BarPacket> pack = cl.asSubclass(BarPacket.class);
            BarPacket p = pack.newInstance();
            bar = p;
        }
        catch(Throwable t) {}
    }
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		ConfigurationSection bars = JoinMaster.getInstance().getConfig().getConfigurationSection("JoinMaster_Config.ActionBar");

		if (bars != null)
		{
			boolean a = bars.getBoolean("Use");
			
			if (!a) return;
			
			String message = bars.getString("ActionMessage");
			
			if (message != null)
			{
				message = ChatColor.translateAlternateColorCodes('&', message);
				message = Util.VariableReplacer(p, message);
				
				new BarSender(p, message);
			}
		}
	}
	
	private class BarSender
	{
		public BarSender(Player player, String message)
		{
			bar.sendToPlayer(player, message);
		}
	}
}

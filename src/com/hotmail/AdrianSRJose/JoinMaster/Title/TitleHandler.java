package com.hotmail.AdrianSRJose.JoinMaster.Title;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hotmail.AdrianSRJose.JoinMaster.JoinMaster;
import com.hotmail.AdrianSRJose.JoinMaster.Util.Util;

public class TitleHandler implements Listener
{
    private TitlePacket packet;
    
    public TitleHandler()
    {
        try
        {
            String version = Util.getVersion();
            String className = "com.hotmail.AdrianSRJose.JoinMaster.Title.versions." + "Title"+version;
            Class<?> cl = Class.forName(className);
            Class<? extends TitlePacket> pack = cl.asSubclass(TitlePacket.class);
            TitlePacket p = pack.newInstance();
            packet = p;
        }
        catch(Throwable t) {}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void TitlePacket(PlayerJoinEvent event)
    {
    	//Send Title on Player Join
    	ConfigurationSection titles = JoinMaster.getInstance().getConfig().getConfigurationSection("JoinMaster_Config.Title");
    	if (titles == null)return;
    	boolean a = titles.getBoolean("Use");
    	Player p = event.getPlayer();
    	
    	if (!a)
    	{return;}

    	String t = titles.getString("TitleMessage");
    	String s = titles.getString("SubTitleMessage");

    	t = Util.VariableReplacer(p, t);
    	s = Util.VariableReplacer(p, s);
    	
    	new TitlePacketSender(p, t ,s);
    }
    
    private class TitlePacketSender
    {
		public TitlePacketSender(Player player, String t , String s)
		{
			packet.sendToPlayer(player, t, s);
		}
    }
}

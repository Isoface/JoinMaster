package com.hotmail.AdrianSRJose.JoinMaster;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.bobacadodl.imgmessage.ImageChar;
import com.bobacadodl.imgmessage.ImageMessage;
import com.hotmail.AdrianSRJose.JoinMaster.ActionBar.BarHandle;
import com.hotmail.AdrianSRJose.JoinMaster.BossBar.BarAPI;
import com.hotmail.AdrianSRJose.JoinMaster.Title.TitleHandler;
import com.hotmail.AdrianSRJose.JoinMaster.Util.Util;
import com.stringer.annotations.HideAccess;

public class JoinMaster extends JavaPlugin implements Listener, CommandExecutor
{
	public static JavaPlugin instance;
	
	public static JavaPlugin getInstance()
	{
		return instance;
	}
	
	public Location spawn = null;
	
	@Override
	public void onEnable()
	{
		//--------------------------------------------
		instance = this;
		//--------------------------------------------
		PluginManager mg = Bukkit.getPluginManager();
		mg.registerEvents(this, this);
		mg.registerEvents(new TitleHandler(), this);
		mg.registerEvents(new BarHandle(), this);
		mg.registerEvents(new BarAPI(), this);
		//--------------------------------------------
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults();
		this.getConfig().options().copyDefaults(true);
		//--------------------------------------------
		getCommand("JoinMaster").setExecutor(this);
		//--------------------------------------------
		
		ConfigurationSection ss = getConfig().getConfigurationSection("JoinMaster_Config.Spawn");
		if (ss != null)
			spawn = getLocationFromConfig(ss);
	}
	
	
	@EventHandler
	public void spawn(final PlayerJoinEvent eve)
	{
		if (spawn == null)
			return;
		
		if (spawn.getWorld() == null || spawn.getWorld().getName() == null)
		{
			spawn.setWorld(eve.getPlayer().getWorld());
			Bukkit.getConsoleSender().sendMessage("§cInvalid Spawn World");
		}
		
		final Player p = eve.getPlayer();
		if (p != null && p.isOnline())
		{
			ConfigurationSection cs = getConfig().getConfigurationSection("JoinMaster_Config.Spawn");
			boolean b = cs.getBoolean("Use");
			if (b)
				p.teleport(spawn);
			else
				Bukkit.getConsoleSender().sendMessage("§cInvalid Spawn");
		}
	}

	@EventHandler
	public void ImageOnJoin(final PlayerJoinEvent eve)
	{
		eve.setJoinMessage(null);
		final Player p = eve.getPlayer();
		
		try
		{
			File f = new File(this.getDataFolder(), "Image.png");
			if (f.exists() && f != null) 
			{
				BufferedImage im = ImageIO.read(f);
				ImageMessage me = new ImageMessage(im, 10, ImageChar.MEDIUM_SHADE.getChar());
				me.appendTextToLine(6, "");
				
				me.sendToPlayer(p);
			}
		}
		catch(Throwable e)
		{
			
		}
	}
	
	@EventHandler
	public void MessageOnJoin(final PlayerJoinEvent eve)
	{
		eve.setJoinMessage(null);
		
		final Player p = eve.getPlayer();

		// Send Message on Player Join
		ConfigurationSection messages = getConfig().getConfigurationSection("JoinMaster_Config.Message");
		if (messages == null) return;
		boolean a = getConfig().getBoolean("JoinMaster_Config.Message.Use");
		if (!a) return;
		
		List<String> sl = getConfig().getStringList("JoinMaster_Config.Message.Messages");
		if (sl != null && !sl.isEmpty())
		{
			for (String toSend : sl)
			{
				if (toSend != null)
				{
					toSend = Util.VariableReplacer(p, toSend);
					toSend = ChatColor.translateAlternateColorCodes('&', toSend);
					
					p.sendMessage(toSend);
				}
			}
		}
	}
	
	@EventHandler
	public void WelcomeMessage(final PlayerJoinEvent eve)
	{
		if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null) return;
		
		final Player p = eve.getPlayer();

		// Send VipMessage to all Players on VIP Join
		ConfigurationSection messages = getConfig().getConfigurationSection("JoinMaster_Config.Message");
		if (messages == null) return;
		boolean a = getConfig().getBoolean("JoinMaster_Config.Message.Use_WelcomeMessage");
		if (!a) return;

		String VipMessage = getConfig().getString("JoinMaster_Config.Message.WelcomeMessage");

		if (VipMessage == null)return;
		
		VipMessage = Util.VariableReplacer(p, VipMessage);
		VipMessage = ChatColor.translateAlternateColorCodes('&', VipMessage);
		
		if (p.hasPermission("JoinMaster.WelcomeMessage"))
		{
			for (Player all : Bukkit.getOnlinePlayers())
				all.sendMessage(VipMessage);
		}
	}
	
	@EventHandler
	public void StrikeOnJoin(final PlayerJoinEvent eve)
	{
		final Player p = eve.getPlayer();
		final Location jl = p.getLocation();
		final World w = jl.getWorld();

		//StrikeLinghting Effect on Join
		ConfigurationSection StrikeLightning = getConfig().getConfigurationSection("JoinMaster_Config.StrikeLightningEffect");
		if (StrikeLightning == null)return;
		boolean a = getConfig().getBoolean("JoinMaster_Config.StrikeLightningEffect.Use");
		if (!a)return;
		w.strikeLightningEffect(jl);
	}
	
	@EventHandler
	public void FireWorksOnJoin(final PlayerJoinEvent eve)
	{
		final Player p = eve.getPlayer();
		final Location jl = p.getLocation();

		//StrikeLinghting Effect on Join
		ConfigurationSection FireWorks = getConfig().getConfigurationSection("JoinMaster_Config.FireWorks");
		if (FireWorks == null)return;
		boolean a = getConfig().getBoolean("JoinMaster_Config.FireWorks.Use");
		if (!a)return;
		boolean b = getConfig().getBoolean("JoinMaster_Config.FireWorks.RandomColors");
		
		boolean c1 = true;
		boolean c2 = true;
		Color col1 = null;
		Color col2 = null;
		
		if (getConfig().get("Color_1") == null)
		{
			c1 = false;
		} 
		else {
			col1 = getConfig().getColor("JoinMaster_Config.FireWorks.Color_1");
		}
		
		if (getConfig().get("Color_2") == null)
		{
			c2 = false;
		} else {
			col2 = getConfig().getColor("JoinMaster_Config.FireWorks.Color_2");
		}
		
		if (b)
			Util.FireWorkEffect(jl);
		
		
		if (c1 && !c2 && !b)
			Util.FireWorkEffect(jl, col1);

		if (!c1 && c2 && !b)
			Util.FireWorkEffect(jl, col2);
		
		if (c1 && c2 && !b)
			Util.FireWorkEffect(jl, col1, col2);
	}
	
	@EventHandler
	public void SoundOnJoin(final PlayerJoinEvent eve)
	{
		final Player p = eve.getPlayer();
		final Location jl = p.getLocation();
		final World w = jl.getWorld();
		
		boolean a = getConfig().getBoolean("JoinMaster_Config.Sound.Use");
		
		if (!a)return;
		
		ConfigurationSection SoundSection = getConfig().getConfigurationSection("JoinMaster_Config.Sound");
		if (SoundSection == null)return;
		
		String ss = getConfig().getString("JoinMaster_Config.Sound.SoundName");
		ss = ss.toUpperCase();
		
		if (ss.equalsIgnoreCase("''") || ss.equalsIgnoreCase("''"))
			return;
		try
		{
			Sound s1 = Sound.valueOf(ss);

			if (ss != null)
			{
				Integer i = getConfig().getInt("JoinMaster_Config.Sound.Pitch");
				w.playSound(jl, s1, 10f, (float)i);
			}
		}
		catch(Exception e)
		{

		}
	}
	
	@EventHandler
	public void EffectOnJoin(final PlayerJoinEvent eve)
	{
		final Player p = eve.getPlayer();
		final Location jl = p.getLocation();
		final World w = jl.getWorld();
		
		boolean a = getConfig().getBoolean("JoinMaster_Config.Effect.Use");
		
		if (!a)return;
		
		ConfigurationSection effsect = getConfig().getConfigurationSection("JoinMaster_Config.Effect");
		if (effsect == null)return;
		
		String efna = getConfig().getString("JoinMaster_Config.Effect.EffectName");
		efna = efna.toUpperCase();
		
		if (efna.equalsIgnoreCase("''") || efna.equalsIgnoreCase(""))
			return;
		
		int i = getConfig().getInt("JoinMaster_Config.Effect.EffectIntensity");
		
		try
		{
			Effect f = Effect.valueOf(efna.toUpperCase());
			
			Bukkit.getScheduler().runTaskLater(instance, new Runnable()
			{
				@Override
				public void run() 
				{
					w.playEffect(jl, f, 1, i);
				}
			}, 10);
		}
		catch(Exception e)
		{
			
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (label.equalsIgnoreCase("JoinMaster"))
		{
			if (args.length > 0)
			{
				if (args[0].equalsIgnoreCase("reload"))
				{
					this.reloadConfig();
					this.getConfig().options().copyDefaults(true);
					this.getConfig().options().copyDefaults();
					sender.sendMessage("§a§lJoinMaster Reloaded");
				}
				else if (args[0].equalsIgnoreCase("Spawn"))
				{
					if (!(sender instanceof Player))
						return false;
					
					if (spawn != null)
					{
						((Player)sender).teleport(spawn);
						((Player)sender).sendMessage("§a§lTeleporting....");
					}
					else
						Bukkit.getConsoleSender().sendMessage("§c§lInvalid Spawn");
				}
				else if (args[0].equalsIgnoreCase("set") && args.length > 1 && args[1].equalsIgnoreCase("spawn"))
				{
					if (!(sender instanceof Player))
						return false;
					
					Player p = ((Player)sender);
					spawn = p.getLocation();
					ConfigurationSection sc = null;
					if (!getConfig().isConfigurationSection("JoinMaster_Config.Spawn"))
						sc = getConfig().createSection("JoinMaster_Config.Spawn");
					else
						sc = getConfig().getConfigurationSection("JoinMaster_Config.Spawn");
					
					this.saveLocationInConfig(spawn, sc);
					this.saveConfig();
					
					((Player)sender).sendMessage("§a§lSpawn Seted :)");
				}
			}
		}
		return true;
	}
	
	public Location getLocationFromConfig(ConfigurationSection sc)
	{
		Location loc = null;
		if (sc != null)
		{
			World w = null;
			String wn = sc.getString("World");
			if (wn != null)
				w = Bukkit.getWorld(wn);
			
			double x = sc.getDouble("X");
			double y = sc.getDouble("Y");
			double z = sc.getDouble("Z");
			float pitch = (float) sc.getDouble("pitch");
			float yaw = (float) sc.getDouble("yaw");
			
			return new Location(w, x, y, z, yaw, pitch);
		}
		return loc;
	}
	
	public void saveLocationInConfig(Location loc, ConfigurationSection sc)
	{
		if (loc != null && sc != null)
		{
			if (loc.getWorld() != null && loc.getWorld().getName() != null)
			{
				sc.set("World", loc.getWorld().getName());
				sc.set("X", loc.getX());
				sc.set("Y", loc.getY());
				sc.set("Z", loc.getZ());
				sc.set("yaw", loc.getYaw());
				sc.set("pitch", loc.getPitch());
			}
		}
	}
	
	@Override
	public void onDisable()
	{}
}

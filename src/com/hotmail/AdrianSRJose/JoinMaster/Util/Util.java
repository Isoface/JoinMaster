package com.hotmail.AdrianSRJose.JoinMaster.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Util 
{
private static List<String> toReplace;
	
	public static String getVersion()
    {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf(".") + 1);
    }
	
	public static Class<?> getNMSClass(String paramString)
	{
		String str = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try
		{
			return Class.forName("net.minecraft.server." + str + "." + paramString);
	    }
		catch (ClassNotFoundException localClassNotFoundException)
		{
			localClassNotFoundException.printStackTrace();
	    }
		return null;
	  }
	
	@SuppressWarnings("deprecation")
	public static String VariableReplacer(Player pl, String message)
	{
		toReplace = new ArrayList<String>();
		toReplace.add("%player_name%");
		toReplace.add("%player_gamemode%");
		toReplace.add("%player_world%");
		toReplace.add("%player_DisplayName%");
		toReplace.add("%player_itemonhand%");
		toReplace.add("%player_prefix%");
		
		for (String tr : toReplace)
		{
			if (message.contains(tr))
			{
				try
				{
					if (tr.equalsIgnoreCase("%player_prefix%"))
						message = message.replaceAll(tr, PermissionsEx.getUser(pl).getPrefix());
				}
				catch(Exception e)
				{Bukkit.getConsoleSender().sendMessage("§c[JoinMaster] No PermissionsEx Detected");}
				catch(NoClassDefFoundError e)
				{Bukkit.getConsoleSender().sendMessage("§c[JoinMaster] No PermissionsEx Detected");}
				
				if (tr.equalsIgnoreCase("%player_name%"))
					message = message.replaceAll(tr, pl.getName());
				else if (tr.equalsIgnoreCase("%player_gamemode%"))
					message = message.replaceAll(tr, pl.getGameMode().name());
				else if (tr.equalsIgnoreCase("%player_world%"))
					message = message.replaceAll(tr, pl.getLocation().getWorld().getName());
				else if (tr.equalsIgnoreCase("%player_DisplayName%"))
					message = message.replaceAll(tr, pl.getDisplayName());
					if (pl.getItemInHand() != null && pl.getItemInHand().hasItemMeta() && pl.getItemInHand().getItemMeta().hasDisplayName())
						message = message.replaceAll(tr, pl.getItemInHand().getItemMeta().getDisplayName());
					else message = message.replaceAll(tr, "nada");
			}
		}
		return message;
	}
	
	  public static void FireWorkEffect(Location loc)
	  {
	    Random colour = new Random();
	    Firework f = loc.getWorld().spawn(loc, Firework.class);
	    FireworkMeta fMeta = f.getFireworkMeta();
	    FireworkEffect.Type fwType = FireworkEffect.Type.BALL_LARGE;
	    
	    int c1i = colour.nextInt(17) + 1;
	    int c2i = colour.nextInt(17) + 1;
	    
	    Color c1 = getColF(c1i);
	    Color c2 = getColF(c2i);
	    FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).build();
	    fMeta.addEffect(effect);
	    fMeta.setPower(1);
	    f.setFireworkMeta(fMeta);
	  }
	  
	  public static void FireWorkEffect(Location loc, Color col)
	  {
	    Firework f = loc.getWorld().spawn(loc, Firework.class);
	    FireworkMeta fMeta = f.getFireworkMeta();
	    FireworkEffect.Type fwType = FireworkEffect.Type.BALL_LARGE;
	    
	    Color c1 = col;
	    
	    FireworkEffect effect = FireworkEffect.builder().withFade(c1).withColor(c1).with(fwType).build();
	    fMeta.addEffect(effect);
	    fMeta.setPower(1);
	    f.setFireworkMeta(fMeta);
	  }
	  
	  public static void FireWorkEffect(Location loc, Color col, Color col2)
	  {
	    Firework f = loc.getWorld().spawn(loc, Firework.class);
	    FireworkMeta fMeta = f.getFireworkMeta();
	    FireworkEffect.Type fwType = FireworkEffect.Type.BALL_LARGE;
	    
	    Color c1 = col;
	    Color c2 = col2;
	    
	    FireworkEffect effect = FireworkEffect.builder().withFade(c2).withColor(c1).with(fwType).build();
	    fMeta.addEffect(effect);
	    fMeta.setPower(1);
	    f.setFireworkMeta(fMeta);
	  }
	  
	  public static Color getColF(int c)
	  {
	    switch (c)
	    {
	    case 1: 
	      return Color.TEAL;
	    case 2: 
	    default: 
	      return Color.WHITE;
	    case 3: 
	      return Color.YELLOW;
	    case 4: 
	      return Color.AQUA;
	    case 5: 
	      return Color.BLACK;
	    case 6: 
	      return Color.BLUE;
	    case 7: 
	      return Color.FUCHSIA;
	    case 8: 
	      return Color.GRAY;
	    case 9: 
	      return Color.GREEN;
	    case 10: 
	      return Color.LIME;
	    case 11: 
	      return Color.MAROON;
	    case 12: 
	      return Color.NAVY;
	    case 13: 
	      return Color.OLIVE;
	    case 14: 
	      return Color.ORANGE;
	    case 15: 
	      return Color.PURPLE;
	    case 16: 
	      return Color.RED;
	    }
	}
}
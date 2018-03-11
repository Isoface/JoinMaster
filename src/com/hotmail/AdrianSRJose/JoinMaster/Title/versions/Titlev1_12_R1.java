package com.hotmail.AdrianSRJose.JoinMaster.Title.versions;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hotmail.AdrianSRJose.JoinMaster.Title.TitlePacket;

import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_12_R1.PacketPlayOutTitle.EnumTitleAction;

public class Titlev1_12_R1 implements TitlePacket
{
	@Override
	public void sendToPlayer(final Player player, final String title, String subtitle) 
	{
		String t = title.replaceAll("&", "§");
		String s = subtitle.replaceAll("&", "§");
		IChatBaseComponent ATitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + t + "\"}");
		IChatBaseComponent STitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + s + "\"}");
    	PacketPlayOutTitle Title = new PacketPlayOutTitle(EnumTitleAction.TITLE, ATitle, 6, 6, 6);
    	PacketPlayOutTitle SubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, STitle, 6, 6, 6);
    	CraftPlayer cp = (CraftPlayer)player;
    	cp.getHandle().playerConnection.sendPacket(Title);
    	cp.getHandle().playerConnection.sendPacket(SubTitle);
	}
	
}

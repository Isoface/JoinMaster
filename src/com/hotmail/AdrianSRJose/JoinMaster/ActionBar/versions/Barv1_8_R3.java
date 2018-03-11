package com.hotmail.AdrianSRJose.JoinMaster.ActionBar.versions;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hotmail.AdrianSRJose.JoinMaster.ActionBar.BarPacket;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Barv1_8_R3 implements BarPacket
{
	@Override
	public void sendToPlayer(Player player, String message) 
	{
		IChatBaseComponent ac = ChatSerializer.a("{\"text\":\"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(ac, (byte)2);
		((CraftPlayer)player ).getHandle().playerConnection.sendPacket(ppoc);
	}
}

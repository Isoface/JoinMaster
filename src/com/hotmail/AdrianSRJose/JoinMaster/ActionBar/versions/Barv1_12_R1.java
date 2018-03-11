package com.hotmail.AdrianSRJose.JoinMaster.ActionBar.versions;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.hotmail.AdrianSRJose.JoinMaster.ActionBar.BarPacket;

import net.minecraft.server.v1_12_R1.ChatMessageType;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;

public class Barv1_12_R1 implements BarPacket
{
	@Override
	public void sendToPlayer(Player player, String message) 
	{
		IChatBaseComponent ac = ChatSerializer.a("{\"text\":\"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(ac, ChatMessageType.GAME_INFO);
		((CraftPlayer)player ).getHandle().playerConnection.sendPacket(ppoc);
	}
}

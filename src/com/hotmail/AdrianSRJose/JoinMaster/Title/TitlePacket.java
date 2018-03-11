package com.hotmail.AdrianSRJose.JoinMaster.Title;

import org.bukkit.entity.Player;

public interface TitlePacket
{
    void sendToPlayer(Player player, String title, String subtitle);
}

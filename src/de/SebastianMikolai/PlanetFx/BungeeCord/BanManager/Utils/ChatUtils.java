package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;

public class ChatUtils {

	public static void BroadcastChat(String message) {
		ProxyServer.getInstance().broadcast(new TextComponent(message));
	}
	
	public static String ConvertColorCodes(String message) {
		String msg = ChatColor.translateAlternateColorCodes('&', message);
		msg = ChatColor.translateAlternateColorCodes('§', msg);
		return msg;
	}
}
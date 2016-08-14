package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.DateUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BanManager {

	public static List<Ban> bans = new ArrayList<Ban>();
	
	public static Boolean isPermanentBan(UUID uuid) {
		Boolean bool = false;
		Ban ban = getBan(uuid);
		if (ban.getBannedTime().equalsIgnoreCase("PERMANENT")) {
			bool = true;
		}
		return bool;
	}
	
	public static Boolean isBanTimeOver(UUID uuid) {
		Boolean bool = false;
		Ban ban = getBan(uuid);
		Long BannedTime = Long.valueOf(ban.BannedTime);
		Long now = DateUtils.getTimestampNow();
		Long diff = BannedTime - now;
		if (diff < 0) {
			bool = true;
		}
		return bool;
	}
	
	public static Boolean isPlayerBanned(UUID uuid) {
		Boolean bool = false;
		for (Ban ban : bans) {
			if (ban.getPlayerUUID().equals(uuid)) {
				bool = true;
			}
		}
		return bool;
	}
	
	public static Ban getBan(UUID uuid) {
		Ban ReturnBan = null;
		for (Ban ban : bans) {
			if (ban.getPlayerUUID().equals(uuid)) {
				ReturnBan = ban;
			}
		}
		return ReturnBan;
	}
	
	public static void AddBan(Ban ban) {
		bans.add(ban);
		MySQL.AddBan(ban);
		MySQL.AddReason(new Reason(ban.getPlayerUUID(), ban.getPlayerName(), ban.getReason()));
		for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
			if (p.getUniqueId().equals(ban.PlayerUUID)) {
				if (ban.getServer().equalsIgnoreCase("GLOBAL")) {
					if (ban.getBannedTime().equalsIgnoreCase("PERMANENT")) {
						String kickmessage = Main.getInstance().KickMessageOnJoinGlobal;
						kickmessage = kickmessage.replace("<getBannedTime>", ban.getBannedTime());
						kickmessage = kickmessage.replace("<getReason>", ban.getReason());
						ProxyServer.getInstance().getPlayer(ban.PlayerUUID).disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
					} else {
						String kickmessage = Main.getInstance().KickMessageOnJoinGlobalTemp;
						kickmessage = kickmessage.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
						kickmessage = kickmessage.replace("<getReason>", ban.getReason());
						ProxyServer.getInstance().getPlayer(ban.PlayerUUID).disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
					}
				} else {
					if (ban.getBannedTime().equalsIgnoreCase("PERMANENT")) {
						String kickmessage = Main.getInstance().KickMessageOnJoin;
						kickmessage = kickmessage.replace("<getBannedTime>", ban.getBannedTime());
						kickmessage = kickmessage.replace("<getServer>", ban.getServer());
						kickmessage = kickmessage.replace("<getReason>", ban.getReason());
						ProxyServer.getInstance().getPlayer(ban.PlayerUUID).disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
						} else {
						String kickmessage = Main.getInstance().KickMessageOnJoinTemp;
						kickmessage = kickmessage.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
						kickmessage = kickmessage.replace("<getServer>", ban.getServer());
						kickmessage = kickmessage.replace("<getReason>", ban.getReason());
						ProxyServer.getInstance().getPlayer(ban.PlayerUUID).disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
					}
				}
			}
		}
	}
	
	public static void RemoveBan(UUID BannedPlayerUUID, ProxiedPlayer pp) {
		Ban RemoveBan = null;
		for (Ban ban : bans) {
			if (ban.getPlayerUUID().equals(BannedPlayerUUID)) {
				RemoveBan = ban;
			}
		}
		bans.remove(RemoveBan);
		MySQL.Unban(RemoveBan, pp);
	}
}
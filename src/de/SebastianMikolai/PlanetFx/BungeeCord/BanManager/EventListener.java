package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Ban;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Mute;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MuteManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.DateUtils;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;

public class EventListener implements Listener {

	@EventHandler
    public void onLoginEvent(LoginEvent e){
		UUID uuid = e.getConnection().getUniqueId();
		UUIDDatenbank uuiddb = null;
		if (UUIDManager.isNewPlayer(uuid)) {
			uuiddb = new UUIDDatenbank(uuid, e.getConnection().getName());
			UUIDManager.AddUUID(uuiddb, e);
		} else {
			uuiddb = UUIDManager.getUUIDDatenbank(e.getConnection().getName());
			if (uuiddb != null) {
				if (BanManager.isPlayerBanned(uuiddb.getPlayerUUID())) {
					Ban ban = BanManager.getBan(uuiddb.getPlayerUUID());
					if (ban.getServer().equalsIgnoreCase("GLOBAL")) {
						if (ban.getBannedTime().equalsIgnoreCase("PERMANENT")) {
							String kickmessage = Main.getInstance().KickMessageOnJoinGlobal.replace("<getBannedTime>", ban.getBannedTime());
							kickmessage = kickmessage.replace("<getReason>", ban.getReason());
							e.setCancelReason(ChatUtils.ConvertColorCodes(kickmessage));
							e.setCancelled(true);
						} else {
							if (!BanManager.isBanTimeOver(uuid)) {
								String kickmessage = Main.getInstance().KickMessageOnJoinGlobalTemp.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
								kickmessage = kickmessage.replace("<getReason>", ban.getReason());
								e.setCancelReason(ChatUtils.ConvertColorCodes(kickmessage));
								e.setCancelled(true);
							} else {
								BanManager.RemoveBan(uuid, null);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
    public void onServerSwitchEvent(ServerSwitchEvent e){
		UUID uuid = e.getPlayer().getUniqueId();
		UUIDDatenbank uuiddb = null;
		uuiddb = UUIDManager.getUUIDDatenbank(e.getPlayer().getName());
		if (uuiddb != null) {
			if (BanManager.isPlayerBanned(uuiddb.getPlayerUUID())) {
				Ban ban = BanManager.getBan(uuiddb.getPlayerUUID());
				if (ban.getServer().equalsIgnoreCase(e.getPlayer().getServer().getInfo().getName())) {
					if (ban.getBannedTime().equalsIgnoreCase("PERMANENT")) {
						String kickmessage = Main.getInstance().KickMessageOnJoin;
						kickmessage = kickmessage.replace("<getBannedTime>", ban.getBannedTime());
						kickmessage = kickmessage.replace("<getServer>", ban.getServer());
						kickmessage = kickmessage.replace("<getReason>", ban.getReason());
						e.getPlayer().disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
					} else {
						if (!BanManager.isBanTimeOver(uuid)) {
							String kickmessage = Main.getInstance().KickMessageOnJoinTemp.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
							kickmessage = kickmessage.replace("<getServer>", ban.getServer());
							kickmessage = kickmessage.replace("<getReason>", ban.getReason());
							e.getPlayer().disconnect(new TextComponent(ChatUtils.ConvertColorCodes(kickmessage)));
						} else {
							BanManager.RemoveBan(uuid, null);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
    public void onChatEvent(ChatEvent e){
		if (e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) e.getSender();
			UUIDDatenbank uuiddb = null;
			uuiddb = UUIDManager.getUUIDDatenbank(pp.getName());
			if (uuiddb != null) {
				if (MuteManager.isMute(uuiddb.getPlayerUUID())) {
					Mute mute = MuteManager.getMute(uuiddb.getPlayerUUID());
					if (mute.getMutetBisTime().equalsIgnoreCase("PERMANENT")) {
						String mutemessage = Main.getInstance().MuteMessage;
						mutemessage = mutemessage.replace("<getMutetBisTime>", mute.getMutetBisTime());
						pp.sendMessage(new TextComponent(ChatUtils.ConvertColorCodes(mutemessage)));
						e.setCancelled(true);
					} else {
						if (!MuteManager.isMutetBisTimeOver(uuiddb.getPlayerUUID())) {
							String mutemessage = Main.getInstance().MuteTempMessage;
							mutemessage = mutemessage.replace("<getMutetBisTime>", DateUtils.getBannedTime(Long.valueOf(mute.getMutetBisTime())));
							pp.sendMessage(new TextComponent(ChatUtils.ConvertColorCodes(mutemessage)));
							e.setCancelled(true);
						} else {
							MuteManager.RemoveMute(uuiddb.getPlayerUUID());
						}
					}
				}
			}
		}
	}
}
package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Ban;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GlobalBanCommandListener extends Command {

	public GlobalBanCommandListener(String name) {
		super(name);
	}
	
	private void sendVersuch(CommandSender cs) {
		if (cs.hasPermission("pfx.banmanager.gban")) {
			cs.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager §6= = = ="));
			cs.sendMessage(new TextComponent("§6Versuche: §f/ban help"));
		} else {
			cs.sendMessage(new TextComponent(Main.getInstance().CommandNoPermissions));
		}		
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length > 0 && cs.hasPermission("pfx.banmanager.gban")) {
			UUID PlayerUUID = null;
			String PlayerName = null;
			UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[0]);
			if (uuiddb != null) {
				PlayerUUID = uuiddb.getPlayerUUID();
				PlayerName = uuiddb.getPlayerName();
				if (!BanManager.isPlayerBanned(PlayerUUID)) {
					UUID BannedFromPlayerUUID;
					if (cs instanceof ProxiedPlayer) {
						BannedFromPlayerUUID = ((ProxiedPlayer)cs).getUniqueId();
					} else {
						BannedFromPlayerUUID = UUID.randomUUID();
					}
					String BannedFromPlayerName = cs.getName();
					Calendar c = Calendar.getInstance();
					c.setTime(new Date());
					int i = 0;
					String reason = "";
					for (String arg : args) {
						if (i > 0) {
							if (reason == "") {
								reason = arg;
							} else {
								reason = reason + " " + arg;
							}
						}
						i++;
					}
					if (reason == "") {
						reason = Main.getInstance().DefaultBanMessage;
					}
					Ban ban = new Ban(PlayerUUID, PlayerName, String.valueOf(c.getTimeInMillis()), "PERMANENT", reason, BannedFromPlayerUUID, BannedFromPlayerName, "GLOBAL");
					BanManager.AddBan(ban);
				} else {
					cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gebannt!"));
				}
			} else {
				cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
			}
		} else {
			sendVersuch(cs);
		}
	}
}
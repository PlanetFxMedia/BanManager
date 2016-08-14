package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Ban;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Reason;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommandListener extends Command {

	public BanCommandListener(String name) {
		super(name);
	}
	
	private void sendVersuch(ProxiedPlayer pp) {
		if (pp.hasPermission("pfx.banmanager.ban")) {
			pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager §6= = = ="));
			pp.sendMessage(new TextComponent("§6Versuche: §f/ban help"));
		} else {
			pp.sendMessage(new TextComponent(Main.getInstance().CommandNoPermissions));
		}		
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		ProxiedPlayer pp = (ProxiedPlayer) cs;
		if (args.length > 0 && pp.hasPermission("pfx.banmanager.ban")) {
			if (args[0].equalsIgnoreCase("help")) {
				if (args.length == 1) {
					pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager Help §6= = = ="));
					pp.sendMessage(new TextComponent("§6Hilfe: §f/ban help (Command)"));
					pp.sendMessage(new TextComponent(""));
					pp.sendMessage(new TextComponent("§6Mute Commands: §f/mute help"));
					pp.sendMessage(new TextComponent("§6Ban Commands: §f/ban help ban"));
					pp.sendMessage(new TextComponent("§6Ban List Commands: §f/ban help list"));
					pp.sendMessage(new TextComponent("§6Alte Bans überprüfen: §f/ban check"));
					pp.sendMessage(new TextComponent("§6Datenbank reloaden: §f/ban reload"));
				} else {
					if (args[1].equalsIgnoreCase("ban")) {
						pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager Help §6= = = ="));
						pp.sendMessage(new TextComponent("§6Hilfe: §f/ban help ban"));
						pp.sendMessage(new TextComponent(""));
						pp.sendMessage(new TextComponent("§6Ban: §f/ban [Spieler] (Grund)"));
						pp.sendMessage(new TextComponent("§6TempBan: §f/tban [y/d/h/m/s] [Zeit] [Spieler] (Grund)"));
						pp.sendMessage(new TextComponent("§6Global Ban: §f/gban [Spieler] (Grund)"));
						pp.sendMessage(new TextComponent("§6Global TempBan: §f/gtban [y/d/h/m/s] [Zeit] [Spieler] (Grund)"));
						pp.sendMessage(new TextComponent("§6Unban: §f/unban [Spieler]"));
					} else if (args[1].equalsIgnoreCase("list")) {
						pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager Help §6= = = ="));
						pp.sendMessage(new TextComponent("§6Hilfe: §f/ban help list"));
						pp.sendMessage(new TextComponent(""));
						pp.sendMessage(new TextComponent("§6Alle gebannten Spieler: §f/ban list"));
						pp.sendMessage(new TextComponent("§6Alle Bans eines Spielers: §f/ban list (Spieler)"));
					} else {
						sendVersuch(pp);
					}
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				if (args.length == 1) {
					List<TextComponent> banlist = new ArrayList<TextComponent>();
					int i = 1;
					for (Ban ban : BanManager.bans) {
						UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(ban.getPlayerName());
						banlist.add(new TextComponent("(" + i + ") " + uuiddb.getPlayerName() + " Server: " + ban.getServer()));
						banlist.add(new TextComponent("(" + i + ") Grund: " + ban.getReason()));
						i++;
					}
					i--;
					if (i == 0) {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Es wurd noch keiner gebannt!"));
					} else {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Folgende Bans sind vorhanden:"));
						for (TextComponent tc : banlist) {
							pp.sendMessage(tc);
						}
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Es sind " + i + " Spieler gebannt!"));
					}
				} else {
					UUID PlayerUUID = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[1]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						List<Reason> reasons = MySQL.getReasons(PlayerUUID);
						if (reasons != null) {
							int i = 1;
							for (Reason reason : reasons) {
								pp.sendMessage(new TextComponent("(" + i + ") " + reason.getReason()));
								i++;
							}
							i--; 
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler wurde " + i + " mal gebannt!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler wurde noch nie gebannt!"));
						}
					} else {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				}
			} else if (args[0].equalsIgnoreCase("check")) {
				pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Ban Check gestartet!"));
				List<Ban> RemoveBans = new ArrayList<Ban>();
				for (Ban ban : BanManager.bans) {
					if (ban.getPlayerUUID() != null) {
						if (!BanManager.isPermanentBan(ban.getPlayerUUID())) {
							if (BanManager.isBanTimeOver(ban.getPlayerUUID())) {
								RemoveBans.add(ban);
							}
						}
					}
				}
				for (Ban ban : RemoveBans) {
					if (ban.getPlayerUUID() != null) {
						BanManager.RemoveBan(ban.getPlayerUUID(), pp);
					}
				}
				pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Ban Check fertig!"));
			} else if (args[0].equalsIgnoreCase("reload")) {
				pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Reload gestartet!"));
				BanManager.bans.clear();
				MySQL.Connect();
				MySQL.LadeTabellen();
				BanManager.bans = MySQL.getBans();
				pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Reload fertig!"));
			} else {
				UUID PlayerUUID = null;
				String PlayerName = null;
				UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[0]);
				if (uuiddb != null) {
					PlayerUUID = uuiddb.getPlayerUUID();
					PlayerName = uuiddb.getPlayerName();
					if (!BanManager.isPlayerBanned(PlayerUUID)) {
						UUID BannedFromPlayerUUID = pp.getUniqueId();
						String BannedFromPlayerName = pp.getName();
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
						Ban ban = new Ban(PlayerUUID, PlayerName, String.valueOf(c.getTimeInMillis()), "PERMANENT", reason, BannedFromPlayerUUID, BannedFromPlayerName, ProxyServer.getInstance().getPlayer(BannedFromPlayerUUID).getServer().getInfo().getName());
						BanManager.AddBan(ban);
					} else {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gebannt!"));
					}
				} else {
					cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
				}
			}
		} else {
			sendVersuch(pp);
		}
	}
}
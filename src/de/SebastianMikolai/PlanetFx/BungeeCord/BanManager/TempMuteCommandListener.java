package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Mute;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MuteManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempMuteCommandListener extends Command {

	public TempMuteCommandListener(String name) {
		super(name);
	}

	private void sendVersuch(ProxiedPlayer pp) {
		if (pp.hasPermission("pfx.banmanager.tban")) {
			pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager §6= = = ="));
			pp.sendMessage(new TextComponent("§6Versuche: §f/ban help"));
		} else {
			pp.sendMessage(new TextComponent(Main.getInstance().CommandNoPermissions));
		}		
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if (cs instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer) cs;
			if (args.length > 0 && pp.hasPermission("pfx.banmanager.tmute")) {
				if (args[0].equalsIgnoreCase("y")) {						
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[2]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!BanManager.isPlayerBanned(PlayerUUID)) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.YEAR, Integer.valueOf(args[1]));
							String MutetBisTime = String.valueOf(c.getTimeInMillis());
							Mute mute = new Mute(PlayerUUID, PlayerName, MutetBisTime);
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}	
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				} else if (args[0].equalsIgnoreCase("d")) {
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[2]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!BanManager.isPlayerBanned(PlayerUUID)) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.DAY_OF_YEAR, Integer.valueOf(args[1]));
							String MutetBisTime = String.valueOf(c.getTimeInMillis());
							Mute mute = new Mute(PlayerUUID, PlayerName, MutetBisTime);
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				} else if (args[0].equalsIgnoreCase("h")) {
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[2]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!BanManager.isPlayerBanned(PlayerUUID)) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.HOUR, Integer.valueOf(args[1]));
							String MutetBisTime = String.valueOf(c.getTimeInMillis());
							Mute mute = new Mute(PlayerUUID, PlayerName, MutetBisTime);
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				} else if (args[0].equalsIgnoreCase("m")) {
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[2]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!BanManager.isPlayerBanned(PlayerUUID)) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.MINUTE, Integer.valueOf(args[1]));
							String MutetBisTime = String.valueOf(c.getTimeInMillis());
							Mute mute = new Mute(PlayerUUID, PlayerName, MutetBisTime);
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				} else if (args[0].equalsIgnoreCase("s")) {
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[2]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!BanManager.isPlayerBanned(PlayerUUID)) {
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							c.add(Calendar.SECOND, Integer.valueOf(args[1]));
							String MutetBisTime = String.valueOf(c.getTimeInMillis());
							Mute mute = new Mute(PlayerUUID, PlayerName, MutetBisTime);
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				} else {
					sendVersuch(pp);
				}
			} else {
				sendVersuch(pp);
			}
		} else {
			cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.DARK_RED + "Du musst ein Spieler sein!"));
		}
	}
}
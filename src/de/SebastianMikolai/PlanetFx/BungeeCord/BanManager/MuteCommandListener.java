package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.Mute;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MuteManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.DateUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommandListener extends Command {

	public MuteCommandListener(String name) {
		super(name);
	}
	
	private void sendVersuch(ProxiedPlayer pp) {
		if (pp.hasPermission("pfx.banmanager.gban")) {
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
			if (args.length > 0 && pp.hasPermission("pfx.banmanager.mute")) {
				if (args[0].equalsIgnoreCase("help")) {
					pp.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager Help §6= = = ="));
					pp.sendMessage(new TextComponent("§6Hilfe: §f/ban help (Command)"));
					pp.sendMessage(new TextComponent(""));
					pp.sendMessage(new TextComponent("§6Mute: §f/mute [Spieler]"));
					pp.sendMessage(new TextComponent("§6TempMute: §f/tmute [y/d/h/m/s] [Zeit] [Spieler]"));
					pp.sendMessage(new TextComponent("§6Unmute: §f/unmute [Spieler]"));
					pp.sendMessage(new TextComponent("§6Alle Mutet Spieler: §f/mute list"));
					pp.sendMessage(new TextComponent("§6Alte Mutes überprüfen: §f/mute check"));
				} else if (args[0].equalsIgnoreCase("list")) {
					List<TextComponent> mutelist = new ArrayList<TextComponent>();
					int i = 1;
					for (Mute mute : MuteManager.mutes) {
						UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(mute.getPlayerName());
						mutelist.add(new TextComponent("(" + i + ") " + uuiddb.getPlayerName() + " Server: " + DateUtils.getBannedTime(Long.valueOf(mute.getMutetBisTime()))));
						i++;
					}
					i--;
					if (i == 0) {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Es wurd noch keiner gemutet!"));
					} else {
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Folgende Mutes sind vorhanden:"));
						for (TextComponent tc : mutelist) {
							pp.sendMessage(tc);
						}
						pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Es sind " + i + " Spieler gemutet!"));
					}
				} else if (args[0].equalsIgnoreCase("check")) {
					pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Mute Check gestartet!"));
					List<Mute> RemoveMutes = new ArrayList<Mute>();
					for (Mute mute : MuteManager.mutes) {
						if (mute.getPlayerUUID() != null) {
							if (!MuteManager.isPermanentMute(mute.getPlayerUUID())) {
								if (MuteManager.isMutetBisTimeOver(mute.getPlayerUUID())) {
									RemoveMutes.add(mute);
								}
							}
						}
					}
					for (Mute mute : RemoveMutes) {
						if (mute.getPlayerUUID() != null) {
							MuteManager.RemoveMute(mute.getPlayerUUID());
						}
					}
					pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Mute Check fertig!"));
				} else {
					UUID PlayerUUID = null;
					String PlayerName = null;
					UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[0]);
					if (uuiddb != null) {
						PlayerUUID = uuiddb.getPlayerUUID();
						PlayerName = uuiddb.getPlayerName();
						if (!MuteManager.isMute(PlayerUUID)) {
							Mute mute = new Mute(PlayerUUID, PlayerName, "PERMANENT");
							MuteManager.AddMute(mute);
							cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt gemutet!"));
						} else {
							pp.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist bereits gemutet!"));
						}
					} else {
						cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
					}
				}
			} else {
				sendVersuch(pp);
			}
		} else {
			cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.DARK_RED + "Du musst ein Spieler sein!"));
		}
	}
}
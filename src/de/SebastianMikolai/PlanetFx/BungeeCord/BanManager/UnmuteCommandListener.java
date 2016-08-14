package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MuteManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnmuteCommandListener extends Command {

	public UnmuteCommandListener(String name) {
		super(name);
	}
	
	private void sendVersuch(CommandSender cs) {
		if (cs.hasPermission("pfx.banmanager.unban")) {
			cs.sendMessage(new TextComponent("§6= = = = §a§lPlanet-Fx BanManager §6= = = ="));
			cs.sendMessage(new TextComponent("§6Versuche: §f/ban help"));
		} else {
			cs.sendMessage(new TextComponent(Main.getInstance().CommandNoPermissions));
		}		
	}
	
	@Override
	public void execute(CommandSender cs, String[] args) {
		if (args.length > 0 && cs.hasPermission("pfx.banmanager.unban")) {
			UUID PlayerUUID = null;
			UUIDDatenbank uuiddb = UUIDManager.getUUIDDatenbank(args[0]);
			if (uuiddb != null) {
				PlayerUUID = uuiddb.getPlayerUUID();
				if (MuteManager.isMute(PlayerUUID)) {
					MuteManager.RemoveMute(PlayerUUID);
					cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist jetzt nicht mehr gemutet!"));						
				} else {
					cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist nicht gemutet!"));
				}
			} else {
				cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
			}
		} else {
			sendVersuch(cs);
		}
	}
}
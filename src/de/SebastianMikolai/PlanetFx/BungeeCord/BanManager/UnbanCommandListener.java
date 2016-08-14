package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.util.UUID;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDDatenbank;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnbanCommandListener extends Command {

	public UnbanCommandListener(String name) {
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
				if (BanManager.isPlayerBanned(PlayerUUID)) {
					if (cs instanceof ProxiedPlayer) {
						ProxiedPlayer pp = (ProxiedPlayer) cs;
				     	BanManager.RemoveBan(PlayerUUID, pp);			
					} else {
						BanManager.RemoveBan(PlayerUUID, null);	
					}					
				} else {
					cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler ist nicht gebannt!"));
				}
			} else {
				cs.sendMessage(new TextComponent(Main.getInstance().prefix + ChatColor.GOLD + "Der Spieler war noch nie auf diesem Server!"));
			}
		} else {
			sendVersuch(cs);
		}
	}
}
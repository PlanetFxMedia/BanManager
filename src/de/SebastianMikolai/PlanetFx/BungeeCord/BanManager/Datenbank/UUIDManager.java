package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.event.LoginEvent;

public class UUIDManager {
	
	public static List<UUIDDatenbank> uuiddbs = new ArrayList<UUIDDatenbank>();
	
	public static Boolean isNewPlayer(UUID uuid) {
		Boolean bool = true;
		for (UUIDDatenbank uuiddb : uuiddbs) {
			if (uuiddb.getPlayerUUID().equals(uuid)) {
				bool = false;
			}
		}
		return bool;
	}
	
	public static UUIDDatenbank getUUIDDatenbank(String PlayerName) {
		UUIDDatenbank ReturnUUIDDatenbank = null;
		for (UUIDDatenbank uuiddb : uuiddbs) {
			if (uuiddb.getPlayerName().equals(PlayerName)) {
				ReturnUUIDDatenbank = uuiddb;
			}
		}
		return ReturnUUIDDatenbank;
	}
	
	public static void AddUUID(UUIDDatenbank uuiddb, LoginEvent e) {
		uuiddbs.add(uuiddb);
		MySQL.AddUUID(uuiddb, e);
	}
}
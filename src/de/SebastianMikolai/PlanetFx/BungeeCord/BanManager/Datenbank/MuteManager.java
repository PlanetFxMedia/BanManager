package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.DateUtils;

public class MuteManager {

	public static List<Mute> mutes = new ArrayList<Mute>();

	public static Boolean isPermanentMute(UUID uuid) {
		Boolean bool = false;
		Mute mute = getMute(uuid);
		if (mute.getMutetBisTime().equalsIgnoreCase("PERMANENT")) {
			bool = true;
		}
		return bool;
	}
	
	public static Boolean isMute(UUID uuid) {
		Boolean bool = false;
		for (Mute mute : mutes) {
			if (mute.getPlayerUUID().equals(uuid)) {
				bool = true;
			}
		}
		return bool;
	}
	
	public static Boolean isMutetBisTimeOver(UUID uuid) {
		Boolean bool = false;
		Mute ban = getMute(uuid);
		Long MutetBisTime = Long.valueOf(ban.getMutetBisTime());
		Long now = DateUtils.getTimestampNow();
		Long diff = MutetBisTime - now;
		if (diff < 0) {
			bool = true;
		}
		return bool;
	}
	
	public static Mute getMute(UUID uuid) {
		Mute ReturnMute = null;
		for (Mute mute : mutes) {
			if (mute.getPlayerUUID().equals(uuid)) {
				ReturnMute = mute;
			}
		}
		return ReturnMute;
	}
	
	public static void AddMute(Mute mute) {
		mutes.add(mute);
		MySQL.AddMute(mute);
	}
	
	public static void RemoveMute(UUID uuid) {
		Mute RemoveMute = null;
		for (Mute mute : mutes) {
			if (mute.getPlayerUUID().equals(uuid)) {
				RemoveMute = mute;
			}
		}
		mutes.remove(RemoveMute);
		MySQL.RemoveMute(RemoveMute);
	}
}
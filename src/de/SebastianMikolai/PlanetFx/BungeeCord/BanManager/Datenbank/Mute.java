package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.UUID;

public class Mute {

	final UUID PlayerUUID;
	final String PlayerName;
	final String MutetBisTime;

	public Mute(UUID PlayerUUID, String PlayerName, String MutetBisTime) {
		this.PlayerUUID = PlayerUUID;
		this.PlayerName = PlayerName;
		this.MutetBisTime = MutetBisTime;
	}

	public UUID getPlayerUUID() {
		return this.PlayerUUID;
	}
	
	public String getPlayerName() {
		return this.PlayerName;
	}

	public String getMutetBisTime() {
		return this.MutetBisTime;
	}
}
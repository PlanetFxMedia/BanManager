package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.UUID;

public class Reason {

	final UUID PlayerUUID;
	final String PlayerName;
	final String reason;

	public Reason(UUID PlayerUUID, String PlayerName, String reason) {
		this.PlayerUUID = PlayerUUID;
		this.PlayerName = PlayerName;
		this.reason = reason;
	}

	public UUID getPlayerUUID() {
		return this.PlayerUUID;
	}
	
	public String getPlayerName() {
		return this.PlayerName;
	}
	
	public String getReason() {
		return this.reason;
	}
}
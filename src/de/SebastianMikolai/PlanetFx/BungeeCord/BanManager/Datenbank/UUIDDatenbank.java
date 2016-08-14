package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.UUID;

public class UUIDDatenbank {
	
	final UUID PlayerUUID;
	final String PlayerName;

	public UUIDDatenbank(UUID PlayerUUID, String PlayerName) {
		this.PlayerUUID = PlayerUUID;
		this.PlayerName = PlayerName;
	}

	public UUID getPlayerUUID() {
		return this.PlayerUUID;
	}
	
	public String getPlayerName() {
		return this.PlayerName;
	}
}
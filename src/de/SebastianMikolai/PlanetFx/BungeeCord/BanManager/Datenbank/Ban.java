package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.util.UUID;

public class Ban {

	final UUID PlayerUUID;
	final String PlayerName;
	final String BanTime;
	final String BannedTime;
	final String reason;
	final UUID BannedFromPlayerUUID;
	final String BannedFromPlayerName;
	final String server;

	public Ban(UUID PlayerUUID, String PlayerName, String BanTime, String BannedTime, String reason, UUID BannedFromPlayerUUID, String BannedFromPlayerName, String server) {
		this.PlayerUUID = PlayerUUID;
		this.PlayerName = PlayerName;
		this.BanTime = BanTime;
		this.BannedTime = BannedTime;
		this.reason = reason;
		this.BannedFromPlayerUUID = BannedFromPlayerUUID;
		this.BannedFromPlayerName = BannedFromPlayerName;
		this.server = server;
	}

	public UUID getPlayerUUID() {
		return this.PlayerUUID;
	}
	
	public String getPlayerName() {
		return this.PlayerName;
	}

	public String getBanTime() {
		return this.BanTime;
	}

	public String getBannedTime() {
		return this.BannedTime;
	}

	public String getReason() {
		return this.reason;
	}

	public UUID getBannedFromPlayerUUID() {
		return this.BannedFromPlayerUUID;
	}
	
	public String getBannedFromPlayerName() {
		return this.BannedFromPlayerName;
	}
	
	public String getServer() {
		return this.server;
	}
}
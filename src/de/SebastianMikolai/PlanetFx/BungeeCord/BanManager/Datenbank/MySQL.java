package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.ChatUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.DateUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.UUIDUtils;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Main;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;

public class MySQL {
	
	public static Connection con;
	
	public static Connection Connect() {
		try {
			Connection con = DriverManager.getConnection("jdbc:mysql://" + Main.getInstance().database_host + ":" + 
					Main.getInstance().database_port + "/" + Main.getInstance().database_db + "?autoReconnect=true", Main.getInstance().database_user, Main.getInstance().database_password);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Connection getConnection() {
		try {
			if (con == null) {
				con = Connect();
			} else if (con.isClosed() || con.isReadOnly() || con.isValid(500)) {
				con = Connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void LadeTabellen() {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SHOW TABLES LIKE 'PlanetFxBanManager'");
			if (rss.next()) {
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManager wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE PlanetFxBanManager (id INTEGER PRIMARY KEY AUTO_INCREMENT, PlayerUUID TEXT, PlayerName TEXT, BanTime TEXT, BannedTime TEXT, reason TEXT, BannedFromPlayerUUID TEXT, BannedFromPlayerName TEXT, server TEXT)");
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManager wurde erstellt! (" + rs + ")");			
			}
			rss = stmt.executeQuery("SHOW TABLES LIKE 'PlanetFxBanManagerBanReasons'");
			if (rss.next()) {
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerBanReasons wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE PlanetFxBanManagerBanReasons (id INTEGER PRIMARY KEY AUTO_INCREMENT, PlayerUUID TEXT, PlayerName TEXT, reason TEXT)");
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerBanReasons wurde erstellt! (" + rs + ")");			
			}
			rss = stmt.executeQuery("SHOW TABLES LIKE 'PlanetFxBanManagerMute'");
			if (rss.next()) {
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerMute wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE PlanetFxBanManagerMute (id INTEGER PRIMARY KEY AUTO_INCREMENT, PlayerUUID TEXT, PlayerName TEXT, MutetBisTime TEXT)");
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerMute wurde erstellt! (" + rs + ")");			
			}
			rss = stmt.executeQuery("SHOW TABLES LIKE 'PlanetFxBanManagerUUID'");
			if (rss.next()) {
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerUUID wurde geladen!");
			} else {
				int rs = stmt.executeUpdate("CREATE TABLE PlanetFxBanManagerUUID (id INTEGER PRIMARY KEY AUTO_INCREMENT, PlayerUUID TEXT, PlayerName TEXT)");
				Main.getInstance().getLogger().info("Die Tabelle PlanetFxBanManagerUUID wurde erstellt! (" + rs + ")");			
			}
		} catch (SQLException e) {
			e.printStackTrace();
			Main.getInstance().getLogger().info("ERROR #02 LadeTabellen: Die Tabellen konnten nicht geladen werden!");
		}
	}
	
	public static void AddUUID(UUIDDatenbank uuiddb, LoginEvent event) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			String uuid = UUIDUtils.UUIDtoString(uuiddb.getPlayerUUID());
			String name = uuiddb.getPlayerName();
			BungeeCord.getInstance().getLogger().log(Level.WARNING, name);
			BungeeCord.getInstance().getLogger().log(Level.WARNING, uuiddb.getPlayerUUID().toString());
			BungeeCord.getInstance().getLogger().log(Level.WARNING, String.valueOf(uuiddb.getPlayerUUID().version()));
			if (stmt.isClosed()) {
				c = Connect();
				stmt = c.createStatement();
			}
			boolean bool = stmt.execute("INSERT INTO PlanetFxBanManagerUUID (PlayerUUID, PlayerName) VALUES ('" + uuid +  "', '" + name + "')");
			Main.getInstance().getLogger().info("Neuer Spieler " + uuiddb.PlayerName + " - " + uuiddb.PlayerUUID + ", " + bool + ")");
		} catch (SQLException e) {
			event.setCancelReason("&cBeim anmelden ist ein Fehler entstanden");
			event.setCancelled(true);
			Main.getInstance().getLogger().info("ERROR #03 AddUUID: Der Datensatz konnte nicht eingetragen werden!");
			e.printStackTrace();
		}
	}
	
	public static List<UUIDDatenbank> getUUIDs() {
		List<UUIDDatenbank> uuiddb = new ArrayList<UUIDDatenbank>();
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM PlanetFxBanManagerUUID");
			while (rss.next()) {
				uuiddb.add(new UUIDDatenbank(UUID.fromString(rss.getString("PlayerUUID")), rss.getString("PlayerName")));
			}
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #05 getUUIDs: Die Einträge konnten nicht geladen werden!");
			e.printStackTrace();
		}
		return uuiddb;
	}
	
	public static void AddBan(Ban ban) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			boolean bool = stmt.execute("INSERT INTO PlanetFxBanManager (PlayerUUID, PlayerName, BanTime, BannedTime, reason, BannedFromPlayerUUID, BannedFromPlayerName, server) VALUES ('" + UUIDUtils.UUIDtoString(ban.getPlayerUUID()) +  "', '" + ban.getPlayerName() + "', '" + ban.getBanTime() + "', '" + ban.getBannedTime() + "', '" + ban.getReason() + "', '" + UUIDUtils.UUIDtoString(ban.getBannedFromPlayerUUID()) + "', '" + ban.getBannedFromPlayerName() + "', '" + ban.getServer() + "')");
			Main.getInstance().getLogger().info("Spieler " + ban.getPlayerName() + "wurde gebannt! (" + ban.getReason() + ", " + bool + ")");
			if (ban.getServer() == "GLOBAL") {
				if (ban.getBannedTime().equalsIgnoreCase("PERMANENT") == true) {
					String broadcastmessage = Main.getInstance().BroadcastMessageGlobal;
					broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedFromPlayerName>", ban.getBannedFromPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedTime>", ban.getBannedTime().toLowerCase());
					String broadcastreason = Main.getInstance().BroadcastMessageBanReason;
					broadcastreason = broadcastreason.replace("<getReason>", ban.getReason());
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastreason));
				} else {
					String broadcastmessage = Main.getInstance().BroadcastMessageGlobalTemp;
					broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedFromPlayerName>", ban.getBannedFromPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
					String broadcastreason = Main.getInstance().BroadcastMessageBanReason;
					broadcastreason = broadcastreason.replace("<getReason>", ban.getReason());
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastreason));
				}
			} else {
				if (ban.getBannedTime().equalsIgnoreCase("PERMANENT") == true) {
					String broadcastmessage = Main.getInstance().BroadcastMessage;
					broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedFromPlayerName>", ban.getBannedFromPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedTime>", ban.getBannedTime().toLowerCase());
					broadcastmessage = broadcastmessage.replace("<getServer>", ban.getServer());
					String broadcastreason = Main.getInstance().BroadcastMessageBanReason;
					broadcastreason = broadcastreason.replace("<getReason>", ban.getReason());
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastreason));
				} else {
					String broadcastmessage = Main.getInstance().BroadcastMessageTemp;
					broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedFromPlayerName>", ban.getBannedFromPlayerName());
					broadcastmessage = broadcastmessage.replace("<getBannedTime>", DateUtils.getBannedTime(Long.valueOf(ban.getBannedTime())));
					broadcastmessage = broadcastmessage.replace("<getServer>", ban.getServer());
					String broadcastreason = Main.getInstance().BroadcastMessageBanReason;
					broadcastreason = broadcastreason.replace("<getReason>", ban.getReason());
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
					ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastreason));
				}
			}			
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #03 AddBan: Der Datensatz konnte nicht eingetragen werden!");
			e.printStackTrace();
		}
	}
	
	public static void AddReason(Reason reason) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			boolean bool = stmt.execute("INSERT INTO PlanetFxBanManagerBanReasons (PlayerUUID, PlayerName, reason) VALUES ('" + UUIDUtils.UUIDtoString(reason.getPlayerUUID()) +  "', '" + reason.getPlayerName() + "', '" + reason.getReason() + "')");
			Main.getInstance().getLogger().info("New Reason (" + reason.getReason() + ", " + bool + ")");	
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #03 AddReason: Der Datensatz konnte nicht eingetragen werden!");
			e.printStackTrace();
		}
	}
	
	public static void AddMute(Mute mute) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			boolean bool = stmt.execute("INSERT INTO PlanetFxBanManagerMute (PlayerUUID, PlayerName, MutetBisTime) VALUES ('" + UUIDUtils.UUIDtoString(mute.getPlayerUUID()) +  "', '" + mute.getPlayerName() + "', '" + mute.getMutetBisTime() + "')");
			Main.getInstance().getLogger().info("New Mute (" + mute.getPlayerName() + ", " + bool + ")");	
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #03 AddMute: Der Datensatz konnte nicht eingetragen werden!");
			e.printStackTrace();
		}
	}
	
	public static void RemoveMute(Mute mute) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM PlanetFxBanManagerMute WHERE PlayerUUID='" + UUIDUtils.UUIDtoString(mute.getPlayerUUID()) + "'");
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #04 RemoveMute: Der Eintrag konnte nicht geladen werden!");
			e.printStackTrace();
		}
	}
	
	public static List<Mute> getMutes() {
		List<Mute> mutes = new ArrayList<Mute>();
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM PlanetFxBanManagerMute");
			while (rss.next()) {
				mutes.add(new Mute(UUID.fromString(rss.getString("PlayerUUID")), rss.getString("PlayerName"), rss.getString("MutetBisTime")));
			}
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #05 getBans: Die Einträge konnten nicht geladen werden!");
			e.printStackTrace();
		}
		return mutes;
	}
	
	public static List<Reason> getReasons(UUID uuid) {
		List<Reason> reasons = new ArrayList<Reason>();
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM PlanetFxBanManagerBanReasons WHERE PlayerUUID='" + UUIDUtils.UUIDtoString(uuid) + "'");
			while (rss.next()) {
				reasons.add(new Reason(UUID.fromString(rss.getString("PlayerUUID")), rss.getString("PlayerName"), rss.getString("reason")));
			}
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #05 getReasons: Die Einträge konnten nicht geladen werden!");
			e.printStackTrace();
		}
		return reasons;
	}
	
	public static void Unban(Ban ban, ProxiedPlayer pp) {
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			stmt.execute("DELETE FROM PlanetFxBanManager WHERE PlayerUUID='" + UUIDUtils.UUIDtoString(ban.getPlayerUUID()) + "'");
			if (pp != null) {
				String broadcastmessage = Main.getInstance().BroadcastUnbanFromAdmin;
				broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
				broadcastmessage = broadcastmessage.replace("<getName>", pp.getName());
				ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
			} else {
				String broadcastmessage = Main.getInstance().BroadcastUnban;
				broadcastmessage = broadcastmessage.replace("<getPlayerName>", ban.getPlayerName());
				ChatUtils.BroadcastChat(ChatUtils.ConvertColorCodes(broadcastmessage));
			}
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #04 Unban: Der Eintrag konnte nicht geladen werden!");
			e.printStackTrace();
		}
	}
	
	public static List<Ban> getBans() {
		List<Ban> bans = new ArrayList<Ban>();
		try {
			Connection c = getConnection();
			Statement stmt = c.createStatement();
			ResultSet rss = stmt.executeQuery("SELECT * FROM PlanetFxBanManager");
			while (rss.next()) {
				bans.add(new Ban(UUID.fromString(rss.getString("PlayerUUID")), rss.getString("PlayerName"), rss.getString("BanTime"), rss.getString("BannedTime"), rss.getString("reason"), UUID.fromString(rss.getString("BannedFromPlayerUUID")), rss.getString("BannedFromPlayerName"), rss.getString("server")));
			}
		} catch (SQLException e) {
			Main.getInstance().getLogger().info("ERROR #05 getBans: Die Einträge konnten nicht geladen werden!");
			e.printStackTrace();
		}
		return bans;
	}
}
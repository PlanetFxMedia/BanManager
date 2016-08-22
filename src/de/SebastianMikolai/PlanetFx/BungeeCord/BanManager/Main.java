package de.SebastianMikolai.PlanetFx.BungeeCord.BanManager;

import java.io.File;
import java.io.IOException;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.BanManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MuteManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.MySQL;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Datenbank.UUIDManager;
import de.SebastianMikolai.PlanetFx.BungeeCord.BanManager.Utils.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {

	public static Main instance;
	public String database_host;
	public int database_port;
	public String database_db;
	public String database_user;
	public String database_password;
	public String DefaultBanMessage;
	public String CommandNoPermissions;
	public String KickMessageOnJoinGlobal;
	public String KickMessageOnJoinGlobalTemp;
	public String KickMessageOnJoin;
	public String KickMessageOnJoinTemp;
	public String BroadcastMessageBanReason;
	public String BroadcastMessageGlobal;
	public String BroadcastMessageGlobalTemp;
	public String BroadcastMessage;
	public String BroadcastMessageTemp;
	public String BroadcastUnbanFromAdmin;
	public String BroadcastUnban;
	public String MuteMessage;
	public String MuteTempMessage;
	public String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "PlanetFx" + ChatColor.DARK_GRAY + "] ";

	public static Main getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		SaveConfig();
		LoadConfig();
		MySQL.LadeTabellen();
		BanManager.bans = MySQL.getBans();
		UUIDManager.uuiddbs = MySQL.getUUIDs();
		MuteManager.mutes = MySQL.getMutes();
		PluginManager pm = ProxyServer.getInstance().getPluginManager();
		pm.registerCommand(instance, new BanCommandListener("ban"));
		pm.registerCommand(instance, new TempBanCommandListener("tban"));
		pm.registerCommand(instance, new GlobalBanCommandListener("gban"));
		pm.registerCommand(instance, new GlobalTempBanCommandListener("gtban"));
		pm.registerCommand(instance, new UnbanCommandListener("unban"));
		pm.registerCommand(instance, new MuteCommandListener("mute"));
		pm.registerCommand(instance, new TempMuteCommandListener("tmute"));
		pm.registerCommand(instance, new UnmuteCommandListener("unmute"));
		pm.registerListener(instance, new EventListener());
	}
	
	public void SaveConfig() {
		try {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File fconfig = new File(getDataFolder().getPath(), "config.yml");
			if (!fconfig.exists()) {
				fconfig.createNewFile();
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
				config.set("version", getDescription().getVersion());
				config.set("database.host", "mysql.centrohost.de");
				config.set("database.port", Integer.valueOf(3306));
				config.set("database.db", "100006");
				config.set("database.user", "CH1000022");
				config.set("database.password", "z0LTy3j2Sn");
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, fconfig);
			}
			File fmessages = new File(getDataFolder().getPath(), "messages.yml");
			if (!fmessages.exists()) {
				fmessages.createNewFile();
				Configuration messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fmessages);
				messages.set("DefaultBanMessage", "Vom Admin gebannt!");
				messages.set("CommandNoPermissions", "&cDu hast dafür keine Rechte #Owner");
				messages.set("KickMessageOnJoinGlobal", "&cDu bist global &4<getBannedTime> &cvom Server gebannt. &bGrund: &c<getReason>");
				messages.set("KickMessageOnJoinGlobalTemp", "&cDu bist global bis &4<getBannedTime> &cvom Server gebannt. &bGrund: &c<getReason>");
				messages.set("KickMessageOnJoin", "&cDu bist &4<getBannedTime> &cvom Server <getServer> gebannt. &bGrund: &c<getReason>");
				messages.set("KickMessageOnJoinTemp", "&cDu bist bis &4<getBannedTime> &cvom Server <getServer> gebannt. &bGrund: &c<getReason>");
				messages.set("BroadcastMessageGlobal", "&c<getPlayerName> wurde von <getBannedFromPlayerName> <getBannedTime> gebannt!");
				messages.set("BroadcastMessageGlobalTemp", "&c<getPlayerName> wurde von <getBannedFromPlayerName> temporär gebannt!");
				messages.set("BroadcastMessage", "&c<getPlayerName> wurde von <getBannedFromPlayerName> <getBannedTime> vom Server gebannt!");
				messages.set("BroadcastMessageTemp", "&c<getPlayerName> wurde von <getBannedFromPlayerName> temporär vom Server gebannt!");
				messages.set("BroadcastUnbanFromAdmin", "&a<getPlayerName> wurde von <getName> entsperrt!");
				messages.set("BroadcastUnban", "&a<getPlayerName> wurde entsperrt!");
				messages.set("BroadcastMessageBanReason", "&cGrund: <getReason>");
				messages.set("MuteMessage", "&cDu bist <getMutetBisTime> gemutet!");
				messages.set("MuteTempMessage", "&cDu bist bis <getMutetBisTime> gemutet!");			
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(messages, fmessages);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void LoadConfig() {
		try {
			File fconfig = new File(getDataFolder().getPath(), "config.yml");
			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fconfig);
			database_host = config.getString("database.host");
			database_port = config.getInt("database.port");
			database_db = config.getString("database.db");
			database_user = config.getString("database.user");
			database_password = config.getString("database.password");
			File fmessages = new File(getDataFolder().getPath(), "messages.yml");
			Configuration messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(fmessages);
			DefaultBanMessage = messages.getString("DefaultBanMessage");
			CommandNoPermissions = ChatUtils.ConvertColorCodes(messages.getString("CommandNoPermissions"));
			KickMessageOnJoinGlobal = messages.getString("KickMessageOnJoinGlobal");
			KickMessageOnJoinGlobalTemp = messages.getString("KickMessageOnJoinGlobalTemp");
			KickMessageOnJoin = messages.getString("KickMessageOnJoin");
			KickMessageOnJoinTemp = messages.getString("KickMessageOnJoinTemp");
			BroadcastMessageGlobal = messages.getString("BroadcastMessageGlobal");
			BroadcastMessageGlobalTemp = messages.getString("BroadcastMessageGlobalTemp");
			BroadcastMessage = messages.getString("BroadcastMessage");
			BroadcastMessageTemp = messages.getString("BroadcastMessageTemp");
			BroadcastUnbanFromAdmin = messages.getString("BroadcastUnbanFromAdmin");
			BroadcastUnban = messages.getString("BroadcastUnban");
			BroadcastMessageBanReason = messages.getString("BroadcastMessageBanReason");
			MuteMessage = messages.getString("MuteMessage");
			MuteTempMessage = messages.getString("MuteTempMessage");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
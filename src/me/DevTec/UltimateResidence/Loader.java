package me.DevTec.UltimateResidence;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.Straiker123.ConfigAPI;

public class Loader extends JavaPlugin {
	public static boolean loaded;
	public static HashMap<String,Residence> map = new HashMap<String,Residence>();
	public static ConfigAPI c = new ConfigAPI("UltimateResidence", "Config");
	public static ResidenceAPI api;
	public static HashMap<World, ConfigAPI> a = new HashMap<World, ConfigAPI>();
	public void onEnable() {
		for(World w : Bukkit.getWorlds()) {
			ConfigAPI config = new ConfigAPI("UltimateResidence/Data",w.getName());
			config.create();
			a.put(w, config);
		}
		api=new ResidenceAPI();
		api.load();
		Bukkit.getPluginCommand("UltimateResidence").setExecutor(new URCMD());
		Bukkit.getPluginManager().registerEvents(new ResEvents(), this);
		
	}
	public void onDisable() {
		c.reload();
	}
	public static ConfigAPI getData(World world) {
		return a.get(world);
	}
}

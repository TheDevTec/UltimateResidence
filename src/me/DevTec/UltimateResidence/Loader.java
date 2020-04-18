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
	public static ConfigAPI g = new ConfigAPI("UltimateResidence","Groups");
	public void onEnable() {
		g.addDefault("Groups.default.Residences", 5);
		g.addDefault("Groups.default.SubResidences", 3);
		g.addDefault("Groups.default.Size", "50x50");

		g.addDefault("Groups.builder.Residences", 50);
		g.addDefault("Groups.builder.SubResidences", 50);
		g.addDefault("Groups.builder.Size", "500x500");

		g.addDefault("Groups.admin.Residences", 20);
		g.addDefault("Groups.admin.SubResidences", 20);
		g.addDefault("Groups.admin.Size", "200x200");
		
		g.setHeader("residence.group.<group> to get access for group");
		g.create();
		c.create();
		api=new ResidenceAPI();
		api.load();
		Bukkit.getPluginCommand("UltimateResidence").setExecutor(new URCMD());
		Bukkit.getPluginManager().registerEvents(new ResEvents(), this);
		
	}
	public void onDisable() {
		c.reload();
		g.reload();
		api.unload();
	}
	
	public static ConfigAPI getData(World world) {
		if(a.containsKey(world)) {
		return a.get(world);
		}else {
			ConfigAPI config = new ConfigAPI("UltimateResidence/Data",world.getName());
			config.create();
			a.put(world, config);
			return config;
		}
	}
}

package me.DevTec.UltimateResidence;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.DevTec.UltimateResidence.Events.ResidenceEnterEvent;
import me.DevTec.UltimateResidence.Events.ResidenceLeaveEvent;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;
import me.Straiker123.TheRunnable;

public class Loader extends JavaPlugin {
	public static boolean loaded;
	public static HashMap<String,Residence> map = new HashMap<String,Residence>();
	public static ConfigAPI c = new ConfigAPI("UtlimateResidence", "Config");
	public static ResidenceAPI api;
	public static TheRunnable r=new TheRunnable();
	public static HashMap<World, ConfigAPI> a = new HashMap<World, ConfigAPI>();
	public void onEnable() {
		for(World w : Bukkit.getWorlds()) {
			ConfigAPI config = new ConfigAPI("UtlimateResidence/Data",w.getName());
			config.create();
			a.put(w, config);
		}
		api=new ResidenceAPI();
		api.load();
		Bukkit.getPluginCommand("UltimateResidence").setExecutor(new URCMD());
		Bukkit.getPluginManager().registerEvents(new ResEvents(), this);
		r.runRepeating(new Runnable() {
			HashMap<Player, Residence> in = new HashMap<Player, Residence>();
			@SuppressWarnings("deprecation")
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					Residence r = ResidenceAPI.getResidence(p);
					if(r!=null) {
						if(in.containsKey(p) && !in.get(p).equals(r)) {
							ResidenceEnterEvent e = new ResidenceEnterEvent(r,p);
							Bukkit.getPluginManager().callEvent(e);
							if(e.getTitle()!=null) {
								p.sendTitle(TheAPI.colorize(e.getTitle()[0]), TheAPI.colorize(e.getTitle()[1]));
							}
							in.put(p, r);
						}else
							if(!in.containsKey(p)) {
								ResidenceEnterEvent e = new ResidenceEnterEvent(r,p);
								Bukkit.getPluginManager().callEvent(e);
								if(e.getTitle()!=null) {
									p.sendTitle(TheAPI.colorize(e.getTitle()[0]), TheAPI.colorize(e.getTitle()[1]));
								}
						in.put(p, r);
							}
						for(ResidenceFlag f : r.getFlags()) {
							switch(f.getType()) {
							case HEAL:
								if(p.getHealth() !=p.getMaxHealth())
								p.setHealth((p.getHealth()+2) > p.getMaxHealth() ? p.getHealth()+2 : p.getMaxHealth());
								break;
							case FEED:
								if(p.getFoodLevel() !=20)
								p.setFoodLevel((p.getFoodLevel()+2) > 20 ? p.getFoodLevel()+2 : 20);
								break;
								default:
									break;
							}
						}
					}else {
						if(in.containsKey(p)) {
						ResidenceLeaveEvent e = new ResidenceLeaveEvent(in.get(p),p);
						Bukkit.getPluginManager().callEvent(e);
						if(e.getTitle()!=null) {
							p.sendTitle(TheAPI.colorize(e.getTitle()[0]), TheAPI.colorize(e.getTitle()[1]));
						}
						in.remove(p);
						}
					}
				}
			}
		}, 40);
		
	}
	public void onDisable() {
		r.cancel();
		c.reload();
	}
	public static ConfigAPI getData(World world) {
		return a.get(world);
	}
}

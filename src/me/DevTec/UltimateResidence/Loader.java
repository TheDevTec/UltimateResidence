package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.ConfigAPI.ConfigAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.DevTec.UltimateResidence.Commands.URCMD;
import me.DevTec.UltimateResidence.Utils.ResEvents;

public class Loader extends JavaPlugin {
	public static Loader a;
	public static boolean loaded;
	public static ConfigAPI c = new ConfigAPI("UltimateResidence", "Config");
	public static ConfigAPI g = new ConfigAPI("UltimateResidence","Groups");
	public static HashMap<World, ConfigAPI> map = new HashMap<World, ConfigAPI>();
	public static ArrayList<String> debugging = Lists.newArrayList();
	public void onEnable() {
		a=this;
		g.addDefault("Groups.default.Residences", 5);
		g.addDefault("Groups.default.SubResidences", 3);
		g.addDefault("Groups.default.Size", "50x50");
		
		g.addDefault("Groups.default.Title.Use", false);
		g.addDefault("Groups.default.Title.Enter.Line1", "&7Welcome in residence");
		g.addDefault("Groups.default.Title.Enter.Line2", "&a%residence&7, owned by &a%owner");

		g.addDefault("Groups.default.Title.Leave.Line1", "&7Leaving residence");
		g.addDefault("Groups.default.Title.Leave.Line2", "&a%residence");

		g.addDefault("Groups.default.ActionBar.Use", true);
		g.addDefault("Groups.default.ActionBar.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.default.ActionBar.Leave", "&7Leaving residence &a%residence");

		g.addDefault("Groups.default.Chat.Use", false);
		g.addDefault("Groups.default.Chat.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.default.Chat.Leave", "&7Leaving residence &a%residence");

		g.addDefault("Groups.builder.Residences", 50);
		g.addDefault("Groups.builder.SubResidences", 50);
		g.addDefault("Groups.builder.Size", "500x500");
		g.addDefault("Groups.builder.Title.Use", false);
		g.addDefault("Groups.builder.Title.Enter.Line1", "&7Welcome in residence");
		g.addDefault("Groups.builder.Title.Enter.Line2", "&a%residence&7, owned by &a%owner");

		g.addDefault("Groups.builder.Title.Leave.Line1", "&7Leaving residence");
		g.addDefault("Groups.builder.Title.Leave.Line2", "&a%residence");

		g.addDefault("Groups.builder.ActionBar.Use", true);
		g.addDefault("Groups.builder.ActionBar.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.builder.ActionBar.Leave", "&7Leaving residence &a%residence");

		g.addDefault("Groups.builder.Chat.Use", false);
		g.addDefault("Groups.builder.Chat.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.builder.Chat.Leave", "&7Leaving residence &a%residence");
		
		g.addDefault("Groups.admin.Residences", 20);
		g.addDefault("Groups.admin.SubResidences", 20);
		g.addDefault("Groups.admin.Size", "200x200");
		g.addDefault("Groups.admin.Title.Use", false);
		g.addDefault("Groups.admin.Title.Enter.Line1", "&7Welcome in residence");
		g.addDefault("Groups.admin.Title.Enter.Line2", "&a%residence&7, owned by &a%owner");

		g.addDefault("Groups.admin.Title.Leave.Line1", "&7Leaving residence");
		g.addDefault("Groups.admin.Title.Leave.Line2", "&a%residence");

		g.addDefault("Groups.admin.ActionBar.Use", true);
		g.addDefault("Groups.admin.ActionBar.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.admin.ActionBar.Leave", "&7Leaving residence &a%residence");

		g.addDefault("Groups.admin.Chat.Use", false);
		g.addDefault("Groups.admin.Chat.Enter", "&7Welcome &a%player &7in residence &a%residence&7, owned by: &a%owner");
		g.addDefault("Groups.admin.Chat.Leave", "&7Leaving residence &a%residence");
		
		g.setHeader("residence.group.<group> to get access for group & Required relog of player to apply new group");
		g.create();
		c.addDefault("ShowNoPermsMsg", true);
		c.create();
				
		Bukkit.getPluginCommand("UltimateResidence").setExecutor(new URCMD());
		Bukkit.getPluginManager().registerEvents(new ResEvents(), this);
		new Tasker() {
			public void run() {
				try {
				for(Player p : TheAPI.getOnlinePlayers()) {
					if(p.getGameMode() == GameMode.CREATIVE)continue;
					Residence r = API.getResidence(p);
					if(r==null)continue;
					Subzone z = r.getSubzone(p);
				if(z!=null) {
						if(z.getFlag(Flag.HEAL)||z.getPlayerFlag(Flag.HEAL,p.getName())) {
							@SuppressWarnings("deprecation")
							double max = p.getMaxHealth();
							if(p.getHealth() !=max)
							p.setHealth((p.getHealth()+2) <max ? p.getHealth()+2 :max);
					}
					if(z.getFlag(Flag.FEED)||z.getPlayerFlag(Flag.FEED,p.getName())) {
							if(p.getFoodLevel() !=20)
							p.setFoodLevel((p.getFoodLevel()+2) < 20 ? p.getFoodLevel()+2 : 20);
						}
					return;
				}
		if(r.getFlag(Flag.HEAL)||r.getPlayerFlag(Flag.HEAL,p.getName())) {
			@SuppressWarnings("deprecation")
			double max = p.getMaxHealth();
				if(p.getHealth() !=max)
				p.setHealth((p.getHealth()+2) <max ? p.getHealth()+2 :max);
		}
		if(r.getFlag(Flag.FEED)||r.getPlayerFlag(Flag.FEED,p.getName())) {
				if(p.getFoodLevel() !=20)
				p.setFoodLevel((p.getFoodLevel()+2) < 20 ? p.getFoodLevel()+2 : 20);
			}}}catch(Exception er) {}
			}
		}.repeatingAsync(0,10);
	}
	
	public void onDisable() {
		API.reload();
	}
	
	public static ConfigAPI getData(World world) {
		if(world==null)return null;
		if(!map.containsKey(world)) {
		ConfigAPI config = new ConfigAPI("UltimateResidence/Data",world.getName());
		config.create();
		map.put(world, config);
		}
		return map.get(world);
	}
}

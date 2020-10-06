package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.Reflections.Ref;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.DevTec.UltimateResidence.Commands.URCMD;
import me.DevTec.UltimateResidence.Events.ResidenceEnterEvent;
import me.DevTec.UltimateResidence.Events.ResidenceLeaveEvent;
import me.DevTec.UltimateResidence.Events.ResidenceSwitchEvent;
import me.DevTec.UltimateResidence.Events.SubzoneEnterEvent;
import me.DevTec.UltimateResidence.Events.SubzoneLeaveEvent;
import me.DevTec.UltimateResidence.Events.SubzoneSwitchEvent;
import me.DevTec.UltimateResidence.Utils.ResEvents;
import me.DevTec.UltimateResidence.Utils.ad;

public class Loader extends JavaPlugin {
	public static Loader a;
	public static boolean loaded;
	public static Config c = new Config("UltimateResidence/Config.yml");
	public static Config g = new Config("UltimateResidence/Groups.yml");
	public static WeakHashMap<World, Config> map = new WeakHashMap<>();
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
		
		g.getData().setHeader(Arrays.asList("residence.group.<group> to get access for group","Required relog of player to apply new group"));
		c.addDefault("ShowNoPermsMsg", true);
				
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
		}.runRepeating(0,10);
		new Tasker() {
			HashMap<String, Location> from = new HashMap<>();
			HashMap<String, Residence> in =new HashMap<>();
			HashMap<String, Subzone> inz =new HashMap<>();
			public void run() {
				try {
				for(Player p : TheAPI.getOnlinePlayers()) {
					Location from = this.from.getOrDefault(p.getName(), p.getLocation()), to = p.getLocation();
					boolean cancel = false;
					    			Residence r = API.getResidence(new Position(to));
					    			Subzone z = r != null ? r.getSubzone(p): null;
					    			if(r!=null) {
					    				if(in.containsKey(p.getName()) && in.get(p.getName()).equals(r)) { //in residence
					    					if(inz.containsKey(p.getName()) && z != null && !inz.get(p.getName()).equals(z)) { //switch subzone
					    						SubzoneSwitchEvent es = new SubzoneSwitchEvent(z,p);
					    						TheAPI.callEvent(es);
					    								if(!ad.has(p,"residence.admin") && !z.getPlayerFlag(Flag.MOVE,p.getName()) && !z.getFlag(Flag.MOVE))
					    									cancel=true;
					    								else {
					    								inz.put(p.getName(), z);
					    								if(es.getTitle()!=null)
					    									TheAPI.sendTitle(p,es.getTitle()[0].replace("%player", p.getName()).replace("%residence", z.getName())
					    											.replace("%owner", z.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", z.getName())
					    											.replace("%owner", z.getOwner()));
					    								if(es.getActionBar()!=null) 
					    									TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", z.getName())
					    											.replace("%owner", z.getOwner()));
					    								if(es.getChat()!=null) 
					    									TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", z.getName())
					    											.replace("%owner", z.getOwner()), p);
					    								}
					    					}else if(inz.containsKey(p.getName()) && z == null) { //leave subzone
					    						SubzoneLeaveEvent es = new SubzoneLeaveEvent(inz.get(p.getName()),new Position(from),p);
					    						inz.remove(p.getName());
					    						TheAPI.callEvent(es);
					    						String group = API.getData(p.getName()).getGroup().getName();
					    						if(Loader.g.getBoolean("Groups."+group+".Chat.Use")) {
					    							TheAPI.msg(Loader.g.getString("Groups."+group+".Chat.Enter").replace("%player", p.getName())
					    									.replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()),p);
					    						}
					    						if(Loader.g.getBoolean("Groups."+group+".Title.Use")) {
					    							TheAPI.sendTitle(p, Loader.g.getString("Groups."+group+".Title.Enter.Line1")
					    									.replace("%player", p.getName())
					    									.replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()),Loader.g.getString("Groups."+group+".Title.Enter.Line2")
					    									.replace("%player", p.getName())
					    									.replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()));
					    						}
					    						if(Loader.g.getBoolean("Groups."+group+".ActionBar.Use")) {
					    							TheAPI.sendActionBar(p, Loader.g.getString("Groups."+group+".ActionBar.Enter").replace("%player", p.getName())
					    									.replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()));
					    						}
					    					}else if(!inz.containsKey(p.getName()) && z!=null) { //enter subzone
					    						SubzoneEnterEvent es = new SubzoneEnterEvent(z,new Position(from),p);
					    						TheAPI.callEvent(es);
				    								if(!ad.has(p,"residence.admin")&&!z.getPlayerFlag(Flag.MOVE,p.getName()) && !z.getFlag(Flag.MOVE))
				    									cancel=true;
				    								else {
				    									inz.put(p.getName(), z);
					    						if(es.getTitle()!=null)
					    							TheAPI.sendTitle(p,es.getTitle()[0].replace("%player", p.getName()).replace("%residence", z.getName())
					    									.replace("%owner", z.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", z.getName())
					    											.replace("%owner", z.getOwner()));
					    						if(es.getActionBar()!=null) 
					    							TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", z.getName())
					    									.replace("%owner", z.getOwner()));
					    						if(es.getChat()!=null) 
					    							TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", z.getName())
					    									.replace("%owner", z.getOwner()), p);
					    					}}
					    				}else
					    				if(in.containsKey(p.getName()) && !in.get(p.getName()).equals(r)) { //switch residence
					    					ResidenceSwitchEvent es = new ResidenceSwitchEvent(r,p);
					    					TheAPI.callEvent(es);
			    								if(!ad.has(p,"residence.admin")&&!r.getPlayerFlag(Flag.MOVE,p.getName()) && !r.getFlag(Flag.MOVE))
			    									cancel=true;
			    								else {
					    					in.put(p.getName(), r);
					    					if(es.getTitle()!=null)
					    						TheAPI.sendTitle(p,es.getTitle()[0].replace("%player", p.getName()).replace("%residence", r.getName())
					    								.replace("%owner", r.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", r.getName())
					    										.replace("%owner", r.getOwner()));
					    					if(es.getActionBar()!=null) 
					    						TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", r.getName())
					    								.replace("%owner", r.getOwner()));
					    					if(es.getChat()!=null) 
					    						TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", r.getName())
					    								.replace("%owner", r.getOwner()), p);
			    								}}else
					    					if(!in.containsKey(p.getName())) { //enter residence
					    						ResidenceEnterEvent es = new ResidenceEnterEvent(r,p);
					    						TheAPI.callEvent(es);
				    								if(!ad.has(p,"residence.admin")&&!r.getPlayerFlag(Flag.MOVE,p.getName()) && !r.getFlag(Flag.MOVE))
				    									cancel=true;
				    								else {
				    									in.put(p.getName(), r);
					    						if(es.getTitle()!=null)
					    							TheAPI.sendTitle(p,es.getTitle()[0].replace("%player", p.getName()).replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()),es.getTitle()[1].replace("%player", p.getName()).replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()));
					    						if(es.getActionBar()!=null) 
					    							TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()));
					    						if(es.getChat()!=null) 
					    							TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", r.getName())
					    									.replace("%owner", r.getOwner()), p);
				    								}
					    					}
					    			}else { //leave residence
					    				if(in.containsKey(p.getName()) && r==null) {
					    				ResidenceLeaveEvent es = new ResidenceLeaveEvent(in.get(p.getName()),from,p);
					    				TheAPI.callEvent(es);
					    				Residence rs = in.get(p.getName());
					    				if(inz.containsKey(p.getName()) && z == null) { //leave subzone
					    					SubzoneLeaveEvent ess = new SubzoneLeaveEvent(inz.get(p.getName()),new Position(from),p);
					    					TheAPI.callEvent(ess);
					    					Subzone rr = inz.get(p.getName());
					    					inz.remove(p.getName());
					    					if(ess.getTitle()!=null)
					    						TheAPI.sendTitle(p,ess.getTitle()[0].replace("%player", p.getName()).replace("%residence", rr.getName())
					    								.replace("%owner", rr.getOwner()), ess.getTitle()[1].replace("%player", p.getName()).replace("%residence", rr.getName())
					    								.replace("%owner", rr.getOwner()));
					    					if(ess.getActionBar()!=null) 
					    						TheAPI.sendActionBar(p, ess.getActionBar().replace("%player", p.getName()).replace("%residence", rr.getName())
					    								.replace("%owner", rr.getOwner()));
					    					if(ess.getChat()!=null) 
					    						TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", rr.getName())
					    								.replace("%owner", rr.getOwner()), p);
					    				}
					    				in.remove(p.getName());
					    				if(es.getTitle()!=null)
					    					TheAPI.sendTitle(p,es.getTitle()[0].replace("%player", p.getName()).replace("%residence", rs.getName())
					    							.replace("%owner", rs.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", rs.getName())
					    							.replace("%owner", rs.getOwner()));
					    				if(es.getActionBar()!=null) 
					    					TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", rs.getName())
					    							.replace("%owner", rs.getOwner()));
					    				if(es.getChat()!=null) 
					    					TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", rs.getName())
					    							.replace("%owner", rs.getOwner()), p);
					    				}}
					if(cancel)
						Ref.invoke(Ref.playerCon(p),"teleport",from);
					this.from.put(p.getName(), p.getLocation());
				}}catch(Exception er) {
					TheAPI.msg(er.getStackTrace()[0].toString(), TheAPI.getConsole());
				}
			}
		}.runRepeating(0,3);
	}
	
	public void onDisable() {
		API.reload();
	}
	
	public static Config getData(World world) {
		if(world==null)return null;
		if(!map.containsKey(world)) {
			Config config = new Config("UltimateResidence/Data/"+world.getName()+".yml");
			map.put(world, config);
		}
		return map.get(world);
	}
}

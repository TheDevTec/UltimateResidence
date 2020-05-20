package me.DevTec.UltimateResidence.Utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.google.common.collect.Maps;

import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Data;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Position;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.DevTec.UltimateResidence.Events.ResidenceEnterEvent;
import me.DevTec.UltimateResidence.Events.ResidenceLeaveEvent;
import me.DevTec.UltimateResidence.Events.ResidenceSwitchEvent;
import me.DevTec.UltimateResidence.Events.SubzoneEnterEvent;
import me.DevTec.UltimateResidence.Events.SubzoneLeaveEvent;
import me.DevTec.UltimateResidence.Events.SubzoneSwitchEvent;
import me.Straiker123.TheAPI;
import me.Straiker123.Scheduler.Scheduler;
import me.Straiker123.Scheduler.Tasker;

public class ResEvents implements Listener {
	HashMap<Player, Residence> in =Maps.newHashMap();
	HashMap<Player, Subzone> inz =Maps.newHashMap();
	HashMap<Player, Long> wait =Maps.newHashMap();
	HashMap<Player, Long> w =Maps.newHashMap();
	//left,right
	
	public void s(String msg,Player p) {
		if(w.containsKey(p)) {
			if(w.get(p)-System.currentTimeMillis()/1000+3 <= 0) {
				w.put(p, System.currentTimeMillis()/1000);
				TheAPI.msg(msg, p);
			}
		}else{
			w.put(p, System.currentTimeMillis()/1000);
			TheAPI.msg(msg, p);
		}
	}
	
	public static HashMap<String, Location[]> locs = Maps.newHashMap();

	@EventHandler
	public void onClick(BlockFadeEvent e) {
		if(e.getBlock().getType().name().contains("ANVIL")) {
		Residence r= API.getResidence(e.getBlock().getWorld(),new Position(e.getBlock().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getBlock().getLocation());	
			if(z!=null) {
				if(z.getFlag(Flag.ANVILBREAK)) { //only global.
					e.setCancelled(true);
					return;
				}else
					return;
			}else
				if(r.getFlag(Flag.ANVILBREAK)) { //only global.
					e.setCancelled(true);
					return;
				}else
					return;
			}}}
	@EventHandler
	public void onToolDamage(PlayerItemDamageEvent e) {
		Player s = e.getPlayer();
		if(ad.has(s,"residence.admin"))return;
		Residence r= API.getResidence(s.getWorld(),new Position(s.getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(s.getLocation());	
			if(z!=null) {
				if(z.getOwner().equals(s.getName())|| z.getMembers().contains(s.getName())) {
					if(z.getFlag(Flag.NODURABILITY)|| z.getPlayerFlag(Flag.NODURABILITY,s.getName())) {
						e.setCancelled(true);
						return;
					}else
						return;
				}}else
			if(r.getOwner().equals(s.getName())|| r.getMembers().contains(s.getName())) {
				if(r.getFlag(Flag.NODURABILITY)|| r.getPlayerFlag(Flag.NODURABILITY,s.getName())) {
					e.setCancelled(true);
					return;
				}else
					return;
				}
			}
	}
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player s = e.getPlayer();
		new Tasker() {
			public void run() {
				for(String sd: Loader.g.getConfig().getConfigurationSection("Groups").getKeys(false)) {
					if(s.hasPermission("residence.group."+sd)) {
						new Data(s.getName()).setGroup(sd);
						break;
					}}
			}
		}.laterAsync(20);
	}
	
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e) {
	Player s = e.getPlayer();
	if(ad.has(s,"residence.admin"))return;
	if(e.isFlying()==false)return;
	Residence r= API.getResidence(s.getWorld(),new Position(s.getLocation()));
	if(r!= null) {
		Subzone z = r.getSubzone(s.getLocation());	
		if(z!=null) {
				if(z.getFlag(Flag.FLY)||z.getPlayerFlag(Flag.FLY,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cFLY &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
			if(r.getFlag(Flag.FLY)|| r.getPlayerFlag(Flag.FLY,s.getName())) {
				return;
			}else {
				if(Loader.c.getBoolean("ShowNoPermsMsg"))
				s("&c&lUResidence &8&l• &7You do not have permission &cFLY &7here.", s);
				e.setCancelled(true);
				return;
			}
		}}}
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player s = e.getPlayer();
		if(!ad.has(s,"residence.admin"))
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			Residence r= API.getResidence(e.getClickedBlock().getWorld(),new Position(e.getClickedBlock().getLocation()));
			if(r!= null) {
				Subzone z = r.getSubzone(e.getClickedBlock().getLocation());	
				if(z!=null) {
						if(e.getClickedBlock().getType().name().contains("DOOR"))
							if(z.getFlag(Flag.DOOR)|| z.getPlayerFlag(Flag.DOOR,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l• &7You do not have permission &cDOOR &7here.", s);
								e.setCancelled(true);
								return;
							}

							if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
								if(z.getFlag(Flag.WORKBENCH)|| z.getPlayerFlag(Flag.WORKBENCH,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l• &7You do not have permission &CWORKBENCH &7here.", s);
									e.setCancelled(true);
									return;
								}

							if(e.getClickedBlock().getType().name().contains("ANVIL"))
								if(z.getFlag(Flag.ANVIL)|| z.getPlayerFlag(Flag.ANVIL,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l• &7You do not have permission &CANVIL &7here.", s);
									e.setCancelled(true);
									return;
								}
							if(e.getClickedBlock().getType().name().contains("CHEST"))
								if(z.getFlag(Flag.CONTAINER)|| z.getPlayerFlag(Flag.CONTAINER,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l• &7You do not have permission &cCONTAINER &7here.", s);
									e.setCancelled(true);
									return;
								}
							if(e.getClickedBlock().getType().name().contains("BUTTON")
									||e.getClickedBlock().getType().name().contains("SIGN")
									||e.getClickedBlock().getType().name().contains("NOTE")
									||e.getClickedBlock().getType().name().contains("DISPENSER")
									||e.getClickedBlock().getType().name().contains("DAYLIGHT")
									||e.getClickedBlock().getType().name().equals("LECTERN")
									||e.getClickedBlock().getType().name().equals("HOPPER")
									||e.getClickedBlock().getType().name().equals("DROPPER")
									||e.getClickedBlock().getType().name().equals("LEVER")
									||e.getClickedBlock().getType().name().contains("GATE"))
							if(z.getFlag(Flag.USE)|| z.getPlayerFlag(Flag.USE,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
							s("&c&lUResidence &8&l• &7You do not have permission &cUSE &7here.", s);
							e.setCancelled(true);
							return;
					}}else
					if(e.getClickedBlock().getType().name().contains("DOOR"))
						if(r.getFlag(Flag.DOOR)|| r.getPlayerFlag(Flag.DOOR,s.getName())) {
							return;
						}else {
							if(Loader.c.getBoolean("ShowNoPermsMsg"))
							s("&c&lUResidence &8&l• &7You do not have permission &cDOOR &7here.", s);
							e.setCancelled(true);
							return;
						}

						if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
							if(r.getFlag(Flag.WORKBENCH)|| r.getPlayerFlag(Flag.WORKBENCH,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l• &7You do not have permission &cWORKBENCH &7here.", s);
								e.setCancelled(true);
								return;
							}

						if(e.getClickedBlock().getType().name().contains("ANVIL"))
							if(r.getFlag(Flag.ANVIL)|| r.getPlayerFlag(Flag.ANVIL,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l• &7You do not have permission &cANVIL &7here.", s);
								e.setCancelled(true);
								return;
							}
						if(e.getClickedBlock().getType().name().contains("CHEST"))
							if(r.getFlag(Flag.CONTAINER)|| r.getPlayerFlag(Flag.CONTAINER,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l• &7You do not have permission &cCONTAINER &7here.", s);
								e.setCancelled(true);
								return;
							}
						if(e.getClickedBlock().getType().name().contains("BUTTON")
								||e.getClickedBlock().getType().name().contains("SIGN")
								||e.getClickedBlock().getType().name().contains("NOTE")
								||e.getClickedBlock().getType().name().contains("DISPENSER")
								||e.getClickedBlock().getType().name().contains("DAYLIGHT")
								||e.getClickedBlock().getType().name().equals("LECTERN")
								||e.getClickedBlock().getType().name().equals("HOPPER")
								||e.getClickedBlock().getType().name().equals("DROPPER")
								||e.getClickedBlock().getType().name().equals("LEVER")
								||e.getClickedBlock().getType().name().contains("GATE"))
						if(r.getFlag(Flag.USE)|| r.getPlayerFlag(Flag.USE,s.getName())) {
							return;
						}else {
							if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l• &7You do not have permission &cUSE &7here.", s);
						e.setCancelled(true);
						return;
					}}}
		if(!ad.has(s,"residence.create"))return;
		if(e.getItem()!=null && e.getItem().getType()==Material.STICK) {
			e.setCancelled(true);
			if((wait.containsKey(s)?wait.get(s):0)-System.currentTimeMillis()/1000 + 1 <= 0) {
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Location[] l = new Location[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Location a = e.getClickedBlock().getLocation();
					l[1]=a;
					locs.put(s.getName(),l);
					TheAPI.msg("&c&lUltimateResidence &8&l» &72# Corner of residence set at X:"+a.getBlockX()+", Y:"+a.getBlockY()+", Z:"+a.getBlockZ(), s);
				}
				if(e.getAction()==Action.LEFT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Location[] l = new Location[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Location a = e.getClickedBlock().getLocation();
					l[0]=a;
					locs.put(s.getName(),l);
					TheAPI.msg("&c&lUltimateResidence &8&l» &71# Corner of residence set at X:"+a.getBlockX()+", Y:"+a.getBlockY()+", Z:"+a.getBlockZ(), s);
				}}
			}
	}
	private boolean isMob(String s) {
		return s.contains("PIG")||s.contains("COW")||s.contains("CHICKEN")||s.contains("OCELOT")||s.contains("CAT")
				||s.contains("BAT")||s.contains("DONKEY")||s.contains("HORSE")||s.contains("MULE")||s.contains("LLAMA")
				||s.contains("PARROT")||s.contains("BEAR")||s.contains("RABBIT")||s.contains("SHEEP")||s.contains("SQUIT")
				||s.contains("VILLAGER")||s.contains("WOLF")||s.contains("ARMOR_STAND")||s.contains("ITEM_FRAME");
	}
	HashMap<String, Integer> m = new HashMap<String, Integer>();
	//heal, feed task
	public void startTasks(Residence r, Subzone z, String s) {
		if(m.containsKey(s))return;
		m.put(s,
		new Tasker() {
			public void run() {
				Player p = TheAPI.getPlayer(s);
				if(p==null) {
					stopTasks(s);
					return;
				}
				if(z!=null) {
						if(z.getFlag(Flag.HEAL)||z.getPlayerFlag(Flag.HEAL,p.getName())) {
							double max = 20;
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
			double max = 20;
				if(p.getHealth() !=max)
				p.setHealth((p.getHealth()+2) <max ? p.getHealth()+2 :max);
		}
		if(r.getFlag(Flag.FEED)||r.getPlayerFlag(Flag.FEED,p.getName())) {
				if(p.getFoodLevel() !=20)
				p.setFoodLevel((p.getFoodLevel()+2) < 20 ? p.getFoodLevel()+2 : 20);
			}}
		}.repeating(0,10));
	}
	
	public void stopTasks(String s) {
		if(m.containsKey(s)) {
			Scheduler.cancelTask(m.get(s));
		m.remove(s);
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(Math.abs(e.getFrom().getBlockX() - e.getTo().getBlockX()) > 0 || Math.abs(e.getFrom().getBlockZ() - e.getTo().getBlockZ()) > 0 
       		 || Math.abs(e.getFrom().getBlockY() - e.getTo().getBlockY()) > 0) {
			Player p = e.getPlayer();
		Residence r = API.getResidence(e.getTo().getWorld(),new Position(e.getTo()));
		Subzone z = r != null ? r.getSubzone(p): null;
		if(r!=null && !e.isCancelled()) {
			if(in.containsKey(p) && in.get(p).equals(r)) { //in residence
				if(inz.containsKey(p) && z != null && !inz.get(p).equals(z)) { //switch subzone
					SubzoneSwitchEvent es = new SubzoneSwitchEvent(z,p);
						Bukkit.getPluginManager().callEvent(es);
					if(es.isCancelled()||!z.getFlag(Flag.MOVE) && !ad.has(p,"residence.admin")
							&&!z.getPlayerFlag(Flag.MOVE,p.getName())) {
						if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l• &7You do not have permission &cMOVE &7here.", p);
						e.setCancelled(true);
						return;
					}
					if(es.getTitle()!=null)
						TheAPI.getPlayerAPI(p).sendTitle(es.getTitle()[0].replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()));
					if(es.getActionBar()!=null) 
						TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()));
					if(es.getChat()!=null) 
						TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()), p);
					inz.put(p, z);
				}else if(inz.containsKey(p) && z == null) { //leave subzone
					SubzoneLeaveEvent es = new SubzoneLeaveEvent(inz.get(p),e.getFrom(),p);
					Bukkit.getPluginManager().callEvent(es);
					String group = API.getData(p.getName()).getGroup().getName();
					if(Loader.g.getConfig().getBoolean("Groups."+group+".Chat.Use")) {
						TheAPI.msg(Loader.g.getConfig().getString("Groups."+group+".Chat.Enter").replace("%player", p.getName())
								.replace("%residence", r.getName())
								.replace("%owner", r.getOwner()),p);
					}
					if(Loader.g.getConfig().getBoolean("Groups."+group+".Title.Use")) {
						TheAPI.sendTitle(p, Loader.g.getConfig().getString("Groups."+group+".Title.Enter.Line1")
								.replace("%player", p.getName())
								.replace("%residence", r.getName())
								.replace("%owner", r.getOwner()),Loader.g.getConfig().getString("Groups."+group+".Title.Enter.Line2")
								.replace("%player", p.getName())
								.replace("%residence", r.getName())
								.replace("%owner", r.getOwner()));
					}
					if(Loader.g.getConfig().getBoolean("Groups."+group+".ActionBar.Use")) {
						TheAPI.sendActionBar(p, Loader.g.getConfig().getString("Groups."+group+".ActionBar.Enter").replace("%player", p.getName())
								.replace("%residence", r.getName())
								.replace("%owner", r.getOwner()));
					}
					inz.remove(p);
				}else if(!inz.containsKey(p) && z!=null) { //enter subzone
					SubzoneEnterEvent es = new SubzoneEnterEvent(z,p);
					Bukkit.getPluginManager().callEvent(es);
					if(es.isCancelled()||!z.getFlag(Flag.MOVE) && !ad.has(p,"residence.admin")
							&&!z.getPlayerFlag(Flag.MOVE,p.getName())) {
						e.setCancelled(true);
						return;
					}
					if(es.getTitle()!=null)
						TheAPI.getPlayerAPI(p).sendTitle(es.getTitle()[0].replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", z.getName())
										.replace("%owner", z.getOwner()));
					if(es.getActionBar()!=null) 
						TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()));
					if(es.getChat()!=null) 
						TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", z.getName())
								.replace("%owner", z.getOwner()), p);
					inz.put(p, z);
				}
			}else
			if(in.containsKey(p) && !in.get(p).equals(r)) { //switch residence
				stopTasks(p.getName());
				ResidenceSwitchEvent es = new ResidenceSwitchEvent(r,p);
				Bukkit.getPluginManager().callEvent(es);
				if(es.isCancelled()||!r.getFlag(Flag.MOVE) && !ad.has(p,"residence.admin")
						&&!r.getPlayerFlag(Flag.MOVE,p.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cMOVE &7here.", p);
					e.setCancelled(true);
					return;
				}
				if(es.getTitle()!=null)
					TheAPI.getPlayerAPI(p).sendTitle(es.getTitle()[0].replace("%player", p.getName()).replace("%residence", r.getName())
							.replace("%owner", r.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", r.getName())
									.replace("%owner", r.getOwner()));
				if(es.getActionBar()!=null) 
					TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", r.getName())
							.replace("%owner", r.getOwner()));
				if(es.getChat()!=null) 
					TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", r.getName())
							.replace("%owner", r.getOwner()), p);
				in.put(p, r);
			}else
				if(!in.containsKey(p)) { //enter residence
					ResidenceEnterEvent es = new ResidenceEnterEvent(r,p);
					Bukkit.getPluginManager().callEvent(es);
					if(es.isCancelled()||!r.getFlag(Flag.MOVE) && !ad.has(p,"residence.admin")
							&&!r.getPlayerFlag(Flag.MOVE,p.getName())) {
						if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l• &7You do not have permission &cMOVE &7here.", p);
						e.setCancelled(true);
						return;
					}
					if(es.getTitle()!=null)
						TheAPI.getPlayerAPI(p).sendTitle(es.getTitle()[0].replace("%player", p.getName()).replace("%residence", r.getName())
								.replace("%owner", r.getOwner()),es.getTitle()[1].replace("%player", p.getName()).replace("%residence", r.getName())
								.replace("%owner", r.getOwner()));
					if(es.getActionBar()!=null) 
						TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", r.getName())
								.replace("%owner", r.getOwner()));
					if(es.getChat()!=null) 
						TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", r.getName())
								.replace("%owner", r.getOwner()), p);
			in.put(p, r);
				}
					startTasks(r,r.getSubzone(p.getLocation()),p.getName());
		}else { //leave residence
			if(in.containsKey(p) && r==null) {
			ResidenceLeaveEvent es = new ResidenceLeaveEvent(in.get(p),e.getFrom(),p);
			Bukkit.getPluginManager().callEvent(es);
			Residence rs = in.get(p);
			if(es.getTitle()!=null)
				TheAPI.getPlayerAPI(p).sendTitle(es.getTitle()[0].replace("%player", p.getName()).replace("%residence", rs.getName())
						.replace("%owner", rs.getOwner()), es.getTitle()[1].replace("%player", p.getName()).replace("%residence", rs.getName())
						.replace("%owner", rs.getOwner()));
			if(es.getActionBar()!=null) 
				TheAPI.sendActionBar(p, es.getActionBar().replace("%player", p.getName()).replace("%residence", rs.getName())
						.replace("%owner", rs.getOwner()));
			if(es.getChat()!=null) 
				TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", rs.getName())
						.replace("%owner", rs.getOwner()), p);
			stopTasks(p.getName());
			in.remove(p);
			if(inz.containsKey(p) && z == null) { //leave subzone
				SubzoneLeaveEvent ess = new SubzoneLeaveEvent(inz.get(p),e.getFrom(),p);
				Bukkit.getPluginManager().callEvent(ess);
				Subzone rr = inz.get(p);
				inz.remove(p);
				if(ess.getTitle()!=null)
					TheAPI.getPlayerAPI(p).sendTitle(ess.getTitle()[0].replace("%player", p.getName()).replace("%residence", rr.getName())
							.replace("%owner", rr.getOwner()), ess.getTitle()[1].replace("%player", p.getName()).replace("%residence", rr.getName())
							.replace("%owner", rr.getOwner()));
				if(ess.getActionBar()!=null) 
					TheAPI.sendActionBar(p, ess.getActionBar().replace("%player", p.getName()).replace("%residence", rr.getName())
							.replace("%owner", rr.getOwner()));
				if(ess.getChat()!=null) 
					TheAPI.msg(es.getChat().replace("%player", p.getName()).replace("%residence", rr.getName())
							.replace("%owner", rr.getOwner()), p);
			}
			}
		} //THIS IS CRAZY! - Straikerina 14:20, 25. 4. 2020
	}}	//*Straikerina is lost in own code* 14:21

	@EventHandler
	public void onDamage(PlayerInteractAtEntityEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= API.getResidence(e.getRightClicked().getWorld(),new Position(e.getRightClicked().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getRightClicked().getLocation());	
			if(z!=null) {
				if(z.getFlag(Flag.INTERACT) ||z.getPlayerFlag(Flag.INTERACT,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cINTERACT &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
				if(r.getFlag(Flag.INTERACT)|| r.getPlayerFlag(Flag.INTERACT,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cINTERACT &7here.", s);
					e.setCancelled(true);
					return;
				}}}
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player||e.getDamager() instanceof Arrow) {
		Player s = e.getDamager() instanceof Arrow ? (((Arrow)e.getDamager()).getShooter() instanceof Player ? (Player)((Arrow)e.getDamager()).getShooter() : null) : (Player)e.getDamager();
		if(s==null)return;
		if(s.hasPermission("residence.admin"))return;
		Residence r= API.getResidence(e.getEntity().getWorld(),new Position(e.getEntity().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getEntity().getLocation());	
			if(z!=null) {
					Flag f = e.getEntityType()==EntityType.PLAYER ? Flag.PVP : 
						(isMob(e.getEntityType().name()) ? Flag.ANIMALKILL:Flag.MONSTERKILL);
					if(z.getFlag(f)|| z.getPlayerFlag(f,s.getName())) {
						return;
					}else {
						if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l• &7You do not have permission &c"+f.name()+" &7here.", s);
						e.setCancelled(true);
						return;
					}
				}else {
				Flag f = e.getEntityType()==EntityType.PLAYER ? Flag.PVP : 
					(isMob(e.getEntityType().name()) ? Flag.ANIMALKILL:Flag.MONSTERKILL);
				if(r.getFlag(f)|| r.getPlayerFlag(f,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &c"+f.name()+" &7here.", s);
					e.setCancelled(true);
					return;
				}
			}}}}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= API.getResidence(s.getWorld(),new Position(e.getBlock().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getBlock().getLocation());	
			if(z!=null) {
				if(z.getFlag(Flag.BREAK) ||z.getPlayerFlag(Flag.BREAK,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cBREAK &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
				if(r.getFlag(Flag.BREAK)|| r.getPlayerFlag(Flag.BREAK,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cBREAK &7here.", s);
					e.setCancelled(true);
					return;
				}}}}
	@EventHandler
	public void onGod(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
		Player s = (Player) e.getEntity();
		Residence r= API.getResidence(s.getWorld(),new Position(e.getEntity().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(s.getLocation());
		if(z!=null) {
			if(e.getCause()==DamageCause.FALL)
				if(z.getFlag(Flag.NOFALLDAMAGE)||z.getPlayerFlag(Flag.NOFALLDAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}else {
					return;
				}

				if(z.getFlag(Flag.NODAMAGE)||z.getPlayerFlag(Flag.NODAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}
					return;
		}else {
				if(e.getCause()==DamageCause.FALL)
				if(r.getFlag(Flag.NOFALLDAMAGE)||r.getPlayerFlag(Flag.NOFALLDAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}else {
					return;
				}

				if(r.getFlag(Flag.NODAMAGE)||r.getPlayerFlag(Flag.NODAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}
					return;
			}}}}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= API.getResidence(e.getBlock().getWorld(),new Position(e.getBlock().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getBlock().getLocation());	
			if(z!=null) {
				if(z.getFlag(Flag.BUILD)|| z.getPlayerFlag(Flag.BUILD,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cBUILD &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
				if(r.getFlag(Flag.BUILD)|| r.getPlayerFlag(Flag.BUILD,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l• &7You do not have permission &cBUILD &7here.", s);
					e.setCancelled(true);
					return;
				}
			}}}}

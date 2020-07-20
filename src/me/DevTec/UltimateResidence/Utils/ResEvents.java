package me.DevTec.UltimateResidence.Utils;

import java.util.HashMap;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

import me.DevTec.TheAPI;
import me.DevTec.Other.Position;
import me.DevTec.Scheduler.Tasker;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Data;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.DevTec.UltimateResidence.Events.ResidenceEnterEvent;
import me.DevTec.UltimateResidence.Events.ResidenceLeaveEvent;
import me.DevTec.UltimateResidence.Events.ResidenceSwitchEvent;
import me.DevTec.UltimateResidence.Events.SubzoneEnterEvent;
import me.DevTec.UltimateResidence.Events.SubzoneLeaveEvent;
import me.DevTec.UltimateResidence.Events.SubzoneSwitchEvent;

public class ResEvents implements Listener {
	HashMap<Player, Residence> in =Maps.newHashMap();
	HashMap<Player, Subzone> inz =Maps.newHashMap();
	HashMap<Player, Long> wait =Maps.newHashMap(),w =Maps.newHashMap();
	public static HashMap<String, Position[]> locs = Maps.newHashMap();
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

	@EventHandler
	public void onClick(BlockFadeEvent e) {
		if(e.getBlock().getType().name().contains("ANVIL")) {
		Residence r= API.getResidence(new Position(e.getBlock().getLocation()));
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
		Residence r= API.getResidence(new Position(s.getLocation()));
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
	Residence r= API.getResidence(new Position(s.getLocation()));
	if(r!= null) {
		Subzone z = r.getSubzone(s.getLocation());	
		if(z!=null) {
				if(z.getFlag(Flag.FLY)||z.getPlayerFlag(Flag.FLY,s.getName())) {
					return;
				}else {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cFLY &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
			if(r.getFlag(Flag.FLY)|| r.getPlayerFlag(Flag.FLY,s.getName())) {
				return;
			}else {
				if(Loader.c.getBoolean("ShowNoPermsMsg"))
				s("&c&lUResidence &8&l» &7You do not have permission &cFLY &7here.", s);
				e.setCancelled(true);
				return;
			}
		}}}
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player s = e.getPlayer();
		if(!ad.has(s,"residence.admin")) {
			if(e.getAction()==Action.PHYSICAL
					&& (e.getClickedBlock().getType()==Material.getMaterial("SOIL")||e.getClickedBlock().getType()==Material.getMaterial("CROPS"))) {
				Residence r= API.getResidence(new Position(e.getClickedBlock().getLocation()));
				if(r!= null) {
					Subzone z = r.getSubzone(e.getClickedBlock().getLocation());
					if(z!=null) {
						if(z.getFlag(Flag.USE)|| z.getPlayerFlag(Flag.USE,s.getName())) {
							return;
						}else {
							if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l» &7You do not have permission &cUSE &7here.", s);
			                e.setCancelled(true);
			            return;
				}
					}
					if(r.getFlag(Flag.USE)|| r.getPlayerFlag(Flag.USE,s.getName())) {
						return;
					}else {
						if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cUSE &7here.", s);
		                e.setCancelled(true);
		           return;
			}
				}
		}
		if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
			Residence r= API.getResidence(new Position(e.getClickedBlock().getLocation()));
			if(r!= null) {
				Subzone z = r.getSubzone(e.getClickedBlock().getLocation());	
				if(z!=null) {
						if(e.getClickedBlock().getType().name().contains("DOOR"))
							if(z.getFlag(Flag.DOOR)|| z.getPlayerFlag(Flag.DOOR,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l» &7You do not have permission &cDOOR &7here.", s);
								e.setCancelled(true);
								return;
							}

							if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
								if(z.getFlag(Flag.WORKBENCH)|| z.getPlayerFlag(Flag.WORKBENCH,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l» &7You do not have permission &CWORKBENCH &7here.", s);
									e.setCancelled(true);
									return;
								}

							if(e.getClickedBlock().getType().name().contains("ANVIL"))
								if(z.getFlag(Flag.ANVIL)|| z.getPlayerFlag(Flag.ANVIL,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l» &7You do not have permission &CANVIL &7here.", s);
									e.setCancelled(true);
									return;
								}
							if(e.getClickedBlock().getType().name().contains("CHEST"))
								if(z.getFlag(Flag.CONTAINER)|| z.getPlayerFlag(Flag.CONTAINER,s.getName())) {
									return;
								}else {
									if(Loader.c.getBoolean("ShowNoPermsMsg"))
									s("&c&lUResidence &8&l» &7You do not have permission &cCONTAINER &7here.", s);
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
							s("&c&lUResidence &8&l» &7You do not have permission &cUSE &7here.", s);
							e.setCancelled(true);
							return;
					}}else
					if(e.getClickedBlock().getType().name().contains("DOOR"))
						if(r.getFlag(Flag.DOOR)|| r.getPlayerFlag(Flag.DOOR,s.getName())) {
							return;
						}else {
							if(Loader.c.getBoolean("ShowNoPermsMsg"))
							s("&c&lUResidence &8&l» &7You do not have permission &cDOOR &7here.", s);
							e.setCancelled(true);
							return;
						}

						if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
							if(r.getFlag(Flag.WORKBENCH)|| r.getPlayerFlag(Flag.WORKBENCH,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l» &7You do not have permission &cWORKBENCH &7here.", s);
								e.setCancelled(true);
								return;
							}

						if(e.getClickedBlock().getType().name().contains("ANVIL"))
							if(r.getFlag(Flag.ANVIL)|| r.getPlayerFlag(Flag.ANVIL,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l» &7You do not have permission &cANVIL &7here.", s);
								e.setCancelled(true);
								return;
							}
						if(e.getClickedBlock().getType().name().contains("CHEST"))
							if(r.getFlag(Flag.CONTAINER)|| r.getPlayerFlag(Flag.CONTAINER,s.getName())) {
								return;
							}else {
								if(Loader.c.getBoolean("ShowNoPermsMsg"))
								s("&c&lUResidence &8&l» &7You do not have permission &cCONTAINER &7here.", s);
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
						s("&c&lUResidence &8&l» &7You do not have permission &cUSE &7here.", s);
						e.setCancelled(true);
						return;
					}}}}
		if(!ad.has(s,"residence.create"))return;
		if(e.getItem()!=null && e.getItem().getType()==Material.STICK) {
			e.setCancelled(true);
			if((wait.containsKey(s)?wait.get(s):0)-System.currentTimeMillis()/1000 + 1 <= 0) {
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Position[] l = new Position[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Position a = new Position(e.getClickedBlock().getLocation());
					
					l[1]=a;
					locs.put(s.getName(),l);
					TheAPI.msg("&c&lUltimateResidence &8&l» &72# Corner of residence set at X:"+a.getBlockX()+", Y:"+a.getBlockY()+", Z:"+a.getBlockZ(), s);
				}
				if(e.getAction()==Action.LEFT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Position[] l = new Position[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Position a = new Position(e.getClickedBlock().getLocation());
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent e) {
		long nano = System.currentTimeMillis();
		Location from = e.getFrom(), to = e.getTo();
		Player p = e.getPlayer();
		boolean c = (Boolean)new Executor(new Callable<Boolean>() {
			@Override
			public Boolean call() {
		if(Math.abs(from.getBlockX() - to.getBlockX()) > 0 || Math.abs(from.getBlockZ() - to.getBlockZ()) > 0 
	       		 || Math.abs(from.getBlockY() - to.getBlockY()) > 0) {
		    			Residence r = API.getResidence(new Position(to));
		    			Subzone z = r != null ? r.getSubzone(p): null;
		    			boolean cancel = false;
		    			if(r!=null && !e.isCancelled()) {
		    				if(in.containsKey(p) && in.get(p).equals(r)) { //in residence
		    					if(inz.containsKey(p) && z != null && !inz.get(p).equals(z)) { //switch subzone
		    						SubzoneSwitchEvent es = new SubzoneSwitchEvent(z,p);
		    							Bukkit.getPluginManager().callEvent(es);
		    								if(!ad.has(p,"residence.admin") && !z.getPlayerFlag(Flag.MOVE,p.getName()) && !z.getFlag(Flag.MOVE))
		    									cancel=true;
		    								else {
		    								inz.put(p, z);
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
		    								}
		    					}else if(inz.containsKey(p) && z == null) { //leave subzone
		    						SubzoneLeaveEvent es = new SubzoneLeaveEvent(inz.get(p),new Position(e.getFrom()),p);
		    						inz.remove(p);
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
		    					}else if(!inz.containsKey(p) && z!=null) { //enter subzone
		    						SubzoneEnterEvent es = new SubzoneEnterEvent(z,new Position(e.getFrom()),p);
		    						Bukkit.getPluginManager().callEvent(es);
	    								if(!ad.has(p,"residence.admin")&&!z.getPlayerFlag(Flag.MOVE,p.getName()) && !z.getFlag(Flag.MOVE))
	    									cancel=true;
	    								else {
	    									inz.put(p, z);
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
		    					}}
		    				}else
		    				if(in.containsKey(p) && !in.get(p).equals(r)) { //switch residence
		    					ResidenceSwitchEvent es = new ResidenceSwitchEvent(r,p);
		    					Bukkit.getPluginManager().callEvent(es);
    								if(!ad.has(p,"residence.admin")&&!r.getPlayerFlag(Flag.MOVE,p.getName()) && !r.getFlag(Flag.MOVE))
    									cancel=true;
    								else {
		    					in.put(p, r);
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
    								}}else
		    					if(!in.containsKey(p)) { //enter residence
		    						ResidenceEnterEvent es = new ResidenceEnterEvent(r,p);
		    						Bukkit.getPluginManager().callEvent(es);
	    								if(!ad.has(p,"residence.admin")&&!r.getPlayerFlag(Flag.MOVE,p.getName()) && !r.getFlag(Flag.MOVE))
	    									cancel=true;
	    								else {
	    									in.put(p, r);
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
	    								}
		    					}
		    			}else { //leave residence
		    				if(in.containsKey(p) && r==null) {
		    				ResidenceLeaveEvent es = new ResidenceLeaveEvent(in.get(p),e.getFrom(),p);
		    				Bukkit.getPluginManager().callEvent(es);
		    				Residence rs = in.get(p);
		    				in.remove(p);
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
		    				if(inz.containsKey(p) && z == null) { //leave subzone
		    					SubzoneLeaveEvent ess = new SubzoneLeaveEvent(inz.get(p),new Position(e.getFrom()),p);
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
		    				}}}
		    				return cancel;
		        }
		return false;
		}}).get();
		if(c)e.setCancelled(true);
		nano=System.currentTimeMillis()-nano;
		if(nano!=0)
		for(String s : Loader.debugging)
			if(TheAPI.getPlayer(s)!=null)
				TheAPI.msg("Move event done in "+nano, TheAPI.getPlayer(s));
		}

	@EventHandler
	public void onDamage(PlayerInteractAtEntityEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin")||e.isCancelled())return;
		Position clicked = new Position(e.getRightClicked().getLocation());
		e.setCancelled((Boolean)new Executor(new Callable<Boolean>() {
	        public Boolean call() {
		Residence r= API.getResidence(clicked);
		if(r!= null) {
			Subzone z = r.getSubzone(clicked);	
			if(z!=null) {
				if(s.hasPermission("residence.admin"))return true;
				if(!z.getFlag(Flag.INTERACT)&&!z.getPlayerFlag(Flag.INTERACT,s.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cINTERACT &7here.", s);
					return true;
				}
			}else {
				if(s.hasPermission("residence.admin"))return true;
				if(!r.getFlag(Flag.INTERACT)&&!r.getPlayerFlag(Flag.INTERACT,s.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cINTERACT &7here.", s);
					return true;
				}}}
		return false;
	}}).get());
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player||e.getDamager() instanceof Arrow) {
			e.setCancelled((Boolean)new Executor(new Callable<Boolean>() {
		        public Boolean call() {
		Player s = e.getDamager() instanceof Arrow ? (((Arrow)e.getDamager()).getShooter() instanceof Player ? (Player)((Arrow)e.getDamager()).getShooter() : null) : (Player)e.getDamager();
		if(s==null)return false;
		Residence r= API.getResidence(new Position(e.getEntity().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getEntity().getLocation());	
			if(z!=null) {
					Flag f = e.getEntity() instanceof Player ? Flag.PVP : 
						(isMob(e.getEntityType().name()) ? Flag.ANIMALKILL:Flag.MONSTERKILL);
					if(f==Flag.ANIMALKILL||f==Flag.MONSTERKILL)
					if(s.hasPermission("residence.admin"))return false;
					if(!z.getFlag(f)&&!z.getPlayerFlag(f,s.getName())) {
						if(Loader.c.getBoolean("ShowNoPermsMsg"))
						s("&c&lUResidence &8&l» &7You do not have permission &c"+f.name()+" &7here.", s);
						e.setCancelled(true);
						return true;
					}
				}else {
				Flag f = e.getEntity() instanceof Player ? Flag.PVP : 
					(isMob(e.getEntityType().name()) ? Flag.ANIMALKILL:Flag.MONSTERKILL);
				if(f==Flag.ANIMALKILL||f==Flag.MONSTERKILL)
				if(s.hasPermission("residence.admin"))return false;
				if(!r.getFlag(f)&&!r.getPlayerFlag(f,s.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &c"+f.name()+" &7here.", s);
					e.setCancelled(true);
					return false;
				}}}return false;}}).get());
			}}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBreak(BlockBreakEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin")||e.isCancelled())return;
		e.setCancelled((Boolean)new Executor(new Callable<Boolean>() {
		        public Boolean call() {
				    Position p = new Position(e.getBlock().getLocation());
				    Residence r= API.getResidence(p);
				    if(r!= null) {
				    	Subzone z = r.getSubzone(p);	
				    	if(z!=null) {
				    		if(!z.getFlag(Flag.BREAK) && !z.getPlayerFlag(Flag.BREAK,s.getName())) {
				    			if(Loader.c.getBoolean("ShowNoPermsMsg"))
				    			s("&c&lUResidence &8&l» &7You do not have permission &cBREAK &7here.", s);
				    			return true;
				    		}
				    	}else {
				    		if(!r.getFlag(Flag.BREAK)&&!r.getPlayerFlag(Flag.BREAK,s.getName())) {
				    			if(Loader.c.getBoolean("ShowNoPermsMsg"))
				    			s("&c&lUResidence &8&l» &7You do not have permission &cBREAK &7here.", s);
				    			return true;
				    	}}}
				    return false;
				    }}).get());
		
	}
	@EventHandler
	public void onGod(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
		Player s = (Player) e.getEntity();
		if(s.hasPermission("residence.admin")||e.isCancelled()) {
			e.setCancelled(true);
			return;
		}
		e.setCancelled((Boolean)new Executor(new Callable<Boolean>() {
	        public Boolean call() {
		Residence r= API.getResidence(new Position(e.getEntity().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(s.getLocation());
		if(z!=null) {
			if(e.getCause()==DamageCause.FALL)
				if(z.getFlag(Flag.NOFALLDAMAGE)||z.getPlayerFlag(Flag.NOFALLDAMAGE,s.getName())) {
					return true;
				}

				if(z.getFlag(Flag.NODAMAGE)||z.getPlayerFlag(Flag.NODAMAGE,s.getName())) {
					return true;
				}
		}else {
				if(e.getCause()==DamageCause.FALL)
				if(r.getFlag(Flag.NOFALLDAMAGE)||r.getPlayerFlag(Flag.NOFALLDAMAGE,s.getName())) {
					return true;
				}

				if(r.getFlag(Flag.NODAMAGE)||r.getPlayerFlag(Flag.NODAMAGE,s.getName())) {
					return true;
				}
			}}return false;}}).get());
		}}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= API.getResidence(new Position(e.getBlock().getLocation()));
		if(r!= null) {
			Subzone z = r.getSubzone(e.getBlock().getLocation());	
			if(z!=null) {
				if(!z.getFlag(Flag.BUILD)&&!z.getPlayerFlag(Flag.BUILD,s.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cBUILD &7here.", s);
					e.setCancelled(true);
					return;
				}
			}else {
				if(!r.getFlag(Flag.BUILD)&&!r.getPlayerFlag(Flag.BUILD,s.getName())) {
					if(Loader.c.getBoolean("ShowNoPermsMsg"))
					s("&c&lUResidence &8&l» &7You do not have permission &cBUILD &7here.", s);
					e.setCancelled(true);
					return;
				}
			}}}}

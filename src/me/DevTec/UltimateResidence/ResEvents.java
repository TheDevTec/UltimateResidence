package me.DevTec.UltimateResidence;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import me.DevTec.UltimateResidence.Events.ResidenceEnterEvent;
import me.DevTec.UltimateResidence.Events.ResidenceLeaveEvent;
import me.DevTec.UltimateResidence.ResidenceFlag.Flag;
import me.Straiker123.TheAPI;

public class ResEvents implements Listener {
	//left,right
	public static HashMap<String, Location[]> locs = new HashMap<String, Location[]>();
	HashMap<Player, Long> wait = new HashMap<Player, Long>();
	@EventHandler
	public void onClick(BlockFadeEvent e) {
		if(e.getBlock().getType().name().contains("ANVIL")) {
		Residence r= ResidenceAPI.getResidence(e.getBlock().getLocation());
		if(r!= null)
				if(r.getFlag(Flag.ANVILBREAK)) { //only global.
					e.setCancelled(true);
					return;
				}else {
					return;
				}
			}}
	@EventHandler
	public void onToolDamage(PlayerItemDamageEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(s.getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName())|| r.getMembers().contains(s.getName())) {
				if(r.getFlag(Flag.NODURABILITY)|| r.getPlayerFlag(Flag.NODURABILITY,s.getName())) {
					e.setCancelled(true);
					return;
				}else {
					return;
				}
			}
	}
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e) {
	Player s = e.getPlayer();
	if(s.hasPermission("residence.admin"))return;
	Residence r= ResidenceAPI.getResidence(s.getLocation());
	if(r!= null)
		if(e.isFlying())
		if(r.getOwner().equals(s.getName())|| r.getMembers().contains(s.getName())) {
			if(r.getFlag(Flag.FLY)|| r.getPlayerFlag(Flag.FLY,s.getName())) {
				return;
			}else {
				e.setCancelled(true);
				return;
			}
		}}
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player s = e.getPlayer();
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
			}else {
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
					if(s.hasPermission("residence.admin"))return;
					Residence r= ResidenceAPI.getResidence(e.getClickedBlock().getLocation());
					if(r!= null)
						if(r.getOwner().equals(s.getName()) ||  r.getMembers().contains(s.getName())) {
							if(e.getClickedBlock().getType().name().contains("DOOR"))
								if(r.getFlag(Flag.DOOR)|| r.getPlayerFlag(Flag.DOOR,s.getName())) {
									return;
								}else {
									e.setCancelled(true);
									return;
								}

								if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
									if(r.getFlag(Flag.WORKBENCH)|| r.getPlayerFlag(Flag.WORKBENCH,s.getName())) {
										return;
									}else {
										e.setCancelled(true);
										return;
									}

								if(e.getClickedBlock().getType().name().contains("ANVIL"))
									if(r.getFlag(Flag.ANVIL)|| r.getPlayerFlag(Flag.ANVIL,s.getName())) {
										return;
									}else {
										e.setCancelled(true);
										return;
									}
								
								if(r.getFlag(Flag.USE)|| r.getPlayerFlag(Flag.USE,s.getName()))
									return;
								e.setCancelled(true);
								return;
							}
	}}}
	
	private boolean isMob(String s) {
		return s.contains("PIG")||s.contains("COW")||s.contains("CHICKEN")||s.contains("OCELOT")||s.contains("CAT")
				||s.contains("BAT")||s.contains("DONKEY")||s.contains("HORSE")||s.contains("MULE")||s.contains("LLAMA")
				||s.contains("PARROT")||s.contains("BEAR")||s.contains("RABBIT")||s.contains("SHEEP")||s.contains("SQUIT")
				||s.contains("VILLAGER")||s.contains("WOLF");
	}

	HashMap<Player, Residence> in = new HashMap<Player, Residence>();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getTo().getBlockX() > e.getFrom().getBlockX()|| e.getTo().getBlockZ() > e.getFrom().getBlockZ() ||e.getTo().getBlockY() > e.getFrom().getBlockY()) {
		Player p = e.getPlayer();
		Residence r = ResidenceAPI.getResidence(e.getTo());
		if(r!=null && e.isCancelled()==false) {
			if(in.containsKey(p) && !in.get(p).equals(r)) {
				ResidenceEnterEvent es = new ResidenceEnterEvent(r,p);
				Bukkit.getPluginManager().callEvent(es);
				if(es.isCancelled()||r.getFlag(Flag.MOVE)) {
					e.setCancelled(true);
					return;
				}
				if(es.getTitle()!=null)
					TheAPI.getPlayerAPI(p).sendTitle(TheAPI.colorize(es.getTitle()[0]), TheAPI.colorize(es.getTitle()[1]));
				if(es.getActionBar()!=null) 
					TheAPI.sendActionBar(p, es.getActionBar());
				in.put(p, r);
			}else
				if(!in.containsKey(p)) {
					ResidenceEnterEvent es = new ResidenceEnterEvent(r,p);
					Bukkit.getPluginManager().callEvent(es);
					if(es.getTitle()!=null)
						TheAPI.getPlayerAPI(p).sendTitle(TheAPI.colorize(es.getTitle()[0]), TheAPI.colorize(es.getTitle()[1]));
					if(es.getActionBar()!=null) 
						TheAPI.sendActionBar(p, es.getActionBar());
			in.put(p, r);
				}
			for(ResidenceFlag f : r.getFlags()) {
				switch(f.getFlag()) {
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
			ResidenceLeaveEvent es = new ResidenceLeaveEvent(in.get(p),e.getFrom(),p);
			Bukkit.getPluginManager().callEvent(es);
			if(es.getTitle()!=null)
				p.sendTitle(TheAPI.colorize(es.getTitle()[0]), TheAPI.colorize(es.getTitle()[1]));
			if(es.getActionBar()!=null) 
				TheAPI.sendActionBar(p, es.getActionBar());
			in.remove(p);
			}
		}
	}}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
		Player s = (Player)e.getDamager();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(e.getEntity().getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName()) || r.getMembers().contains(s.getName())) {
				Flag f = e.getEntityType()==EntityType.PLAYER ? Flag.PVP : 
					(isMob(e.getEntityType().name()) ? Flag.ANIMALKILL:Flag.MONSTERKILL);
				if(r.getFlag(f)|| r.getPlayerFlag(f,s.getName())) {
					return;
				}else {
					e.setCancelled(true);
					return;
				}
			}}}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(e.getBlock().getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName())|| r.getMembers().contains(s.getName())) {
				if(r.getFlag(Flag.BREAK)|| r.getPlayerFlag(Flag.BREAK,s.getName())) {
					return;
				}else {
					e.setCancelled(true);
					return;
				}
			}}
	@EventHandler
	public void onGod(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
		Player s = (Player) e.getEntity();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(e.getEntity().getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName()) || r.getMembers().contains(s.getName())) {
				if(e.getCause()==DamageCause.FALL)
				if(r.getFlag(Flag.NOFALLDAMAGE)|| r.getPlayerFlag(Flag.NOFALLDAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}else {
					return;
				}

				if(r.getFlag(Flag.NODAMAGE)|| r.getPlayerFlag(Flag.NODAMAGE,s.getName())) {
					e.setCancelled(true);
					return;
				}
					return;
			}}}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(e.getBlock().getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName()) || r.getMembers().contains(s.getName())) {
				if(r.getFlag(Flag.BUILD)|| r.getPlayerFlag(Flag.BUILD,s.getName())) {
					return;
				}else {
					e.setCancelled(true);
					return;
				}
			}}}

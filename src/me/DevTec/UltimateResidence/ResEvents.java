package me.DevTec.UltimateResidence;

import java.util.HashMap;

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
import org.bukkit.event.player.PlayerToggleFlightEvent;

import me.DevTec.UltimateResidence.ResidenceFlag.ResidenceFlagType;
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
				if(r.getFlag(ResidenceFlagType.ANVILBREAK)) { //only global.
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
				if(r.getFlag(ResidenceFlagType.NODURABILITY)|| r.getFlag(s,ResidenceFlagType.NODURABILITY)) {
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
			if(r.getFlag(ResidenceFlagType.FLY)|| r.getFlag(s,ResidenceFlagType.FLY)) {
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
								if(r.getFlag(ResidenceFlagType.DOOR)|| r.getFlag(s,ResidenceFlagType.DOOR)) {
									return;
								}else {
									e.setCancelled(true);
									return;
								}

								if(e.getClickedBlock().getType().name().contains("WORKBENCH"))
									if(r.getFlag(ResidenceFlagType.WORKBENCH)|| r.getFlag(s,ResidenceFlagType.WORKBENCH)) {
										return;
									}else {
										e.setCancelled(true);
										return;
									}

								if(e.getClickedBlock().getType().name().contains("ANVIL"))
									if(r.getFlag(ResidenceFlagType.ANVIL)|| r.getFlag(s,ResidenceFlagType.ANVIL)) {
										return;
									}else {
										e.setCancelled(true);
										return;
									}
								
								if(r.getFlag(ResidenceFlagType.USE)|| r.getFlag(s,ResidenceFlagType.USE))
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
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
		Player s = (Player)e.getDamager();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(e.getEntity().getLocation());
		if(r!= null)
			if(r.getOwner().equals(s.getName()) || r.getMembers().contains(s.getName())) {
				ResidenceFlagType f = e.getEntityType()==EntityType.PLAYER ? ResidenceFlagType.PVP : 
					(isMob(e.getEntityType().name()) ? ResidenceFlagType.ANIMALKILL:ResidenceFlagType.MONSTERKILL);
				if(r.getFlag(f)|| r.getFlag(s,f)) {
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
				if(r.getFlag(ResidenceFlagType.BREAK)|| r.getFlag(s,ResidenceFlagType.BREAK)) {
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
				if(r.getFlag(ResidenceFlagType.NOFALLDAMAGE)|| r.getFlag(s,ResidenceFlagType.NOFALLDAMAGE)) {
					e.setCancelled(true);
					return;
				}else {
					return;
				}

				if(r.getFlag(ResidenceFlagType.NODAMAGE)|| r.getFlag(s,ResidenceFlagType.NODAMAGE)) {
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
				if(r.getFlag(ResidenceFlagType.BUILD)|| r.getFlag(s,ResidenceFlagType.BUILD)) {
					return;
				}else {
					e.setCancelled(true);
					return;
				}
			}}}

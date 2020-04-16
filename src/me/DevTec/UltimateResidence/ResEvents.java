package me.DevTec.UltimateResidence;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.DevTec.UltimateResidence.ResidenceFlag.ResidenceFlagType;
import me.Straiker123.TheAPI;

public class ResEvents implements Listener {
	//left,right
	public static HashMap<String, Location[]> locs = new HashMap<String, Location[]>();
	HashMap<Player, Long> wait = new HashMap<Player, Long>();
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player s = e.getPlayer();
		if(e.getItem()!=null && e.getItem().getType()==Material.STICK)
			if((wait.containsKey(s)?wait.get(s):0)-System.currentTimeMillis()/1000 + 1 <= 0) {
				if(e.getAction()==Action.RIGHT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Location[] l = new Location[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Location a = e.getClickedBlock().getLocation();
					l[1]=a;
					locs.put(s.getName(),l);
					TheAPI.msg("&c&lUltimateResidence &8&l» &72# Corner of residence set at X:"+a.getBlockX()+", Y:"+a.getBlockY()+", Z:"+a.getBlockZ(), s);
					e.setCancelled(true);
				}
				if(e.getAction()==Action.LEFT_CLICK_BLOCK) {
					wait.put(s, System.currentTimeMillis()/1000);
					Location[] l = new Location[2];
					if(locs.containsKey(s.getName()))l=locs.get(s.getName());
					Location a = e.getClickedBlock().getLocation();
					l[0]=a;
					locs.put(s.getName(),l);
					TheAPI.msg("&c&lUltimateResidence &8&l» &71# Corner of residence set at X:"+a.getBlockX()+", Y:"+a.getBlockY()+", Z:"+a.getBlockZ(), s);
					e.setCancelled(true);
				}
			}
	}
	@EventHandler
	public void onClick(BlockBreakEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(s);
		if(r!= null) {
			if(!r.getOwner().equals(s.getName()) && !r.getMembers().contains(s.getName()) && !r.getFlag(ResidenceFlagType.BREAK)
					 && !r.getFlag(s,ResidenceFlagType.BREAK))
				e.setCancelled(true);
		}
	}
	@EventHandler
	public void onClick(BlockPlaceEvent e) {
		Player s = e.getPlayer();
		if(s.hasPermission("residence.admin"))return;
		Residence r= ResidenceAPI.getResidence(s);
		if(r!= null) {
			if(!r.getOwner().equals(s.getName()) && !r.getMembers().contains(s.getName()) && !r.getFlag(ResidenceFlagType.BUILD)
					 && !r.getFlag(s,ResidenceFlagType.BUILD))
				e.setCancelled(true);
		}
	}
}

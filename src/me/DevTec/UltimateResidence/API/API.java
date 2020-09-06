package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.BlocksAPI.BlocksAPI;
import me.DevTec.TheAPI.ConfigAPI.Config;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Group.SizeType;
import me.DevTec.UltimateResidence.Utils.ResEvents;

public class API {
	private static HashMap<String, Residence> cache = Maps.newHashMap();
	public static void reload() {
		for(World w : Loader.map.keySet()) 
			Loader.map.get(w).reload();
		Loader.c.reload();
		Loader.g.reload();
	}
	
	public static Residence getResidence(World world, String residence) {
		if(Loader.getData(world).exists("Residence."+residence)) {
			Residence r = cache.containsKey(residence) ? cache.get(residence) : null;
			if(r==null) {
				r=new Residence(residence,Loader.getData(world).getSection("Residence."+residence));
				cache.put(residence, r);
			}
			return r;
		}
		return null;
	}

	public static String getResidenceOwner(World world, String residence) {
		return Loader.getData(world).getString("Residence."+residence+".Owner");
	}

	public static Residence getResidenceByName(String residence) {
		Residence r = null;
		for(World w : Bukkit.getWorlds())
			if(getResidences(w).contains(residence)) {
				r=getResidence(w,residence);
				break;
			}
		return r;
	}
	
	public static World getResidenceWorldByName(String res) {
		World r = null;
		for(World w : Bukkit.getWorlds())
			if(getResidences(w).contains(res)) {
				r=w;
				break;
			}
		return r;
	}
	
	public static List<String> getResidences(World world, String player){
		return getData(player).getResidences(world.getName());
	}
	
	public static Residence getResidence(Player player) {
		return getResidence(new Position(player.getLocation()));
	}

	private static List<String> getResidences(World world) {
		List<String> a = new ArrayList<String>();
		for(String s: Loader.getData(world).getKeys("Residence"))
			a.add(s);
		return a;
	}

	public static void create(World world, String owner,String res) {
		Config a = Loader.getData(world);
		a.set("Residence."+res+".Corners", 
				StringUtils.getLocationAsString(ResEvents.locs.get(owner)[0].toLocation())+":"+
						StringUtils.getLocationAsString(ResEvents.locs.get(owner)[1].toLocation()));
		a.set("Residence."+res+".Owner", owner);
		a.set("Residence."+res+".Members", Arrays.asList(owner));
		a.set("Residence."+res+".Limit.Size", getData(owner).getGroup().getMaxSize(SizeType.X)+"x"+
				getData(owner).getGroup().getMaxSize(SizeType.Z));
		a.set("Residence."+res+".Limit.Subzones", getData(owner).getGroup().getMaxSubResidences());
		a.set("Residence."+res+".Tp"
				,StringUtils.getLocationAsString(TheAPI.getPlayer(owner).getLocation()));
		new Tasker() {
			public void run() {
				a.save();
			}
		}.runAsync();
		Residence r = new Residence(res, a.getSection("Residence."+res));
		r.setFlag(Flag.MOVE, true);
		r.setFlag(Flag.FLY, true);
		r.setFlag(Flag.DOOR, true);
		r.setFlag(Flag.ANIMALSPAWN, true);
		r.setFlag(Flag.MONSTERSPAWN, true);
		r.setFlag(Flag.MONSTERKILL, true);
		cache.put(res, r);
		getData(owner).addResidence(r);
	}
	
	public static boolean isBigSize(String player, Location l1, Location l2) {
		int x = Math.max(l1.getBlockX(), l2.getBlockX()) - Math.min(l1.getBlockX(), l2.getBlockX()) + 1;
		int z = Math.max(l1.getBlockZ(), l2.getBlockZ())-Math.min(l1.getBlockZ(), l2.getBlockZ())+1;
		return getData(player).getGroup().getMaxSize(SizeType.X) < x||getData(player).getGroup().getMaxSize(SizeType.X) < z;
	}

	public static void delete(String owner,String res) {
		getData(owner).removeResidence(getResidenceByName(res));
		Config a = Loader.getData(getResidenceByName(res).getWorld());
		a.set("Residence."+res,null);
		new Tasker() {
			public void run() {
				a.save();
			}
		}.runAsync();
		cache.remove(res);
	}
	
public static Residence getResidence(Position location) {
    		Residence rr=null;
    		Config ac = Loader.getData(location.getWorld());
        	for(String s : getResidences(location.getWorld())) {
        		String[] sd = ac.getString("Residence."+s+".Corners").split(":");
		if(BlocksAPI.isInside(location, Position.fromString(sd[0]), Position.fromString(sd[1]))) {
			rr = getResidence(location.getWorld(),s);
			break;
		}
	}
	return rr;
	}

	public static List<String> getResidences(String owner) {
		return getData(owner).getResidences();
	}

	public static boolean isColliding(Residence res, Residence anotherOne) {
		if(anotherOne==null||res==null)return false;
		return res.inside(anotherOne.getCorners()[0])&&res.inside(anotherOne.getCorners()[1]);
	}

	public static boolean isCollidingWithSubzone(Subzone sub, Subzone anotherOne) {
		if(anotherOne==null||sub==null)return false;
		   return sub.inside(anotherOne.getCorners()[0])&&sub.inside(anotherOne.getCorners()[1]);
	}

	public static Data getData(String player) {
		return new Data(player);
	}
}

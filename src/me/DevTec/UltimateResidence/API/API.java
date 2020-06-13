package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import me.DevTec.ConfigAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.Position;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Executor;
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
		if(getResidenceOwner(world,residence)!=null) {
			Residence r = cache.containsKey(residence) ? cache.get(residence) : null;
			if(r==null) {
				r=new Residence(residence,world,getResidenceOwner(world,residence));
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
		for(World w : Bukkit.getWorlds()) {
			if(getResidences(w).contains(residence)) {
				r=getResidence(w,residence);
				break;
			}
		}
		return r;
	}
	
	public static World getResidenceWorldByName(String res) {
		World r = null;
		for(World w : Bukkit.getWorlds()) {
			if(getResidences(w).contains(res)) {
				r=getResidence(w,res).getWorld();
				break;
			}
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
		if(new ConfigAPI("UltimateResidence","Data/"+world.getName()).existPath("Residence"))
		for(String s: Loader.getData(world).getKeys("Residence"))
			a.add(s);
		return a;
	}

	public static void create(World world, String owner,String res) {
		ConfigAPI a = Loader.getData(world);
		a.set("Residence."+res+".Corners", 
				ResEvents.locs.get(owner)[0].toString()+":"+
				ResEvents.locs.get(owner)[1].toString());
		a.set("Residence."+res+".Owner", owner);
		a.set("Residence."+res+".Members", Arrays.asList(owner));
		a.set("Residence."+res+".Limit.Size", getData(owner).getGroup().getMaxSize(SizeType.X)+"x"+
				getData(owner).getGroup().getMaxSize(SizeType.Z));
		a.set("Residence."+res+".Limit.Subzones", getData(owner).getGroup().getMaxSubResidences());
		a.set("Residence."+res+".Tp"
				,TheAPI.getStringUtils().getLocationAsString(TheAPI.getPlayer(owner).getLocation()));
		a.save();
		Residence r = new Residence(res, world, owner);
		r.setFlag(Flag.MOVE, true);
		r.setFlag(Flag.FLY, true);
		r.setFlag(Flag.DOOR, true);
		r.setFlag(Flag.ANIMALSPAWN, true);
		r.setFlag(Flag.MONSTERSPAWN, true);
		r.setFlag(Flag.MONSTERKILL, true);
		cache.put(res, r); //default flags
		getData(owner).addResidence(r);
	}
	
	public static boolean isBigSize(String player, Location l1, Location l2) {
		int x = Math.max(l1.getBlockX(), l2.getBlockX()) - Math.min(l1.getBlockX(), l2.getBlockX()) + 1;
		int z = Math.max(l1.getBlockZ(), l2.getBlockZ())-Math.min(l1.getBlockZ(), l2.getBlockZ())+1;
		return getData(player).getGroup().getMaxSize(SizeType.X) < x||getData(player).getGroup().getMaxSize(SizeType.X) < z;
	}

	public static void delete(String owner,String res) {
		getData(owner).removeResidence(getResidenceByName(res));
		Loader.getData(getResidenceByName(res).getWorld()).set("Residence."+res,null);
		cache.remove(res);
	}
	
	public static Residence getResidence(Position location) {
	   return new Executor<Residence>().get(new Callable<Residence>() {
	    	@Override
	        public Residence call() {
	    		Residence rr=null;
	        	for(String s : getResidences(location.getWorld())) {
	    			Residence r = getResidence(location.getWorld(),s);
	    			if(r.inside(location)) {
	    				rr=r;
	    				break;
	    			}
	    		}
	        	return rr;
	        }});
	}

	public static List<String> getResidences(String owner) {
		return getData(owner).getResidences();
	}

	public static boolean isColliding(Residence res, Residence anotherOne) {
		   return res.inside(anotherOne.getCorners()[0])&&res.inside(anotherOne.getCorners()[1]);
	}

	public static boolean isCollidingWithSubzone(Subzone sub, Subzone anotherOne) {
		   return sub.inside(anotherOne.getCorners()[0])&&sub.inside(anotherOne.getCorners()[1]);
	}

	public static Data getData(String player) {
		return new Data(player);
	}
}

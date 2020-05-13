package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Group.SizeType;
import me.DevTec.UltimateResidence.Utils.ResEvents;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;

public class API {
	
	public static void reload() {
		for(World w : Loader.map.keySet()) 
			Loader.map.get(w).reload();
		Loader.c.reload();
		Loader.g.reload();
	}
	
	public static Residence getResidence(World world, String residence) {
		if(getResidenceOwner(world,residence)!=null)
		return new Residence(residence,world,getResidenceOwner(world,residence));
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
		return getResidence(player.getWorld(),new Position(player.getLocation()));
	}

	private static List<String> getResidences(World world) {
		List<String> a = new ArrayList<String>();
		if(new ConfigAPI("UltimateResidence","Data/"+world.getName()).existPath("Residence"))
		for(String s: Loader.getData(world).getConfigurationSection("Residence",false))
			a.add(s);
		return a;
	}

	public static void create(World world, String owner,String res) {
		ConfigAPI a = Loader.getData(world);
		a.set("Residence."+res+".Corners", 
				TheAPI.getStringUtils().getLocationAsString(ResEvents.locs.get(owner)[0])+":"+
				TheAPI.getStringUtils().getLocationAsString(ResEvents.locs.get(owner)[1]));
		a.set("Residence."+res+".Owner", owner);
		a.set("Residence."+res+".Members", Arrays.asList(owner));
		a.set("Residence."+res+".Limit.Size", getData(owner).getGroup().getMaxSize(SizeType.X)+"x"+
				getData(owner).getGroup().getMaxSize(SizeType.Z));
		a.set("Residence."+res+".Limit.Subzones", getData(owner).getGroup().getMaxSubResidences());
		a.set("Residence."+res+".Tp"
				,TheAPI.getStringUtils().getLocationAsString(TheAPI.getPlayer(owner).getLocation()));
		a.save();
		getData(owner).addResidence(new Residence(res, world, owner));
	}
	
	public static boolean isBigSize(String player, Location l1, Location l2) {
		int x = Math.max(l1.getBlockX(), l2.getBlockX()) - Math.min(l1.getBlockX(), l2.getBlockX()) + 1;
		int z = Math.max(l1.getBlockZ(), l2.getBlockZ())-Math.min(l1.getBlockZ(), l2.getBlockZ())+1;
		return getData(player).getGroup().getMaxSize(SizeType.X) < x||getData(player).getGroup().getMaxSize(SizeType.X) < z;
	}

	public static Data getData(String player) {
		return new Data(player);
	}

	public static void delete(String owner,String res) {
		getData(owner).removeResidence(getResidenceByName(res));
		Loader.getData(getResidenceByName(res).getWorld()).set("Residence."+res,null);
	}

	public static List<String> getResidences(String owner) {
		return getData(owner).getResidences();
	}

	public static boolean isColliding(World world, Position x,Position x2) {
		boolean is = false;
		for(String s : getResidences(world)) {
			Residence r = getResidence(world,s);
			int topBlockX = (int)(x.x() < x2.x() ? x2.x() : x.x());
	        int bottomBlockX = (int)(x.x() > x2.x() ? x2.x() : x.x());
	        int topBlockY = (int)(x.y() < x2.y() ? x2.y() : x.y());
	        int bottomBlockY = (int)(x.y() > x2.y() ? x2.y() : x.y());
	        int topBlockZ = (int)(x.z() < x2.z() ? x2.z() : x.z());
	        int bottomBlockZ = (int)(x.z() > x2.z() ? x2.z() : x.z());
	        for(int xx = bottomBlockX; xx <= topBlockX; xx++){
	            for(int zz = bottomBlockZ; zz <= topBlockZ; zz++){
	                for(int yy = bottomBlockY; yy <= topBlockY; yy++){
	                if(r.inResidence(new Position(xx,yy,zz))) {
				is=true;
				break;
			}
			}}}}
		return is;
	}

	public static Residence getResidence(World w,Position location) {
		Residence find = null;
		for(String s : getResidences(w)) {

			Residence a= getResidence(w,s);
			if(a!=null)
			if(a.inResidence(location)) {
				find=a;
				break;
			}
		}
		return find;
	}
	
	public static boolean isInsideResidence(World w, Position x, Position x2) {
		boolean is = true;
		String r = null;
		int topBlockX = (int)(x.x() < x2.x() ? x2.x() : x.x());
        int bottomBlockX = (int)(x.x() > x2.x() ? x2.x() : x.x());
        int topBlockY = (int)(x.y() < x2.y() ? x2.y() : x.y());
        int bottomBlockY = (int)(x.y() > x2.y() ? x2.y() : x.y());
        int topBlockZ = (int)(x.z() < x2.z() ? x2.z() : x.z());
        int bottomBlockZ = (int)(x.z() > x2.z() ? x2.z() : x.z());
        for(int xx = bottomBlockX; xx <= topBlockX; xx++){
            for(int zz = bottomBlockZ; zz <= topBlockZ; zz++){
                for(int yy = bottomBlockY; yy <= topBlockY; yy++){
                	Position l = new Position(xx,yy,zz);
    				Residence a = getResidence(w,l);
    				if(a==null) {
    					is=false;
    					break;
    				}
    				if(a.inResidence(l)) {
    					if(r==null)r=a.getName();
    					if(!r.equals(a.getName())) {
    						is=false;
    						break;
    					}
    				}else {
    					is=false;
    					break;
    				}
                }
            }
        }
		return is;
	}
	//must be inside residence
	public static boolean isInsideSubzone(World w, Position x, Position x2) {
		boolean is = true;
		String r = null;
		String f = null;
		int topBlockX = (int)(x.x() < x2.x() ? x2.x() : x.x());
        int bottomBlockX = (int)(x.x() > x2.x() ? x2.x() : x.x());
        int topBlockY = (int)(x.y() < x2.y() ? x2.y() : x.y());
        int bottomBlockY = (int)(x.y() > x2.y() ? x2.y() : x.y());
        int topBlockZ = (int)(x.z() < x2.z() ? x2.z() : x.z());
        int bottomBlockZ = (int)(x.z() > x2.z() ? x2.z() : x.z());
        for(int xx = bottomBlockX; xx <= topBlockX; xx++){
            for(int zz = bottomBlockZ; zz <= topBlockZ; zz++){
                for(int yy = bottomBlockY; yy <= topBlockY; yy++){
                	Position l = new Position(xx,yy,zz);
    				Residence a = getResidence(w,l);
    				if(a!=null)
    				if(r==null)r=a.getName();
    				if(a==null||!r.equals(a.getName())) {
    					is=false;
    					break;
    				}
    				if(a.inResidence(l)) {
    					Subzone t = a.getSubzone(l);
    					if(t!=null)
    					if(f==null)f=t.getName();
    					if(t==null||!t.getName().equals(f)) {
    						is=false;
    						break;
    					}
    				}else {
    					is=false;
    					break;
    				}
                }
            }}
		return is;
	}

}

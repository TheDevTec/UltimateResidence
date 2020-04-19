package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Group.SizeType;
import me.Straiker123.BlocksAPI;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;

public class ResidenceAPI {
	public void load() {
		if(Loader.loaded)return;
		Loader.loaded=true;
		TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l«------", TheAPI.getConsole());
		for(World w : Bukkit.getWorlds()) {
			int amount = 0;
			TheAPI.msg("&c&lUltimateResidence &8&l» &7Loading residences of world "+w.getName()+"..", TheAPI.getConsole());

			if(Loader.getData(w).getConfig().getString("Residence") != null)
			for(String s: Loader.getData(w).getConfig().getConfigurationSection("Residence").getKeys(false)) {
				for(String res: Loader.getData(w).getConfig().getConfigurationSection("Residence."+s).getKeys(false)) {
				++amount;
				Loader.map.put(res, new Residence(res,w,s));
				}
			}
			TheAPI.msg("&c&lUltimateResidence &8&l» &7Loaded "+amount+" residences in world "+w.getName()+".", TheAPI.getConsole());
		}
		TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l«------", TheAPI.getConsole());
	}
	
	public void unload() {
		Loader.loaded=false;
		Loader.map.clear();
		for(World w : Bukkit.getWorlds()) {
			Loader.getData(w).save();
		}
		TheAPI.msg("&c&lUltimateResidence &8&l» &7Residence unloaded. Goodbye!", TheAPI.getConsole());
	}
	
	public void reload() {

		for(World w : Bukkit.getWorlds()) 
		if(Loader.getData(w)!=null)
			Loader.getData(w).reload();
		Loader.a.clear();
		Loader.c.reload();
		unload();
		load();
	}
	
	public static Residence getResidence(String residence) {
		if(Loader.map.containsKey(residence))
		return Loader.map.get(residence);
		return null;
	}
	
	public static List<String> getResidences(World world, String player){
		return getData(player).getResidences(world.getName());
	}
	
	public static Residence getResidence(Player player) {
		return getResidence(player.getLocation());
	}

	private static List<String> getResidences(World world) {
		List<String> a = new ArrayList<String>();
		if(Loader.getData(world).getConfig().getString("Residence") != null)
		for(String s: Loader.getData(world).getConfig().getConfigurationSection("Residence").getKeys(false)) {
			for(String sd: Loader.getData(world).getConfig().getConfigurationSection("Residence."+s).getKeys(false)) {
			a.add(sd);
		}}
		return a;
	}

	public static void createResidence(World world, String owner,String res) {
		ConfigAPI a = Loader.getData(world);
		a.getConfig().set("Residence."+owner+"."+res+".Corners", 
				TheAPI.getStringUtils().getLocationAsString(ResEvents.locs.get(owner)[0])+":"+
				TheAPI.getStringUtils().getLocationAsString(ResEvents.locs.get(owner)[1]));
		a.getConfig().set("Residence."+owner+"."+res+".Members", Arrays.asList(owner));
		a.getConfig().set("Residence."+owner+"."+res+".Limit.Size", getData(owner).getGroup().getMaxSize(SizeType.X)+"x"+
				getData(owner).getGroup().getMaxSize(SizeType.Z));
		a.getConfig().set("Residence."+owner+"."+res+".Limit.Members", 100);
		a.getConfig().set("Residence."+owner+"."+res+".Limit.Residences", getData(owner).getGroup().getMaxResidences());
		a.getConfig().set("Residence."+owner+"."+res+".Limit.Subzones", getData(owner).getGroup().getMaxSubResidences());
		a.getConfig().set("Residence."+owner+"."+res+".Tp"
				,TheAPI.getStringUtils().getLocationAsString(Bukkit.getPlayer(owner).getLocation()));
		Residence r = new Residence(res, world, owner);
		Loader.map.put(res, r);
		getData(owner).addResidence(r);
	}
	
	public static boolean isTooBigSize(String player, Location l1, Location l2) {
		int x = Math.max(l1.getBlockX(), l2.getBlockX()) - Math.min(l1.getBlockX(), l2.getBlockX()) + 1;
		int z = Math.max(l1.getBlockZ(), l2.getBlockZ())-Math.min(l1.getBlockZ(), l2.getBlockZ())+1;
		return getData(player).getGroup().getMaxSize(SizeType.X) < x||getData(player).getGroup().getMaxSize(SizeType.X) < z;
	}

	public static Data getData(String player) {
		return new Data(player);
	}

	public static void deleteResidence(World world, String owner,String res) {
		getData(owner).removeResidence(getResidence(res));
		Loader.getData(world).getConfig().set("Residence."+owner+"."+res,null);
		Loader.map.remove(res);
	}

	public static List<String> getResidences(String owner) {
		return getData(owner).getResidences();
	}

	public static boolean isColliding(World world, Location x,Location z) {
		boolean is = false;
		for(String s : getResidences(world)) {
			for(Location l : new BlocksAPI().getBlockLocations(x, z))
			if(getResidence(s).inResidence(l)) {
				is=true;
				break;
			}
		}
		return is;
	}

	public static Residence getResidence(Location location) {
		String find = null;
		for(String s : getResidences(location.getWorld())) {
			if(getResidence(s)!=null)
			if(getResidence(s).inResidence(location)) {
				find= s;
				break;
			}
		}
		return getResidence(find);
	}

	public static boolean isInsideResidence(World world, Location x, Location z) {
		boolean is = true;
		for(String s : getResidences(world)) {
			for(Location l : new BlocksAPI().getBlockLocations(x, z))
			if(!getResidence(s).inResidence(l)) {
				is=false;
				break;
			}
		}
		return is;
	}

}

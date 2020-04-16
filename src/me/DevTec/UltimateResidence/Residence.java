package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.ResidenceFlag.ResidenceFlagType;
import me.Straiker123.TheAPI;

public class Residence {
	private Location l1,l2;
	private String name,owner;
	private World world;
	private int x,z;
	public Residence(String name, World w, String owner) {
		this.name=name;
		this.owner=owner;
		this.world=w;
		String[] sd = Loader.getData(w).getConfig().getString("Residence."+owner+"."+name+".Corners").split(":");
		l1=TheAPI.getStringUtils().getLocationFromString(sd[0]);
		l2=TheAPI.getStringUtils().getLocationFromString(sd[1]);
		 x = Math.max(l1.getBlockX(), l2.getBlockX()) - Math.min(l1.getBlockX(), l2.getBlockX()) + 1;
	     z = Math.max(l1.getBlockZ(), l2.getBlockZ())-Math.min(l1.getBlockZ(), l2.getBlockZ())+1;
	}

	public boolean isMember(String player) {
		return getMembers().contains(player);
	}

	public int getMaximumMembers() {
		return Loader.getData(world).getConfig().getInt("Residence."+owner+"."+name+".Limit.Members");
	}

	public List<Player> getPlayersInResidence(){
		List<Player> list = new ArrayList<Player>();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(inResidence(p))list.add(p);
		}
		return list;
	}
	
	public void setMaximumMembers(int value) {
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Limit.Members",value);
		Loader.getData(world).save();
	}

	/**
	 * @see see getSize()[0] - X
	 * getSize()[1] - Z
	 * @return int[]
	 */
	public int[] getSize() {
		return new int[] {x,z};
	}
	
	/**
	 * @see see getMaximumSize()[0] - X
	 * getMaximumSize()[1] - Z
	 * @return int[]
	 */
	public int[] getMaximumSize() {
		String[] s = Loader.getData(world).getConfig().getString("Residence."+owner+"."+name+".Limit.Size").split("x");
		return new int[] {TheAPI.getStringUtils().getInt(s[0]),TheAPI.getStringUtils().getInt(s[1])};
	}

	public void setMaximumSize(int x, int z) {
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Limit.Size",x+"x"+z);
		Loader.getData(world).save();
	}
	
	public int getMaximumSubzones() {
		return Loader.getData(world).getConfig().getInt("Residence."+owner+"."+name+".Limit.Subzones");
	}

	public void setMaximumSubzones(int value) {
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Limit.Subzones",value);
		Loader.getData(world).save();
	}

	public void setMaximumSize(int[] size) {
		if(size.length >= 1) {
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Limit.Size",size[0]+"x"+size[1]);
		Loader.getData(world).save();
		}
	}

	public void setTeleportLocation(Location location) {
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Tp",TheAPI.getStringUtils().getLocationAsString(location));
		Loader.getData(world).save();
	}

	public Location getTeleportLocation() {
		return TheAPI.getStringUtils().getLocationFromString(Loader.getData(world).getConfig().getString("Residence."+owner+"."+name+".Tp"));
	}
	
	public void addMember(String player) {
		List<String> a = getMembers();
		a.add(player);
		Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Members",a);
		Loader.getData(world).save();
	}
	
	public List<String> getMembers(){
		return Loader.getData(world).getConfig().getStringList("Residence."+owner+"."+name+".Members");
	}
	
	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return world;
	}
	
	/**
	 * @see see getCorners()[0] - corner #1
	 * getCorners()[1] - corner #2
	 * @return Location[]
	 */
	public Location[] getCorners() {
		return new Location[]{l1,l2};
	}
	
	public List<ResidenceFlag> getFlags(){
		List<ResidenceFlag> a = new ArrayList<ResidenceFlag>();
		for(String s : Loader.getData(world).getConfig().getStringList("Residence."+owner+"."+name+".Flags-Global")){
			String[] f = s.split(":");
			a.add(new ResidenceFlag(null,ResidenceFlagType.valueOf(f[0]),Boolean.valueOf(f[1])));
		}
		for(String s : Loader.getData(world).getConfig().getStringList("Residence."+owner+"."+name+".Flags-Player")) {
			String[] f = s.split(":");
			a.add(new ResidenceFlag(f[0],ResidenceFlagType.valueOf(f[1]),Boolean.valueOf(f[2])));
		}
		return a;
	}

	public void setFlag(ResidenceFlag flag) {
		if(flag.getPlayer() != null) {
			List<String> a = Loader.getData(world).getConfig().getStringList("Residence."+owner+"."+name+".Flags-Player");
			a.add(flag.getPlayer()+":"+flag.getType().name()+":"+flag.getValue());
			Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Flags-Player",a);
			Loader.getData(world).save();
		}else {
			List<String> a = Loader.getData(world).getConfig().getStringList("Residence."+owner+"."+name+".Flags-Global");
			a.add(flag.getType().name()+":"+flag.getValue());
			Loader.getData(world).getConfig().set("Residence."+owner+"."+name+".Flags-Global",a);
			Loader.getData(world).save();
		}
	}

	public void setFlag(String name, boolean value) {
		setFlag(new ResidenceFlag(null,ResidenceFlagType.valueOf(name.toUpperCase()),value));
	}

	public void setFlag(String player, String name, boolean value) {
		setFlag(new ResidenceFlag(player,ResidenceFlagType.valueOf(name.toUpperCase()),value));
	}

	public void setFlag(ResidenceFlagType name, boolean value) {
		setFlag(new ResidenceFlag(null,name,value));
	}

	public void setFlag(String player, ResidenceFlagType name, boolean value) {
		setFlag(new ResidenceFlag(player,name,value));
	}
	
	public boolean inResidence(Location location){
        return new IntRange(l1.getX(), l2.getX()).containsDouble(location.getX())
                && new IntRange(l1.getY(), l2.getY()).containsDouble(location.getY())
                &&  new IntRange(l1.getZ(), l2.getZ()).containsDouble(location.getZ());
    }

	public boolean inResidence(Player player){
		return inResidence(player.getLocation());
	}

	public boolean getFlag(Player s, ResidenceFlagType flag) {
		boolean a = false;
		for(ResidenceFlag f : getFlags()) {
			if(f.getType()==flag && f.getPlayer().equals(s.getName())) {
				a=f.getValue();
				break;
			}
		}
		return a;
	}

	public boolean getFlag(ResidenceFlagType flag) {
		boolean a = false;
		for(ResidenceFlag f : getFlags()) {
			if(f.getType()==flag && f.getPlayer()==null) {
				a=f.getValue();
				break;
			}
		}
		return a;
	}

}

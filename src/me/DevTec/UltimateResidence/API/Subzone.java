package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.BlocksAPI.BlocksAPI;
import me.DevTec.TheAPI.ConfigAPI.Section;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Executor;

public class Subzone {
	private Residence r;
	private double[] size;
	private String s;
	private Position[] l;
	private Section c;
	public Subzone(Residence residence, String name, Section configurationSection) {
		r=residence;
		s=name;
		c=configurationSection;
		String[] corners = c.getString("Corners").split(":");
		l=new Position[] {Position.fromString(corners[0]),Position.fromString(corners[1])};
		size = new double[] {Math.max(l[0].getBlockX(), l[1].getBlockX()) - Math.min(l[0].getBlockX(), l[1].getBlockX()) + 1
	    , Math.max(l[0].getBlockZ(), l[1].getBlockZ())-Math.min(l[0].getBlockZ(), l[1].getBlockZ())+1};
	}

	/**
	 * @see see getSize()[0] - X
	 * getSize()[1] - Z
	 * @return int[]
	 */
	public double[] getSize() {
		return size;
	}
	
	/**
	 * @see see getMaximumSize()[0] - X
	 * getMaximumSize()[1] - Z
	 * @return int[]
	 */
	public int[] getMaximumSize() {
		String[]d = c.getString("Residence."+r.getName()+".Limit.Size").split("x");
		return new int[] {StringUtils.getInt(d[0]),StringUtils.getInt(d[1])};
	}

	public void setSpawn(Position location) {
		c.set("Tp",location.toString());
	}

	public Position getSpawn() {
		return Position.fromString(c.getString("Tp"));
	}
	
	public String getName() {
		return s;
	}
	
	public Residence getResidence() {
		return r;
	}
	
	public Position[] getCorners() {
		return l;
	}
	
	public List<Player> getPlayers(){
		List<Player> list = new ArrayList<Player>();
		for(Player p : r.getPlayers())
			if(r.inside(p) && inside(p))list.add(p);
		return list;
	}

	public boolean getFlag(Flag f){
		return (Boolean)new Executor(new Callable<Boolean>() {
			public Boolean call() {
				boolean fr = false;
				for(String a : getFlags()) {
					String[] s = a.split(":");
					if(s[0].equalsIgnoreCase(f.name())) {
						fr=Boolean.valueOf(s[1]);
						break;
					}
				}
				return fr;
			}
		}).get();
	}

	public boolean getPlayerFlag(Flag f, String player){
		if(getMembers().contains(player))return true; //members has all flags!
		return (Boolean) new Executor(new Callable<Boolean>() {
			public Boolean call() {
				boolean fr = false;
				if(getMembers().contains(player))return true; //members has all flags!
				for(String a : getPlayerFlags()) {
					String[] s = a.split(":");
					if(s[1].equalsIgnoreCase(f.name()) && s[0].equals(player)) {
						fr=Boolean.valueOf(s[2]);
						break;
					}
				}
				return fr;
			}
		}).get();
	}
	public List<String> getFlags(){
		return c.getStringList("Flags-Global");
	}

	public List<String> getPlayerFlags(){
		return c.getStringList("Flags-Player");
	}
	
	public void setFlag(Flag flag, boolean value) {
		List<String> a = getFlags();
		if(a.contains(flag.name()+":"+value))return;
		if(a.contains(flag.name()+":"+(!value)))a.remove(flag.name()+":"+(!value));
		a.add(flag.name()+":"+value);
		c.set("Flags-Global",a);
		Loader.getData(l[0].getWorld()).save();
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
		List<String> b = getPlayerFlags();
		if(b.contains(player+":"+flag.name()+":"+value))return;
		if(b.contains(player+":"+flag.name()+":"+(!value)))b.remove(player+":"+flag.name()+":"+(!value));
		b.add(player+":"+flag.name()+":"+value);
		c.set("Flags-Player",b);
		Loader.getData(l[0].getWorld()).save();
	}

	public boolean inside(Player player){
		return inside(new Position(player.getLocation()));
	}

	public boolean inside(Entity ent){
		return inside(new Position(ent.getLocation()));
	}

	public boolean inside(Location loc){
		return inside(new Position(loc));
	}

	public boolean inside(Position c) {
        return BlocksAPI.isInside(c,l[0],l[1]);
	}
	
	public boolean isMember(String player) {
		return getMembers().contains(player);
	}

	public void addMember(String player) {
		List<String> a = getMembers();
		if(a.contains(player))return;
		a.add(player);
		c.set("Members",a);
		Loader.getData(l[0].getWorld()).save();
	}
	
	public void removeMember(String player) {
		List<String> a = getMembers();
		if(!a.contains(player))return;
		a.remove(player);
		c.set("Members",a);
		Loader.getData(l[0].getWorld()).save();
	}
	
	public List<String> getMembers(){
		return c.getStringList("Members");
	}
	
	public void setOwner(String name) {
		c.set("Owner",name);
		Loader.getData(l[0].getWorld()).save();
	}
	
	public String getOwner() {
		return c.getString("Owner");
	}
}

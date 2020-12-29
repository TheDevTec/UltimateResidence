package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.blocksapi.BlocksAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.datakeeper.maps.MultiMap;

public class Residence {
	private Position[] l;
	private final String name;
	private final String c;
	private final Data d;
	private double[] size;
	//Residence, SubzoneName, Subzone
	private static final MultiMap<String, String, Subzone> cache = new MultiMap<>();
	public Residence(String name, Data d, String sec) {
		this.name=name;
		c=sec;
		this.d=d;
		String[] sd = d.getString(c+".Corners").split(":");
		l=new Position[] {Position.fromString(sd[0]),Position.fromString(sd[1])};
		size = new double[] { Math.max(l[0].getBlockX(), l[1].getBlockX()) - Math.min(l[0].getBlockX(), l[1].getBlockX()) + 1, Math.max(l[0].getBlockZ(), l[1].getBlockZ())-Math.min(l[0].getBlockZ(), l[1].getBlockZ())+1};
	}

	public boolean isMember(String player) {
		return getMembers().contains(player);
	}

	public List<Player> getPlayers(){
		List<Player> list = new ArrayList<Player>();
		for(Player p : TheAPI.getOnlinePlayers()) {
			if(inside(p))list.add(p);
		}
		return list;
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
		String[] s = d.getString(c+".Limit.Size").split("x");
		return new int[] {StringUtils.getInt(s[0]),StringUtils.getInt(s[1])};
	}

	public void setMaximumSize(int x, int z) {
		d.set(c+".Limit.Size",x+"x"+z);
		d.save();
	}
	
	public int getMaximumSubzones() {
		return d.getInt(c+".Limit.Subzones");
	}

	public void setMaximumSubzones(int value) {
		d.set(c+".Limit.Subzones",value);
		d.save();
	}

	public void setMaximumSize(int[] size) {
		if(size.length >= 1) {
		d.set(c+".Limit.Size",size[0]+"x"+size[1]);
		d.save();
		}
	}

	public void setSpawn(Position location) {
		d.set(c+".Tp",location.toString());
		d.save();
	}

	public Position getSpawn() {
		return Position.fromString(d.getString(c+".Tp"));
	}
	
	public void addMember(String player) {
		List<String> a = getMembers();
		if(a.contains(player))return;
		a.add(player);
		d.set(c+".Members",a);
		d.save();
	}
	
	public void removeMember(String player) {
		List<String> a = getMembers();
		if(!a.contains(player))return;
		a.remove(player);
		d.set(c+".Members",a);
		d.save();
	}
	
	public List<String> getMembers(){
		return d.getStringList(c+".Members");
	}
	
	public String getOwner() {
		return d.getString(c+".Owner");
	}

	public String getName() {
		return name;
	}

	public World getWorld() {
		return l[0].getWorld();
	}
	
	/**
	 * @see see getCorners()[0] - corner #1
	 * getCorners()[1] - corner #2
	 * @return Location[]
	 */
	public Position[] getCorners() {
		return l;
	}

	public boolean getFlag(Flag f){
		boolean fr = false;
		for(Object a : getFlags()) {
			String[] s = a.toString().split(":");
			if(s[0].equalsIgnoreCase(f.name())) {
				fr=Boolean.valueOf(s[1]);
				break;
			}
		}
		return fr;
	}

	public boolean getPlayerFlag(Flag f, String player){
		boolean fr = false;
		if(getMembers().contains(player))return true; //members has all flags!
		for(Object a : getPlayerFlags()) {
			String[] s = a.toString().split(":");
			if(s[1].equalsIgnoreCase(f.name()) && s[0].equals(player)) {
				fr=Boolean.valueOf(s[2]);
				break;
			}
		}
		return fr;
	}
	public Collection<Object> getFlags(){
		return d.getList(c+".Flags-Global");
	}

	public Collection<Object> getPlayerFlags(){
		return d.getList(c+".Flags-Player");
	}
	
	public void setFlag(Flag flag, boolean value) {
		Collection<Object> a = getFlags();
		if(a.contains(flag.name()+":"+value))return;
		if(a.contains(flag.name()+":"+(!value)))a.remove(flag.name()+":"+(!value));
		a.add(flag.name()+":"+value);
		d.set(c+".Flags-Global",a);
		d.save();
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
		Collection<Object> b = getPlayerFlags();
		if(b.contains(player+":"+flag.name()+":"+value))return;
		if(b.contains(player+":"+flag.name()+":"+(!value)))b.remove(player+":"+flag.name()+":"+(!value));
		b.add(player+":"+flag.name()+":"+value);
		d.set(c+".Flags-Player",b);
		d.save();
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
        return BlocksAPI.isInside(c, l[0], l[1]);
	}

	public List<String> getSubzones() {
		return new ArrayList<String>(d.getKeys(c+".Subzone"));
	}
	
	public Subzone getSubzone(Player a) {
		return getSubzone(new Position(a.getLocation()));
	}
	
	public Subzone getSubzone(Location a) {
		return getSubzone(new Position(a));
	}

	public Subzone getSubzone(Position c) {
		Subzone d= null;
		for(String s: getSubzones()) {
			Subzone f = getSubzone(s);
			if(f.inside(c)) {
				d=f;
				break;
			}
		}
		return d;
	}

	public Subzone getSubzone(String name) {
		if(getSubzones().contains(name)) {
			Subzone s = cache.containsKey(name) ? cache.get(Residence.this.name,name) : null;
			if(s==null) {
				s=new Subzone(Residence.this, name, d, c+".Subzone."+name);
				cache.put(Residence.this.name, name, s);
			}
		return s;
		}
		return null;
	}

	public Subzone createSubzone(String string, Position location, Position location2) {
		d.set(c+".Subzone."+string+".Corners",
				StringUtils.getLocationAsString(location.toLocation())+":"+StringUtils.getLocationAsString(location2.toLocation()));
		d.set(c+".Subzone."+string+".Tp",StringUtils.getLocationAsString(location.toLocation()));
		d.set(c+".Subzone."+string+".Owner",getOwner());
		d.set(c+".Subzone."+string+".Members",Arrays.asList(getOwner()));
		d.save();
		Subzone c = new Subzone(this, string, d, this.c+".Subzone."+string);
		cache.put(name, string, c);
		return c;
	}

	public void removeSubzone(Subzone z2) {
		removeSubzone(z2.getName());
	}

	public void removeSubzone(String z2) {
		cache.remove(name, z2);
		d.remove(c+".Suzone."+z2);
		d.save();
	}

}

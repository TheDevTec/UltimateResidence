package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Loader;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;

public class Residence {
	private Location[] l;
	private String name,owner;
	private World world;
	private int x,z;
	private ConfigAPI ac;
	public Residence(String name, World w, String owner) {
		this.name=name;
		this.owner=owner;
		this.world=w;
		ac=Loader.getData(world);
		String[] sd = ac.getString("Residence."+name+".Corners").split(":");
		l=new Location[] {TheAPI.getStringUtils().getLocationFromString(sd[0]),TheAPI.getStringUtils().getLocationFromString(sd[1])};
		x = Math.max(l[0].getBlockX(), l[1].getBlockX()) - Math.min(l[0].getBlockX(), l[1].getBlockX()) + 1;
	    z = Math.max(l[0].getBlockZ(), l[1].getBlockZ())-Math.min(l[0].getBlockZ(), l[1].getBlockZ())+1;
	}

	public boolean isMember(String player) {
		return getMembers().contains(player);
	}

	public List<Player> getPlayersInResidence(){
		List<Player> list = new ArrayList<Player>();
		for(Player p : TheAPI.getOnlinePlayers()) {
			if(inResidence(p))list.add(p);
		}
		return list;
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
		String[] s = ac.getString("Residence."+name+".Limit.Size").split("x");
		return new int[] {TheAPI.getStringUtils().getInt(s[0]),TheAPI.getStringUtils().getInt(s[1])};
	}

	public void setMaximumSize(int x, int z) {
		ac.set("Residence."+name+".Limit.Size",x+"x"+z);
		ac.save();
	}
	
	public int getMaximumSubzones() {
		return ac.getInt("Residence."+name+".Limit.Subzones");
	}

	public void setMaximumSubzones(int value) {
		ac.set("Residence."+name+".Limit.Subzones",value);
		ac.save();
	}

	public void setMaximumSize(int[] size) {
		if(size.length >= 1) {
		ac.set("Residence."+name+".Limit.Size",size[0]+"x"+size[1]);
		ac.save();
		}
	}

	public void setTeleportLocation(Location location) {
		ac.set("Residence."+name+".Tp",TheAPI.getStringUtils().getLocationAsString(location));
		ac.save();
	}

	public Location getTeleportLocation() {
		return TheAPI.getStringUtils().getLocationFromString(ac.getString("Residence."+name+".Tp"));
	}
	
	public void addMember(String player) {
		List<String> a = getMembers();
		if(a.contains(player))return;
		a.add(player);
		ac.set("Residence."+name+".Members",a);
		ac.save();
	}
	
	public void removeMember(String player) {
		List<String> a = getMembers();
		if(!a.contains(player))return;
		a.remove(player);
		ac.set("Residence."+name+".Members",a);
		ac.save();
	}
	
	public List<String> getMembers(){
		return ac.getStringList("Residence."+name+".Members");
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
		return l;
	}

	public boolean getFlag(Flag f){
		boolean fr = false;
		for(ResidenceFlag a : getFlags()) {
			if(a.getFlag()==f) {
				fr=a.getValue();
				break;
			}
		}
		return fr;
	}

	public boolean getPlayerFlag(Flag f, String player){
		boolean fr = false;
		if(getMembers().contains(player))return true; //members has all flags!
		for(ResidenceFlag a : getPlayerFlags()) {
			if(a.getFlag()==f && a.getPlayer().equals(player)) {
				fr=a.getValue();
				break;
			}
		}
		return fr;
	}

	public List<ResidenceFlag> getFlags(){
		List<ResidenceFlag> a = new ArrayList<ResidenceFlag>();
		for(String s :ac.getStringList("Residence."+name+".Flags-Global")){
			String[] f = s.split(":");
			a.add(new ResidenceFlag(null,Flag.valueOf(f[0]),Boolean.valueOf(f[1])));
		}
		return a;
	}

	public List<ResidenceFlag> getPlayerFlags(){
		List<ResidenceFlag> a = new ArrayList<ResidenceFlag>();
		for(String s :ac.getStringList("Residence."+name+".Flags-Player")) {
			String[] f = s.split(":");
			a.add(new ResidenceFlag(f[0],Flag.valueOf(f[1]),Boolean.valueOf(f[2])));
		}
		return a;
	}
	
	public void setFlag(Flag flag, boolean value) {
			List<String> a = ac.getStringList("Residence."+name+".Flags-Global");
			if(a.contains(flag.name()+":"+value))return;
			if(a.contains(flag.name()+":"+(!value)))a.remove(flag.name()+":"+(!value));
			a.add(flag.name()+":"+value);
			ac.set("Residence."+name+".Flags-Global",a);
			ac.save();
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
			List<String> a = ac.getStringList("Residence."+name+".Flags-Player");
			if(a.contains(player+":"+flag.name()+":"+value))return;
			if(a.contains(player+":"+flag.name()+":"+(!value)))a.remove(player+":"+flag.name()+":"+(!value));
			a.add(player+":"+flag.name()+":"+value);
			ac.set("Residence."+name+".Flags-Player",a);
			ac.save();
	}

	public boolean inResidence(Player player){
		return TheAPI.getBlocksAPI().isInside(player.getLocation(),l[0],l[1]);
	}

	public boolean inResidence(Location loc){
		return TheAPI.getBlocksAPI().isInside(loc,l[0],l[1]);
	}

	public List<String> getSubzones() {
		List<String> a = new ArrayList<String>();
		if(ac.getString("Residence."+name+".Subzone")!=null)
		for(String s : ac.getConfigurationSection("Residence."+name+".Subzone",false))
			a.add(s);
		return a;
	}
	
	public Subzone getSubzone(Player a) {
		return getSubzone(a.getLocation());
	}
	
	public Subzone getSubzone(Location a) {
		Subzone d= null;
		for(String s: getSubzones()) {
			Subzone f = getSubzone(s);
			if(f.inSubzone(a)) {
				d=f;
				break;
			}
		}
		return d;
	}

	public Subzone getSubzone(Position c) {
		Subzone d= null;
		for(String s: getSubzones()) {
			Subzone f = getSubzone(s);
			if(f.inSubzone(c)) {
				d=f;
				break;
			}
		}
		return d;
	}

	public Subzone getSubzone(String name) {
		if(getSubzones().contains(name))
		return new Subzone(this,name);
		return null;
	}

	public void createSubzone(String string, Location location, Location location2) {
		ac.set("Residence."+name+".Subzone."+string+".Corners",
				TheAPI.getStringUtils().getLocationAsString(location)+":"+
				TheAPI.getStringUtils().getLocationAsString(location2));
		ac.set("Residence."+name+".Subzone."+string+".Tp",
				TheAPI.getStringUtils().getLocationAsString(location));
		ac.set("Residence."+name+".Subzone."+string+".Owner",getOwner());
		ac.set("Residence."+name+".Subzone."+string+".Members",Arrays.asList(getOwner()));
		ac.save();
	}

	public void removeSubzone(Subzone z2) {
		ac.set("Residence."+name+".Subzone."+z2.getName(),null);
		ac.save();
	}

	public void removeSubzone(String z2) {
		ac.set("Residence."+name+".Suzone."+z2,null);
		ac.save();
	}

	public boolean inResidence(Position c) {
        return new IntRange(l[0].getX(), l[1].getX()).containsDouble(c.x())
                && new IntRange(l[0].getY(), l[1].getY()).containsDouble(c.y())
                &&  new IntRange(l[0].getZ(), l[1].getZ()).containsDouble(c.z());
	}

}

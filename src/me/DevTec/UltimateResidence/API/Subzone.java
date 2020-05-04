package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.IntRange;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Loader;
import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;

public class Subzone {
	private Residence r;
	private int x,z;
	private String s;
	private Location[] l;
	private ConfigAPI a;
	public Subzone(Residence residence, String name) {
		r=residence;
		s=name;
		a=Loader.getData(r.getWorld());
		String[] corners = a.getString("Residence."+r.getName()+".Subzone."+s+".Corners").split(":");
		l=new Location[] {TheAPI.getStringUtils().getLocationFromString(corners[0]),
		TheAPI.getStringUtils().getLocationFromString(corners[1])};
		x = Math.max(l[0].getBlockX(), l[1].getBlockX()) - Math.min(l[0].getBlockX(), l[1].getBlockX()) + 1;
	    z = Math.max(l[0].getBlockZ(), l[1].getBlockZ())-Math.min(l[0].getBlockZ(), l[1].getBlockZ())+1;
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
		String[]d = a.getString("Residence."+r.getName()+".Limit.Size").split("x");
		return new int[] {TheAPI.getStringUtils().getInt(d[0]),TheAPI.getStringUtils().getInt(d[1])};
	}

	public void setTeleportLocation(Location location) {
		a.set("Residence."+r.getName()+".Subzone."+s+".Tp",TheAPI.getStringUtils().getLocationAsString(location));
		a.save();
	}

	public Location getTeleportLocation() {
		return TheAPI.getStringUtils().getLocationFromString(a.getString("Residence."+r.getName()+".Subzone."+s+".Tp"));
	}
	
	public String getName() {
		return s;
	}
	
	public Residence getResidence() {
		return r;
	}
	
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
		for(ResidenceFlag a : getPlayerFlags()) {
			if(a.getFlag()==f && a.getPlayer().equals(player)) {
				fr=a.getValue();
				break;
			}
		}
		return fr;
	}
	
	public List<Player> getPlayersInSubzone(){
		List<Player> list = new ArrayList<Player>();
		for(Player p : TheAPI.getOnlinePlayers())
			if(r.inResidence(p) && inSubzone(p))list.add(p);
		return list;
	}

	public List<ResidenceFlag> getFlags(){
		List<ResidenceFlag> d = new ArrayList<ResidenceFlag>();
		for(String s : a.getStringList("Residence."+r.getName()+".Subzone."+s+".Flags-Global")){
			String[] f = s.split(":");
			d.add(new ResidenceFlag(null,Flag.valueOf(f[0]),Boolean.valueOf(f[1])));
		}
		return d;
	}

	public List<ResidenceFlag> getPlayerFlags(){
		List<ResidenceFlag> d = new ArrayList<ResidenceFlag>();
		for(String s : a.getStringList("Residence."+r.getName()+".Subzone."+s+".Flags-Player")) {
			String[] f = s.split(":");
			d.add(new ResidenceFlag(f[0],Flag.valueOf(f[1]),Boolean.valueOf(f[2])));
		}
		return d;
	}
	
	public void setFlag(Flag flag, boolean value) {
			List<String> a = this.a.getStringList("Residence."+r.getName()+".Subzone."+s+".Flags-Global");
			if(a.contains(flag.name()+":"+value))return;
			if(a.contains(flag.name()+":"+(!value)))a.remove(flag.name()+":"+(!value));
			a.add(flag.name()+":"+value);
			this.a.set("Residence."+r.getName()+".Subzone."+s+".Flags-Global",a);
			this.a.save();
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
			List<String> a = this.a.getStringList("Residence."+r.getName()+".Subzone."+s+".Flags-Player");
			if(a.contains(player+":"+flag.name()+":"+value))return;
			if(a.contains(player+":"+flag.name()+":"+(!value)))a.remove(player+":"+flag.name()+":"+(!value));
			a.add(player+":"+flag.name()+":"+value);
			this.a.set("Residence."+r.getName()+".Subzone."+s+".Flags-Player",a);
			this.a.save();
	}

	public boolean inSubzone(Player player){
		return TheAPI.getBlocksAPI().isInside(player.getLocation(),l[0],l[1]);
	}

	public boolean inSubzone(Location loc){
		return TheAPI.getBlocksAPI().isInside(loc,l[0],l[1]);
	}
	
	public boolean isMember(String player) {
		return getMembers().contains(player);
	}

	public void addMember(String player) {
		List<String> a = getMembers();
		if(a.contains(player))return;
		a.add(player);
		this.a.set("Residence."+r.getName()+".Subzone."+s+".Members",a);
		this.a.save();
	}
	
	public void removeMember(String player) {
		List<String> a = getMembers();
		if(!a.contains(player))return;
		a.remove(player);
		this.a.set("Residence."+r.getName()+".Subzone."+s+".Members",a);
		this.a.save();
	}
	
	public List<String> getMembers(){
		return this.a.getStringList("Residence."+r.getName()+".Subzone."+s+".Members");
	}
	
	public String getOwner() {
		return this.a.getString("Residence."+r.getName()+".Subzone."+s+".Owner");
	}

	public boolean inSubzone(Position c) {
        return new IntRange(l[0].getX(), l[1].getX()).containsDouble(c.x())
                && new IntRange(l[0].getY(), l[1].getY()).containsDouble(c.y())
                &&  new IntRange(l[0].getZ(), l[1].getZ()).containsDouble(c.z());
	}
}

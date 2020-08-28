package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.BlocksAPI.BlocksAPI;
import me.DevTec.TheAPI.MultiHashMap.MultiMap;
import me.DevTec.TheAPI.Utils.Position;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.UltimateResidence.Utils.Executor;

public class Residence {
	private Position[] l;
	private final String name;
	private final ConfigurationSection c;
	private double[] size;
	//Residence, SubzoneName, Subzone
	private static final MultiMap<String, String, Subzone> cache = new MultiMap<>();
	public Residence(String name, ConfigurationSection sec) {
		this.name=name;
		c=sec;
		String[] sd = c.getString("Corners").split(":");
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
		String[] s = c.getString("Limit.Size").split("x");
		return new int[] {StringUtils.getInt(s[0]),StringUtils.getInt(s[1])};
	}

	public void setMaximumSize(int x, int z) {
		c.set("Limit.Size",x+"x"+z);
	}
	
	public int getMaximumSubzones() {
		return c.getInt("Limit.Subzones");
	}

	public void setMaximumSubzones(int value) {
		c.set("Limit.Subzones",value);
	}

	public void setMaximumSize(int[] size) {
		if(size.length >= 1) {
		c.set("Limit.Size",size[0]+"x"+size[1]);
		}
	}

	public void setSpawn(Position location) {
		c.set("Tp",location.toString());
	}

	public Position getSpawn() {
		return Position.fromString(c.getString("Tp"));
	}
	
	public void addMember(String player) {
		List<String> a = getMembers();
		if(a.contains(player))return;
		a.add(player);
		c.set("Members",a);
	}
	
	public void removeMember(String player) {
		List<String> a = getMembers();
		if(!a.contains(player))return;
		a.remove(player);
		c.set("Members",a);
	}
	
	public List<String> getMembers(){
		return c.getStringList("Members");
	}
	
	public String getOwner() {
		return c.getString("Owner");
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
		return (Boolean)new Executor(new Callable<Boolean>() {
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
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
		List<String> b = getPlayerFlags();
		if(b.contains(player+":"+flag.name()+":"+value))return;
		if(b.contains(player+":"+flag.name()+":"+(!value)))b.remove(player+":"+flag.name()+":"+(!value));
		b.add(player+":"+flag.name()+":"+value);
		c.set("Flags-Player",b);
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
		List<String> a = new ArrayList<String>();
		if(c.getString("Subzone")!=null)
		a.addAll(c.getConfigurationSection("Subzone").getKeys(false));
		return a;
	}
	
	public Subzone getSubzone(Player a) {
		return getSubzone(new Position(a.getLocation()));
	}
	
	public Subzone getSubzone(Location a) {
		return getSubzone(new Position(a));
	}

	public Subzone getSubzone(Position c) {
		   return (Subzone)new Executor(new Callable<Subzone>() {
		    	@Override
		        public Subzone call() {
		    		Subzone d= null;
		    		for(String s: getSubzones()) {
		    			Subzone f = getSubzone(s);
		    			if(f.inside(c)) {
		    				d=f;
		    				break;
		    			}
		    		}
		    		return d;
		        }}).get();
	}

	public Subzone getSubzone(String name) {
		   return (Subzone)new Executor(new Callable<Subzone>() {
		    	@Override
		        public Subzone call() {
		    		if(getSubzones().contains(name)) {
		    			Subzone s = cache.containsKey(name) ? cache.get(Residence.this.name,name) : null;
		    			if(s==null) {
		    				s=new Subzone(Residence.this, name, c.getConfigurationSection("Subzone."+name));
		    				cache.put(Residence.this.name, name, s);
		    			}
		    		return s;
		    		}
		    		return null;
		        }}).get();
	}

	public Subzone createSubzone(String string, Position location, Position location2) {
		c.set("Subzone."+string+".Corners",
				location.toString()+":"+location2.toString());
		c.set("Subzone."+string+".Tp",location.toLocation());
		c.set("Subzone."+string+".Owner",getOwner());
		c.set("Subzone."+string+".Members",Arrays.asList(getOwner()));
		Subzone c = new Subzone(this, string, this.c.getConfigurationSection("Subzone."+string));
		cache.put(name, string, c);
		return c;
	}

	public void removeSubzone(Subzone z2) {
		removeSubzone(z2.getName());
	}

	public void removeSubzone(String z2) {
		cache.removeThread(name, z2);
		c.set("Suzone."+z2,null);
	}

}

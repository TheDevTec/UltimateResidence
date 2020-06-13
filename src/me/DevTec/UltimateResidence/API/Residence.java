package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.DevTec.ConfigAPI;
import me.DevTec.TheAPI;
import me.DevTec.Other.Position;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Executor;

public class Residence {
	private Position[] l;
	private String name,owner;
	private World world;
	private double[] size;
	private ConfigAPI ac;
	private List<String> a = Lists.newArrayList(), b = Lists.newArrayList();
	private HashMap<String, Subzone> cache = Maps.newHashMap();
	public Residence(String name, World w, String owner) {
		this.name=name;
		this.owner=owner;
		this.world=w;
		ac=Loader.getData(world);
		String[] sd = ac.getString("Residence."+name+".Corners").split(":");
		l=new Position[] {Position.fromString(sd[0]),Position.fromString(sd[1])};
		size = new double[] { Math.max(l[0].getBlockX(), l[1].getBlockX()) - Math.min(l[0].getBlockX(), l[1].getBlockX()) + 1, Math.max(l[0].getBlockZ(), l[1].getBlockZ())-Math.min(l[0].getBlockZ(), l[1].getBlockZ())+1};
		a=ac.getStringList("Residence."+name+".Flags-Global");
		b=ac.getStringList("Residence."+name+".Flags-Player");
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

	public void setSpawn(Position location) {
		ac.set("Residence."+name+".Tp",location.toString());
		ac.save();
	}

	public Position getSpawn() {
		return Position.fromString(ac.getString("Residence."+name+".Tp"));
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
	public Position[] getCorners() {
		return l;
	}

	public boolean getFlag(Flag f){
		return new Executor<Boolean>().get(new Callable<Boolean>() {
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
		});
	}

	public boolean getPlayerFlag(Flag f, String player){
		return new Executor<Boolean>().get(new Callable<Boolean>() {
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
		});
	}
	public List<String> getFlags(){
		return a;
	}

	public List<String> getPlayerFlags(){
		return b;
	}
	
	public void setFlag(Flag flag, boolean value) {
			if(a.contains(flag.name()+":"+value))return;
			if(a.contains(flag.name()+":"+(!value)))a.remove(flag.name()+":"+(!value));
			a.add(flag.name()+":"+value);
			ac.set("Residence."+name+".Flags-Global",a);
			ac.save();
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
			if(b.contains(player+":"+flag.name()+":"+value))return;
			if(b.contains(player+":"+flag.name()+":"+(!value)))b.remove(player+":"+flag.name()+":"+(!value));
			b.add(player+":"+flag.name()+":"+value);
			ac.set("Residence."+name+".Flags-Player",b);
			ac.save();
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
        return TheAPI.getBlocksAPI().isInside(c, l[0], l[1]);
	}

	public List<String> getSubzones() {
		List<String> a = new ArrayList<String>();
		if(ac.exist("Residence."+name+".Subzone"))
		a.addAll(ac.getKeys("Residence."+name+".Subzone"));
		return a;
	}
	
	public Subzone getSubzone(Player a) {
		return getSubzone(a.getLocation());
	}
	
	public Subzone getSubzone(Location a) {
		   return new Executor<Subzone>().get(new Callable<Subzone>() {
		    	@Override
		        public Subzone call() {
		    		Subzone d= null;
		    		for(String s: getSubzones()) {
		    			Subzone f = getSubzone(s);
		    			if(f.inside(a)) {
		    				d=f;
		    				break;
		    			}
		    		}
		    		return d;
		        }});
	}

	public Subzone getSubzone(Position c) {
		   return new Executor<Subzone>().get(new Callable<Subzone>() {
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
		        }});
	}

	public Subzone getSubzone(String name) {
		   return new Executor<Subzone>().get(new Callable<Subzone>() {
		    	@Override
		        public Subzone call() {
		    		if(getSubzones().contains(name)) {
		    			Subzone s = cache.containsKey(name) ? cache.get(name) : null;
		    			if(s==null) {
		    				s=new Subzone(Residence.this, name);
		    				cache.put(name, s);
		    			}
		    		return s;
		    		}
		    		return null;
		        }});
	}

	public void createSubzone(String string, Position location, Position location2) {
		ac.set("Residence."+name+".Subzone."+string+".Corners",
				location.toString()+":"+location2.toString());
		ac.set("Residence."+name+".Subzone."+string+".Tp",location.toLocation());
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

}

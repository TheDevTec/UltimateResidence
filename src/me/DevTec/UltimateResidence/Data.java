package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.List;

import me.Straiker123.ConfigAPI;

public class Data {
	private String s;
	private ConfigAPI c;
	public Data(String player) {
		s=player;
		c=new ConfigAPI("UltimateResidence/User",player);
		c.create();
	}
	
	public ConfigAPI getConfigAPI() {
		return c;
	}
	
	public String getName() {
		return s;
	}


	public Group getGroup() {
		return new Group(c.getConfig().getString("Group"));
	}

	public List<String> getResidences(String world) {
		return c.getConfig().getStringList("Residences."+world);
	}

	public void addResidence(Residence r) {
		List<String> s= getResidences(r.getWorld().getName());
		s.add(r.getName());
		c.getConfig().set("Residences."+r.getWorld().getName(), s);
		save();
	}

	public void removeResidence(Residence r) {
		List<String> s= getResidences(r.getWorld().getName());
		s.remove(r.getName());
		c.getConfig().set("Residences."+r.getWorld().getName(), s);
		save();
	}

	public void setGroup(String s) {
		c.getConfig().set("Group",s);
		save();
	}
	
	public void save() {
		c.save();
	}

	public List<String> getResidences() {
		List<String> a = new ArrayList<String>();
		for(String s: c.getConfig().getConfigurationSection("Residences").getKeys(false))a.addAll(getResidences(s));
		return a;
	}
}

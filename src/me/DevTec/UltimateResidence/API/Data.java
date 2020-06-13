package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.List;

import me.DevTec.ConfigAPI;
import me.DevTec.UltimateResidence.Utils.Group;

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
		return new Group(c.getString("Group"));
	}

	public int getAmountOfResidences() {
		int a= 0;
		if(c.existPath("Residences"))
		for(String s: c.getConfigurationSection("Residences",false))a+=getResidences(s).size();
		return a;
	}
	
	public List<String> getResidences(String world) {
		return c.getStringList("Residences."+world);
	}

	public void addResidence(Residence r) {
		List<String> s= getResidences(r.getWorld().getName());
		if(s.contains(r.getName()))return;
		s.add(r.getName());
		c.set("Residences."+r.getWorld().getName(), s);
		save();
	}

	public void removeResidence(Residence r) {
		List<String> s= getResidences(r.getWorld().getName());
		if(!s.contains(r.getName()))return;
		s.remove(r.getName());
		c.set("Residences."+r.getWorld().getName(), s);
		save();
	}

	public void setGroup(String s) {
		if(c.existPath("Group") && c.getString("Group").equals(s))return;
		c.set("Group",s);
		save();
	}
	
	public void save() {
		c.save();
	}

	public ArrayList<String> getResidences() {
		ArrayList<String> a = new ArrayList<String>();
		if(c.getConfig().getString("Residences")!=null)
		for(String s: c.getConfigurationSection("Residences",false))a.addAll(getResidences(s));
		return a;
	}
}

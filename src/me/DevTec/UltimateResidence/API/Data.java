package me.DevTec.UltimateResidence.API;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.Utils.Group;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.datakeeper.User;

public class Data {
	private String s;
	private User c;
	private Group g;
	public Data(String player) {
		s=player;
		c=TheAPI.getUser(player);
		String g="default";
		if(Loader.g.getString("Groups")!=null)
					for(String sd: Loader.g.getKeys("Groups"))
						if(Bukkit.getPlayer(s).hasPermission("residence.group."+sd)) {
							g = sd;
							break;
			}
		this.g = new Group(g);
	}
	
	public String getName() {
		return s;
	}


	public Group getGroup() {
		return g;
	}

	public int getAmountOfResidences() {
		int a= 0;
		for(String s: c.getKeys("Residences"))a+=getResidences(s).size();
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
	
	public void save() {
		c.save();
	}

	public ArrayList<String> getResidences() {
		ArrayList<String> a = new ArrayList<String>();
		for(String s: c.getKeys("Residences"))a.addAll(getResidences(s));
		return a;
	}
}

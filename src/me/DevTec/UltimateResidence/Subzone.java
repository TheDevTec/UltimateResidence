package me.DevTec.UltimateResidence;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.ResidenceFlag.Flag;
import me.Straiker123.TheAPI;

public class Subzone {
	private Residence r;
	private String s;
	private Location[] l;
	public Subzone(Residence residence, String name) {
		r=residence;
		s=name;
		l=new Location[2];
		String[] corners = Loader.getData(r.getWorld()).getConfig().getString("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Corners").split(":");
		l[0]=TheAPI.getStringUtils().getLocationFromString(corners[0]);
		l[1]=TheAPI.getStringUtils().getLocationFromString(corners[1]);
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

	public List<ResidenceFlag> getFlags(){
		List<ResidenceFlag> a = new ArrayList<ResidenceFlag>();
		for(String s : Loader.getData(r.getWorld()).getConfig().getStringList("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Global")){
			String[] f = s.split(":");
			a.add(new ResidenceFlag(null,Flag.valueOf(f[0]),Boolean.valueOf(f[1])));
		}
		for(String s : Loader.getData(r.getWorld()).getConfig().getStringList("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Player")) {
			String[] f = s.split(":");
			a.add(new ResidenceFlag(f[0],Flag.valueOf(f[1]),Boolean.valueOf(f[2])));
		}
		return a;
	}

	public List<ResidenceFlag> getPlayerFlags(){
		List<ResidenceFlag> a = new ArrayList<ResidenceFlag>();
		for(String s : Loader.getData(r.getWorld()).getConfig().getStringList("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Player")) {
			String[] f = s.split(":");
			a.add(new ResidenceFlag(f[0],Flag.valueOf(f[1]),Boolean.valueOf(f[2])));
		}
		return a;
	}
	
	public void setFlag(Flag flag, boolean value) {
			List<String> a = Loader.getData(r.getWorld()).getConfig().getStringList("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Global");
			a.add(flag.name()+":"+value);
			Loader.getData(r.getWorld()).getConfig().set("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Global",a);
	}
	
	public void setFlag(Flag flag, String player, boolean value) {
			List<String> a = Loader.getData(r.getWorld()).getConfig().getStringList("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Player");
			a.add(player+":"+flag.name()+":"+value);
			Loader.getData(r.getWorld()).getConfig().set("Residence."+r.getOwner()+"."+r.getName()+".Subzone."+s+".Flags-Player",a);
			Loader.getData(r.getWorld()).save();
	}

	public boolean inResidence(Player player){
		return TheAPI.getBlocksAPI().isInside(player.getLocation(),l[0],l[1]);
	}

	public boolean inResidence(Location loc){
		return TheAPI.getBlocksAPI().isInside(loc,l[0],l[1]);
	}
}

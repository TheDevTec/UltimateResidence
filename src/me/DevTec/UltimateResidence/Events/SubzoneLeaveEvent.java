package me.DevTec.UltimateResidence.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Subzone;

public class SubzoneLeaveEvent extends Event {
	private Player s;
	private Subzone r;
	private Location loc;
	private String ac,chat;
	private String[] title;
	public SubzoneLeaveEvent(Subzone r, Location l, Player s) {
		this.s=s;
		this.r=r;
		loc=l;
		String group = API.getData(s.getName()).getGroup().getName();
		if(Loader.g.getBoolean("Groups."+group+".Chat.Use")) {
			setChat(Loader.g.getString("Groups."+group+".Chat.Enter"));
		}
		if(Loader.g.getBoolean("Groups."+group+".Title.Use")) {
			setTitle(Loader.g.getString("Groups."+group+".Title.Enter.Line1"),Loader.g.getString("Groups."+group+".Title.Enter.Line2"));
		}
		if(Loader.g.getBoolean("Groups."+group+".ActionBar.Use")) {
			setActionBar(Loader.g.getString("Groups."+group+".ActionBar.Enter"));
		}
	}
	
	public Location getLocation() {
		return loc;
	}

	public void setChat(String a) {
		chat=a;
	}
	
	public String getChat() {
		return chat;
	}
	public void setTitle(String a, String b) {
		if(a !=null && b != null)
		title=new String[] {a,b};
	}
	
	public String[] getTitle() {
		return title;
	}
	
	public void setActionBar(String a) {
		ac=a;
	}
	
	public String getActionBar() {
		return ac;
	}
	
	public Residence getResidence() {
		return r.getResidence();
	}
	
	public Subzone getSubzone() {
		return r;
	}
	
	public Player getPlayer() {
		return s;
	}
	

	private static final HandlerList a = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return a;
	}

	public static HandlerList getHandlerList() {
		return a;
	}
}

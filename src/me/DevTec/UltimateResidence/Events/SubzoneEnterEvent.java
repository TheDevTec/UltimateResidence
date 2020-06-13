package me.DevTec.UltimateResidence.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.DevTec.Other.Position;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class SubzoneEnterEvent extends Event {
	private Player s;
	private Subzone r;
	private String ac,chat;
	private Position f;
	private String[] title;
	public SubzoneEnterEvent(Subzone r, Position c, Player s) {
		this.s=s;
		this.r=r;
		f=c;
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
	
	public Position getFrom() {
		return f;
	}
	
	public void setTitle(String a, String b) {
		if(a !=null && b != null)
		title=new String[] {a,b};
	}

	public void setChat(String a) {
		chat=a;
	}
	
	public String getChat() {
		return chat;
	}
	public void setActionBar(String a) {
		ac=a;
	}
	
	public String getActionBar() {
		return ac;
	}
	
	public String[] getTitle() {
		return title;
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

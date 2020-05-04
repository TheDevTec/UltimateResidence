package me.DevTec.UltimateResidence.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Subzone;

public class SubzoneSwitchEvent extends Event implements Cancellable {
	private Player s;
	private boolean c;
	private Subzone r;
	private String ac,chat;
	private String[] title;
	public SubzoneSwitchEvent(Subzone z, Player s) {
		this.s=s;
		this.r=z;
		String group = API.getData(s.getName()).getGroup().getName();
		if(Loader.g.getConfig().getBoolean("Groups."+group+".Chat.Use")) {
			setChat(Loader.g.getConfig().getString("Groups."+group+".Chat.Enter"));
		}
		if(Loader.g.getConfig().getBoolean("Groups."+group+".Title.Use")) {
			setTitle(Loader.g.getConfig().getString("Groups."+group+".Title.Enter.Line1"),Loader.g.getConfig().getString("Groups."+group+".Title.Enter.Line2"));
		}
		if(Loader.g.getConfig().getBoolean("Groups."+group+".ActionBar.Use")) {
			setActionBar(Loader.g.getConfig().getString("Groups."+group+".ActionBar.Enter"));
		}
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
	
	@Override
	public HandlerList getHandlers() {
		return new HandlerList();
	}

	public static HandlerList getHandlerList() {
		return new HandlerList();
	}

	@Override
	public boolean isCancelled() {
		return c;
	}

	@Override
	public void setCancelled(boolean cancel) {
		c=cancel;
	}

}

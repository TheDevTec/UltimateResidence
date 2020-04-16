package me.DevTec.UltimateResidence.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.DevTec.UltimateResidence.Residence;

public class ResidenceLeaveEvent extends Event {
	private Player s;
	private Residence r;
	private String ac;
	private String[] title;
	public ResidenceLeaveEvent(Residence r, Player s) {
		this.s=s;
		this.r=r;
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

}

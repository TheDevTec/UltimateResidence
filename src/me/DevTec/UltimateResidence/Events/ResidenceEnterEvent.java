package me.DevTec.UltimateResidence.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.DevTec.UltimateResidence.Residence;

public class ResidenceEnterEvent extends Event implements Cancellable {
	private Player s;
	private boolean c;
	private Residence r;
	private String ac;
	private String[] title;
	public ResidenceEnterEvent(Residence r, Player s) {
		this.s=s;
		this.r=r;
	}
	
	public void setTitle(String a, String b) {
		if(a !=null && b != null)
		title=new String[] {a,b};
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

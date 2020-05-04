package me.DevTec.UltimateResidence.API;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public class ResidenceFlag {
	
	private Flag s;
	private String g;
	private boolean b;
	public ResidenceFlag(String player, Flag name, boolean value) {
		s=name;
		g=player;
		b=value;
	}

	public Flag getFlag() {
		return s;
	}
	
	@Nullable
	public String getPlayer() {
		return g;
	}
	
	public boolean getValue() {
		return b;
	}
}

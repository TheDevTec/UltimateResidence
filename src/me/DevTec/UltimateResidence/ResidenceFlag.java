package me.DevTec.UltimateResidence;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;

public class ResidenceFlag {
	
	public static enum ResidenceFlagType {
		MOVE,
		PVP,
		FEED,
		HEAL,
		ANIMALSPAWN,
		ANIMALKILL,
		MONSTERSPAWN,
		MOSTERKILL,
		USE,
		FLY,
		BUILD,
		BREAK
	}
	
	private ResidenceFlagType s;
	private String g;
	private boolean b;
	public ResidenceFlag(String player, ResidenceFlagType name, boolean value) {
		s=name;
		g=player;
		b=value;
	}

	public ResidenceFlagType getType() {
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

package me.DevTec.UltimateResidence.API;

public enum Flag {
	MOVE,
	PVP,
	FEED,
	HEAL,
	ANIMALSPAWN,
	ANIMALKILL,
	MONSTERSPAWN,
	MONSTERKILL,
	USE,
	FLY,
	ANVIL,NODURABILITY,
	WORKBENCH,DOOR,ANVILBREAK,
	BUILD,
	BREAK, NODAMAGE, NOFALLDAMAGE, CONTAINER, INTERACT;
	
	public static Flag getByName(String name) {
		try {
			return Flag.valueOf(name.toUpperCase());
		}catch(Exception e) {
			return null;
		}
	}
}

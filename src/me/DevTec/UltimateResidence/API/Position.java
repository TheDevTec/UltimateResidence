package me.DevTec.UltimateResidence.API;

import org.bukkit.Location;

public class Position {
	private double x,y,z;
	public Position(double x, double y, double z) {
		this.x=x;
		this.z=z;
		this.y=y;
	}
	public Position(Location loc) {
		this.x=loc.getX();
		this.z=loc.getZ();
		this.y=loc.getY();
	}
	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double z() {
		return z;
	}
}

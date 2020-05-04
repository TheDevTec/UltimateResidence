package me.DevTec.UltimateResidence.Commands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Utils.ResEvents;
import me.Straiker123.TheAPI;

public class VertCmd {

	public VertCmd(Player s) {
		if(ResEvents.locs.containsKey(s.getName())&&ResEvents.locs.get(s.getName()).length==2) {
			Location[] a = ResEvents.locs.get(s.getName());
			ResEvents.locs.put(s.getName(), new Location[] {
					new Location(a[0].getWorld(),a[0].getX(),0,a[0].getZ()),
					new Location(a[1].getWorld(),a[1].getX(),255,a[1].getZ())});
			TheAPI.msg(URCMD.d+"Selected area from bedrock to sky.", s);
			return;
		}
		TheAPI.msg(URCMD.d+"Missing corners for residence.", s);
		return;
	}

}

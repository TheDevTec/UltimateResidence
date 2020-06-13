package me.DevTec.UltimateResidence.Commands;

import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.Other.Position;
import me.DevTec.UltimateResidence.Utils.ResEvents;

public class VertCmd {

	public VertCmd(Player s) {
		if(ResEvents.locs.containsKey(s.getName())&&ResEvents.locs.get(s.getName()).length==2) {
			Position[] a = ResEvents.locs.get(s.getName());
			a[0].setY(0);
			a[1].setY(255);
			ResEvents.locs.put(s.getName(),a);
			TheAPI.msg(URCMD.d+"Selected area from bedrock to sky.", s);
			return;
		}
		TheAPI.msg(URCMD.d+"Missing corners for residence.", s);
		return;
	}

}

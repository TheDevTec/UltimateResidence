package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.Other.Position;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class TeleportSet {

	public TeleportSet(CommandSender s) {
		if(s instanceof Player) {
			Residence r = API.getResidence(new Position(((Player)s).getLocation()));
			if(r!=null) {
				Subzone z = r.getSubzone(new Position(((Player)s).getLocation()));
			if(z!=null) {
				z.setSpawn(new Position(((Player)s).getLocation()));
				TheAPI.msg("Set teleport location of subzone &a"+z.getName()+" &7at your location.", s);
			return;
		}
			r.setSpawn(new Position(((Player)s).getLocation()));
			TheAPI.msg("Set teleport location of residence &a"+r.getName()+" &7at your location.", s);
			return;
		}
		TheAPI.msg(URCMD.d+"You must be in residence/subzone to do this.", s);
		return;
	}
		TheAPI.msg(URCMD.d+"This command is only for players", s);
		return;
	}

}

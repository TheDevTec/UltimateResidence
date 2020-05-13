package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.API.API;
import me.Straiker123.TheAPI;

public class ListCmd {

	public ListCmd(CommandSender s, String[] args) {
		if(args.length==1) {
			if(!(s instanceof Player)) {
				TheAPI.msg(URCMD.d+"&e/Residence list [user]", s);
				return;
			}
			TheAPI.msg("&8&l»------ &cResidences of user "+s.getName()+" &8&l------«", s);
			for(String res : API.getResidences(s.getName())) {
				TheAPI.msg("&7- &c"+res+" &7("+API.getResidenceByName(res).getWorld().getName()+")",s);
			}
		return;
		}
		TheAPI.msg("&8&l»------ &cResidences of user "+args[1]+" &8&l------«", s);
			for(String res : API.getResidences(args[1])) {
				TheAPI.msg("&7- &c"+res+" &7("+API.getResidenceWorldByName(res).getName()+")",s);
			}
			return;
	}

}

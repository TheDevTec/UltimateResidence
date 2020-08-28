package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class TeleportCmd {

	public TeleportCmd(CommandSender s, String[] args) {
		if(args.length==1) {
			if(s instanceof Player)
			TheAPI.msg(URCMD.d+"&e/Residence teleport [residence.<subzone>]", s);
			else
			TheAPI.msg(URCMD.d+"&e/Residence teleport [residence.<subzone>] [player]", s);
			return;
		}
		if(args.length==2) {
		if(s instanceof Player) {
			String[] name= args[1].split("\\.");
		Residence res = API.getResidenceByName(name[0]);
		if(res==null) {
			TheAPI.msg(URCMD.d+"Residence &c"+name[0]+" &7doesn't exist.", s);
			return;
		}
		if(name.length>=2) {
			Subzone z = res.getSubzone(name[1]);
			if(z==null) {
				TheAPI.msg(URCMD.d+"Subzone &c"+name[1]+" &7doesn't exist.", s);
				return;
			}
			((Player)s).teleport(z.getSpawn().toLocation());
			TheAPI.msg(URCMD.d+"Teleported to the subzone &a"+name[1]+"&7.", s);
			return;
		}
		((Player)s).teleport(res.getSpawn().toLocation());
		TheAPI.msg(URCMD.d+"Teleported to the residence &a"+name[0]+"&7.", s);
		return;
		}
		TheAPI.msg(URCMD.d+"&e/Residence teleport [residence] [player]", s);
		return;
		}
		Player p = TheAPI.getPlayer(args[2]);
		if(p==null) {
			TheAPI.msg(URCMD.d+"Player &c"+args[2]+" &7isn't online.", s);
			return;
		}
		String[] name= args[1].split("\\.");
		Residence res = API.getResidenceByName(name[0]);
		if(res==null) {
			TheAPI.msg(URCMD.d+"Residence &c"+name[0]+" &7doesn't exist.", s);
			return;
		}
		if(name.length>=2) {
			Subzone z = res.getSubzone(name[1]);
			if(z==null) {
				TheAPI.msg(URCMD.d+"Subzone &c"+name[1]+" &7doesn't exist.", s);
				return;
			}
			p.teleport(z.getSpawn().toLocation());
			TheAPI.msg(URCMD.d+"Teleported to the subzone &a"+name[1]+"&7.", p);
			TheAPI.msg(URCMD.d+"Player &a"+p.getName()+" &7teleported to the subzone &a"+name[1]+"&7.", s);
			return;
		}
		p.teleport(res.getSpawn().toLocation());
		TheAPI.msg(URCMD.d+"Teleported to the residence &a"+name[0]+"&7.", p);
		TheAPI.msg(URCMD.d+"Player &a"+p.getName()+" &7teleported to the residence &a"+name[0]+"&7.", s);
		return;
	}

}

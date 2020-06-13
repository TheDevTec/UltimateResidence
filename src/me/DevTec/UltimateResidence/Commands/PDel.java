package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class PDel {

	public PDel(CommandSender s, String[] args) {
		if(args.length==1) {
			if(s instanceof Player)
			TheAPI.msg(URCMD.d+"&e/Residence pdel [player]", s);
			else
			TheAPI.msg(URCMD.d+"&e/Residence pdel [residence.<subzone>] [player]", s);
			return;
		}
		if(args.length==2) {
			if(s instanceof Player==false||s instanceof Player && API.getResidence((Player)s)==null) {
			TheAPI.msg(URCMD.d+"&e/Residence pdel [residence.<subzone>] [player]", s);
			return;
			}
			API.getResidence((Player)s).removeMember(args[1]);
			TheAPI.msg(URCMD.d+"You disallowed residence to player "+args[1], s);
			return;
		}
		if(args.length==3) {
			String[] a  = args[1].split("\\.");
			Residence r = API.getResidenceByName(a[0]);
			if(a.length>=2) {
				Subzone z = r.getSubzone(a[1]);
				if(z!=null) {
					z.removeMember(args[2]);
					TheAPI.msg(URCMD.d+"You disallowed subzone &a"+a[1]+" &7to player "+args[2], s);
				}
				TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
				return;
			}
			r.removeMember(args[2]);
			TheAPI.msg(URCMD.d+"You disallowed residence &a"+a[0]+" &7to player "+args[2], s);
			return;
		}
	}

}

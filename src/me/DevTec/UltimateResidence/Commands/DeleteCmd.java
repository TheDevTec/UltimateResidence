package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.devtec.theapi.TheAPI;

public class DeleteCmd {
	//ONLY owner can DELETE residence
	public DeleteCmd(CommandSender s, String[] args) {
		if(args.length==1) {
			if(s instanceof Player) {
				Residence r=API.getResidence((Player)s);
				if(r == null) {
					TheAPI.msg(URCMD.d+"&e/Residence delete [residence.<subzone>]", s);
					return;
				}
				Subzone z = r.getSubzone(((Player)s).getLocation());
				if(z!=null) {
					if(z.getOwner().equals(s.getName())||s.hasPermission("residence.admin")) {
						r.removeSubzone(z);
						TheAPI.msg(URCMD.d+"Subzone &a"+z.getName()+" &7deleted.", s);
						return;
						}
						TheAPI.msg(URCMD.d+"You must be owner of subzone to delete it.", s);
					return;
				}
				if(r.getOwner().equals(s.getName())||s.hasPermission("residence.admin")) {
					API.delete(s.getName(),r.getName());
					TheAPI.msg(URCMD.d+"Residence &a"+r.getName()+" &7deleted.", s);
					return;
					}
					TheAPI.msg(URCMD.d+"You must be owner of residence to delete it.", s);
				return;
			}
			TheAPI.msg(URCMD.d+"&e/Residence delete [residence.<subzone>]", s);
			return;
			}
		String[] a = args[1].split("\\.");
		if(a.length>=2) {
			Residence r=API.getResidenceByName(a[0]);
			Subzone z = r.getSubzone(a[1]);
			if(z!=null) {
				if(z.getOwner().equals(s.getName())||s.hasPermission("residence.admin")) {
					r.removeSubzone(z);
					TheAPI.msg(URCMD.d+"Subzone &a"+a[1]+" &7deleted.", s);
					return;
					}
					TheAPI.msg(URCMD.d+"You must be owner of subzone to delete it.", s);
				return;
			}
			TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
			return;
		}else {
			Residence r=API.getResidenceByName(args[1]);
			if(r == null) {
				TheAPI.msg(URCMD.d+"Residence &c"+args[1]+" &7doesn't exist.", s);
				return;
			}
			if(r.getOwner().equals(s.getName())||s.hasPermission("residence.admin")) {
			API.delete(s.getName(),args[1]);
			TheAPI.msg(URCMD.d+"Residence &a"+args[1]+" &7deleted.", s);
			return;
			}
			TheAPI.msg(URCMD.d+"You must be owner of residence to delete it.", s);
			return;
	}}

}

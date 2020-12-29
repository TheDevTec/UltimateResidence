package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.API.API;
import me.devtec.theapi.TheAPI;

public class Padd {

	public Padd(CommandSender s, String[] args) {
		if(args.length==1) {
			if(s instanceof Player)
			TheAPI.msg(URCMD.d+"&e/Residence padd [player]", s);
			else
			TheAPI.msg(URCMD.d+"&e/Residence padd [residence] [player]", s);
			return;
		}
		if(args.length==2) {
			if(s instanceof Player==false||s instanceof Player && API.getResidence((Player)s)==null) {
			TheAPI.msg(URCMD.d+"&e/Residence padd [residence] [player]", s);
			return;
			}
			API.getResidence((Player)s).addMember(args[1]);
			TheAPI.msg(URCMD.d+"You allowed residence to player "+args[1], s);
			return;
		}
		if(args.length==3) {
			API.getResidenceByName(args[1]).addMember(args[2]);
			TheAPI.msg(URCMD.d+"You allowed residence &a"+args[1]+" &7to player "+args[2], s);
			return;
		}
	}

}

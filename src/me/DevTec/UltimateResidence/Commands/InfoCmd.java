package me.DevTec.UltimateResidence.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class InfoCmd {

	public InfoCmd(CommandSender s, String[] args) {
		if(args.length==1) {
			if(s instanceof Player) {
				Residence r = API.getResidence((Player)s);
				if(r!=null) {
					Subzone z = r.getSubzone((Player)s);
				if(z==null) {
				TheAPI.msg("&8&l»------ &c&lInfo about residence &8&l------»", s);
				TheAPI.msg(URCMD.d+"Residence: &a"+r.getName(), s);
				TheAPI.msg(URCMD.d+"Subzones: &a"+StringUtils.join(r.getSubzones(), ", "), s);
				TheAPI.msg(URCMD.d+"Owner: &a"+r.getOwner(), s);
				TheAPI.msg(URCMD.d+"Members: &a"+StringUtils.join(r.getMembers(), ", "), s);
				TheAPI.msg(URCMD.d+"Flags: &a"+constructFlags(r.getFlags()), s);
				TheAPI.msg(URCMD.d+"Size: &a"+r.getSize()[0]+"x"+r.getSize()[1], s);
				TheAPI.msg("&8&l»------ &c&lInfo about residence &8&l------»", s);
				return;
			}
				TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
				TheAPI.msg(URCMD.d+"Residence: &a"+r.getName(), s);
				TheAPI.msg(URCMD.d+"Subzone: &a"+z.getName(), s);
				TheAPI.msg(URCMD.d+"Owner: &a"+z.getOwner(), s);
				TheAPI.msg(URCMD.d+"Members: &a"+StringUtils.join(z.getMembers(), ", "), s);
				TheAPI.msg(URCMD.d+"Flags: &a"+constructFlags(z.getFlags()), s);
				TheAPI.msg(URCMD.d+"Size: &a"+z.getSize()[0]+"x"+z.getSize()[1], s);
				TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
				return;
			}
			TheAPI.msg(URCMD.d+"&e/Residence info [residence]", s);
			return;
		}
			TheAPI.msg(URCMD.d+"&e/Residence info [residence]", s);
			return;
		}
		String[] a = args[1].split("\\.");
		Residence r = API.getResidenceByName(a[0]);
		if(r==null) {
			TheAPI.msg(URCMD.d+"Residence &c"+a[0]+" &7doesn't exist.", s);
			return;
		}
		
		if(a.length>=2) {
			Subzone z = r.getSubzone(a[1]);
			if(z!=null) {
			TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
			TheAPI.msg(URCMD.d+"Residence: &a"+r.getName(), s);
			TheAPI.msg(URCMD.d+"Subzone: &a"+z.getName(), s);
			TheAPI.msg(URCMD.d+"Owner: &a"+z.getOwner(), s);
			TheAPI.msg(URCMD.d+"Members: &a"+StringUtils.join(z.getMembers(), ", "), s);
			TheAPI.msg(URCMD.d+"Flags: &a"+constructFlags(z.getFlags()), s);
			TheAPI.msg(URCMD.d+"Size: &a"+z.getSize()[0]+"x"+z.getSize()[1], s);
			TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
			return;
			}
			TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
			return;
		}
		
		if(r.getSubzone((Player)s)!=null) {
				Subzone z = r.getSubzone((Player)s);
				TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
				TheAPI.msg(URCMD.d+"Residence: &a"+r.getName(), s);
				TheAPI.msg(URCMD.d+"Subzone: &a"+z.getName(), s);
				TheAPI.msg(URCMD.d+"Owner: &a"+z.getOwner(), s);
				TheAPI.msg(URCMD.d+"Members: &a"+StringUtils.join(z.getMembers(), ", "), s);
				TheAPI.msg(URCMD.d+"Flags: &a"+constructFlags(z.getFlags()), s);
				TheAPI.msg(URCMD.d+"Size: &a"+z.getSize()[0]+"x"+z.getSize()[1], s);
				TheAPI.msg("&8&l»------ &c&lInfo about subzone &8&l------»", s);
				return;
			}
		
		TheAPI.msg("&8&l»------ &c&lInfo about residence &8&l------»", s);
		TheAPI.msg(URCMD.d+"Residence: &a"+r.getName(), s);
		TheAPI.msg(URCMD.d+"Subzones: &a"+StringUtils.join(r.getSubzones(), ", "), s);
		TheAPI.msg(URCMD.d+"Owner: &a"+r.getOwner(), s);
		TheAPI.msg(URCMD.d+"Members: &a"+StringUtils.join(r.getMembers(), ", "), s);
		TheAPI.msg(URCMD.d+"Flags: &a"+constructFlags(r.getFlags()), s);
		TheAPI.msg(URCMD.d+"Size: &a"+r.getSize()[0]+"x"+r.getSize()[1], s);
		TheAPI.msg("&8&l»------ &c&lInfo about residence &8&l------»", s);
		return;
	}

	private String constructFlags(List<String> list) {
		List<String> a = new ArrayList<String>();
		for(String f : list) {
			String[] d = f.split(":");
			a.add((Boolean.valueOf(d[1])?"&a" : "&c")+d[0]);
		}
		return StringUtils.join(a,"&7, ");
	}
}

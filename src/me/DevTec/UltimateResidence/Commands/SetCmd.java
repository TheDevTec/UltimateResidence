package me.DevTec.UltimateResidence.Commands;

import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;
import me.Straiker123.TheAPI;

public class SetCmd {
	public SetCmd(Player s, String[] args) {
		Residence r= API.getResidence(s);
		if(args.length==1) {
			if(r!=null)
				TheAPI.msg(URCMD.d+"&e/Residence set [flag] [true/false]", s);
			else
			TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
		return;
		}
			if(args.length==2) {
				if(r==null) {
				TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
				return;
				}
				Subzone z = r.getSubzone((Player)s);
				if(z!=null) {
					if(Flag.getByName(args[1])!=null) {
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in subzone &a"+z.getName()+" &7is set to &a"+z.getFlag(Flag.getByName(args[1])), s);
						return;
					}
					TheAPI.msg(URCMD.d+"&e/Residence set [flag] [true/false]", s);
					return;
				}
				if(Flag.getByName(args[1])!=null) {
					TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in subzone &a"+r.getName()+" &7is set to &a"+r.getFlag(Flag.getByName(args[1])), s);
					return;
				}
				TheAPI.msg(URCMD.d+"&e/Residence set [flag] [true/false]", s);
				return;
			}
			if(args.length==3) {
				if(r==null) {
					String[] a = args[1].split("\\.");
					r=API.getResidenceByName(a[0]);
					if(r==null) {
						TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
						return;
					}
					if(a.length>=2) {
						Subzone z = r.getSubzone(a[1]);
						if(z!=null) {
						if(Flag.getByName(args[2])!=null) {
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in subzone &a"+a[1]+" &7is set to &a"+z.getFlag(Flag.getByName(args[2])), s);
							return;
						}
						TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
						return;
						}
						TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
						return;
					}
					if(Flag.getByName(args[2])!=null) {
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in residence &a"+a[0]+" &7is set to &a"+r.getFlag(Flag.getByName(args[2])), s);
						return;
					}
					TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
					return;
				}
				Subzone z = r.getSubzone((Player)s);
				if(z!=null) {
					if(Flag.getByName(args[1])!=null) {
						if(args[2].equalsIgnoreCase("true")) {
							z.setFlag(Flag.getByName(args[1]), true);
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in subzone &a"+z.getName()+" &7set to &atrue", s);
							return;	
						}
						if(args[2].equalsIgnoreCase("false")) {
							z.setFlag(Flag.getByName(args[1]), false);
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in subzone &a"+z.getName()+" &7set to &cfalse", s);
							return;
						}
					}
					TheAPI.msg(URCMD.d+"&e/Residence set [flag] [true/false]", s);
					return;
				}
				if(Flag.getByName(args[1])!=null) {
					if(args[2].equalsIgnoreCase("true")) {
						r.setFlag(Flag.getByName(args[1]), true);
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in residence &a"+r.getName()+" &7set to &atrue", s);
						return;	
					}
					if(args[2].equalsIgnoreCase("false")) {
						r.setFlag(Flag.getByName(args[1]), false);
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[1].toUpperCase()+" &7in residence &a"+r.getName()+" &7set to &cfalse", s);
						return;
					}
				}
				TheAPI.msg(URCMD.d+"&e/Residence set [flag] [true/false]", s);
				return;
			}
			if(args.length==4) {
				String[] a = args[1].split("\\.");
				r=API.getResidenceByName(a[0]);
				if(r==null) {
					TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
					return;
				}
				if(a.length>=2) {
					Subzone z = r.getSubzone(a[1]);
					if(z!=null) {
						if(Flag.getByName(args[2])!=null) {
							if(args[2].equalsIgnoreCase("true")) {
								z.setFlag(Flag.getByName(args[2]), true);
								TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in subzone &a"+a[1]+" set to &atrue", s);
								return;	
							}
							if(args[2].equalsIgnoreCase("false")) {
								z.setFlag(Flag.getByName(args[2]), false);
								TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in subzone &a"+a[1]+" set to &cfalse", s);
								return;
							}
					}
					TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
					return;
					}
					TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
					return;
				}
			if(Flag.getByName(args[2])!=null) {
					if(args[2].equalsIgnoreCase("true")) {
						r.setFlag(Flag.getByName(args[2]), true);
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in residence &a"+r.getName()+" set to &atrue", s);
						return;	
					}
					if(args[2].equalsIgnoreCase("false")) {
						r.setFlag(Flag.getByName(args[2]), false);
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in residence &a"+r.getName()+" set to &cfalse", s);
						return;
					}
			}
			TheAPI.msg(URCMD.d+"&e/Residence set [residence.<subzone>] [flag] [true/false]", s);
		return;
	}}}

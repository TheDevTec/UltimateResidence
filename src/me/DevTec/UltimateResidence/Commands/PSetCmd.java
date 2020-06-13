package me.DevTec.UltimateResidence.Commands;

import java.io.File;

import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Flag;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.API.Subzone;

public class PSetCmd {
	public PSetCmd(Player s, String[] args) {
		Residence r= API.getResidence(s);
		if(args.length==1) {
			if(r!=null)
				TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
			else
			TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
		return;
		}
			if(args.length==2) {
				if(r!=null)
					TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
				else
				TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
				return;
			}
			if(args.length==3) {
				if(r==null) {
					if(!new File("plugins/UltimateResidence/User/"+args[2]).exists()) {
						TheAPI.msg(URCMD.d+"&7Player &c"+args[2]+" &7doesn't exist.", s);
						return;
					}
					String[] a = args[1].split("\\.");
					r=API.getResidenceByName(a[0]);
					if(r==null) {
						TheAPI.msg(URCMD.d+"Residence &c"+a[0]+" &7doesn't exist.", s);
						return;
					}
					if(a.length<=2) {
						Subzone z = r.getSubzone(a[1]);
						if(z==null) {
							TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7in residence &c"+a[0]+" &7doesn't exist.", s);
							return;
						}
						TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
						return;
					}
					TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
					return;
				}
				if(!new File("plugins/UltimateResidence/User/"+args[1]).exists()) {
					TheAPI.msg(URCMD.d+"&7Player &c"+args[1]+" &7doesn't exist.", s);
					return;
				}
				Subzone z = r.getSubzone((Player)s);
				if(z!=null) {
					if(Flag.getByName(args[2])!=null) {
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7of player &a"+args[1]+" &7in subzone &a"+z.getName()+" &7is set to &a"+z.getPlayerFlag(Flag.getByName(args[2]),args[1]), s);
						return;
					}
					TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
					return;
				}
				if(Flag.getByName(args[2])!=null) {
						TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7of player &a"+args[1]+" &7in residence &a"+r.getName()+" &7is set to &a"+r.getPlayerFlag(Flag.getByName(args[2]),args[1]), s);
						return;
					}
				TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
					return;
			}
			if(args.length==4) {
				if(r==null) {
					if(!new File("plugins/UltimateResidence/User/"+args[2]).exists()) {
						TheAPI.msg(URCMD.d+"&7Player &c"+args[2]+" &7doesn't exist.", s);
						return;
					}
					String[] a = args[1].split("\\.");
					r=API.getResidenceByName(a[0]);
					if(r==null) {
						TheAPI.msg(URCMD.d+"Residence &c"+a[0]+" &7doesn't exist.", s);
						return;
					}
					if(a.length<=2) {
						Subzone z = r.getSubzone(a[1]);
						if(z==null) {
							TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7in residence &c"+a[0]+" &7doesn't exist.", s);
							return;
						}
						if(Flag.getByName(args[3])!=null) {
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[3].toUpperCase()+" &7of player &a"+args[2]+" &7in subzone &a"+a[1]+" &7is set to &a"+z.getPlayerFlag(Flag.getByName(args[3]),args[2]), s);
							return;
						}
						TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
						return;
					}
			if(Flag.getByName(args[3])!=null) {
					TheAPI.msg(URCMD.d+"&7Flag &a"+args[3].toUpperCase()+" &7of player &a"+args[2]+" &7in residence &a"+a[0]+" &7is set to &a"+r.getPlayerFlag(Flag.getByName(args[3]),args[2]), s);
					return;
				}
			TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
			return;
				}

				Subzone z = r.getSubzone((Player)s);
				if(z!=null) {
					if(Flag.getByName(args[2])!=null) {
						if(args[2].equalsIgnoreCase("true")) {
							z.setFlag(Flag.getByName(args[2]), true);
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in subzone &a"+z.getName()+" set to &atrue", s);
							return;	
						}
						if(args[2].equalsIgnoreCase("false")) {
							z.setFlag(Flag.getByName(args[2]), false);
							TheAPI.msg(URCMD.d+"&7Flag &a"+args[2].toUpperCase()+" &7in subzone &a"+z.getName()+" set to &cfalse", s);
							return;
						}
				}
					TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
					return;
				}
				
			if(Flag.getByName(args[2])!=null) {
					if(args[2].equalsIgnoreCase("true")) {
						r.setFlag(Flag.getByName(args[2]), true);
						TheAPI.msg(URCMD.d+"Flag &a"+args[2].toUpperCase()+" &7in residence &a"+r.getName()+" set to &atrue", s);
						return;	
					}
					if(args[2].equalsIgnoreCase("false")) {
						r.setFlag(Flag.getByName(args[2]), false);
						TheAPI.msg(URCMD.d+"Flag &a"+args[2].toUpperCase()+" &7in residence &a"+r.getName()+" set to &cfalse", s);
						return;
					}
			}
			TheAPI.msg(URCMD.d+"&e/Residence pset [player] [flag] [true/false]", s);
			return;
			}
			if(args.length==5) {
				if(!new File("plugins/UltimateResidence/User/"+args[2]).exists()) {
					TheAPI.msg(URCMD.d+"Player &c"+args[2]+" &7doesn't exist.", s);
					return;
				}
				String[] a = args[1].split("\\.");
				r=API.getResidenceByName(a[0]);
			if(r==null) {
				TheAPI.msg(URCMD.d+"Residence &c"+a[0]+" &7doesn't exist.", s);
				return;
			}
			if(a.length>=2) {
			Subzone z = r.getSubzone(a[1]);
			if(z!=null) {
				if(Flag.getByName(args[3])!=null) {
					if(args[4].equalsIgnoreCase("true")) {
						z.setFlag(Flag.getByName(args[3]), true);
						TheAPI.msg(URCMD.d+"Flag &a"+args[3].toUpperCase()+" &7in residence &a"+r.getName()+" set to &atrue", s);
						return;	
					}
					if(args[4].equalsIgnoreCase("false")) {
						z.setFlag(Flag.getByName(args[3]), false);
						TheAPI.msg(URCMD.d+"Flag &a"+args[3].toUpperCase()+" &7in residence &a"+r.getName()+" set to &cfalse", s);
						return;
					}
				}
				TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
				return;
			}
			TheAPI.msg(URCMD.d+"Subzone &c"+a[1]+" &7doesn't exist.", s);
			return;
			}
			if(Flag.getByName(args[3])!=null) {
				if(args[4].equalsIgnoreCase("true")) {
					r.setFlag(Flag.getByName(args[3]), true);
					TheAPI.msg(URCMD.d+"Flag &a"+args[3].toUpperCase()+" &7in residence &a"+r.getName()+" set to &atrue", s);
					return;	
				}
				if(args[4].equalsIgnoreCase("false")) {
					r.setFlag(Flag.getByName(args[3]), false);
					TheAPI.msg(URCMD.d+"Flag &a"+args[3].toUpperCase()+" &7in residence &a"+r.getName()+" set to &cfalse", s);
					return;
				}
			}
			TheAPI.msg(URCMD.d+"&e/Residence pset [residence.<subzone>] [player] [flag] [true/false]", s);
			return;
			}
	}}

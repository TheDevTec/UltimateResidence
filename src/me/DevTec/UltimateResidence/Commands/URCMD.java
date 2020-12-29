package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.Utils.ad;
import me.devtec.theapi.TheAPI;

public class URCMD implements CommandExecutor {
	public static String d = "&c&lUResidence &8&l» &7";
	@Override
	public boolean onCommand(CommandSender s, Command dawd, String dawds, String[] args) {
		if(!s.hasPermission("residence.user")) {
			s.sendMessage("Uknown command. Type \"/help\" for help.");
			return true;
		}
		if(args.length==0) {
			TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l------»", s);
			TheAPI.msg(d+"&e/Residence create [name]", s); //done
			TheAPI.msg(d+"&e/Residence delete [residence]", s); //done
			TheAPI.msg(d+"&e/Residence pset [residence] [player] [flag] [true/false]", s); //done
			// /Residence pset [player] [flag] [true/false]
			TheAPI.msg(d+"&e/Residence set [residence] [flag] [true/false]", s); //done
			// /Residence set [flag] [true/false]
			TheAPI.msg(d+"&e/Residence padd [residence] [player]", s); //done
			// /Residence padd [player]
			TheAPI.msg(d+"&e/Residence pdel [residence] [player]", s); //done
			// /Residence pdel [player]
			TheAPI.msg(d+"&e/Residence subzone [name]", s); //done
			TheAPI.msg(d+"&e/Residence vert", s); //done
			TheAPI.msg(d+"&e/Residence tpset", s); //done
			TheAPI.msg(d+"&e/Residence teleport [residence]", s); //done
			TheAPI.msg(d+"&e/Residence info [residence]", s); //done
			TheAPI.msg(d+"&e/Residence limits", s); //done
			TheAPI.msg(d+"&e/Residence list [user]", s); //done
			if(ad.has(s,"residence.reload"))
			TheAPI.msg(d+"&e/Residence reload", s); //done
			TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l------»", s);
			return true;
		}
		if(args[0].equalsIgnoreCase("reload")) {
			if(!ad.has(s,"residence.reload")) {
				return true;
			}
			new ReloadCmd(s);
			return true;
		}
		if(args[0].equalsIgnoreCase("tpset")) {
			if(!ad.has(s,"residence.tpset")) {
				return true;
			}
			new TeleportSet(s);
			return true;
		}
		if(args[0].equalsIgnoreCase("vert")) {
			if(!ad.has(s,"residence.vert")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new VertCmd((Player)s);
			return true;
		}
		if(args[0].equalsIgnoreCase("limits")) {
			if(!ad.has(s,"residence.limits")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new LimitsCmd((Player)s);
			return true;
		}
		if(args[0].equalsIgnoreCase("teleport")||args[0].equalsIgnoreCase("tp")) {
			if(!ad.has(s,"residence.teleport")) {
				return true;
			}
			new TeleportCmd(s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("info")) {
			if(!ad.has(s,"residence.info")) {
				return true;
			}
			new InfoCmd(s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("list")) {
			if(!ad.has(s,"residence.list")) {
				return true;
			}
			new ListCmd(s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("create")) {
			if(!ad.has(s,"residence.create")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new CreateCmd((Player)s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("set")) {
			if(!ad.has(s,"residence.set")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new SetCmd((Player)s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("pset")) {
			if(!ad.has(s,"residence.set")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new PSetCmd((Player)s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("padd")) {
			if(!ad.has(s,"residence.padd")) {
				return true;
			}
			new Padd(s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("pdel")) {
			if(!ad.has(s,"residence.padd")) {
				return true;
			}
			new PDel(s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("subzone")) {
			if(!ad.has(s,"residence.subzone")) {
				return true;
			}
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			new SubzoneCmd((Player)s,args);
			return true;
		}
		if(args[0].equalsIgnoreCase("delete")) {
			if(!ad.has(s,"residence.create")) {
				return true;
			}
			new DeleteCmd(s,args);
			return true;
		}
		return true;
	}

}

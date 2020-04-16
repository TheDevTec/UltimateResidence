package me.DevTec.UltimateResidence;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Straiker123.HoverMessage.ClickAction;
import me.Straiker123.TheAPI;

public class URCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command dawd, String dawds, String[] args) {
		String d = "&c&lUResidence &8&l» &7";
		if(args.length==0) {
			TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l«------", s);
			TheAPI.msg(d+"&e/Residence create [name]", s); //done
			TheAPI.msg(d+"&e/Residence delete [residence]", s); //done
			TheAPI.msg(d+"&e/Residence teleport [residence]", s); //done
			TheAPI.msg(d+"&e/Residence info [residence]", s); //done
			TheAPI.msg(d+"&e/Residence limits", s); //done
			TheAPI.msg(d+"&e/Residence list [user]", s); //done
			if(s.hasPermission("residence.reload"))
			TheAPI.msg(d+"&e/Residence reload", s); //done
			TheAPI.msg("&8&l»------ &c&lUltimateResidence &8&l«------", s);
			return true;
		}
		if(args[0].equalsIgnoreCase("reload")) {
			if(s.hasPermission("residence.reload")) {
				Loader.api.reload();
				TheAPI.msg(d+"Plugin reloaded.", s);
				return true;
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("limits")) {
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			TheAPI.msg(d+"Maximum Residences: &a5", s);
			TheAPI.msg(d+"Maximum Subzones: &a3", s);
			TheAPI.msg(d+"Maximum size: &a150x150", s);
			TheAPI.msg(d+"Maximum members: &a100", s);
			return true;
		}
		if(args[0].equalsIgnoreCase("teleport")||args[0].equalsIgnoreCase("tp")) {
			if(!(s instanceof Player)) {
				TheAPI.msg(d+"&cThis command is available only for players", s);
				return true;
			}
			if(args.length==1) {
				TheAPI.msg(d+"&e/Residence teleport [residence]", s);
				return true;
			}
			Residence res = ResidenceAPI.getResidence(args[1]);
			if(res==null) {
				TheAPI.msg(d+"Residence &c"+args[1]+" &7doesn't exist.", s);
				return true;
			}
			((Player)s).teleport(res.getTeleportLocation());
			TheAPI.msg(d+"Teleported to the residence &a"+args[1]+"&7.", s);
			return true;
		}
		if(args[0].equalsIgnoreCase("list")) {
			if(args.length==1) {
				if(!(s instanceof Player)) {
					TheAPI.msg(d+"&e/Residence list [user]", s);
					return true;
				}
				TheAPI.msg("&8&l»------ &cResidences of user "+s.getName()+" &8&l«------", s);
				for(String res : ResidenceAPI.getResidences(s.getName()))
				TheAPI.getStringUtils().getHoverMessage("&7- ")
				.addText("&6"+res)
				.setHoverEvent("&7Click to teleport")
				.setClickEvent(ClickAction.RUN_COMMAND, "/Residence teleport "+res)
				.addText("&7 ("+ResidenceAPI.getResidence(res).getWorld().getName()+")")
				.send((Player)s);
			return true;
			}
			TheAPI.msg("&8&l»------ &cResidences of user "+args[1]+" &8&l«------", s);
			if(!(s instanceof Player)) {
				for(String res : ResidenceAPI.getResidences(args[1]))
					TheAPI.msg("&7- &6"+res+" &7("+ResidenceAPI.getResidence(res).getWorld().getName()+")",s);
				return true;
			}
			for(String res : ResidenceAPI.getResidences(args[1]))
				TheAPI.getStringUtils().getHoverMessage("&7- ")
				.addText("&6"+res)
				.setHoverEvent("&7Click to teleport")
				.setClickEvent(ClickAction.RUN_COMMAND, "/Residence teleport "+res)
				.addText("&7 ("+ResidenceAPI.getResidence(res).getWorld().getName()+")")
				.send((Player)s);
			return true;
		}
		if(args[0].equalsIgnoreCase("create")) {
			if(!(s instanceof Player)) {
				TheAPI.msg("&8&l»------ &cResidences of user "+args[1]+" &8&l«------", s);
				for(String res : ResidenceAPI.getResidences(args[1]))
					TheAPI.msg("&7- &6"+res+" &7("+ResidenceAPI.getResidence(res).getWorld().getName()+")",s);
				return true;
			}
			if(args.length==1) {
			TheAPI.msg(d+"&e/Residence create [name]", s);
			return true;
			}
			if(ResidenceAPI.getResidence(args[1]) != null) {
				TheAPI.msg(d+"Residence &c"+args[1]+" &7already exists.", s);
				return true;
			}
			if(ResEvents.locs.containsKey(s.getName()) && ResEvents.locs.get(s.getName()).length==2) {
			if(ResidenceAPI.isColliding(((Player)s).getWorld(),ResEvents.locs.get(s.getName())[0],ResEvents.locs.get(s.getName())[1])) {
				TheAPI.msg(d+"Residence &c"+args[1]+" &7is colliding with another one.", s);
				return true;
			}
			ResidenceAPI.createResidence(((Player)s).getWorld(),s.getName(),args[1]);
			TheAPI.msg(d+"Residence &a"+args[1]+" &7created.", s);
			}
			return true;
		}
		if(args[0].equalsIgnoreCase("delete")) {
			if(args.length==1) {
			TheAPI.msg(d+"&e/Residence delete [name]", s);
			return true;
			}
			Residence r=ResidenceAPI.getResidence(args[1]);
			if(r == null) {
				TheAPI.msg(d+"Residence &c"+args[1]+" &7doesn't exist.", s);
				return true;
			}
			if(r.getOwner().equals(s.getName())||s.hasPermission("residence.admin")) {
			ResidenceAPI.deleteResidence(((Player)s).getWorld(),s.getName(),args[1]);
			TheAPI.msg(d+"Residence &a"+args[1]+" &7deleted.", s);
			return true;
			}
			TheAPI.msg(d+"You must be owner of residence to delete it.", s);
			return true;
		}
		return true;
	}

}

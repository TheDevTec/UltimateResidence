package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.UltimateResidence.Loader;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Data;

public class ReloadCmd {
	public ReloadCmd(CommandSender s) {
		API.reload();
		if(Loader.g.existPath("Groups"))
		for(Player sf : TheAPI.getOnlinePlayers()) {
				for(String sd: Loader.g.getConfigurationSection("Groups",false)) {
					if(sf.hasPermission("residence.group."+sd)) {
						new Data(sf.getName()).setGroup(sd);
						break;
		}}}
		TheAPI.msg(URCMD.d+"Plugin reloaded.", s);
	}

}

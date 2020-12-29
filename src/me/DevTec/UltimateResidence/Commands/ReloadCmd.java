package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;

import me.DevTec.UltimateResidence.API.API;
import me.devtec.theapi.TheAPI;

public class ReloadCmd {
	public ReloadCmd(CommandSender s) {
		API.reload();
		TheAPI.msg(URCMD.d+"Plugin reloaded.", s);
	}

}

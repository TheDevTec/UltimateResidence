package me.DevTec.UltimateResidence.Commands;

import org.bukkit.command.CommandSender;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.UltimateResidence.API.API;

public class ReloadCmd {
	public ReloadCmd(CommandSender s) {
		API.reload();
		TheAPI.msg(URCMD.d+"Plugin reloaded.", s);
	}

}

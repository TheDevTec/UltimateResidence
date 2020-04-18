package me.DevTec.UltimateResidence;

import org.bukkit.command.CommandSender;

public class ad {

	public static boolean has(CommandSender s, String perm) {
		if(s.hasPermission(perm)||s.hasPermission("residence.admin")||s.isOp())return true;
		return false;
	}
}

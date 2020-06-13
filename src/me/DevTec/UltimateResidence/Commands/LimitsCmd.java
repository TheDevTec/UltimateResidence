package me.DevTec.UltimateResidence.Commands;

import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.UltimateResidence.API.Data;
import me.DevTec.UltimateResidence.Utils.Group.SizeType;

public class LimitsCmd {

	public LimitsCmd(Player s) {
		TheAPI.msg("&8&l»------ &c&lLimits &8&l------«", s);
		TheAPI.msg(URCMD.d+"Maximum Residences: &a"+new Data(s.getName()).getGroup().getMaxResidences(), s);
		TheAPI.msg(URCMD.d+"Maximum Subzones: &a"+new Data(s.getName()).getGroup().getMaxSubResidences(), s);
		TheAPI.msg(URCMD.d+"Maximum size: &a"+new Data(s.getName()).getGroup().getMaxSize(SizeType.X)+"x"+new Data(s.getName()).getGroup().getMaxSize(SizeType.Z), s);
		TheAPI.msg("&8&l»------ &c&lLimits &8&l------«", s);
	}

}

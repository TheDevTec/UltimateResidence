package me.DevTec.UltimateResidence.Commands;

import org.bukkit.entity.Player;

import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Position;
import me.DevTec.UltimateResidence.Utils.ResEvents;
import me.Straiker123.TheAPI;

public class CreateCmd {

	public CreateCmd(Player s, String[] args) {
		if(args.length==1) {
			TheAPI.msg(URCMD.d+"&e/Residence create [residence]", s);
			return;
			}
		
			if(API.getData(s.getName()).getAmountOfResidences() <= API.getData(s.getName()).getGroup().getMaxResidences()) {
				TheAPI.msg(URCMD.d+"You reach maximum amount of residences.", s);
				return;
			}
		
			if(Utils.specialSymbol(args[1])) {
				TheAPI.msg(URCMD.d+"You can't use symbols in name of residence.", s);
				return;
			}
			if(API.getResidenceByName(args[1]) != null) {
				TheAPI.msg(URCMD.d+"Residence &c"+args[1]+" &7already exists.", s);
				return;
			}
			if(ResEvents.locs.containsKey(s.getName()) && ResEvents.locs.get(s.getName()).length==2) {
			if(API.isColliding(((Player)s).getWorld(),new Position(ResEvents.locs.get(s.getName())[0]),new Position(ResEvents.locs.get(s.getName())[1]))) {
				TheAPI.msg(URCMD.d+"Residence &c"+args[1]+" &7is colliding with another one.", s);
				return;
			}
			API.create(((Player)s).getWorld(),s.getName(),args[1]);
			TheAPI.msg(URCMD.d+"Residence &a"+args[1]+" &7created.", s);
			return;
			}
			TheAPI.msg(URCMD.d+"Missing corners for residence.", s);
			return;
	}

}

package me.DevTec.UltimateResidence.Commands;

import org.bukkit.entity.Player;

import me.DevTec.TheAPI;
import me.DevTec.UltimateResidence.API.API;
import me.DevTec.UltimateResidence.API.Residence;
import me.DevTec.UltimateResidence.Utils.ResEvents;

public class SubzoneCmd {

	public SubzoneCmd(Player s, String[] args) {
		if(args.length==1) {
			TheAPI.msg(URCMD.d+"&e/Residence subzone [name]", s);
			return;
			}
		if(Utils.specialSymbol(args[1])) {
			TheAPI.msg(URCMD.d+"You can't use symbols in name of residence.", s);
			return;
		}

			if(ResEvents.locs.containsKey(s.getName()) && ResEvents.locs.get(s.getName()).length==2) {
				Residence r = API.getResidence(ResEvents.locs.get(s.getName())[0]);
				if(r==null) {
					TheAPI.msg(URCMD.d+"Subzone isn't in residence.", s);
					return;
				}

				if(r.getSubzones().size() >= API.getData(s.getName()).getGroup().getMaxSubResidences()) {
					TheAPI.msg(URCMD.d+"You reach maximum amount of subzones in residence &c"+r.getName()+"&7.", s);
					return;
				}
				if(r.getSubzones().contains(args[1])) {
					TheAPI.msg(URCMD.d+"Subzone &c"+args[1]+" &7in residence &c"+r.getName()+" &7already exists.", s);
					return;
				}
				Residence res = API.getResidence(ResEvents.locs.get(s.getName())[0]);
				if(API.isCollidingWithSubzone(res.getSubzone(ResEvents.locs.get(s.getName())[0]), res.getSubzone(ResEvents.locs.get(s.getName())[1]))) {
					TheAPI.msg(URCMD.d+"Subzone &c"+args[1]+" &7is colliding with another subzone.", s);
					return;
				}
			r.createSubzone(args[1],ResEvents.locs.get(s.getName())[0],ResEvents.locs.get(s.getName())[1]);
			TheAPI.msg(URCMD.d+"Subzone &a"+args[1]+" &7of residence &a"+r.getName()+" &7created.", s);
			return;
			}
			TheAPI.msg(URCMD.d+"Missing courners for subzone &c"+args[1]+"&7.", s);
			return;
	}

}

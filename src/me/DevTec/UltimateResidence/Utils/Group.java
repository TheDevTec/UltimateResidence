package me.DevTec.UltimateResidence.Utils;

import me.DevTec.UltimateResidence.Loader;
import me.Straiker123.TheAPI;

public class Group {
	private String s;
	public Group(String name) {
		s=name;
	}
	public static enum SizeType {
		X,
		Z
	}
	
	public int getMaxSize(SizeType type) {
		int a = 0;
		String[] sd = Loader.g.getConfig().getString("Groups."+s+".Size").split("x");
		int x= TheAPI.getStringUtils().getInt(sd[0]);
		int z=TheAPI.getStringUtils().getInt(sd[1]);
		switch(type) {
		case X:
			a=x;
			break;
		case Z:
			a=z;
			break;
		}
		return a;
	}

	public int getMaxResidences() {
		return Loader.g.getConfig().getInt("Groups."+s+".Residence");
	}

	public int getMaxSubResidences() {
		return Loader.g.getConfig().getInt("Groups."+s+".SubResidence");
	}

	public void setMaxResidences(int max) {
		 Loader.g.getConfig().set("Groups."+s+".Residence",max);
		 Loader.g.save();
	}

	public void setMaxSubResidences(int max) {
		 Loader.g.getConfig().set("Groups."+s+".SubResidence",max);
		 Loader.g.save();
	}

	public void setMaxSize(int x,int z) {
		 Loader.g.getConfig().set("Groups."+s+".Size",x+"x"+z);
		 Loader.g.save();
	}

	public String getName() {
		return s;
	}
}

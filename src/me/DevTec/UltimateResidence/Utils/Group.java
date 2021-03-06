package me.DevTec.UltimateResidence.Utils;

import me.DevTec.UltimateResidence.Loader;
import me.devtec.theapi.utils.StringUtils;

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
		String[] sd = Loader.g.getString("Groups."+s+".Size").split("x");
		int x= StringUtils.getInt(sd[0]);
		int z=StringUtils.getInt(sd[1]);
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
		return Loader.g.getInt("Groups."+s+".Residences");
	}

	public int getMaxSubResidences() {
		return Loader.g.getInt("Groups."+s+".SubResidences");
	}

	public void setMaxResidences(int max) {
		 Loader.g.set("Groups."+s+".Residences",max);
		 Loader.g.save();
	}

	public void setMaxSubResidences(int max) {
		 Loader.g.set("Groups."+s+".SubResidences",max);
		 Loader.g.save();
	}

	public void setMaxSize(int x,int z) {
		 Loader.g.set("Groups."+s+".Size",x+"x"+z);
		 Loader.g.save();
	}

	public String getName() {
		return s;
	}
}

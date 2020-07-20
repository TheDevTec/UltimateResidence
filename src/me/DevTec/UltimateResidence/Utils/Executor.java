package me.DevTec.UltimateResidence.Utils;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;

public class Executor {
	private static ExecutorService ex = Executors.newFixedThreadPool(16);
	private static HashMap<Callable<?>, Object> a = Maps.newHashMap();
	
	private Callable<?> c;
	public Executor(Callable<?> callable) {
		c=callable;
		try {
			a.put(c, ex.submit(callable).get());
		} catch (Exception e) {
		}
	}
	
	public synchronized Object get() {
		while(!a.containsKey(c));
		try {
		return a.get(c);
		}catch(Exception e) {
			return null;
		} finally {
			a.remove(c);
		}
	}
}

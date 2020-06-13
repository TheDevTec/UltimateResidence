package me.DevTec.UltimateResidence.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.DevTec.TheAPI;
import me.DevTec.UltimateResidence.Loader;

public class Executor<T> {
	public T get(Callable<T> task) {
		Instant a = Instant.now();
		ExecutorService executor = Executors.newSingleThreadExecutor();
	    Future<T> result = executor.submit(task);
	    try {
	        return result.get();
	    } catch(Exception e){
	    	return null;
	    }finally {
			Instant b = Instant.now();
			for(String s : Loader.debugging)
				if(TheAPI.getPlayer(s)!=null)
					TheAPI.msg("Task called in "+Duration.between(a,b).toMillis()+"ms", TheAPI.getPlayer(s));
	        executor.shutdown();
	    }
	}
}

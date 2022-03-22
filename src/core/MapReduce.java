package core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MapReduce {
	
	private List<List<Integer>> segments, segmentsAux;
	private int partitions;
	
	ExecutorService pool = Executors.newCachedThreadPool();
	
	public MapReduce(){
		int cores = Runtime.getRuntime().availableProcessors();
		partitions = 2*cores;
		segments = new LinkedList<List<Integer>>();
		segmentsAux = new LinkedList<List<Integer>>();
	}
	
	//split the initial dataset
	public void map(List<Integer> args){
		
		//args.size > cores
		int partitionSize = args.size()/partitions;
		
		for(int i=0; i<args.size(); i+=partitionSize)
			segments.add(args.subList(i, i+partitionSize));
		
	}
	
	public void parallelReduce(){
		
		while(segments.size()>=2 || segmentsAux.size()>=2){
			if(!segments.isEmpty()){
				List<Future<List<Integer>>> results = new LinkedList<Future<List<Integer>>>();
				Future<List<Integer>> result=null;
				while((result = processSegments())!=null){
					results.add(result);
				}
				System.out.println("results of segments... " + results.size());
				try {
					for(Future<List<Integer>> r: results)					
						segmentsAux.add(r.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("step of segments finished");
				results.clear();
			}else{
				List<Future<List<Integer>>> results = new LinkedList<Future<List<Integer>>>();
				Future<List<Integer>> result=null;
				while((result = processSegmentsAux())!=null){
					results.add(result);
				}				
				System.out.println("results of segmentsAux... " + results.size());
				try {
					for(Future<List<Integer>> r: results)					
						segments.add(r.get());
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("step of segmentsAux finished");
				results.clear();
			}
			
			System.out.println("sizes... " + segments.size() + "||" + segmentsAux.size());
		}
		
		pool.shutdown();
		
		if(!segments.isEmpty())
			System.out.println(segments.get(0));
		else System.out.println(segmentsAux.get(0));
		
		segments.clear();
		segmentsAux.clear();
		
		System.out.println("Finished");
	}
	
	private Future<List<Integer>> processSegments(){
		
		try{
			List<Integer> args1 = segments.remove(0);
			List<Integer> args2 = segments.remove(0);
			return pool.submit(new GenericReduce(args1, args2));
			
		}catch(Exception e){
			return null;
		}
		
	}
	
	private Future<List<Integer>> processSegmentsAux(){
		
		try{
			List<Integer> args1 = segmentsAux.remove(0);
			List<Integer> args2 = segmentsAux.remove(0);
			return pool.submit(new GenericReduce(args1, args2));
			
		}catch(Exception e){
			return null;
		}
		
	}

}

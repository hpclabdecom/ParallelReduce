package core;

import java.util.List;
import java.util.concurrent.Callable;

import core.reduces.SumReduce;

public class GenericReduce implements Callable<List<Integer>>{
	
	private List<Integer> args1;
	private List<Integer> args2;
	
	public GenericReduce(List<Integer> args1, List<Integer> args2) {
		this.args1 = args1;
		this.args2 = args2;
	}

	@Override
	public List<Integer> call() {
		//we can improve for a generic solution....
		//sum, min, max, sort and many more reduce types
		return SumReduce.reduce(args1, args2);
	}
	
}

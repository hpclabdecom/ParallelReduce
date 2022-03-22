package appl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import core.MapReduce;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}
	
	public Main(){
		List<Integer> input = new LinkedList<Integer>();
		Random r = new Random();
		
		for(int i=0; i<640000; i++)
			input.add(r.nextInt(1000));
		
		MapReduce mr = new MapReduce();
		mr.map(input);
		mr.parallelReduce();
	}

}

package utils;

import java.util.HashMap;

public class StateMachine {
	private HashMap<State, State> states;
	
	public StateMachine(){
		states = new HashMap<State, State>();
	}
	
	public void addStateTransition(State from, State to){
		states.put(from, to);
	}
	
	public void transition(State from){
		from.exit();
		states.get(from).enter();
	}
}

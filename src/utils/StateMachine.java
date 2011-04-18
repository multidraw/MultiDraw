package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class StateMachine {
	private HashMap<State, State> states;
	private State currentState;
	
	public StateMachine(){
		states = new HashMap<State, State>();
	}
	
	/**
	 * addStateTransition(from, to)
	 * Adds a state transition from one state to another in the state machine.
	 * @param from - State, preliminary state.
	 * @param to - State, final state.
	 */
	public void addStateTransition(State from, State to){
		states.put(from, to);
	}
	
	/**
	 * transition(from)
	 * Transitions from the given state to its next state.
	 * @param from - State, preliminary state
	 */
	public void transition(State from){
		State to = states.get(from);
		currentState = to; 
		
		from.exit();
		to.enter(); 
	}
	
	/**
	 * transition()
	 * Transitions from the current state to its next state.
	 */
	public void transition(){
		transition(currentState);
	}
	
	/**
	 * revertTransition()
	 * Reverts the current state backwards.
	 */
	public void revertTransition(){
		int key = new ArrayList<State>(states.values()).indexOf(currentState);
		if ( key >= 0 ){
			State prevState = (State)states.keySet().toArray()[key];
			currentState.exit();
			prevState.enter();
			currentState = prevState;
		}
	}
	
	/**
	 * startingState(start)
	 * Gives the first state to start at in the state machine
	 * @param start - State, starting state
	 */
	public void startingState(State start){
		currentState = start;
		start.enter();
	}
}

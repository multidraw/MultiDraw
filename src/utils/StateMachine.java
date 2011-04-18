package utils;

import java.util.ArrayList;
import java.util.HashMap;

public class StateMachine {
	private HashMap<String, StateTransition> states;
	private ArrayList<String> stateOrder;
	private int currentState;
	
	public StateMachine(){
		states = new HashMap<String, StateTransition>();
		stateOrder = new ArrayList<String>();
		currentState = 0;
	}
	
	/**
	 * addStateTransition(from, to)
	 * Adds a state transition from one state to another in the state machine.
	 * @param description - String, describing the state transition.
	 * @param from - State, preliminary state.
	 * @param to - State, final state.
	 */
	public void addStateTransition(String description, State from, State to){
		states.put(description, new StateTransition(from, to));
		stateOrder.add(description);
	}
	
	/**
	 * transition(from)
	 * Transitions from the given state to its next state.
	 * @param description - String, description of the current state transition
	 */
	public void transition(String description){
		states.get(description).perform();
		
		currentState = stateOrder.indexOf(description) + 1;
	}
	
	/**
	 * transition()
	 * Transitions from the current state to its next state.
	 */
	public void transition(){
		transition(stateOrder.get(currentState));
	}
	
	/**
	 * startingState(start)
	 * Gives the first state to start at in the state machine
	 * @param start - String, starting state
	 */
	public void start(){
		states.get(stateOrder.get(currentState)).from.enter();
	}
	
	private class StateTransition {
		public State from, to;
		
		public StateTransition(State from, State to){
			this.from = from;
			this.to = to;
		}
		
		public void perform(){
			from.exit();
			to.enter();
		}
	}
}

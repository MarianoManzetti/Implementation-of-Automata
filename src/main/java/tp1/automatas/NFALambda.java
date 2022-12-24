package tp1.automatas;

import java.util.*;
import tp1.utils.Tupla;

/**
 * @author Fernandez, Camilo
 * @author Manzetti, Mariano
 */
public class NFALambda extends FA {

	/*
	 *  Automata methods
	*/
	public NFALambda(StateSet states,	Alphabet alphabet, Set<Tupla<State,Character,State>> transitions) 
	throws IllegalArgumentException, AutomatonException{
		this.states = states;
		this.alphabet = alphabet;
		this.delta = new HashMap<State, HashMap<Character, StateSet>>();
		
		HashMap<Character, StateSet> stateArcs;
		StateSet currentStateSet;

		//For each tupla in transitions Set
		for (Tupla<State, Character, State> tupla : transitions) {
			currentStateSet = new StateSet();
			
			if (states.belongTo(tupla.first().getName()) != null) {
				//Gets the hash mapped for the current state if exists
				if (this.delta.containsKey(tupla.first())) {
					stateArcs = this.delta.get(tupla.first());
				} else {
					stateArcs =  new HashMap<Character, StateSet>();
				}
				//Gets the StateSet mapped for the current char
				if (stateArcs.containsKey(tupla.second())) {
					currentStateSet = stateArcs.get(tupla.second());
				}
				currentStateSet.addState(tupla.third());
				stateArcs.put(tupla.second(), currentStateSet);
				this.delta.put(tupla.first(), stateArcs);
				
			} else {
				throw new IllegalArgumentException("Transition's states must be in NFA 'states' attribute");
			}
		}

		//assert repOk();
	}

	@Override
	public boolean accepts(String string) throws AutomatonException, CloneNotSupportedException {
		//assert repOk();
		if (string == null) throw new IllegalArgumentException("String can't be null");
		if (!verifyString(string)) 
			throw new IllegalArgumentException("The string's characters must belong to automaton's alphabet");
		
		Stack<Tupla<State, Integer, Integer>> p = new Stack<Tupla<State, Integer, Integer>>();
		p.push(new Tupla<State,Integer,Integer>(initialState(), 0, null));

		while (!p.empty()) {

			Tupla<State,Integer,Integer> t = p.pop();
			if((t.first().isFinal()) && (string.length()==t.second())) {
				return true;
			} else if(string.length()<t.second()) {
				return false;
			}
			
			StateSet ssl = new StateSet();
			try {
				ssl = delta(t.first(), null);
			} catch (Exception e) {

			}

			for (State sl : ssl) {
				p.push(new Tupla<State,Integer,Integer>(sl, t.second(), null));
			}

			StateSet ss = new StateSet();
			try {
				ss = delta(t.first(), string.charAt(t.second()));
			} catch (Exception e) {

			}

			for (State s : ss) {
				p.push(new Tupla<State,Integer,Integer>(s, t.second()+1, null));
			}
		}

		return false;
	}

	/**
	 * Check that the alphabet does not contains lambda.
	 * Check that one and just one  state is marked to be a initial state.
	 * Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
	 */
	@Override
	public boolean repOk() {
		if (states == null && alphabet == null && delta == null) {
			return true;
		}
		
		int initState = 0;

		Set<State> ss = this.delta.keySet();

		for (State s : this.states) {
			if (s.isInitial())
				initState++;
		}

		if (initState != 1) {
			return false;
		}

		for (State s : ss) {
			if (!states.contains(s)) {
				return false;
			} else {
				Map<Character, StateSet> m = delta.get(s);
				Set<Character> c = m.keySet();

				for (Character ch : c) {
					if ((ch != null) && (!alphabet.contains(ch))) {
						return false;
					} else if ((ch == null) || (alphabet.contains(ch))) {
						StateSet p = new StateSet();
						p = m.get(ch);
						for (State state : p) {
							if (!states.contains(state)) {
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Converts the automaton to a DFA.
	 * @return DFA recognizing the same language.
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 */
	public DFA toDFA() throws AutomatonException, CloneNotSupportedException {
	
		return null;
	}

	
	/**
	 * Builds the transitions of the new automaton
	 * @param toVisit unvisited StateSets
	 * @param visited visited StateSets
	 * @return all transitions for StateSet on the top of toVisit, and all chars into Alphabet
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 */
	public Set<Tupla<State,Character,State>> buildTransitions(StateSet visited, StateSet toVisit, Set<Tupla<StateSet,Character,StateSet>> st) throws CloneNotSupportedException, AutomatonException {
		return null;

    }
	
	/**
	 * Lambda closure takes a set of states and returns the set of reachable states taking only "lambda" transitions
	 * @param q Queue<State> used as starting point
	 * @return StateSet with the States reached by "lambda", starting from q
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 */
	public StateSet closure(Queue<State> q) throws CloneNotSupportedException, AutomatonException {
		
		StateSet ss = new StateSet();
		
		while (!q.isEmpty()) {
			State s = q.poll();
			if(ss.belongTo(s.getName()) == null);
				ss.addState(s);
			try {
				StateSet auxss = new StateSet();
				auxss = delta(s, null);
				for (State auxs : auxss) {
					q.add(auxs);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return ss;
	}
}
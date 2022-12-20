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
		
		// Map<StateSet, State> stateRef = new HashMap<StateSet, State>();
		
		// // creates singleton StateSet with initial state 
		// Set<State> aux = new HashSet<State>();
		// aux.add(this.initialState());
		// StateSet initSet = new StateSet(aux);
		
		
		// Set<Tupla<StateSet,Character,StateSet>> dfaTransitions = new HashSet<Tupla<StateSet,Character,StateSet>>();
		// // apply lamda closure to initSet
		// initSet = closure(initSet, '/', true);
		// Stack<StateSet> toVisit = new Stack<StateSet>();
		// Set<StateSet> visited = new HashSet<StateSet>();
		// toVisit.push(initSet);
		// // generates all transitions between statesets
		// while(!toVisit.isEmpty()) {
		// 	dfaTransitions.addAll(buildTransitions(toVisit, visited));
		// }		
		// // maps StateSets visited to States 
		// int j = 0;
		// Set<State> dfaStates = new HashSet<State>();
		// Boolean initialSt = false, finalSt = false;
		// for (StateSet ss : visited) {
		// 	if (ss.contains(this.initialState())) {
		// 		initialSt = true;
		// 	} else {initialSt = false;}
		// 	if (ss.containsSomeOf(this.finalStates())) {
		// 		finalSt = true;
		// 	} else {finalSt = false;}
		// 	State ns = new State("q"+j, initialSt, finalSt);
		// 	stateRef.put(ss, ns);
		// 	dfaStates.add(ns);
		// 	j++;
		// }

		// Set<Tupla<State, Character, State>> finalTransitions = new HashSet<Tupla<State, Character, State>>();
		// // transforms <StateSet, Character, StateSet> transitions to <State, Char, State>
		// for (Tupla<StateSet, Character, StateSet> t : dfaTransitions) {
		// 	State fst = stateRef.get(t.first()); 
		// 	Character snd = t.second();
		// 	State trd = stateRef.get(t.third());
		// 	Tupla<State, Character, State> auxT = new Tupla<State,Character,State>(fst, snd, trd);
		// 	finalTransitions.add(auxT);
		// }
		
		// return new DFA(new StateSet(dfaStates), this.alphabet, finalTransitions);
		
		assert repOk();

		StateSet ss1 = new StateSet();
		StateSet ss2 = new StateSet();
		StateSet ss3 = new StateSet();
		StateSet setInitialState = new StateSet();

		ss1.addState(this.initialState());
		setInitialState = closure(ss1, ss2, '/');

		Set<Tupla<StateSet,Character,StateSet>> st = new HashSet<Tupla<StateSet,Character,StateSet>>();

		Set<Tupla<State,Character,State>> newTransitions = this.buildTransitions(ss3, setInitialState, st);
	
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
		// if (visited.contains(toVisit.peek())) {
		// 	toVisit.pop();
		// 	return new HashSet<Tupla<StateSet, Character, StateSet>>();
		// }
		// Set<Tupla<StateSet,Character,StateSet>> allTransitions = new HashSet<Tupla<StateSet,Character,StateSet>>();
		// StateSet aux = toVisit.pop();
		// visited.add(aux);
		// Tupla<StateSet, Character, StateSet> auxTupla = null;
		
		// for (Character c : this.alphabet) {
		// 	// get the neighbors states for c char
		// 	StateSet arriveSt = closure(aux, c, false);
		// 	if (arriveSt.size() > 0) {
		// 		// adds states reached by lamda starting from itself
		// 		arriveSt = closure(aux, '/', true);
		// 		auxTupla = new Tupla<StateSet, Character, StateSet>(aux, c, arriveSt);
		// 		allTransitions.add(auxTupla);
		// 		toVisit.push(arriveSt);
		// 	}
		// }
        // return allTransitions;

		for (State s : toVisit) {
			for (Character c : this.alphabet) {
				if(c!='/') {
					StateSet ss = new StateSet();
					ss.addState(s);
					StateSet newToVisit = new StateSet();
					newToVisit = closure(ss, new StateSet(), c);
					if(!toVisit.containsAll(newToVisit)) {
						Tupla<StateSet,Character,StateSet> t = new Tupla<StateSet,Character,StateSet>(toVisit, c, newToVisit);
						st.add(t);
					}
				}
			}
		}




		return null; 

    }
	
	/**
	 * Gets all states reached by a specific char in all the way, or just neighbors depending on 'total' param value 
	 * @param ss StateSet used as starting point
	 * @param c the symbol used to reach another states
	 * @param total used to determine if go recursive or not. 
	 * @return StateSet with the States reached by c, starting from ss.
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 */
	public StateSet closure(StateSet ss, StateSet ssf, Character c) throws CloneNotSupportedException, AutomatonException {
		// if ss is empty, returns an empty set
		// if (ss.size() == 0) {
		// 	return new StateSet();
		// } else {
		// 	StateSet temp;
		// 	StateSet result = new StateSet();
		// 	HashMap<Character, StateSet> arcs;
		// 	// iterates over ss
		// 	for (State q : ss) {
		// 		temp = new StateSet();
		// 		arcs = delta.get(q);
		// 		// gets the states reached by c, if they exists
		// 		if (arcs.containsKey(c)) 
		// 			temp = arcs.get(c);
		
		// 		result = result.union(temp);
		// 	}
		// 	if (total) {
		// 		return ss.union(closure(result, c, true));
		// 	} else {
		// 		return result;
		// 	}

		// }

		StateSet achieveSet = new StateSet();
		StateSet auxAchieveSet = new StateSet();

		if(c != '/') {
			if(ss.size() == 1 && ssf.size() == 0) {
				
				auxAchieveSet = delta(ss.get(0), c);
	
				closure(auxAchieveSet, achieveSet, c);
			} else if(ss.size() > 0) {

				achieveSet = ss.cloneSS();
				achieveSet.union(ssf.cloneSS());

				for (State state : ss) {
					auxAchieveSet.union(delta(state, c));
				}
	
				closure(auxAchieveSet, achieveSet, c);
			}
			
			return achieveSet;
		} else {

			achieveSet = ss.cloneSS();
			achieveSet.union(ssf.cloneSS());

			if(ss.size() > 0) {
				for (State state : ss) {
					auxAchieveSet.union(delta(state, c));
				}
	
				closure(auxAchieveSet, achieveSet, c);
			}
			
			return achieveSet;
		}
		
	}
}

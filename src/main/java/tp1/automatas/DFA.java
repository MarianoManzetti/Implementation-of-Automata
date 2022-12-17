package tp1.automatas;

import java.util.*;


import tp1.utils.Tupla;
/**
 * @author Fernandez, Camilo
 * @author Manzetti, Mariano
 */
public class DFA extends FA {

	public DFA(StateSet states, Alphabet alphabet, Set<Tupla<State, Character, State>> transitions) 
	throws IllegalArgumentException, AutomatonException {

		this.states = states;
		this.alphabet = alphabet;
		this.delta = new HashMap<State, HashMap<Character, StateSet>>();
		
		HashMap<Character, StateSet> stateArcs;
		StateSet singletonStateSet;

		for (Tupla<State, Character, State> tupla : transitions) {
			singletonStateSet = new StateSet();
			
			if (states.belongTo(tupla.first().getName()) != null) {
				//Gets the map value for the current state if exists
				if (this.delta.containsKey(tupla.first())) {
					stateArcs = this.delta.get(tupla.first());
				} else {
					stateArcs = new HashMap<Character, StateSet>();
				}
				
				if (stateArcs.containsKey(tupla.second())) {
					// since dfa must have one transition per character per state
					throw new IllegalArgumentException("Invalid transitions for DFA");
				} else {
					singletonStateSet.addState(tupla.third());
					stateArcs.put(tupla.second(), singletonStateSet);
					this.delta.put(tupla.first(), stateArcs);
				}
			} else {
				throw new IllegalArgumentException("Transition's states must be in DFA 'states' attribute");
			}
		}

		//assert repOk();
	}

	public DFA cloneDFA() throws CloneNotSupportedException, AutomatonException{
		StateSet ss = states.cloneSS();
		Alphabet a = alphabet.cloneAlpha();
		Set<Tupla<State, Character, State>> t = cloneDelta();

		return new DFA(ss, a, t);
	}

	public Set<Tupla<State, Character, State>> cloneDelta() throws CloneNotSupportedException, AutomatonException{
		Set<Tupla<State, Character, State>> t = new HashSet<Tupla<State, Character, State>>();

		for (State s : states) {
			for (Character c : alphabet) {
				StateSet setD = delta(s, c);
				if(setD.size() > 0) {
					t.add(new Tupla<State,Character,State>(s.cloneState(), c, (setD.get(0)).cloneState()));
				}
			}
		}

		return t;
	}

	@Override
	public boolean accepts(String string) throws IllegalArgumentException, AutomatonException{
		assert repOk();
		if (string == null) throw new IllegalArgumentException("String can't be null");
		if (!verifyString(string)) 
			throw new IllegalArgumentException("The string's characters must belong to automaton's alphabet");

		State s = this.initialState();
		for (char c : string.toCharArray()) {
			StateSet singletonSet = new StateSet();
			try {
				singletonSet = delta(s, c);
			} catch (Exception e) {
				return false;
			}
			System.out.println("im "+c+"!");
			System.out.println(s.toString());
			if (singletonSet.size() > 0) {
				s = singletonSet.get(0);
				System.out.println("Image state"+s.toString());
			} else {
				return false;
			}
		}
		System.out.println("is final "+s.isFinal());
		return s.isFinal();
	}
	
	/**
	 * Check that one and just one  state is marked to be a initial state.
	 * Check that all transitions are correct. All states and characters should be part of the automaton set of states and alphabet.
	 * Check that there are not lambda transitions.
	 * Check that the transition relation is deterministic.
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

		if (initState != 1)
			return false;

		for (State s : ss) {
			if (!states.contains(s)) {
				return false;
			} else {
				Map<Character, StateSet> m = delta.get(s);
				Set<Character> c = m.keySet();

				for (Character ch : c) {
					if ((!alphabet.contains(ch)) || (ch == null)) {
						return false;
					} else {
						StateSet p = new StateSet();
						p = m.get(ch);
						if (p.size() > 1) {
							return false;
						} else {
							for (State state : p) {
								if (!states.contains(state))
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
	 * Returns a new automaton which recognizes the complementary language.
	 * @throws CloneNotSupportedException
	 * 
	 *
	 * @throws AutomatonException
	 *
	 * @returns a new DFA accepting the language's complement.
	 */
	public DFA complement() throws CloneNotSupportedException, AutomatonException {
		
		assert repOk();

		DFA dfa = cloneDFA();

		StateSet ss = new StateSet();
		Alphabet a = new Alphabet();
		Set<Tupla<State,Character,State>> t = new HashSet<Tupla<State,Character,State>>();

		ss = dfa.states.cloneSS();
		for (State s : ss) {
			if(s.isFinal()) {
				s.setFinal(false);
			} else {
				s.setFinal(true);
			}
		}

		a = dfa.alphabet.cloneAlpha();

		// New cloned delta
		for (State s : dfa.states) {
			State s1 = s.cloneState();
			if(s1.isFinal()) {
				s1.setFinal(false);
			} else {
				s1.setFinal(true);
			}
			for (Character c : dfa.alphabet) {
				StateSet setD = dfa.delta(s, c);
				if(setD.size() > 0) {
					State s2 = (setD.get(0)).cloneState();
					if(s2.isFinal()) {
						s2.setFinal(false);
					} else {
						s2.setFinal(true);
					}
					t.add(new Tupla<State,Character,State>(s1, c, s2));
				}
			}
		}

		return new DFA(ss, a, t);
	}

	/**
	 * Returns a new automaton which recognizes the intersection of both languages,
	 * the one accepted by 'this' and the one represented by 'other'.
	 *
	 * @throws Exception
	 *
	 * @returns a new DFA accepting the intersection of both languages.
	 */
	public DFA intersection(DFA other) throws Exception {

		assert repOk();
		assert other.repOk();
		
		return (complement().union(other.complement())).complement();
	}

	/**
	 * Returns a new automaton which recognizes the union of both languages, the one
	 * accepted by ’this’ and the one represented by ’other’.
	 *
	 * @throws Exception
	 * @throws AutomatonException
	 *
	 * @returns a new DFA accepting the union of both languages.
	 */
	public DFA union(DFA other) throws AutomatonException, Exception {
		assert repOk();
		assert other.repOk();
		
		// New initial state
		State newInitState = new State("q'", true, false);
		// New StateSet
		StateSet ss = new StateSet(); 
		//New Alphabet
		Alphabet a = new Alphabet();
		// New Transitions
		Set<Tupla<State, Character, State>> t = new HashSet<Tupla<State, Character, State>>();

		// New cloned StateSet 
		ss = (this.states).cloneSS();
		ss.union((other.states).cloneSS());

		for (State s : ss) {
			if(s.isInitial()) {
				s.setInitial(false);
				t.add(new Tupla<State,Character,State>(newInitState, null, s));
			} 
		}
		
		ss.addState(newInitState);

		// New cloned Alphabet
		a = (this.alphabet).cloneAlpha();
		a.union((other.alphabet).cloneAlpha());

		// New cloned this.delta
		for (State s : this.states) {
			for (Character c : this.alphabet) {
				StateSet setD = this.delta(s, c);
				if(setD.size() > 0) {
					t.add(new Tupla<State,Character,State>(s.cloneState(), c, (setD.get(0)).cloneState()));
				}
			}
		}

		// New cloned other.delta
		for (State s : other.states) {
			for (Character c : other.alphabet) {
				StateSet setD = other.delta(s, c);
				if(setD.size() > 0) {
					t.add(new Tupla<State,Character,State>(s.cloneState(), c, (setD.get(0)).cloneState()));
				}
			}
		}

		//NFALambda newNFA = new NFALambda(ss, a, t);
		//DFA newDFA = newNFA.toDFA();
		//assert newDFA.repOk();

		return new DFA(ss, a, t);
	}
}

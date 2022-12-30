package tp1.automatas;

import java.util.*;
import tp1.utils.Tupla;

/**
 * @author Fernandez, Camilo
 * @author Manzetti, Mariano
 */
public class NFALambda extends FA {

	// Constructor
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

	public NFALambda cloneNFALambda() throws CloneNotSupportedException, AutomatonException{
		StateSet ss = states.cloneSS();
		Alphabet a = alphabet.cloneAlpha();
		Set<Tupla<State, Character, State>> t = cloneDelta();

		return new NFALambda(ss, a, t);
	}

	public Set<Tupla<State, Character, State>> cloneDelta() throws CloneNotSupportedException, AutomatonException{
		Set<Tupla<State, Character, State>> t = new HashSet<Tupla<State, Character, State>>();

		for (State s : states) {
			StateSet setL = new StateSet();
			
			try {
				setL = delta(s, null);
				if(setL.size() > 0) {
					for (State s2 : setL) {
						t.add(new Tupla<State,Character,State>(s.cloneState(), null, s2.cloneState()));
					}
				}
			} catch (Exception e) {

			}
			
			for (Character c : alphabet) {
				StateSet setD = new StateSet();
				
				try {
					setD = delta(s, c);
					if(setD.size() > 0) {
						for (State s3 : setD) {
							t.add(new Tupla<State,Character,State>(s.cloneState(), c, s3.cloneState()));
						}
					}
				} catch (Exception e) {
					continue;
				}
			}
		}

		return t;
	}
	
	/**
	 * Converts the automaton to a DFA.
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 * @return DFA recognizing the same language.
	 */
	public DFA toDFA() throws AutomatonException, CloneNotSupportedException {
		
		NFALambda nfal = this.cloneNFALambda();

		Set<Tupla<Queue<State>, Character, Queue<State>>> transitions = new HashSet<Tupla<Queue<State>, Character, Queue<State>>>();

		Queue<State> q = new LinkedList<State>();
		Queue<Queue<State>> t = new LinkedList<Queue<State>>();
		Queue<Queue<State>> visit = new LinkedList<Queue<State>>();

		q.add(nfal.initialState());

		t.add(nfal.closure(q));

		while (!t.isEmpty()) {
			Queue<State> s = t.poll();
			if(!visit.contains(s)) {
				visit.add(s);
				for (Character c : nfal.alphabet) {
					Queue<State> m = new LinkedList<State>();
					m = nfal.closure(nfal.move(s, c));
					if(!m.isEmpty()) {
						if(!t.contains(m)) {
							t.add(m);
						}
						transitions.add(new Tupla<Queue<State>,Character,Queue<State>>(s, c, m));
					}
				}
			} else {
				continue;
			}
		}

		Tupla<StateSet, Set<Tupla<State, Character, State>>, Character> build = nfal.buildTransitions(transitions, visit);

		DFA dfa = new DFA(build.first().cloneSS(), nfal.alphabet.cloneAlpha(), build.second());

		return dfa;
	}

	
	/**
	 * Move takes a set of states and a symbol from the alphabet and returns the set of states reachable by the given symbol
	 * @param q Queue<State> used as starting point
	 * @param a alphabet symbol
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 * @return set of states reachable by the given symbol
	 */
	public Queue<State> move(Queue<State> q, Character a) throws CloneNotSupportedException, AutomatonException {
		
		Queue<State> qss = new LinkedList<State>();

		for (State s : q) {
			try {
				StateSet auxss = new StateSet();
				auxss = delta(s, a);
				for (State auxs : auxss) {
					qss.add(auxs);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return qss;

    }
	
	/**
	 * Lambda closure takes a set of states and returns the set of reachable states taking only "lambda" transitions
	 * @param q Queue<State> used as starting point
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 * @return Queue<State> with the States reached by "lambda", starting from q
	 */
	public Queue<State> closure(Queue<State> q) throws CloneNotSupportedException, AutomatonException {
		
		Queue<State> qss = new LinkedList<State>();
		Queue<State> auxQ = new LinkedList<State>();

		for (State s : q) {
			auxQ.add(s);
		}

		while (!auxQ.isEmpty()) {
			State s = auxQ.poll();
			if(!qss.contains(s));
				qss.add(s);
			try {
				StateSet auxss = new StateSet();
				auxss = delta(s, null);
				for (State auxs : auxss) {
					auxQ.add(auxs);
				}
			} catch (Exception e) {
				continue;
			}
		}

		return qss;
	}

	/**
	 * Transition constructor and state set
	 * @param t set of extended transitions
	 * @param v set of visited states
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 * @return tuple with new states and new transitions
	 */
	public Tupla<StateSet, Set<Tupla<State, Character, State>>, Character> buildTransitions(Set<Tupla<Queue<State>, Character, Queue<State>>> t, Queue<Queue<State>> v) throws AutomatonException {
		
		HashMap<Queue<State>, String> aux = new HashMap<Queue<State>, String>();
		Set<Tupla<State, Character, State>> newT = new HashSet<Tupla<State,Character,State>>();
		StateSet newSS = new StateSet();

		int i = 0;
		for (Queue<State> queue : v) {
			aux.put(queue, "q"+i);

			Boolean f1 = false;
			Boolean i1 = false;

			for (State s : queue) {
				if(!f1) {
					if(s.isFinal()) {
						f1 = true;
					}
				}
				if(!i1) {
					if(s.isInitial()) {
						i1 = true;
					}
				}
			}

			State newS = new State("q"+i, i1, f1);
			if(newS.isInitial()) {
				State is = newSS.containsInitialState();
				if(is != null) {
					newS.setInitial(false);
					newSS.addState(newS);
				} else {
					newSS.addState(newS);
				}
			} else {
				newSS.addState(newS);
			}

			i++;
		}
		
		for (Tupla<Queue<State>, Character, Queue<State>> tupla : t) {

			String name1 = aux.get(tupla.first());
			String name2 = aux.get(tupla.third());

			State s1 = newSS.belongTo(name1);
			State s2 = newSS.belongTo(name2);

			newT.add(new Tupla<State,Character,State>(s1, tupla.second(), s2));
		}

		return new Tupla<StateSet, Set<Tupla<State, Character, State>>, Character>(newSS, newT, null);
	};
}
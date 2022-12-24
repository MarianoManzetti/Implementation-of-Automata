package tp1.automatas;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * @author Fernandez, Camilo
 * @author Manzetti, Mariano
 */
public class StateSet implements Iterable<State>, Cloneable {

	private Set<State> states;

	public StateSet() {
		states = new HashSet<State>();
	}

	public StateSet cloneSS() throws CloneNotSupportedException, AutomatonException {
		StateSet css = new StateSet();
		for (State s : states) {
			css.addState(s.cloneState());
		}
		return css;
	}

	public State addState(String name)throws AutomatonException{
		if(name == null || name =="")
			throw new AutomatonException("Node name invalid");

		for(State f : states) {
			if(f.getName().equals(name)) {
				return f;
			}
		}
		State freshState = new State(name, false, false);
		states.add(freshState);
		return freshState;
	}
	

	public State addState(String name, boolean isInitial, boolean isFinal) throws AutomatonException{
		if(name == null || name =="")
			throw new AutomatonException("Node name invalid");

		for(State f : states) {
			if(f.getName().equals(name)) {
				f.setInitial(isInitial);
				f.setFinal(isFinal);
				return f;
			}
		}
		State freshState = new State(name, isInitial, isFinal);
		states.add(freshState);
		return freshState;
	}

	public State addState(State s) throws AutomatonException {
		if (s.getName() == null || s.getName() == "") {
			throw new AutomatonException("Node name invalid");
		}

		if(belongTo(s.getName()) == null) {
			states.add(s);
			return s;
		}
			
		return belongTo(s.getName());
	}

	public void deleteState(String name) {
		for(State f : states) {
			if(f.getName().equals(name)) {
				states.remove(f);
				return;
			}
		}
	}

	public State belongTo(String name) {
		for(State f : states) {
			if(f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	public int size() {
		return states.size();
	}

	public State get(int index) {
		int i = 0;
		for (State state : states) {
			if (i == index) return state;
			i += 1;
		}
		return null;
	}

	public StateSet union(StateSet ss) {
		states.addAll(ss.states);
		return this;
	}

	@Override
	public Iterator<State> iterator() {
		return states.iterator();
	}

	@Override
	public String toString() {
		String str = "( ";
		for (State s : states) {
			str += s.toString() + " ";
		}
		return str + ")";
	}

	/**
	 * compare two StateSets
	 * @param s StateSet to compare
	 * @return true if at least one State of s param belongs to the current StateSet
	 */
	public boolean containsSomeOf(StateSet s) {
		return this.states.retainAll(s.getSet());
	}
	/**
	 * gets the corresponding set of states out of the class
	 * @return a set of states
	 */
	public Set<State> getSet() {
		return this.states;
	}

	public boolean contains(State state) {
		return states.contains(state);
	}

	public boolean containsAll(StateSet ss) {
		if(ss.size() == 0)
			return false;

		for (State s : ss) {
			if(!contains(s))
				return false;
		}
		
		return true;
	}
}
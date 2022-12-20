package tp1.automatas;

import java.util.HashMap;

/**
 * @author Fernandez, Camilo
 * @author Manzetti, Mariano
 */
public abstract class FA {

	protected StateSet states;

	protected Alphabet alphabet;

	/**All states used in delta function
	* must belong to the automaton states  set
	* and all labels must belong to the automaton alphabet
	**/
	public HashMap<State, HashMap<Character, StateSet>> delta;

	/*
	 * @return the atomaton's set of states.
	 */
	public StateSet getStates() {

		if (states.size() == 0)
			throw new NullPointerException("Set is empty");

		return states;
	}

	/**
	 * @return the atomaton's alphabet.
	 */
	public Alphabet getAlphabet() {
		
		return alphabet;
	}

	/**
	 * @return the atomaton's initial state.
	 */
	public State initialState() {

		for (State s:states) {
			if (s.isInitial()) 
				return s;
		}
		return null;
	}

	/**
	 * @return the atomaton's final states.
	 * @throws AutomatonException
	 */
	public StateSet finalStates() throws AutomatonException{

		StateSet result = new StateSet();
		for (State s:states) {
			if (s.isFinal() == true) {
				result.addState(s.getName());
			}
		}
		return result;
	}

	/**
	 * Query for the automaton's transition function.
	 *
	 * @return A set of states (when FA is a DFA this method return a
	 * singleton set) corresponding to the successors of the given state
	 * via the given label according to the transition function.
	 * @throws AutomatonException
	 */
	public StateSet delta(State from, Character label) throws AutomatonException {

		if (label != null && !alphabet.belongsTo(label)) 
			throw new IllegalArgumentException("'label' must belong to the automaton's alphabet");
			
		if (!delta.containsKey(from) || states.belongTo(from.getName()) == null)
			throw new IllegalArgumentException("Invalid state!");

		HashMap<Character, StateSet> s = delta.get(from);
		if (!s.containsKey(label))
			throw new IllegalArgumentException("No states mapped for this label");

		return s.get(label);
	}

	/**
	 * Verifies whether the string is composed of characters in the alphabet of the automaton.
	 * @return True iff the string consists only of characters in the alphabet.
	 */
	public boolean verifyString(String s) {
		Boolean control = true;
		int i = 0;

		while (i < s.length() && control == true) {
			control = alphabet.belongsTo(s.charAt(i));
			i++;
		}
		return control;
	}

	/**
	 * converts the automaton to a string representation of it.
	 * @return a string representation of the automaton.
	 */
	@Override
	public String toString() {
		return "( States -> " + states.toString() + " )\n" +
			"( Alphabet -> "+ alphabet.toString()+ " )\n" +
			"( State transitions -> " + delta.toString() + " )";
	}

	/**
	 * TODO: Abstract methods to be implements for the subclasses
	 * **/

	/**
	 * @return True iff the automaton is in a consistent state.
	 */
	public abstract boolean repOk();

	/**
	 * Tests whether a string belongs to the language of the current
	 * finite automaton.
	 *
	 * @param string String to be tested for acceptance.
	 * @return Returns true iff the current automaton accepts the given string.
	 * @throws AutomatonException
	 * @throws CloneNotSupportedException
	 */
	public abstract boolean accepts(String string) throws AutomatonException, CloneNotSupportedException;	
}

package tp1.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tp1.automatas.Alphabet;
import tp1.automatas.AutomatonException;
import tp1.automatas.State;
import tp1.automatas.StateSet;

/**
 * @author Vale
 * @version 1.0
 *
 **/
public class DotReader {

	private static BufferedReader breader = null;
	private static String dir = System.getProperty("user.dir") + "/";

	private static Pattern patternInitialNode;
	private static Pattern patternFinalNode;
	private static Pattern patternArcs;
	private static Pattern patternUnLabeledArcs;

	// To save the symbols, nodes and arcs read from dot file
	private StateSet nodes = new StateSet();
	private Alphabet symbols = new Alphabet();
	private Set<Tupla<State, Character, State>> arcs = new HashSet<Tupla<State, Character, State>>();

	/**
	 * public API
	 *
	 */

	/**
	 * builds a dotReader object from a .dot file
	 *
	 * @param relative path of .dot file representing a graph
	 **/
	public DotReader(String path) {
		FileReader freader = null;
		try {
			// Opens file
			freader = new FileReader(dir + path + ".dot");
		} catch (Exception e) {
			System.out.println("Problem to open file: " + dir + path + ".dot" + e.toString());
		}
		breader = new BufferedReader(freader);
		initPatterns();
	}

	/**
	 * getters
	 **/

	/**
	 * returns graph nodes set
	 *
	 * @return nodes set of the graph built from the parse() method
	 **/
	public StateSet getNodes() {
		return nodes;
	}

	/**
	 * returns arcs symbols of the graph
	 *
	 * @return symbols set of the graph built from the parse() method
	 **/
	public Alphabet getSymbols() {
		return symbols;
	}

	/**
	 * returns arcs of the graph built from the parse() method
	 *
	 * @return arcs set of the graph
	 **/
	public Set<Tupla<State, Character, State>> getArcs() {
		return arcs;
	}

	/**
	 * parses the .dot file representing a graph and determines the set of nodes,
	 * symbols and arcs of it, saving them in the nodes, symbols and arcs attributes
	 * respectively.
	 **/
	public void parse() throws Exception {
		String line;
		while ((line = breader.readLine()) != null) {
			line = line.replaceAll("\t", "");
			line = line.replaceAll(" ", "");

			searchAndSaveInitialNode(line);
			searchAndSaveArcs(line);
			searchAndSaveFinalNode(line);

		}
		close();
	}

	@Override
	public String toString() {
		return "states=" + nodes.toString() + ", alphabet=" + symbols.toString() + ", transitions=" + arcs.toString();
	}

	/**
	 * helper static methods
	 **/

	/**
	 * builds and compile regular expressions that describe the different parts
	 * (nodes and arcs) of a graph in the dot language
	 **/
	private static void initPatterns() {

		// Regular Expressions
		final String regexInitialNode = "inic->([a-zA-Z0-9]+);";
		final String regexArcs = "([a-zA-Z0-9]+)->([a-zA-Z0-9]+)\\[label=\"([a-zA-Z0-9]+)\"];";
		final String regexUnLabeledArcs = "([a-zA-Z0-9]+)->([a-zA-Z0-9]+)\\[color=red\\];";
		final String regexFinalNodes = "([a-zA-Z0-9]+)\\[shape=doublecircle];";

		// Compiles the patterns
		patternInitialNode = Pattern.compile(regexInitialNode);
		patternFinalNode = Pattern.compile(regexFinalNodes);
		patternArcs = Pattern.compile(regexArcs);
		patternUnLabeledArcs = Pattern.compile(regexUnLabeledArcs);
	}

	/**
	 * private methods used in the parse() method
	 **/
	private void searchAndSaveInitialNode(String line) throws AutomatonException {
		Matcher mat = patternInitialNode.matcher(line);
		if (mat.matches()) {
			// Line is a initial state
			String initialNodeName = mat.group(1);
			State s = nodes.addState(initialNodeName);
			s.setInitial(true);
		}
	}

	private void searchAndSaveFinalNode(String line) throws AutomatonException {
		Matcher mat = patternFinalNode.matcher(line);
		if (mat.matches()) {
			// Line is a initial state
			String finalNodeName = mat.group(1);
			State n = nodes.addState(finalNodeName);
			n.setFinal(true);
		}
	}

	private void searchAndSaveArcs(String line) throws AutomatonException {
		Matcher mat = patternArcs.matcher(line);
		if (mat.matches()) {
			String nameFromNode = mat.group(1);
			String nameToNode = mat.group(2);
			State from = nodes.addState(nameFromNode);
			State to = nodes.addState(nameToNode);

			Character with = mat.group(3).charAt(0);
			symbols.addSymbol(with);
			arcs.add(new Tupla<State, Character, State>(from, with, to));
		}
		mat = patternUnLabeledArcs.matcher(line);
		if (mat.matches()) {
			// Line is a transition
			String nameFromNode = mat.group(1);
			String nameToNode = mat.group(2);
			State from = nodes.addState(nameFromNode);
			State to = nodes.addState(nameToNode);
			arcs.add(new Tupla<State, Character, State>(from, null, to));
		}
	}

	private static void close() {
		try {
			breader.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}

package tp1.automatas;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import tp1.utils.DotReader;
import tp1.utils.Tupla;

public class DFAAutomatonMethodsTests2 {

	private static DFA dfa;
	private static StateSet s;
	private static Alphabet a;
	private static Set<Tupla<State,Character,State>> t;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		DotReader dotReader = new DotReader("src/test/java/tp1/dfa2");
		dotReader.parse();

		s = dotReader.getNodes();
		a = dotReader.getSymbols();
		t = dotReader.getArcs();

		dfa = new DFA(s, a, t);
	}

	// Tests for DFA2

	@Test
	public void testRepOk() {
		assertTrue(dfa.repOk());
	}
	
	@Test
	public void testAccept() throws Exception {
		assertTrue(dfa.accepts("bbbbbb"));
	}

	@Test
	public void testAccept2() throws Exception {
		assertTrue(dfa.accepts("bb"));
	}

	@Test
	public void testAccept3() throws Exception {
		assertTrue(dfa.accepts(""));
	}

	@Test
	public void testNoAccept() throws Exception {
		assertFalse(dfa.accepts("bbbbb"));
	}

	@Test
	public void testNoAccept2() throws Exception {
		assertFalse(dfa.accepts("b"));
	}

	@Test
	public void testComplement() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertTrue(dfa.accepts("bbbbbb"));
		assertFalse(dfaComplement.accepts("bbbbbb"));
	}

	@Test
	public void testComplement2() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertTrue(dfa.accepts("bb"));
		assertFalse(dfaComplement.accepts("bb"));
	}

	@Test
	public void testComplement3() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertTrue(dfa.accepts(""));
		assertFalse(dfaComplement.accepts(""));
	}

	@Test
	public void testComplement4() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertFalse(dfa.accepts("bbbbb"));
		assertTrue(dfaComplement.accepts("bbbbb"));
	}

	@Test
	public void testComplement5() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertFalse(dfa.accepts("b"));
		assertTrue(dfaComplement.accepts("b"));
	}

	@Test
	public void testInitialState() {
		State initS = dfa.initialState();
		assertEquals(initS.getName(), "q0");
		assertTrue(initS.isInitial());
	}

	@Test
	public void testFinalStates() throws AutomatonException {
		StateSet finS = dfa.finalStates();
		assertTrue(finS.size()==1);
		assertTrue(finS.belongTo("q0") != null);
	}
}

package tp1.automatas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import tp1.utils.DotReader;
import tp1.utils.Tupla;

public class DFAAutomatonMethodsTests4 {

	private static DFA dfa;
    private static StateSet s;
	private static Alphabet a;
	private static Set<Tupla<State,Character,State>> t;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		DotReader dotReader = new DotReader("src/test/java/tp1/dfa4");
		dotReader.parse();


		s = dotReader.getNodes();
		a = dotReader.getSymbols();
		t = dotReader.getArcs();

		dfa = new DFA(s, a, t);
	} 

    // Tests for DFA4
	
    @Test
	public void testRepOk() throws Exception {
		assertTrue(dfa.repOk());
	}

    @Test
	public void testAccept() throws Exception {
		assertTrue(dfa.accepts("automatas"));
	}

	
	@Test
	public void testAccept2() throws Exception {
		assertTrue(dfa.accepts("y"));
	}
	
	@Test
	public void testAccept3() throws Exception {
		DFA dfa0 = new DFA(s,a,t);
		assertTrue(dfa0.accepts("lenguajes"));
	}

	@Test
	public void testNoAccept() throws Exception {
		assertFalse(dfa.accepts("automata"));
	}

	@Test
	public void testNoAccept2() throws Exception {
		assertFalse(dfa.accepts("lenguaje"));
	}

	@Test
	public void testFinalState2() throws Exception {
		StateSet f = dfa.finalStates();
		assertTrue(f.size()==3);
		assertTrue(f.belongTo("q10")!=null);
		assertTrue(f.belongTo("q9")!=null);
		assertTrue(f.belongTo("q19")!=null);
	}
	
	@Test
	public void testFinalState3() throws Exception {
		StateSet f = dfa.finalStates();
		assertTrue(f.belongTo("q0")==null);
	}
	
	@Test
	public void testInitialState() throws Exception {
		State i = dfa.initialState();
		assertTrue(i.getName().equals("q0"));
	}

	@Test
	public void testComplement1() throws Exception {
		DFA dfaComplement = dfa.complement();
		assertTrue(dfaComplement.repOk());
		assertTrue(dfa.accepts("automatas"));
		assertFalse(dfaComplement.accepts("automatas"));
	}

	@Test
	public void testComplement2() throws Exception {
		assertFalse(dfa.complement().accepts("y"));
	}

	@Test
	public void testComplement3() throws Exception {
		assertFalse(dfa.complement().accepts("lenguajes"));
	}
	
	@Test
	public void testComplement4() throws Exception {
		assertTrue(dfa.complement().accepts("automata"));
	}	

	@Test
	public void testComplement5() throws Exception {
		assertTrue(dfa.complement().accepts("lenguaje"));
	}	

	@Test
	public void testComplement6() throws Exception {
		assertTrue(dfa.complement().accepts(""));
	}	
}

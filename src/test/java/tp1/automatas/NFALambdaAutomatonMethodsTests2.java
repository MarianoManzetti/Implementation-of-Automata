package tp1.automatas;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import tp1.utils.DotReader;
import tp1.utils.Tupla;

public class NFALambdaAutomatonMethodsTests2 {
    
    private static NFALambda nfal;
	private static StateSet s;
	private static Alphabet a;
	private static Set<Tupla<State,Character,State>> t;
	private static Queue<State> q = new LinkedList<State>();
	private static StateSet auxSS;

    @BeforeClass
	public static void setUpBeforeClass() throws Exception{
		// NFALambda
		DotReader dotReader = new DotReader("src/test/java/tp1/nfalambda2");
		dotReader.parse();

		s = dotReader.getNodes();
		a = dotReader.getSymbols();
		t = dotReader.getArcs();
		nfal = new NFALambda(s, a, t);
    }

    @Test
	public void testBrokenRepOk() throws IllegalArgumentException, AutomatonException {
		assertTrue(nfal.repOk());
	}

	@Test
	public void testAccept() throws Exception {
		assertTrue(nfal.accepts("casa"));
	}

	@Test
	public void testAccept2() throws Exception {
		assertTrue(nfal.accepts("casacasa"));
	}

	@Test
	public void testAccept3() throws Exception {
		assertTrue(nfal.accepts("casacasacasa"));
	}

	@Test
	public void testNoAccept() throws Exception {
		assertFalse(nfal.accepts(""));
	}

	@Test
	public void testNoAccept2() throws Exception {
		assertFalse(nfal.accepts("ca"));
	}

	@Test
	public void testNoAccept3() throws Exception {
		assertFalse(nfal.accepts("c"));
	}

	@Test
	public void testNoAccept4() throws Exception {
		assertFalse(nfal.accepts("cas"));
	}

	@Test
	public void testNoAccept5() throws Exception {
		assertFalse(nfal.accepts("casac"));
	}

	@Test
	public void testClosure() throws CloneNotSupportedException, AutomatonException {

		q.add(nfal.finalStates().get(0));

		auxSS = nfal.closure(q);

		assertTrue(auxSS.size() == 2);
		assertTrue(auxSS.belongTo("q0") != null);
		assertTrue(auxSS.belongTo("q4") != null);
	}
}

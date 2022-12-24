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

public class NFALambdaAutomatonMethodsTests1 {
    
    private static NFALambda nfal;
	private static StateSet s;
	private static Alphabet a;
	private static Set<Tupla<State,Character,State>> t;
	private static Queue<State> q = new LinkedList<State>();
	private static StateSet auxSS;

    @BeforeClass
	public static void setUpBeforeClass() throws Exception{
		// NFALambda
		DotReader dotReader = new DotReader("src/test/java/tp1/nfalambda1");
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
		assertTrue(nfal.accepts("aaaaaaaa"));
	}

	@Test
	public void testAccept2() throws Exception {
		assertTrue(nfal.accepts(""));
	}

	@Test
	public void testAccept3() throws Exception {
		assertTrue(nfal.accepts("ca"));
	}

	@Test
	public void testAccept4() throws Exception {
		assertTrue(nfal.accepts("a"));
	}

	@Test
	public void testAccept5() throws Exception {
		assertTrue(nfal.accepts("caa"));
	}

	@Test
	public void testNoAccept() throws Exception {
		assertFalse(nfal.accepts("ac"));
	}

	@Test
	public void testNoAccept2() throws Exception {
		assertFalse(nfal.accepts("c"));
	}

	@Test
	public void testClosure() throws CloneNotSupportedException, AutomatonException {

		q.add(nfal.initialState());

		auxSS = nfal.closure(q);

		assertTrue(auxSS.size() == 2);
		assertTrue(auxSS.belongTo("q0") != null);
		assertTrue(auxSS.belongTo("q2") != null);
	}
}
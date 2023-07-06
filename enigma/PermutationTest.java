package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Defining custom test rotors and test alphabets. */
    public String cycleRotor1 = "(AELTPHQXRU) "
            + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
    public String cycleRotor2 = "(FIXVYOMW) (CDKLHUP) "
            + "(ESZ) (BJ) (GR) (NT) (A) (Q)";
    public String shiftCypher = "(ABCDEF"
            + "GHIJKLMNOPQRSTUVWXYZ)";
    public Alphabet fromAlpha = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public Permutation permRotor1 = new Permutation(cycleRotor1, fromAlpha);
    public Permutation permRotor2 = new Permutation(cycleRotor2, fromAlpha);
    public Permutation shift = new Permutation(shiftCypher, fromAlpha);
    private Permutation notReal = new Permutation("(ABXYZD)",
            new Alphabet("DXYZAB"));

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha1, String toAlpha) {
        int N = fromAlpha1.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha1.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void permuteTest() {
        assertEquals(permRotor1.permute('A'), 'E');
        assertEquals(permRotor1.permute(0), 4);
        assertEquals(permRotor1.permute('F'), 'G');
        assertEquals(permRotor1.permute(5), 6);
        assertEquals(permRotor1.permute('S'), 'S');
        assertEquals(permRotor1.permute(18), 18);
        assertEquals(permRotor2.permute('P'), 'C');
        assertEquals(permRotor2.permute(15), 2);
        assertEquals(permRotor2.permute('O'), 'M');
        assertEquals(permRotor2.permute(14), 12);
        assertEquals(permRotor2.permute('A'), 'A');
        assertEquals(permRotor2.permute(0), 0);
        assertEquals(shift.permute('A'), 'B');
    }

    @Test
    public void invertTest() {
        assertEquals(permRotor1.invert('C'), 'Y');
        assertEquals(permRotor1.invert(2), 24);
        assertEquals(permRotor1.invert('S'), 'S');
        assertEquals(permRotor1.invert(18), 18);
        assertEquals(permRotor2.invert('T'), 'N');
        assertEquals(permRotor2.invert(19), 13);
        assertEquals(permRotor2.invert('F'), 'W');
        assertEquals(permRotor2.invert(5), 22);
        assertEquals(shift.invert('Y'), 'X');
    }

    @Test
    public void derangementTest() {
        assertFalse(permRotor1.derangement());
        assertFalse(permRotor2.derangement());
        assertTrue(shift.derangement());
    }

    @Test
    public void sizeTest() {
        assertEquals(permRotor1.size(), 26);
        assertEquals(permRotor2.size(), 26);
        assertEquals(shift.size(), 26);
        assertEquals(notReal.size(), 6);
    }


}

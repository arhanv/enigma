package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphabetTest {
    public static Alphabet testAlphabet = new Alphabet();
    public static Alphabet differentAlphabet = new Alphabet("ABCXYZ");

    @Test
    /** Checks if the String representation and length of the default
     *  alphabet are correct. */
    public void checkDefault() {
        assertEquals(testAlphabet.getAlphabet(), "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        assertEquals(testAlphabet.size(), 26);
    }

    @Test
    /** Checks if the .contains() method can correctly recognize whether
     * a character is present in the given alphabet. */
    public void testContains() {
        assertTrue(differentAlphabet.contains('Y'));
        assertFalse(differentAlphabet.contains('H'));
        assertTrue(testAlphabet.contains('A'));
        assertFalse(testAlphabet.contains('ß'));
    }

    @Test
    /** Checks if the .toChar() and .toInt() methods work as expected. */
    public void testCharInt() {
        assertEquals(testAlphabet.toInt('C'), 2);
        assertEquals(differentAlphabet.toInt('X'), 3);
        assertEquals(differentAlphabet.toChar(testAlphabet.toInt('D')), 'X');
        assertEquals(testAlphabet.toChar(8), 'I');
    }
}

package enigma;

import static enigma.EnigmaException.error;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Arhan Vohra
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        originalAlphabet = alphabet;
        alphabetArray = originalAlphabet.getAlphabet().toCharArray();
        _cycles = cycles.split(" ");
        try {
            for (char x : alphabetArray) {
                for (String y : _cycles) {
                    for (int z = 1; z < y.length() - 1; z++) {
                        char permuter = alphabetArray[
                                originalAlphabet.toInt(x)];
                        if ((permuter == y.charAt(z))
                                && (z != y.length() - 2)) {
                            char nextChar = y.charAt(z + 1);
                            alphabetArray[
                                    originalAlphabet.toInt(x)] = nextChar;
                            break;
                        } else if (permuter == y.charAt(z)) {
                            char first = y.charAt(1);
                            alphabetArray[originalAlphabet.toInt(x)] = first;
                            break;
                        }
                    }
                }
            }
            String arrayAsString = new String(alphabetArray);
            _alphabet = new Alphabet(arrayAsString);
        } catch (EnigmaException excp) {
            throw error("Bad permutation format.");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        return alphabet().toInt(alphabetArray[wrap(p)]);
    }

    /** Return the result of applying the inverse of this permutation
     *  to C modulo the alphabet size. */
    int invert(int c) {
        int cMod = wrap(c);
        for (int i = 0; i < alphabet().size(); i++) {
            if (permute(i) == cMod) {
                return i;
            }
        }
        return 0;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return alphabetArray[alphabet().toInt(p)];
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        /** Returns the inverse character translation of C. */
        return alphabet().getAlphabet().charAt(_alphabet.toInt(c));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return originalAlphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (_alphabet.toChar(i) == alphabet().toChar(i)) {
                return false;
            }
        }
        return true;
    }

    /** @return the cycle string, if needed. */
    String getCycles() {
        String holder = "";
        for (String cycle: _cycles) {
            holder += cycle;
        }
        return holder;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles permutation container. */
    private String[] _cycles;

    /** Creates an array out of the alphabet. */
    private char[] alphabetArray;

    /** Alphabet representing the original permutation. */
    private Alphabet originalAlphabet;
}

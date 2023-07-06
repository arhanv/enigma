package enigma;
/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Arhan Vohra
 */
class Alphabet {
    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        /** Constructs the Alphabet object, which contains the characters CHARS
         * in a String. Initiates boolean FALSE and loops over both CHARS and
         * CHARCONTAINER to check if the i-th letter in CHARS
         * is in CHARCONTAINER. If not, it is added to the String. */
        boolean seen = false;
        for (int i = 0; i < chars.length(); i++) {
            for (int a = 0; a < i; a++) {
                if (chars.charAt(i) == charContainer.charAt(a)) {
                    seen = true;
                    break;
                }
            }
            if (!seen) {
                charContainer += chars.charAt(i);
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        /* Uses String method .length() to determine the size of the alphabet */
        return this.charContainer.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        /** Loops over characters in ALPHABET and returns true
         *  if CH is detected. */
        boolean seen = false;
        for (int i = 0; i < this.size(); i++) {
            if (this.charContainer.charAt(i) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        /** Uses String method charAt(int) to find the character
         *  at the corresponding index. */
        return charContainer.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        /** Uses toChar(int) to iteratively search for CH
         *  in the current alphabet. */
        int found = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.toChar(i) == ch) {
                found = i;
            }
        }
        return found;
    }

    /** Accessor method for the entire Alphabet String.
     * @return CharContainer. */
    String getAlphabet() {
        return this.charContainer;
    }

    /** Initiates variable charContainer. */
    private String charContainer = "";

}

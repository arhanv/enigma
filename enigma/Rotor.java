package enigma;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Arhan Vohra
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        currentSetting = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return currentSetting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        currentSetting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        currentSetting = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        return _permutation.permute(p);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return _permutation.invert(e);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** Rotation characteristic of rotor. */
    private boolean canRotate;

    /** Reflection characteristic of rotor. */
    private boolean canReflect;

    /** Current setting of the rotor. */
    private int currentSetting = 0;

    /** Accessor for canRotate.
     * @param rotatable  checks if the rotor is rotatable. */
    void setCanRotate(boolean rotatable) {
        canRotate = rotatable;
    }

    /** Accessor for canReflect.
     * @param reflectable checks if the rotor is reflectable. */
    void setCanReflect(boolean reflectable) {
        canRotate = reflectable;
    }
}

package enigma;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Arhan Vohra
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */

    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        notchArray = notches.toCharArray();
        setCanRotate(true);
    }

    @Override
    void advance() {
        set((setting() + 1) % alphabet().size());
    }

    @Override
    boolean atNotch() {
        for (char x: notchArray) {
            if (setting() == alphabet().toInt(x)) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return true;
    }

    /** Converts @param p forward.
     * @return exit position. */
    int convertForward(int p) {
        contactEntered = (p + setting())
                % (alphabet().size());
        contactExited = permutation().permute(contactEntered);
        if (contactExited - setting() < 0) {
            positionExited = (contactExited - setting()
                    + alphabet().size()) % alphabet().size();
        } else {
            positionExited = (contactExited - setting())
                    % alphabet().size();
        }
        return positionExited;
    }

    /** Converts @param p backward.
     * @return exit position. */
    int convertBackward(int p) {
        contactEntered = (p + setting())
                % (alphabet().size());
        contactExited = permutation().invert(contactEntered);
        if (contactExited - setting() < 0) {
            positionExited = (contactExited - setting()
                    + alphabet().size()) % alphabet().size();
        } else {
            positionExited = (contactExited - setting()) % alphabet().size();
        }
        return positionExited;
    }

    /** Creates a new array for notches. */
    private char[] notchArray;

    /** Creates a new int for contactExited. */
    private int contactExited;

    /** Creates a new int for positionExited. */
    private int positionExited;

    /** Creates a new int for contactEntered. */
    private int contactEntered;

}

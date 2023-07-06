package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.error;

/** Class that represents a complete enigma machine.
 *  @author Arhan Vohra
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            ArrayList<Rotor> allRotors) {
        _alphabet = alpha;
        numRotorsContainer = numRotors;
        numPawlsContainer = pawls;
        allRotorsContainer = allRotors;
        plugboardContainer = new Permutation("", _alphabet);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return numRotorsContainer;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return numPawlsContainer;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (String x: rotors) {
            for (int i = 0; i < allRotorsContainer.size(); i++) {
                Rotor r = allRotorsContainer.get(i);
                String namely = r.name();
                if (namely.equals(x)) {
                    machineRotors.add(r);
                }
            }
        }

        int movableRotors = 0;
        for (Rotor n: machineRotors) {
            if (n.rotates()) {
                movableRotors += 1;
            }
        }
        if (movableRotors != numPawls()) {
            throw error("bad setting for this number of pawls.");
        }
    }

    /** Resets the active rotors. */
    void emptyRotors() {
        machineRotors = new ArrayList<>();
    }

    /** Accessor method for allRotors.
     * @return the private variable for allRotors. */
    ArrayList<Rotor> getAllRotors() {
        return allRotorsContainer;
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        Rotor r;
        try {
            for (int i = 1; i < numRotors(); i++) {
                r = machineRotors.get(i);
                char charToSet = setting.charAt(i - 1);
                r.set(charToSet);
            }
        } catch (IndexOutOfBoundsException excp) {
            throw error("out of bounds");
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        plugboardContainer = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0... alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceMachine();
        int cPostPlug = postPlugboard(c);
        int currentConverted = cPostPlug;
        for (int i = numRotors() - 1; i != 0; i--) {
            Rotor current = machineRotors.get(i);
            currentConverted = current.convertForward(
                    currentConverted);
        }
        for (int j = 0; j < numRotors(); j++) {
            Rotor current = machineRotors.get(j);
            currentConverted = current.convertBackward(
                    currentConverted);
        }
        int toReturn = postPlugboard(currentConverted);
        return toReturn;
    }

    /** Advances the entire sequence of rotors, once. */
    void advanceMachine() {
        /**
         * Create an array of booleans to decide
         * whether each rotor in machineRotors will move.
         * Set this to false by default for all except the fast rotor.
         * Advance the fast rotor.
         * If the numRotors()-2 rotor is at
         * fast rotor's notch, set rotate to true.
         * If the numRotors()-3 rotor is at numRotors()-2's notch,
         * set rotate to true.
         * Also, set numRotors()-2's rotation to true.
         */

        Rotor fastRotor = machineRotors.get(numRotors() - 1);
        boolean[] willRotate = new boolean[numRotors()];
        willRotate[numRotors() - 1] = true;

        for (int i = numRotors() - 2; i > 1; i--) {
            Rotor inFront = machineRotors.get(i + 1);
            Rotor thisRotor = machineRotors.get(i);
            if (inFront.atNotch()) {
                willRotate[i] = true;
            } else if (thisRotor.atNotch()) {
                willRotate[i] = true;
            }
        }

        for (int i = 0; i < numRotors(); i++) {
            if (willRotate[i]) {
                machineRotors.get(i).advance();
            }
        }
    }

    /** Passes @param c through the plugboard.
     * @return value after conversion. */
    int postPlugboard(int c) {
        return plugboardContainer.permute(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int convertedInt;
        int charAsInt;
        char intAsChar;
        char[] msgArray = msg.toCharArray();
        for (int i = 0; i < msgArray.length; i++) {
            if (msgArray[i] == ' ') {
                continue;
            }
            charAsInt = _alphabet.toInt(msgArray[i]);
            convertedInt = convert(charAsInt);
            intAsChar = _alphabet.toChar(convertedInt);
            msgArray[i] = intAsChar;

        }

        String stringEmUp = "";
        for (char x: msgArray) {
            stringEmUp += x;
        }
        return stringEmUp;
    }

    /** CURRENTLY UNUSED METHODS. */

    /** Advances the fast rotor.
     * @param x is the current rotor. */
    void advanceFastRotor(Rotor x) {
        x.advance();
        if (x.atNotch()) {
            advanceSequence(numRotors() - 2);
        }
    }

    /** Advances the sequence.
     * @param i is the current int. */
    void advanceSequence(int i) {
        Rotor thisRotor = machineRotors.get(i);
        Rotor nextRotor = null;
        if ((i + 1) < numRotors()) {
            nextRotor = machineRotors.get(i + 1);
        }
        thisRotor.advance();
        if (thisRotor.atNotch()) {
            advanceSequence(i - 1);
        }
    }

    /** @return private machineRotors. */
    ArrayList<Rotor> getMachineRotors() {
        return machineRotors;
    }

    /** Returns rotor names as an ArrayList of strings. */
    ArrayList<String> getAllRotorsStr() {
        ArrayList<String> stringer = new ArrayList<>();
        for (Rotor x: getAllRotors()) {
            stringer.add(x.name());
        }
        return stringer;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in this machine. */
    private final int numRotorsContainer;

    /** Number of rotors in this machine. */
    private final int numPawlsContainer;

    /** Collection of all available rotors. */
    private ArrayList<Rotor> allRotorsContainer = new ArrayList<>();

    /** Rotors actually used in this machine. */
    private ArrayList<Rotor> machineRotors = new ArrayList<>();

    /** Declares plugboard as a permutation. */
    private Permutation plugboardContainer;

}

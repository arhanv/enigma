package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Iterator;

import static enigma.EnigmaException.error;

/** Enigma simulator.
 *  @author Arhan Vohra
 */

public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine processingMachine = readConfig();
        String settings = "";
        if (!_input.hasNext("\\*")) {
            throw error("bad settings");
        }

        while (_input.hasNext("\\*")) {
            processingMachine.emptyRotors();
            settings = _input.nextLine();
            while ((settings.matches("\\s")) || settings.equals("")) {
                settings = _input.nextLine();
            }

            setUp(processingMachine, settings);
            while (_input.hasNext("[^\\*]([\\S\\s]*)")) {
                String toConvert = _input.nextLine();
                try {
                    printMessageLine(processingMachine.convert(toConvert));
                } catch (EnigmaException b) {
                    throw error("ind err");
                }
            }

            if (_input.hasNext("\\*")) {
                if (_input.hasNext("\n")) {
                    _output.println();
                }
            }

        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        int numRotors;
        int numPawls;
        try {
            _alphabet = new Alphabet(_config.next());
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }

        try {
            numRotors = _config.nextInt();
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }

        try {
            numPawls = _config.nextInt();
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }

        try {
            Rotor current;
            while (_config.hasNext("[ ]*[^\\(\\)]+")) {
                current = readRotor();
                allRotors.add(current);
            }
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }

        try {
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NullPointerException excp) {
            throw error("can't make new machine");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        String name;
        String properties;
        Permutation perm;

        try {
            name = _config.next("[ ]*[^\\(\\)]+");
            properties = _config.next("[MNR][^*\\(\\) ]*");
            String cycles = "";
            while (_config.hasNext("([\\(][^ ]+[\\)])")) {
                cycles += " " + _config.next();
            }
            perm = new Permutation(cycles, _alphabet);
            char typeR = properties.charAt(0);
            if (typeR == 'R') {
                return new Reflector(name, perm);
            } else if (typeR == 'M') {
                String notches = properties.substring(1);
                return new MovingRotor(name, perm, notches);
            } else {
                return new FixedRotor(name, perm);
            }
        } catch (EnigmaException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        try {
            String sub = settings.substring(2);
            Scanner x = new Scanner(sub);
            ArrayList<String> rotorsToAdd = new ArrayList<>();
            String nextToken;
            while (true) {
                nextToken = x.next("[^\\(\\)]+");
                if (M.getAllRotorsStr().contains(nextToken)) {
                    rotorsToAdd.add(nextToken);
                } else {
                    String[] adder = new String[rotorsToAdd.size()];
                    for (int i = 0; i < adder.length; i++) {
                        boolean isRotor = false;
                        for (Rotor b: allRotors) {
                            if (b.name().equals(rotorsToAdd.get(i))) {
                                isRotor = true;
                            }
                        }
                        if (isRotor) {
                            adder[i] = rotorsToAdd.get(i);
                        }
                    }
                    M.insertRotors(adder);
                    M.setRotors(nextToken);
                    if (x.hasNext()) {
                        try {
                            String stringer = "";
                            while (x.hasNext(
                                    "([\\(][^\\*\\(\\)]+"
                                            + "[\\)])")
                            ) {
                                stringer += x.next(
                                        "([\\(][^\\*\\(\\)]"
                                                + "+[\\)])"
                                ) + " ";
                            }
                            Permutation f = new Permutation(stringer.trim(),
                                    _alphabet);
                            M.setPlugboard(f);
                        } catch (InputMismatchException excp) {
                            throw error("input mismatch");
                        }
                    }
                    break;
                }
            }

        } catch (StringIndexOutOfBoundsException exception) {
            throw error("out of bounds");
        }

    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        ArrayList<Character> lister = new ArrayList<>();
        for (int a = 0; a < msg.length(); a++) {
            if (msg.charAt(a) != ' ') {
                lister.add(msg.charAt(a));
            }
        }
        Iterator<Character> h = lister.iterator();
        while (h.hasNext()) {
            for (int i = 0; i < 5; i++) {
                if (h.hasNext()) {
                    _output.print(h.next());
                }
            }
            if (!h.hasNext()) {
                break;
            }
            _output.print(" ");
        }
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** All rotors for this configuration. */
    private ArrayList<Rotor> allRotors = new ArrayList<Rotor>();
}

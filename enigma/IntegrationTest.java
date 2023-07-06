package enigma;

import org.junit.Test;

import java.util.ArrayList;
import static org.junit.Assert.*;


public class IntegrationTest {

    public static void main() {
    }

    @Test
    public void x1Test() {
        addRotorsX();
        Machine x1 = new Machine(upper, numRotors1, numPawls1, listing);
        x1.insertRotors(new String[]{"B", "Beta", "I", "II", "III"});
        x1.setRotors(setting1);
        x1.setPlugboard(plugBoard);
        String msg1 = "HELLO WORLD";
        String msg2 = "IHBDQ QMTQZ";
        String con1 = x1.convert(msg1);
        assertEquals(con1, msg2);
        x1.setRotors(setting1);
        assertEquals(x1.convert(msg2), msg1);
    }

    @Test
    public void x2Test() {
        addRotorsX();
        Machine x2 = new Machine(upper, numRotors1, numPawls1, listing);
        x2.insertRotors(new String[]{"B", "Beta", "III", "IV", "I"});
        x2.setRotors(setting2);
        String msg3 = "FROM HIS SHOULDER HIAWATHA TOOK THE CAMERA OF ROSEWOOD";
        System.out.println(x2.convert(msg3));
    }

    @Test
    public void x3Test() {
        addRotorsX();
        Machine x3 = new Machine(upper, numRotors1, numPawls1, listing);
        x3.insertRotors(new String[]{"B", "Beta", "III", "IV", "I"});
        x3.setPlugboard(new Permutation("(YF) (ZH)", upper));
        x3.setRotors(setting2);
        System.out.println(upper.toChar(x3.convert(upper.toInt('Y'))));
    }

    @Test
    public void allCtest() {
        addRotorsX();
        Machine allC = new Machine(onlyABC, 4, 3, listing);
        allC.insertRotors(new String[]{"reflectorC", "C1", "C2", "C3"});
        allC.setRotors("AAA");
        allC.convert("AAAAAAAAA");
    }

    @Test
    public void carroll01Test() {
        Main.main("testing/correct/default.conf",
                "testing/correct/01-carroll.in");
    }
    Alphabet upper = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    Alphabet onlyABC = new Alphabet("ABC");
    int numRotors1 = 5;
    int numPawls1 = 3;
    Rotor B = new Reflector("B", new Permutation("(AE) (BN)"
            + " (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) "
            + "(RX) (SZ) (TV)", upper));
    Rotor rotorBeta = new FixedRotor("Beta", new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)",
            upper));
    Rotor rI = new MovingRotor("I", new Permutation(
            "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)",
            upper), "MQ");
    Rotor rII = new MovingRotor("II", new Permutation(
            "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)",
            upper), "ME");
    Rotor rIII = new MovingRotor("III", new Permutation(
            "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)",
            upper), "MV");
    Rotor rIV = new MovingRotor("IV", new Permutation(
            "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", upper),
            "MJ");
    Rotor c1 = new MovingRotor("C1", new Permutation(
            "(BCDEFGHIJKLMNOPQRSTUVWXYZA)", onlyABC),
            "C");
    Rotor c2 = new MovingRotor("C2", new Permutation(
            "(BCDEFGHIJKLMNOPQRSTUVWXYZA)", onlyABC), "C");
    Rotor c3 = new MovingRotor("C3", new Permutation(
            "(BCDEFGHIJKLMNOPQRSTUVWXYZA)", onlyABC), "C");
    Rotor c4 = new MovingRotor("C4", new Permutation(
            "(BCDEFGHIJKLMNOPQRSTUVWXYZA)", onlyABC), "C");
    Rotor reflectorC = new Reflector("reflectorC",
            new Permutation("(ACB)", onlyABC));
    String setting1 = "AAAA";
    String setting2 = "AXLE";
    Permutation plugBoard = new Permutation("(AQ) (EP)", upper);
    private ArrayList<Rotor> listing = new ArrayList<>();

    public void addRotorsX() {
        listing.add(B);
        listing.add(rotorBeta);
        listing.add(rI);
        listing.add(rII);
        listing.add(rIII);
        listing.add(rIV);
        listing.add(c1);
        listing.add(c2);
        listing.add(c3);
        listing.add(c4);
        listing.add(reflectorC);
    }
}

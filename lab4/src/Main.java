import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    private static final MyScanner myScanner;

    static {
        try {
            //MyScanner myScanner = new MyScanner("../lab1a/p1.txt");
            //MyScanner myScanner = new MyScanner("../lab1a/p2.txt");
            //MyScanner myScanner = new MyScanner("../lab1a/p3.txt");
            myScanner = new MyScanner("../lab1a/p3err.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final static FiniteAutomata finiteAutomata;

    static {
        try {
            finiteAutomata = new FiniteAutomata("src/FiniteAutomata/FA.in");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void menu(){
        System.out.println("MAIN MENU:");
        System.out.println("0. Exit");
        System.out.println("1. Lexically scan a program file");
        System.out.println("2. Finite Automata menu");
        System.out.println("3. Check DFA sequence");
    }

    public static void finiteAutomataMenu(){
        System.out.println("FA MENU:");
        System.out.println("0. Exit");
        System.out.println("1. Print states");
        System.out.println("2. Print alphabet");
        System.out.println("3. Print transitions");
        System.out.println("4. Print initial state");
        System.out.println("5. Print final states");
    }

    public static void finiteAutomataOptionHandler(){
        System.out.println("User option: ");
        int userOption = scanner.nextInt();
        if (userOption == 1){
            finiteAutomata.statesToString();
        }
        else if (userOption == 2){
            finiteAutomata.alphabetToString();
        }
        else if (userOption == 3){
            finiteAutomata.transitionsToString();
        }
        else if (userOption == 4){
            finiteAutomata.initialStateToString();
        }
        else if (userOption == 5){
            finiteAutomata.finalStatesToString();
        }
        else if (userOption != 0)
            System.out.println("Invalid option!");
        System.out.println("Returning to main menu");
    }

    public static void main(String[] args) throws IOException {
        while (true){
            System.out.println("\n");
            menu();
            System.out.println("User option: ");
            int userOption = scanner.nextInt();
            if (userOption == 0) {
                System.out.println("Goodbye!");
                break;
            }
            else if (userOption == 1){
                myScanner.scan();
            }
            else if (userOption == 2){
                finiteAutomataMenu();
                finiteAutomataOptionHandler();
            }
            else if (userOption == 3){
                System.out.print("Input sequence: ");
                String sequence = scanner.next();
                System.out.println(finiteAutomata.checkSequence(sequence));
            }
            else
                System.out.println("Invalid option!");
        }
    }
}
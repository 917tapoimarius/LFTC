public class Main {
    public static void main(String[] args) {

        SymbolTable<String, Integer> symbolTable = new SymbolTable<>();
        System.out.println(symbolTable.isEmpty());
        symbolTable.add("a", 0);
        symbolTable.add("b", 1);
        symbolTable.add("c", 2);
        symbolTable.add("10", 3);
        symbolTable.add("MSg:", 4);
        System.out.println(symbolTable.size());
        System.out.println(symbolTable.get("c"));
        System.out.println(symbolTable.remove("c"));
        System.out.println(symbolTable.remove("c"));
        System.out.println(symbolTable.get("c"));
        System.out.println(symbolTable.get("10"));
        System.out.println(symbolTable.add("b", 5));
        System.out.println(symbolTable.get("b"));
        System.out.println(symbolTable.size());
    }
}
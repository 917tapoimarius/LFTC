import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyScanner {

    private final ArrayList<String> operators = new ArrayList<>(List.of("+", "-", "*", "%", "/", "=", "==",
            "<", ">", "<=", ">=", "!="));
    private final ArrayList<String> separators = new ArrayList<>(List.of("{", "}", "[", "]", "(", ")", " ",
            ";", ",", "\n", "\t", "'", "\""));
    private final ArrayList<String> reservedWords = new ArrayList<>(List.of("list", "char", "string", "int",
            "while", "read", "write", "if", "else", "and", "or"));

    private final ArrayList<String> operatorsForPattern = new ArrayList<>(List.of("\\+", "-", "\\*", "%", "/" ,
            "=", "==", "<", ">", "<=", ">=", "!="));
    private final ArrayList<String> separatorsForPattern = new ArrayList<>(List.of("\\(", "\\)",  "\\[", "\\]",
            "\\{", "\\}", ";", " ", ",", "\n", "'", "\"", "\t"));
    StringBuilder patternBuilder = new StringBuilder()
            .append("(")
            .append(String.join("|", separatorsForPattern))
            .append(")|(")
            .append(String.join("|", operatorsForPattern))
            .append(")");
    Pattern pattern = Pattern.compile(patternBuilder.toString());
    private final SymbolTable<String> symbolTable;
    private final ProgramInternalForm programInternalForm;
    private final List<String> programLines;

    public MyScanner(String filePath) throws FileNotFoundException {
        this.symbolTable = new SymbolTable<>();
        this.programInternalForm = new ProgramInternalForm();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        programLines = bufferedReader.lines().toList();
    }

    private boolean isIdentifier(String token) {
        return Pattern.compile("^[_a-zA-Z][_a-zA-Z0-9]*$").matcher(token).matches();
    }

    private boolean isIntegerConstant(String token) {
        return Pattern.compile("^0|([+-]?[1-9][0-9]*)$").matcher(token).matches();
    }

    public void readString(Iterator<String> tokenizer) {
        var stringBuilder = new StringBuilder();
        while (tokenizer.hasNext()) {
            String currentToken = tokenizer.next();
            if (currentToken.equals(" "))
                continue;
            if (currentToken.equals("\"")) {
                String stringConstant = "\"%s\"".formatted(stringBuilder.toString());
                programInternalForm.addIdentifierConstant(stringBuilder.toString(), symbolTable.add(stringConstant));
            }
            stringBuilder.append(currentToken);
        }
    }

    public void scan() throws IOException {
        StringBuilder lexicalError = new StringBuilder();
        for (int line = 0; line < programLines.size(); line++){
            String programLine = programLines.get(line);
            Matcher matcher = pattern.matcher(programLine);
            ArrayList<String> tokens = new ArrayList<>();
            int position = 0;
            while (matcher.find()) {
                if (position != matcher.start()) {
                    tokens.add(programLine.substring(position, matcher.start()));
                }
                tokens.add(matcher.group());
                position = matcher.end();
            }
            Iterator<String> tokenizer = tokens.stream().iterator();
            while (tokenizer.hasNext()){
                String token = tokenizer.next();
                if(token.equals(" ") || token.equals("\t"))
                    continue;
                if (reservedWords.contains(token) || operators.contains(token) || separators.contains(token)) {
                    programInternalForm.addOperatorSeparatorReservedWord(token);
                }
                else if (isIdentifier(token) || isIntegerConstant(token)) {
                    programInternalForm.addIdentifierConstant(token, symbolTable.add(token));
                }
                else if(token.equals("'")){
                    programInternalForm.addOperatorSeparatorReservedWord("'");
                    String character = "'%s'".formatted(tokenizer.next());
                    programInternalForm.addIdentifierConstant(character, symbolTable.add(character));
                }
                else if (token.equals("\"")) {
                    programInternalForm.addOperatorSeparatorReservedWord(token);
                    readString(tokenizer);
                }
                else
                    lexicalError.append(("Lexical error found on line %s! Could not categorize %s\n").formatted(line, token));
            }
        }
        if (lexicalError.isEmpty())
            System.out.println("Lexical check completed successfully!");
        else
            System.out.println(lexicalError);
        writeToFiles();
    }

    public void writeToFiles() throws IOException {
        String pifFilePath = "PIF.out";
        String stFilePath = "ST.out";

        FileWriter pifFileWriter = new FileWriter(pifFilePath);
        pifFileWriter.write(programInternalForm.toString());
        pifFileWriter.close();

        FileWriter stFileWriter = new FileWriter(stFilePath);
        stFileWriter.write(symbolTable.toString());
        stFileWriter.close();
    }
}

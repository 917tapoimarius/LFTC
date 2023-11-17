import java.util.HashMap;
import java.util.Map;

public class ProgramInternalForm {
    private final Map<String, Integer> pif = new HashMap<>();

    public void addOperatorSeparatorReservedWord(String token){
        pif.put(token, -1);
    }

    public void addIdentifierConstant(String token, Integer symbolTablePosition){
        pif.put(token, symbolTablePosition);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("_____________________________\n");
        stringBuilder.append("|   Token     |  Position   |\n");
        stringBuilder.append("|-------------|-------------|\n");
        for (Map.Entry<String, Integer> pair : pif.entrySet()) {
            stringBuilder.append("|%-12s | %-12d|\n".formatted(pair.getKey(), pair.getValue()));
        }
        stringBuilder.append("|___________________________|\n");
        return stringBuilder.toString();
    }
}

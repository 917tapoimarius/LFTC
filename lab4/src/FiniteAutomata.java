import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.javatuples.Pair;

public class FiniteAutomata {
    private final List<String> states;

    private final List<String> alphabet;

    HashMap<Pair<String, String>, String> transitions;

    private final String initialState;

    private final List<String> finalStates;

    private final boolean isDeterministic;

    public FiniteAutomata(String filePath) throws IOException {
        var bufferedReader = new BufferedReader(new FileReader(filePath));

        String statesLine = bufferedReader.readLine().strip();
        states = List.of(statesLine.split(","));

        String alphabetLine = bufferedReader.readLine().strip();
        alphabet = List.of(alphabetLine.split(","));

        initialState = bufferedReader.readLine().strip();

        String finalStatesLine = bufferedReader.readLine().strip();
        finalStates = List.of(finalStatesLine.split(","));

        transitions = new HashMap<>();
        String transition;
        while ((transition = bufferedReader.readLine()) != null) {
            transition = transition.strip();
            String[] transitionElements = transition.split(",");
            String currentState = transitionElements[0];
            String symbol = transitionElements[1];
            String nextState = transitionElements[2];
            transitions.put(new Pair<>(currentState, symbol), nextState);
        }

        bufferedReader.close();

        isDeterministic = checkDFA();
    }

    public String checkSequence(String sequence){
        if (!isDeterministic) {
            return "Language is not DFA!";
        }

        String currentState = this.initialState;

        for (char i : sequence.toCharArray()) {
            Pair<String, String> transitionKey = new Pair<>(currentState, String.valueOf(i));
            String nextState = transitions.get(transitionKey);

            if (nextState == null) {
                return "Invalid transition for input: " + i;
            }

            currentState = nextState;
        }
        if (finalStates.contains(currentState)) {
            return "Sequence accepted by DFA!";
        }

        return "Sequence rejected by DFA!";
    }

    public boolean isValidSequence(String sequence){
        String checkedSequence = checkSequence(sequence);
        return checkedSequence.equals("Sequence accepted by DFA!");
    }


    public String statesToString() {
        return "Q = " +
                states.toString().replace("[", "{").replace("]", "}");
    }

    public String alphabetToString() {
        return "Σ = " +
                alphabet.toString().replace("[", "{").replace("]", "}");
    }

    public String transitionsToString() {
        StringBuilder stringBuilder = new StringBuilder();
        transitions.forEach((stateAlphabetPair, nextState) -> {
            String currentState = stateAlphabetPair.getValue0();
            String symbol = stateAlphabetPair.getValue1();
            stringBuilder.append("δ(%s,%s) = %s\n".formatted(currentState, symbol, nextState));
        });
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        return stringBuilder.toString();
    }

    public String initialStateToString() {
        return "q0 = {%s}".formatted(initialState);
    }

    public String finalStatesToString() {
        return "F = " +
                finalStates.toString().replace("[", "{").replace("]", "}");
    }

    private boolean checkDFA() {
        List<Map.Entry<Pair<String, String>, String>> transitionList = new ArrayList<>(transitions.entrySet());

        for (int i = 0; i < transitionList.size() - 1; i++) {
            for (int j = i + 1; j < transitionList.size(); j++) {
                Map.Entry<Pair<String, String>, String> transition1 = transitionList.get(i);
                Map.Entry<Pair<String, String>, String> transition2 = transitionList.get(j);
                // get next states of each transition
                String nextState1 = transition1.getValue();
                String nextState2 = transition2.getValue();

                // if the 2 transitions have the same current state and symbol, but different next states it is not a
                // DFA
                if (transition1.getKey().equals(transition2.getKey()) &&
                        !nextState1.equals(nextState2)){
                    return false;
                }
            }
        }

        return true;
    }
}

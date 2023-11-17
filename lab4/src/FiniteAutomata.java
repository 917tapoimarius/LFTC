import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.javatuples.Pair;

public class FiniteAutomata {
    private List<String> states;

    private List<String> alphabet;

    Map<Pair<String, String>, Object> transitions;

    private String initialState;

    private List<String> finalStates;

    private boolean isDeterministic;

    public FiniteAutomata(String filePath) throws IOException {
        var bufferedReader = new BufferedReader(new FileReader(filePath));

        String statesLine = bufferedReader.readLine().replace("\n", "");
        states = List.of(statesLine.split(","));

        String alphabetLine = bufferedReader.readLine().replace("\n", "");
        alphabet = List.of(alphabetLine.split(","));

        initialState = bufferedReader.readLine().replace("\n", "");

        String finalStatesLine = bufferedReader.readLine().replace("\n", "");
        finalStates = List.of(finalStatesLine.split(","));

        transitions = new HashMap<>();
        String transition;
        while ((transition = bufferedReader.readLine().replace("\n", "")) != null) {
            String[] transitionElements = transition.split(",");
            String currentState = transitionElements[0];
            String alphabetElement = transitionElements[1];
            String nextState = transitionElements[2];

            transitions.compute(new Pair<>(currentState, alphabetElement), (key, previousNextStates) -> {
                        if (previousNextStates == null)
                            return nextState;
                        else if (previousNextStates instanceof String) {
                            isDeterministic = false;
                            return new ArrayList<>(Arrays.asList(previousNextStates, nextState));
                        }
                        List<String> states = (List<String>) previousNextStates;
                        states.add(nextState);
                        return states;
                    }
            );
        }

        bufferedReader.close();
    }

    public String checkSequence(String sequence){
        return "";
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
            String alphabetElement = stateAlphabetPair.getValue1();
            stringBuilder.append("δ(%s,%s) = %s\n".formatted(currentState, alphabetElement, nextState));
        });
        return stringBuilder.toString();
    }

    public String initialStateToString() {
        return "q0 = %s".formatted(initialState);
    }

    public String finalStatesToString() {
        return "F = " +
                finalStates.toString().replace("[", "{").replace("]", "}");
    }

    @Override
    public String toString() {
        return "FiniteAutomata{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", transitions=" + transitions +
                ", initialState='" + initialState + '\'' +
                ", finalStates=" + finalStates +
                ", isDeterministic=" + isDeterministic +
                '}';
    }
}

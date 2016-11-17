package com.apea.compiler.tools.fsm;

import java.util.ArrayList;

/**
 * Представляє детермінований кінцевий автомат.
 * @param <S> тип сигналу переходу.
 * @param <M> тип параметру, що зв'язаний зі кожним станом.
 */
public class DFSM<S, M> {

    /** Стани автомата. */
    private ArrayList<State<S, M>> statesList;

    /** Початковий стан автомата. */
    private State<S,M> firstState;

    /** Поточний стан автомата. */
    private State<S, M> currentState;

    public DFSM() {
        statesList = new ArrayList<>();
    }

    /** Перший стан з масиву робить початковим. */
    public DFSM(State<S, M>[] states) {
        this();
        firstState = states[0];
        for (State state : states) {
            addState(state);
        }
        resetToFirstState();
    }

    /**
     * Пошук відбувається
     * за допомогою метода {@code equals} для
     * типу M.
     * @return перше входження стану, який має ознаку
     * {@code mark}. Якщо такого стану немає,
     * повертає {@code null}.
     */
    public State<S, M> searchState(M mark) {
        State result = null;
        int i = 0;
        while (i < statesList.size()
                && !statesList.get(i).getMark().equals(mark)) {
            i++;
        }
        if (i < statesList.size()) {
            result = statesList.get(i);
        }
        return result;
    }

    /**
     * Повертає стан у автоматі, що слідує за сигналом,
     * та переходить до нього.
     * Якщо наступного стану по цьому переходу немає,
     * то виникає помилка {@link TransitionException}.
     */
    public State<S,M> moveWith(S signal) {
        currentState = currentState.transitionWith(signal);
        if (currentState == null) {
            throw new TransitionException("There is no transition with" + signal);
        }
        return currentState;
    }

    /** @ return чи є перехід по сигналу {@code signal}. */
    public boolean canMoveWith(S signal) {
        return currentState.transitionWith(signal) != null;
    }

    public State<S,M> getCurrentState() {
        return currentState;
    }

    /** Робить поточний стан початковим. */
    public void resetToFirstState() {
        currentState = firstState;
    }

    /** @return {@code true} якщо цього стану не було в автоматі. */
    public boolean addState(State<S, M> state) {
        return statesList.add(state);
    }

    /**
     * Встановлює початковий стан {@code firstState} та додає його
     * до списку станів, якщо його в ньому не було
     */
    public void setFirstState(State<S, M> firstState) {
        this.firstState = firstState;
        statesList.add(firstState);
    }

    public ArrayList<State<S, M>> getStatesList() {
        return statesList;
    }
}

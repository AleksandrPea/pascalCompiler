package com.apea.compiler.tools.fsm;

import java.util.ArrayList;

/**
 * ����������� ������������� ������� �������.
 * @param <S> ��� ������� ��������.
 * @param <M> ��� ���������, �� ��'������ � ������ ������.
 */
public class DFSM<S, M> {

    /** ����� ��������. */
    private ArrayList<State<S, M>> statesList;

    /** ���������� ���� ��������. */
    private State<S,M> firstState;

    /** �������� ���� ��������. */
    private State<S, M> currentState;

    public DFSM() {
        statesList = new ArrayList<>();
    }

    /** ������ ���� � ������ ������ ����������. */
    public DFSM(State<S, M>[] states) {
        this();
        firstState = states[0];
        for (State state : states) {
            addState(state);
        }
        resetToFirstState();
    }

    /**
     * ����� ����������
     * �� ��������� ������ {@code equals} ���
     * ���� M.
     * @return ����� ��������� �����, ���� �� ������
     * {@code mark}. ���� ������ ����� ����,
     * ������� {@code null}.
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
     * ������� ���� � �������, �� ���� �� ��������,
     * �� ���������� �� �����.
     * ���� ���������� ����� �� ����� �������� ����,
     * �� ������ ������� {@link TransitionException}.
     */
    public State<S,M> moveWith(S signal) {
        currentState = currentState.transitionWith(signal);
        if (currentState == null) {
            throw new TransitionException("There is no transition with" + signal);
        }
        return currentState;
    }

    /** @ return �� � ������� �� ������� {@code signal}. */
    public boolean canMoveWith(S signal) {
        return currentState.transitionWith(signal) != null;
    }

    public State<S,M> getCurrentState() {
        return currentState;
    }

    /** ������ �������� ���� ����������. */
    public void resetToFirstState() {
        currentState = firstState;
    }

    /** @return {@code true} ���� ����� ����� �� ���� � �������. */
    public boolean addState(State<S, M> state) {
        return statesList.add(state);
    }

    /**
     * ���������� ���������� ���� {@code firstState} �� ���� ����
     * �� ������ �����, ���� ���� � ����� �� ����
     */
    public void setFirstState(State<S, M> firstState) {
        this.firstState = firstState;
        statesList.add(firstState);
    }

    public ArrayList<State<S, M>> getStatesList() {
        return statesList;
    }
}

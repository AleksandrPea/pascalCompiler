package com.apea.compiler.tools.fsm;

import java.util.HashMap;
import java.util.Iterator;

/**
 * ����������� ���� ���.
 * @see DFSM
 */
public class State<S, M> {

    /** ��������� ������������� �����(�����) */
    private int id;

    /** ������ �����. */
    private M mark;

    /** �������� �� ����� �����. */
    private HashMap<S, State<S, M>> transitions;

    public State(int id, M mark) {
        setId(id);
        setMark(mark);
        transitions = new HashMap<S, State<S,M>>();
    }

    /**
     * ������ ������� �� �������
     * {@code signal}(����� ��������) �� {@code nxtState}.
     * ���� ������� �� ��������� ������� ��� ��������,
     * �� ����� ���� �� �����.
     * @return ��������� ����, ������������ � {@code signal},
     * ��� {@code null}, ���� ������ ����� �� ����.
     */
    public State<S,M> makeTransition(S signal, State nxtState) {
        return transitions.put(signal, nxtState);
    }

    /**
     * ������� ������� �� ���� �������� {@code signal}.
     * @return ��������� �������, ��� {@code null} ���� ����
     * ��� ��� �������.
     */
    public State<S, M> removeTransition(S signal) {
        return transitions.remove(signal);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public M getMark() {
        return mark;
    }

    public void setMark(M mark) {
        this.mark = mark;
    }

    /**
     * ������� ���� �� ����� ����� ������� �� ������ {@code signal}.
     * ���� ������ ����� ����, ������� {@code null}.
     */
    public State<S, M> transitionWith(S signal) {
        return transitions.get(signal);
    }

    public boolean haveNoTransitions() {
        return transitions.isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(
                "S"+getId()+"["+getMark().toString()+ "]:");
        Iterator<S> iter =
                transitions.keySet().iterator();
        State state;
        S signal;
        while (iter.hasNext()) {
            signal = iter.next();
            state = transitions.get(signal);
            result.append("\n("+signal.toString()+")" +
                    "\t\t->\tS"+state.getId()+"["+state.getMark()
                    .toString()+ "]");
        }
        result.append(".\n");
        return result.toString();
    }
}

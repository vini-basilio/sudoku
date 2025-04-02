package br.com.dio.util.model;

import java.util.Collection;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import java.util.List;

public class Board {
    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        if (spaces.stream()
                .flatMap(Collection::stream)
                .noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))) {
            return GameStatusEnum.NON_STARTED;
        }
        return spaces.stream()
                .flatMap(Collection::stream)
                .anyMatch(s -> isNull(s.getActual())) ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETED;
    }

    public boolean hasErros() {
        if (getStatus() == GameStatusEnum.NON_STARTED)
            return false;

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    public boolean changeValue(final int col, final int row, final int value) {
        var space = spaces.get(col).get(row);
        if (space.isFixed())
            return false;

        space.setActual(value);
        return true;
    }

    public boolean clearSpace(final int col, final int row) {
        var space = spaces.get(col).get(row);
        if (space.isFixed())
            return false;

        space.clearSpace();
        return true;
    }

    public void reset() {
        spaces.forEach(s -> s.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished() {
        return !hasErros() && (getStatus() == GameStatusEnum.COMPLETED);
    }
}

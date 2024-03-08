package cn.noy.expr.util;

import java.util.Objects;

/**
 * 二元组
 */
public record Pair<A,B>(A first, B second) {
    public static <A,B> Pair<A,B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

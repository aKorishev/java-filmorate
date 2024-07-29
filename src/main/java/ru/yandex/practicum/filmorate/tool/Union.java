package ru.yandex.practicum.filmorate.tool;


import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;


@Getter
public class Union<T1, T2> {
    private final Optional<T1> value1;
    private final Optional<T2> value2;

    private Union(Optional<T1> value1, Optional<T2> value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public static <T1, T2> Union<T1, T2> setValue1(@NonNull T1 value) {
        var value1 = Optional.of(value);
        Optional<T2> value2 = Optional.empty();

        return new Union<>(value1, value2);
    }

    public static <T1, T2> Union<T1, T2> setValue2(@NonNull T2 value) {
        Optional<T1> value1 = Optional.empty();
        var value2 = Optional.of(value);

        return new Union<>(value1, value2);
    }

    public boolean value1IsPresent() {
        return value1.isPresent() && value2.isEmpty();
    }

    public boolean value2IsPresent() {
        return value2.isPresent() && value1.isEmpty();
    }

    public <K1, K2> Union<K1, K2> map(Function<T1, K1> fun1, Function<T2, K2> fun2) {
        return new Union<>(
                value1.map(fun1),
                value2.map(fun2));
    }
}

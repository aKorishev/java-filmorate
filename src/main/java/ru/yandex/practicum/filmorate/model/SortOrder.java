package ru.yandex.practicum.filmorate.model;

public enum SortOrder {
    ASCENDING,
    DESCENDING,
    UNKNOWN;

    public static SortOrder from(String order) {
        order = order.toLowerCase();

        if (order.startsWith("asc"))
            return ASCENDING;
        else if (order.startsWith("desc"))
            return DESCENDING;
        else
            return UNKNOWN;
    }
}

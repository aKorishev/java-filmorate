package ru.yandex.practicum.filmorate.storage;

import lombok.*;
import ru.yandex.practicum.filmorate.model.SortOrder;

import java.util.Optional;

//@Builder(access = AccessLevel.PRIVATE)
public class SortParameters {
    @Getter
    private SortOrder sortOrder = SortOrder.UNKNOWN;

    @NonNull
    @Getter
    private Optional<Integer> size = Optional.empty();

    @NonNull
    @Getter
    private Optional<Integer> from = Optional.empty();

    @Override
    public String toString() {
        return String.format("sortOrder - %s, size - %s, from - %s", sortOrder, size.orElse(0), from.orElse(0));
    }

    public static Builder builder() {
        return new SortParameters().new Builder();
    }

    public class Builder {
        //SortParametersBuilder builder = SortParameters.builder();

        public Builder sortOrder(@NonNull SortOrder sortOrder) {
            SortParameters.this.sortOrder = sortOrder;

            return this;
        }

        public Builder setAscending() {
            SortParameters.this.sortOrder = SortOrder.ASCENDING;

            return this;
        }

        public Builder setDescending() {
            SortParameters.this.sortOrder = SortOrder.DESCENDING;

            return this;
        }

        public Builder size(int size) {
            SortParameters.this.size = Optional.of(size);

            return this;
        }

        public Builder size(Optional<Integer> size) {
            SortParameters.this.from = size;

            return this;
        }

        public Builder from(int from) {
            SortParameters.this.from = Optional.of(from);

            return this;
        }

        public Builder from(Optional<Integer> from) {
            SortParameters.this.from = from;

            return this;
        }

        public SortParameters build() {
            return SortParameters.this;
        }
    }
}

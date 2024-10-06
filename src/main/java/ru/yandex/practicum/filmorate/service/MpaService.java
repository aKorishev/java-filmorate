package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;


@Service
@Slf4j
public class MpaService {
    private final FilmStorage filmStorage;

    public MpaService(@Qualifier("InDbFilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Mpa> getRatings() {
        log.trace("Request for list of all mpas");

        return filmStorage.getMpas();
    }

    public Mpa getRating(long mpaId) {
        log.trace(String.format("Request for Mpa by mpaId - %d", mpaId));

        if (!filmStorage.containsMpaKey(mpaId)) {
            throw new NotFoundException(String.format("Этот mpaId: %d не был найден", mpaId));
        }

        return filmStorage.findMpaById(mpaId);
    }

    public Mpa putRating(Mpa mpa) {
        log.trace(String.format("Request to put Mpa \"%d\"", mpa));

        var mpaId = mpa.getId();

        if (mpaId != null && filmStorage.containsMpaKey(mpaId)) {
            mpa = filmStorage.updateMpa(mpa);

            log.trace("putFilm: Обновил mpa: " + mpa);
        } else {
            if (mpaId == null)
                throw new NotFoundException("mpaId is null");
            else
                throw new NotFoundException(String.format("mpaId: %d не был найден", mpaId));
        }

        return mpa;
    }

    public Mpa postMpa(Mpa mpa) {
        var mpaId = mpa.getId();

        if (mpaId != null && filmStorage.containsMpaKey(mpaId)) {
            mpa = filmStorage.updateMpa(mpa);

            log.trace("postFilm: Обновил mpa: " + mpa);
        } else {
            mpa = filmStorage.createRating(mpa);

            log.trace("postFilm: Создал mpa: " + mpa);
        }

        return mpa;
    }

    public Mpa deleteMpa(long mpaId) {
        if (!filmStorage.containsMpaKey(mpaId)) {
            throw new NotFoundException(String.format("Этот mpaId: %d не был найден", mpaId));
        }

        var oldMpa = filmStorage.deleteMpa(mpaId);

        log.trace("deleteFilm: Удалил mpa: " + oldMpa);

        return oldMpa;
    }
}

package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;


@Service
@Slf4j
public class GenreService {
    private final FilmStorage filmStorage;

    public GenreService(@Qualifier("InDbFilmStorage") FilmStorage genreStorage) {
        this.filmStorage = genreStorage;
    }

    public List<Genre> getGenres() {
        log.trace("Request for all genres list");

        return filmStorage.getGenres();
    }

    public Genre getGenre(long genreId) {
        log.trace("Request for Genre bu genreId - " + genreId);

        if (!filmStorage.containsGenreIdKey(genreId)) {
            throw new NotFoundException(String.format("Этот genreId: %d не был найден", genreId));
        }

        return filmStorage.findGenreById(genreId);
    }

    public Genre putGenre(Genre genre) {
        log.trace("Request to put genre: " + genre);

        var genreId = genre.getId();

        if (genreId != null && filmStorage.containsGenreIdKey(genreId)) {
            genre = filmStorage.updateGenre(genre);

            log.trace("putFilm: Обновил film: " + genre);
        } else {
            if (genreId == null)
                throw new NotFoundException("putFilm: genreId: null не был найден");
            else
                throw new NotFoundException(String.format("putFilm: genreId: %d не был найден", genreId));
        }

        return genre;
    }

    public Genre postGenre(Genre genre) {
        log.trace("Request to post genre: " + genre);

        var genreId = genre.getId();

        if (genreId != null && filmStorage.containsGenreIdKey(genreId)) {
            genre = filmStorage.updateGenre(genre);

            log.trace("postFilm: Обновил genre: " + genre);
        } else {
            genre = filmStorage.createGenre(genre);

            log.trace("postFilm: Создал genre: " + genre);
        }

        return genre;
    }

    public Genre deleteGenre(long genreId) {
        log.trace("Request to delete genreId: " + genreId);

        if (!filmStorage.containsGenreIdKey(genreId)) {
            throw new NotFoundException(String.format("Этот genreId: %d не был найден", genreId));
        }

        var oldGenre = filmStorage.deleteGenre(genreId);

        log.trace("deleteFilm: Удалил genre: " + oldGenre);

        return oldGenre;
    }
}

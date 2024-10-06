package ru.yandex.practicum.filmorate.storage.dbstorage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdIsAlreadyInUseException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.NotValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.SortParameters;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.GenreDto;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.LikeDto;
import ru.yandex.practicum.filmorate.storage.dbstorage.dto.MpaDto;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.dbstorage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.dbstorage.repositories.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@Qualifier("InDbFilmStorage")
@RequiredArgsConstructor
public class InDbFilmStorage implements FilmStorage {
    final FilmsRepository filmsRepository;
    final FilmRowMapper filmMapper;

    final GenresRepository genreRepository;
    final GenreRowMapper genreRowMapper;

    final LikesRepository likesRepository;
    final UsersRepository usersRepository;
    final MpaRepository mpaRepository;
    final FilmGenresRepository filmGenresRepository;


    @Override
    public boolean containsKey(long filmId) {
        return filmsRepository.containsKey(filmId);
    }

    @Override
    public Film getFilm(long filmId) {
        var dto = filmsRepository.findById(filmId);

        return filmMapper
                .toData(dto)
                .toBuilder()
                .genres(genreRepository
                        .findByFilmID(filmId)
                        .stream()
                        .map(genreRowMapper::toData)
                        .toList())
                .build();
    }

    @Override
    public List<Film> getFilms(@NonNull SortParameters parameters) {
        return filmsRepository.getFilms(parameters)
                .stream()
                .map(filmMapper::toData)
                .toList();
    }

    private boolean containsGenre(Genre genre) {
        return genreRepository.containsKey(genre.getId());
    }

    private void checkContainsReferences(Film film) {
        var mpa = film.getMpa();

        if (mpa != null && !containsMpaKey(mpa.getId())) {
            throw  new NotValidException("Не найден mpa_id " + mpa.getId());
        }

        if (film.getGenres() != null) {
            for (var genre : film.getGenres()) {
                if (!containsGenre(genre)) {
                    throw  new NotValidException("Не найден genre_id " + genre.getId());
                }
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        checkContainsReferences(film);

        filmsRepository.updateFilm(filmMapper.toDto(film));

        updateGenresForFilm(film);

        return film;
    }

    @Override
    public Film createFilm(Film film) {
        checkContainsReferences(film);

        var filmId = filmsRepository.createFilm(filmMapper.toDto(film));

        updateGenresForFilm(filmId, film.getGenres());

        return film.toBuilder()
                .id(filmId)
                .build();
    }

    @Override
    public Film deleteFilm(long filmId) {
        updateGenresForFilm(filmId, List.of());

        return filmMapper.toData(filmsRepository.deleteFilm(filmId));
    }

    @Override
    public void like(long filmId, long userId) {
        if (likesRepository.containsByPair(userId, filmId)) {
            throw new IdIsAlreadyInUseException("Лайк от этого пользователя уже есть");
        }

        if (!filmsRepository.containsKey(filmId)) {
            throw new NotFoundException(String.format("Этот filmId: %d не был найден", filmId));
        }

        if (!usersRepository.containsKey(userId)) {
            throw new NotFoundException(String.format("Этот userId: %d не был найден", userId));
        }

        if (!likesRepository.createLike(userId, filmId)) {
            throw new NotValidException(String.format("Не смог добавить лайк для %d от %d", filmId, userId));
        }
    }

    @Override
    public void disLike(long filmId, long userId) {
        if (!likesRepository.containsByPair(userId, filmId)) {
            throw new NotFoundException("Лайк от этого пользователя не найден");
        }

        if (!likesRepository.delete(userId, filmId)) {
            throw new NotValidException(String.format("Не смог удалить лайк для %d от %d", filmId, userId));
        }
    }

    @Override
    public List<Long> getLikes(long filmId) {
        return likesRepository.getLikesByFilmId(filmId)
                .stream()
                .map(LikeDto::getUserId)
                .toList();
    }

    @Override
    public List<Genre> getGenres() {
        return genreRepository.getAll()
                .stream()
                .map(i -> Genre.builder()
                        .id(i.getGenreId())
                        .name(i.getName())
                        .build())
                .toList();
    }

    @Override
    public Genre findGenreById(long genreId) {
        var genre = genreRepository.findGenreById(genreId);

        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр не найден");
        }

        return genre
                .map(i -> Genre.builder()
                        .id(i.getGenreId())
                        .name(i.getName())
                        .build())
                .get();
    }

    @Override
    public Genre updateGenre(Genre genre) {
        var dto = GenreDto
                .builder()
                .genreId(genre.getId())
                .name(genre.getName())
                .build();

        genreRepository.updateGenre(dto);

        return genre;
    }

    @Override
    public Genre createGenre(Genre genre) {
        var dto = GenreDto
                .builder()
                .genreId(genre.getId())
                .name(genre.getName())
                .build();

        var genreId = genreRepository.createGenre(dto);

        if (genreId.isEmpty()) {
            throw new NotValidException(String.format("Не смог Добавить жанр %s", genre.getName()));
        }

        return genre.toBuilder()
                .id(genreId.get())
                .build();
    }

    @Override
    public Genre deleteGenre(long genreId) {
        var dto = genreRepository.deleteGenre(genreId);

        return Genre.builder()
                .id(dto.getGenreId())
                .name(dto.getName())
                .build();
    }

    @Override
    public List<Mpa> getMpas() {
        return mpaRepository.getAll()
                .stream()
                .map(i -> Mpa.builder()
                        .id(i.getMpaId())
                        .name(i.getName())
                        .build())
                .toList();
    }

    @Override
    public Mpa findMpaById(long mapId) {
        var dto = mpaRepository.findMpaById(mapId);

        if (dto.isEmpty()) {
            throw new NotFoundException("Рейтинг не найден");
        }

        return dto
                .map(i -> Mpa.builder()
                        .id(i.getMpaId())
                        .name(i.getName())
                        .build())
                .get();
    }

    @Override
    public Mpa updateMpa(Mpa rating) {
        var dto = MpaDto
                .builder()
                .mpaId(rating.getId())
                .name(rating.getName())
                .build();

        mpaRepository.updateMpa(dto);

        return rating;
    }

    @Override
    public Mpa createRating(Mpa rating) {
        var dto = MpaDto
                .builder()
                .mpaId(rating.getId())
                .name(rating.getName())
                .build();

        var genreId = mpaRepository.createMpa(dto);

        if (genreId.isEmpty()) {
            throw new NotValidException(String.format("Не смог Добавить жанр %s", rating.getName()));
        }

        return rating.toBuilder()
                .id(genreId.get())
                .build();
    }

    @Override
    public Mpa deleteMpa(long ratingId) {
        var dto = mpaRepository.deleteMpa(ratingId);

        return Mpa.builder()
                .id(dto.getMpaId())
                .name(dto.getName())
                .build();
    }

    @Override
    public boolean containsMpaKey(long mpaId) {
        return mpaRepository.containsKey(mpaId);
    }

    @Override
    public boolean containsGenreIdKey(long genreId) {
        return genreRepository.containsKey(genreId);
    }

    private void updateGenresForFilm(Film film) {
        updateGenresForFilm(film.getId(), film.getGenres());
    }

    private void updateGenresForFilm(long filmId, List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            if (filmGenresRepository.containsKey(filmId)) {
                if (!filmGenresRepository.delete(filmId))
                    throw new NotValidException(String.format("Query to delete genre_film by (%d) to fail", filmId));
            }

            return;
        }

        var idGenres = genres.stream().map(Genre::getId).collect(Collectors.toSet());
        var currentGenres = new HashSet<>(filmGenresRepository.findGenresByFilmId(filmId));

        var idGenresToDelete = currentGenres
                .stream()
                .filter(i -> !idGenres.contains(i))
                .toList();
        var idGenresToInsert = idGenres
                .stream()
                .filter(i -> !currentGenres.contains(i))
                .toList();

        for (var id : idGenresToInsert) {
            if (!filmGenresRepository.insert(filmId, id))
                throw new NotValidException(String.format("Query to insert genre_film by (%d, %d) to fail", filmId, id));
        }

        for (var id : idGenresToDelete) {
            if (!filmGenresRepository.delete(filmId, id))
                throw new NotValidException(String.format("Query to delete genre_film by (%d, %d) to fail", filmId, id));
        }
    }
}

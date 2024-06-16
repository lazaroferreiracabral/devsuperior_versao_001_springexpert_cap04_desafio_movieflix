package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findAll (String genreId, Pageable pageable) {
        List<Long> genreIds = Arrays.asList();
        if ("0".equals(genreId)) {
            Page<Movie> list = repository.findAll(pageable);
            return list.map(x -> new MovieCardDTO(x));
        } else {
            genreIds = Arrays.asList(genreId.split(","))
                    .stream().map(Long::parseLong).toList();

            Page<Movie> list = repository.searchMovieWithGenres(genreIds, pageable);
            return list.map(x -> new MovieCardDTO(x));
        }
    }

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById (Long id) {
        Movie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Recurso de id: %d " +
                        " não encontrado",  id)));
        return new MovieDetailsDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ReviewDTO> findByIdReview (Long id) {
        Movie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Recurso de id: %d não encontradao", id)));

        return reviewRepository.searchMovie(entity)
                .stream()
                .map(x -> new ReviewDTO(x)).toList();
    }

}

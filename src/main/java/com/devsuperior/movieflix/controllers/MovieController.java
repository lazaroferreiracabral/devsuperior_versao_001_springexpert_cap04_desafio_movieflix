package com.devsuperior.movieflix.controllers;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_VISITOR','ROLE_MEMBER')")
    public ResponseEntity<Page<MovieCardDTO>> findAllPagedWithGenre(
            @RequestParam (value="genreId", defaultValue = "0") String genreId,
            Pageable pageable) {
        Page<MovieCardDTO> dto = service.findAll(genreId, pageable);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITOR','ROLE_MEMBER')")
    @GetMapping("/{id}")
    public ResponseEntity<MovieDetailsDTO> findById (@PathVariable Long id) {
        MovieDetailsDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_VISITOR','ROLE_MEMBER')")
    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> findByIdReview (@PathVariable Long id) {
        List<ReviewDTO> list = service.findByIdReview(id);
        return ResponseEntity.ok(list);
    }
}

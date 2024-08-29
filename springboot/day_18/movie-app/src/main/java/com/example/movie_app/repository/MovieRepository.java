package com.example.movie_app.repository;

import com.example.movie_app.entity.Movie;
import com.example.movie_app.model.enums.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MovieRepository extends JpaRepository<Movie,Integer> {
    List<Movie> findByName(String name);

    List<Movie> findByNameContainingIgnoreCase(String name);

    Movie findByNameAndSlug(String name, String slug);

    List<Movie> findByRatingBetween(Double min, Double max);

    List<Movie> findByReleaseYearGreaterThanEqual(Integer year);

    List<Movie> findByStatusOrderByReleaseYearDesc(Boolean status);

    long countByType(MovieType type);

    boolean existsByRatingGreaterThan(Double rating);

    // Tìm kiếm các bộ phim theo loại và status sắp xếp theo thời gian tạo giảm dần, rating tăng dần và lấy 5 bản ghi đầu tiên
    List<Movie> findTop5ByTypeAndStatusOrderByCreatedAtDescRatingAsc(MovieType type, Boolean status);
}

package com.recoia.interactionservice.repository;

import com.recoia.interactionservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByUserIdAndCourseId(Long userId, Long courseId);

    List<Rating> findByUserId(Long userId);

    long countByCourseId(Long courseId);

    // null s'il n'y a aucune note pour ce cours
    @Query("select avg(r.score) from Rating r where r.courseId = :courseId")
    Double averageScoreByCourseId(@Param("courseId") Long courseId);
}

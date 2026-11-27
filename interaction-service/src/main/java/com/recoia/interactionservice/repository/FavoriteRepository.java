package com.recoia.interactionservice.repository;

import com.recoia.interactionservice.dto.CourseCountResponse;
import com.recoia.interactionservice.model.Favorite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserIdAndCourseId(Long userId, Long courseId);

    List<Favorite> findByUserId(Long userId);

    long countByCourseId(Long courseId);

    void deleteByUserIdAndCourseId(Long userId, Long courseId);

    // classement des cours les plus mis en favori
    @Query("select new com.recoia.interactionservice.dto.CourseCountResponse(f.courseId, count(f)) "
            + "from Favorite f group by f.courseId order by count(f) desc")
    List<CourseCountResponse> findTopFavorited(Pageable pageable);
}

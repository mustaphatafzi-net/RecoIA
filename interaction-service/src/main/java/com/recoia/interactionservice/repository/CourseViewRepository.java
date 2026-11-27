package com.recoia.interactionservice.repository;

import com.recoia.interactionservice.dto.CourseCountResponse;
import com.recoia.interactionservice.model.CourseView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseViewRepository extends JpaRepository<CourseView, Long> {

    List<CourseView> findByUserIdOrderByViewedAtDesc(Long userId);

    long countByCourseId(Long courseId);

    // classement des cours les plus consultes
    @Query("select new com.recoia.interactionservice.dto.CourseCountResponse(v.courseId, count(v)) "
            + "from CourseView v group by v.courseId order by count(v) desc")
    List<CourseCountResponse> findTopViewed(Pageable pageable);
}

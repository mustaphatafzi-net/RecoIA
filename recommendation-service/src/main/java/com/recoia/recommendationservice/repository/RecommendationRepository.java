package com.recoia.recommendationservice.repository;

import com.recoia.recommendationservice.dto.CourseCountResponse;
import com.recoia.recommendationservice.model.Recommendation;
import com.recoia.recommendationservice.model.enums.RecommendationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByUserIdAndStatus(Long userId, RecommendationStatus status);

    List<Recommendation> findByUserIdAndStatusIn(Long userId, Collection<RecommendationStatus> statuses);

    // le "AndUserId" empeche un etudiant de toucher la recommandation d'un autre
    Optional<Recommendation> findByIdAndUserId(Long id, Long userId);

    long countByStatus(RecommendationStatus status);

    // classement des cours les plus recommandes
    @Query("select new com.recoia.recommendationservice.dto.CourseCountResponse(r.courseId, count(r)) "
            + "from Recommendation r group by r.courseId order by count(r) desc")
    List<CourseCountResponse> findMostRecommended(Pageable pageable);
}

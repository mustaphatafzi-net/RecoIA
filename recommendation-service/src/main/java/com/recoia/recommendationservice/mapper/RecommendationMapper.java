package com.recoia.recommendationservice.mapper;

import com.recoia.recommendationservice.client.dto.CourseData;
import com.recoia.recommendationservice.dto.RecommendationResponse;
import com.recoia.recommendationservice.model.Recommendation;
import org.springframework.stereotype.Component;

@Component
public class RecommendationMapper {

    // le cours (venu du catalogue) peut etre null s'il a ete supprime depuis
    public RecommendationResponse toResponse(Recommendation rec, CourseData course) {
        return new RecommendationResponse(
                rec.getId(),
                rec.getCourseId(),
                course == null ? null : course.title(),
                course == null ? null : course.categoryName(),
                course == null ? null : course.imageUrl(),
                course == null ? null : course.level(),
                rec.getScore(),
                rec.getReason(),
                rec.getStatus(),
                rec.getFeedbackReason(),
                rec.getCreatedAt());
    }
}

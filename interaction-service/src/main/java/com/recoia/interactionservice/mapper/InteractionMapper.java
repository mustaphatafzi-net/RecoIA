package com.recoia.interactionservice.mapper;

import com.recoia.interactionservice.dto.CourseViewResponse;
import com.recoia.interactionservice.dto.FavoriteResponse;
import com.recoia.interactionservice.dto.RatingResponse;
import com.recoia.interactionservice.model.CourseView;
import com.recoia.interactionservice.model.Favorite;
import com.recoia.interactionservice.model.Rating;
import org.springframework.stereotype.Component;

@Component
public class InteractionMapper {

    public RatingResponse toResponse(Rating rating) {
        return new RatingResponse(rating.getId(), rating.getUserId(), rating.getCourseId(),
                rating.getScore(), rating.getComment(), rating.getUpdatedAt());
    }

    public FavoriteResponse toResponse(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getUserId(),
                favorite.getCourseId(), favorite.getCreatedAt());
    }

    public CourseViewResponse toResponse(CourseView view) {
        return new CourseViewResponse(view.getId(), view.getUserId(),
                view.getCourseId(), view.getViewedAt());
    }
}

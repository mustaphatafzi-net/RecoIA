package com.recoia.recommendationservice.engine;

import com.recoia.recommendationservice.client.dto.*;
import com.recoia.recommendationservice.model.Recommendation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Tout ce que le moteur a besoin de connaitre pour noter les cours d'un etudiant. */
public record ScoringInput(
        StudentData profile,                              // peut etre null (profil pas rempli)
        Collection<CourseData> courses,
        Map<Long, List<CourseSkillData>> skillsByCourse,
        List<RatingData> ratings,
        List<FavoriteData> favorites,
        List<ViewData> views,
        List<Recommendation> history,                     // recommandations deja acceptees / refusees
        Set<Long> excludedCourseIds                       // cours a ne pas proposer
) {
}

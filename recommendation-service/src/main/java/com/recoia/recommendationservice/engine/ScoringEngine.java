package com.recoia.recommendationservice.engine;

import com.recoia.recommendationservice.client.dto.*;
import com.recoia.recommendationservice.model.Recommendation;
import com.recoia.recommendationservice.model.enums.RecommendationStatus;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Le moteur de recommandation : il donne a chaque cours une note sur 100.
 *
 *   Critere                                         Points max
 *   1. Centre d'interet (categorie choisie)             35
 *   2. Prerequis deja maitrises                         15
 *   3. Nouvelles competences a acquerir                 10
 *   4. Niveau du cours adapte au parcours               15
 *   5. Activite (notes, favoris, consultations)         15
 *   6. Retour (recommandations acceptees / refusees)    10
 */
@Component
public class ScoringEngine {

    static final double W_INTEREST = 35;
    static final double W_PREREQUISITES = 15;
    static final double W_NEW_SKILLS = 10;
    static final double W_LEVEL = 15;
    static final double W_ACTIVITY = 15;
    static final double W_FEEDBACK = 10;

    public List<ScoredCourse> score(ScoringInput in) {
        Map<Long, CourseData> coursesById = in.courses().stream()
                .collect(Collectors.toMap(CourseData::id, c -> c, (a, b) -> a));

        Map<Long, Double> activity = activityByCategory(in, coursesById);
        double maxActivity = activity.values().stream().mapToDouble(Double::doubleValue).max().orElse(0);
        Map<Long, Double> feedback = feedbackByCategory(in.history(), coursesById);

        StudentData profile = in.profile();
        Set<Long> interests = profile == null || profile.categoryIds() == null ? Set.of() : profile.categoryIds();
        Set<Long> studentSkills = profile == null || profile.skills() == null ? Set.of()
                : profile.skills().stream().map(StudentSkillData::skillId).collect(Collectors.toSet());
        double studentRank = profile == null || profile.academicLevel() == null ? -1 : studentRank(profile.academicLevel());

        List<ScoredCourse> result = new ArrayList<>();
        for (CourseData course : in.courses()) {
            if (Boolean.FALSE.equals(course.active()) || in.excludedCourseIds().contains(course.id())) {
                continue;
            }
            List<String> reasons = new ArrayList<>();

            // 1. centre d'interet
            double interest = interests.contains(course.categoryId()) ? W_INTEREST : 0;
            if (interest > 0) {
                reasons.add("correspond à votre centre d'intérêt « " + course.categoryName() + " »");
            }

            // 2 et 3. competences du cours
            List<CourseSkillData> skills = in.skillsByCourse().getOrDefault(course.id(), List.of());
            List<Long> prerequisites = skills.stream().filter(s -> "PREREQUISITE".equals(s.type()))
                    .map(CourseSkillData::skillId).toList();
            List<Long> outcomes = skills.stream().filter(s -> "LEARNING_OUTCOME".equals(s.type()))
                    .map(CourseSkillData::skillId).toList();

            double prerequisiteRatio = prerequisites.isEmpty() ? 1.0
                    : (double) prerequisites.stream().filter(studentSkills::contains).count() / prerequisites.size();
            double prerequisitePoints = W_PREREQUISITES * prerequisiteRatio;
            if (!prerequisites.isEmpty() && prerequisiteRatio == 1.0) {
                reasons.add("vous maîtrisez déjà les prérequis");
            }

            double newRatio = outcomes.isEmpty() ? 0
                    : (double) outcomes.stream().filter(id -> !studentSkills.contains(id)).count() / outcomes.size();
            double newSkillPoints = W_NEW_SKILLS * newRatio;
            if (newRatio >= 0.5 && !studentSkills.isEmpty()) {
                reasons.add("vous apprendra de nouvelles compétences");
            }

            // 4. niveau du cours
            double levelPoints = 0;
            if (studentRank >= 0) {
                double diff = Math.abs(studentRank - courseRank(course.level()));
                levelPoints = W_LEVEL * (1 - Math.min(diff, 2.0) / 2.0);
                if (levelPoints >= 11) {
                    reasons.add("niveau adapté à votre parcours");
                }
            }

            // 5. activite
            double categoryActivity = activity.getOrDefault(course.categoryId(), 0.0);
            double activityPoints = 0;
            if (categoryActivity > 0 && maxActivity > 0) {
                activityPoints = W_ACTIVITY * Math.min(categoryActivity / maxActivity, 1.0);
                reasons.add("proche des cours que vous avez appréciés");
            } else if (categoryActivity < 0) {
                activityPoints = -W_ACTIVITY / 2;
            }

            // 6. retour de l'etudiant
            double categoryFeedback = feedback.getOrDefault(course.categoryId(), 0.0);
            double feedbackPoints = Math.max(-W_FEEDBACK, Math.min(W_FEEDBACK, categoryFeedback * 5));
            if (feedbackPoints > 0) {
                reasons.add("similaire à des recommandations que vous avez acceptées");
            }

            double total = interest + prerequisitePoints + newSkillPoints + levelPoints + activityPoints + feedbackPoints;
            total = Math.round(Math.max(0, Math.min(100, total)) * 10) / 10.0;

            String reason = reasons.isEmpty() ? "Cours populaire du catalogue"
                    : capitalize(String.join(" · ", reasons));
            result.add(new ScoredCourse(course, total, reason));
        }
        return result;
    }

    // categorie -> total des points d'activite (note >= 4 : +2, <= 2 : -2, favori : +3, consultation : +1)
    private Map<Long, Double> activityByCategory(ScoringInput in, Map<Long, CourseData> coursesById) {
        Map<Long, Double> result = new HashMap<>();
        for (RatingData r : in.ratings()) {
            double points = r.score() >= 4 ? 2 : r.score() <= 2 ? -2 : 0;
            add(result, coursesById.get(r.courseId()), points);
        }
        for (FavoriteData f : in.favorites()) {
            add(result, coursesById.get(f.courseId()), 3);
        }
        for (ViewData v : in.views()) {
            add(result, coursesById.get(v.courseId()), 1);
        }
        return result;
    }

    // categorie -> (nombre d'acceptees) - (nombre de refusees)
    private Map<Long, Double> feedbackByCategory(List<Recommendation> history, Map<Long, CourseData> coursesById) {
        Map<Long, Double> result = new HashMap<>();
        for (Recommendation rec : history) {
            double points = rec.getStatus() == RecommendationStatus.ACCEPTED ? 1
                    : rec.getStatus() == RecommendationStatus.REJECTED ? -1 : 0;
            add(result, coursesById.get(rec.getCourseId()), points);
        }
        return result;
    }

    private void add(Map<Long, Double> map, CourseData course, double points) {
        if (course != null && course.categoryId() != null && points != 0) {
            map.merge(course.categoryId(), points, Double::sum);
        }
    }

    private double studentRank(String academicLevel) {
        return switch (academicLevel) {
            case "LICENCE_1" -> 0;
            case "LICENCE_2" -> 0.5;
            case "LICENCE_3" -> 1;
            case "MASTER_1" -> 1.5;
            case "MASTER_2" -> 2;
            default -> -1;
        };
    }

    private double courseRank(String courseLevel) {
        return switch (courseLevel == null ? "" : courseLevel) {
            case "BEGINNER" -> 0;
            case "INTERMEDIATE" -> 1;
            case "ADVANCED" -> 2;
            default -> 1;
        };
    }

    private String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}

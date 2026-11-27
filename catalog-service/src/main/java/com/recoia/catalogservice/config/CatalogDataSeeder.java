package com.recoia.catalogservice.config;

import com.recoia.catalogservice.model.Category;
import com.recoia.catalogservice.model.Course;
import com.recoia.catalogservice.model.CourseSkill;
import com.recoia.catalogservice.model.Skill;
import com.recoia.catalogservice.model.enums.CourseFormat;
import com.recoia.catalogservice.model.enums.CourseLevel;
import com.recoia.catalogservice.model.enums.CourseSkillType;
import com.recoia.catalogservice.repository.CategoryRepository;
import com.recoia.catalogservice.repository.CourseRepository;
import com.recoia.catalogservice.repository.CourseSkillRepository;
import com.recoia.catalogservice.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Remplit la base H2 au demarrage : 8 categories (ids 1 a 8), 15 competences (ids 1 a 15)
 * et 40 cours. Comme H2 est en memoire, les ids repartent de 1 a chaque demarrage :
 * ce sont ceux que student-service utilise dans son seeder (categories 1..8, competences 1..15).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final CourseRepository courseRepository;
    private final CourseSkillRepository courseSkillRepository;

    // nom|description
    private static final String[] CATEGORIES = {
            "Intelligence artificielle|IA, machine learning et deep learning",
            "Développement web|Frontend, backend et architectures web",
            "Data science|Analyse, statistiques et visualisation de données",
            "Cybersécurité|Sécurité des systèmes, des réseaux et des applications",
            "Cloud et DevOps|Conteneurs, cloud et intégration continue",
            "Bases de données|Modélisation, SQL et NoSQL",
            "Développement mobile|Applications Android, iOS et multiplateformes",
            "Gestion de projet|Méthodes agiles, planification et leadership"
    };

    private static final String[] SKILLS = {
            "Python|Langage de programmation polyvalent",           // 1
            "Java|Langage orienté objet",                           // 2
            "JavaScript|Langage du web",                            // 3
            "SQL|Interrogation de bases de données",                // 4
            "Machine Learning|Apprentissage automatique",           // 5
            "Deep Learning|Réseaux de neurones profonds",           // 6
            "Spring Boot|Framework Java pour les API",              // 7
            "Angular|Framework frontend",                           // 8
            "Docker|Conteneurisation d'applications",               // 9
            "Git|Gestion de versions",                              // 10
            "Réseaux|Fondamentaux des réseaux informatiques",       // 11
            "Statistiques|Probabilités et statistiques",            // 12
            "Cybersécurité|Protection des systèmes",                // 13
            "Cloud AWS|Services cloud d'Amazon",                    // 14
            "Gestion de projet agile|Scrum, Kanban et suivi"        // 15
    };

    // titre|categorie|niveau(B/I/A)|heures|fournisseur|format(O/S/H)|langue|prerequis|acquis1|acquis2
    // (0 = pas de competence)
    private static final String[] COURSES = {
            // 1 - Intelligence artificielle
            "Introduction à l'intelligence artificielle|1|B|20|Coursera|O|fr|0|5|0",
            "Machine Learning avec Python|1|I|35|OpenClassrooms|O|fr|1|5|12",
            "Deep Learning et réseaux de neurones|1|A|40|Udacity|O|en|5|6|0",
            "IA générative et ingénierie de prompts|1|B|12|DataCamp|O|fr|0|5|0",
            "Vision par ordinateur|1|A|30|Coursera|H|en|6|6|0",
            // 2 - Developpement web
            "HTML, CSS et JavaScript|2|B|25|OpenClassrooms|O|fr|0|3|0",
            "Angular de zéro|2|I|30|Udemy|O|fr|3|8|0",
            "Spring Boot pour API REST|2|I|35|OpenClassrooms|O|fr|2|7|0",
            "Architecture microservices|2|A|40|Udemy|H|fr|7|7|9",
            "Git et GitHub en équipe|2|B|8|Coursera|O|fr|0|10|0",
            // 3 - Data science
            "Python pour la data|3|B|20|DataCamp|O|en|0|1|0",
            "Statistiques pour la data science|3|I|25|Coursera|O|fr|0|12|0",
            "Analyse de données avec SQL|3|B|18|DataCamp|O|en|0|4|0",
            "Visualisation de données|3|I|15|OpenClassrooms|O|fr|1|1|12",
            "Big Data et Spark|3|A|35|Udacity|H|en|1|1|4",
            // 4 - Cybersecurite
            "Fondamentaux de la cybersécurité|4|B|20|Coursera|O|fr|0|13|0",
            "Réseaux informatiques|4|B|25|OpenClassrooms|S|fr|0|11|0",
            "Sécurité des applications web|4|I|30|Udemy|O|en|3|13|0",
            "Tests d'intrusion éthiques|4|A|40|Udemy|H|en|11|13|11",
            "Cryptographie appliquée|4|A|28|Coursera|O|en|12|13|0",
            // 5 - Cloud et DevOps
            "Docker pour débutants|5|B|12|Udemy|O|fr|0|9|0",
            "Introduction à AWS|5|B|20|Coursera|O|en|0|14|0",
            "CI/CD avec GitHub Actions|5|I|15|OpenClassrooms|O|fr|10|10|9",
            "Kubernetes en pratique|5|A|30|Udemy|O|en|9|9|0",
            "Architecture cloud serverless|5|A|25|Coursera|H|en|14|14|0",
            // 6 - Bases de donnees
            "SQL de A à Z|6|B|22|OpenClassrooms|O|fr|0|4|0",
            "Modélisation de bases de données|6|I|18|Coursera|O|fr|4|4|0",
            "NoSQL avec MongoDB|6|I|20|Udemy|O|en|4|4|0",
            "Optimisation des requêtes SQL|6|A|16|Udemy|S|fr|4|4|0",
            "Administration PostgreSQL|6|A|24|OpenClassrooms|H|fr|4|4|0",
            // 7 - Developpement mobile
            "Développement Android avec Java|7|I|35|Udemy|O|fr|2|2|0",
            "React Native|7|I|30|Udemy|O|en|3|3|0",
            "Flutter pour débutants|7|B|25|Udemy|O|fr|0|3|0",
            "Publier une application mobile|7|I|10|Coursera|O|fr|10|10|0",
            "Conception d'interfaces mobiles|7|B|14|OpenClassrooms|S|fr|0|3|0",
            // 8 - Gestion de projet
            "Gestion de projet agile|8|B|15|OpenClassrooms|O|fr|0|15|0",
            "Devenir Scrum Master|8|I|20|Coursera|H|en|15|15|0",
            "Planifier et suivre un projet|8|B|12|OpenClassrooms|O|fr|0|15|0",
            "Leadership technique|8|A|18|Coursera|S|en|15|15|0",
            "Gestion des risques projet|8|I|14|Udemy|O|fr|15|15|0"
    };

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            return; // deja rempli
        }

        // 1) categories : ids 1..8 (ordre d'insertion)
        List<Category> categories = new ArrayList<>();
        for (String line : CATEGORIES) {
            String[] p = line.split("\\|");
            Category category = new Category();
            category.setName(p[0]);
            category.setDescription(p[1]);
            categories.add(categoryRepository.save(category));
        }

        // 2) competences : ids 1..15
        List<Skill> skills = new ArrayList<>();
        for (String line : SKILLS) {
            String[] p = line.split("\\|");
            Skill skill = new Skill();
            skill.setName(p[0]);
            skill.setDescription(p[1]);
            skills.add(skillRepository.save(skill));
        }

        // 3) cours + liens cours-competences
        int number = 1;
        for (String line : COURSES) {
            String[] p = line.split("\\|");
            Course course = new Course();
            course.setTitle(p[0]);
            course.setCategory(categories.get(Integer.parseInt(p[1]) - 1));
            course.setLevel(toLevel(p[2]));
            course.setDurationHours(Integer.parseInt(p[3]));
            course.setProvider(p[4]);
            course.setFormat(toFormat(p[5]));
            course.setLanguage(p[6]);
            course.setDescription("Cours « " + p[0] + " » proposé par " + p[4] + " : "
                    + p[3] + " heures de formation, niveau " + toLevel(p[2]).name().toLowerCase() + ".");
            course.setImageUrl("https://picsum.photos/seed/recoia" + number + "/600/400");
            course = courseRepository.save(course);

            link(course, skills, p[7], CourseSkillType.PREREQUISITE);
            link(course, skills, p[8], CourseSkillType.LEARNING_OUTCOME);
            link(course, skills, p[9], CourseSkillType.LEARNING_OUTCOME);
            number++;
        }

        log.info("Catalogue de test cree : {} categories, {} competences, {} cours",
                categories.size(), skills.size(), COURSES.length);
    }

    private void link(Course course, List<Skill> skills, String skillNumber, CourseSkillType type) {
        int n = Integer.parseInt(skillNumber);
        if (n == 0) {
            return; // pas de competence a lier
        }
        CourseSkill link = new CourseSkill();
        link.setCourse(course);
        link.setSkill(skills.get(n - 1));
        link.setType(type);
        courseSkillRepository.save(link);
    }

    private CourseLevel toLevel(String code) {
        return switch (code) {
            case "B" -> CourseLevel.BEGINNER;
            case "I" -> CourseLevel.INTERMEDIATE;
            default -> CourseLevel.ADVANCED;
        };
    }

    private CourseFormat toFormat(String code) {
        return switch (code) {
            case "S" -> CourseFormat.ONSITE;
            case "H" -> CourseFormat.HYBRID;
            default -> CourseFormat.ONLINE;
        };
    }
}

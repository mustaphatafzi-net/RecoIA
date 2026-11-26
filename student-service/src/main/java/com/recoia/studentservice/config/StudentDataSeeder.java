package com.recoia.studentservice.config;

import com.recoia.studentservice.dto.StudentRequest;
import com.recoia.studentservice.dto.StudentSkillDto;
import com.recoia.studentservice.model.enums.AcademicLevel;
import com.recoia.studentservice.model.enums.SkillLevel;
import com.recoia.studentservice.repository.StudentRepository;
import com.recoia.studentservice.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

// Cree 30 etudiants de test au demarrage (exigence de l'enonce)
@Slf4j
@Component
@RequiredArgsConstructor
public class StudentDataSeeder implements CommandLineRunner {

    private static final int STUDENT_COUNT = 30;

    // Le compte 1 est l'admin : les etudiants de test ont les userId 2 a 31
    private static final long FIRST_USER_ID = 2;

    // ids du catalog-service (a faire correspondre avec ses donnees de test)
    private static final int CATEGORY_COUNT = 8;
    private static final int SKILL_COUNT = 15;

    private static final List<String> GOALS = List.of(
            "Devenir data scientist",
            "Devenir developpeur web full stack",
            "Travailler en cybersecurite",
            "Devenir ingenieur DevOps",
            "Creer ma propre startup",
            "Devenir developpeur mobile",
            "Travailler dans l'intelligence artificielle",
            "Devenir architecte logiciel"
    );

    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Override
    public void run(String... args) {
        // Si des profils existent deja, on ne touche a rien
        if (studentRepository.count() > 0) {
            log.info("Profils etudiants deja presents : pas de donnees de test");
            return;
        }

        // graine fixe : on obtient toujours les memes donnees (pratique pour la demo)
        Random random = new Random(42);

        for (int i = 0; i < STUDENT_COUNT; i++) {
            long userId = FIRST_USER_ID + i;
            studentService.saveMyProfile(userId, randomRequest(random));
        }

        log.info("{} etudiants de test crees (userId {} a {})",
                STUDENT_COUNT, FIRST_USER_ID, FIRST_USER_ID + STUDENT_COUNT - 1);
    }

    private StudentRequest randomRequest(Random random) {
        AcademicLevel level = pick(AcademicLevel.values(), random);
        String goal = GOALS.get(random.nextInt(GOALS.size()));

        // 1 a 3 categories differentes
        Set<Long> categoryIds = randomIds(1 + random.nextInt(3), CATEGORY_COUNT, random);

        // 2 a 4 competences differentes, chacune avec un niveau au hasard
        List<StudentSkillDto> skills = new ArrayList<>();
        for (Long skillId : randomIds(2 + random.nextInt(3), SKILL_COUNT, random)) {
            skills.add(new StudentSkillDto(skillId, pick(SkillLevel.values(), random)));
        }

        return new StudentRequest(level, goal, categoryIds, skills);
    }

    // 'count' numeros differents entre 1 et 'max'
    private Set<Long> randomIds(int count, int max, Random random) {
        List<Long> all = new ArrayList<>();
        for (long id = 1; id <= max; id++) {
            all.add(id);
        }
        Collections.shuffle(all, random);
        return new HashSet<>(all.subList(0, count));
    }

    private <T> T pick(T[] values, Random random) {
        return values[random.nextInt(values.length)];
    }
}

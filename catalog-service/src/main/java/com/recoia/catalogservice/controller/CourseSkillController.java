package com.recoia.catalogservice.controller;

import com.recoia.catalogservice.dto.CourseSkillRequest;
import com.recoia.catalogservice.dto.CourseSkillResponse;
import com.recoia.catalogservice.service.CourseSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/skills")   // les compétences d'un cours
@RequiredArgsConstructor
public class CourseSkillController {

    private final CourseSkillService courseSkillService;

    @GetMapping                                  // GET /courses/1/skills
    public List<CourseSkillResponse> findByCourse(@PathVariable Long courseId) {
        return courseSkillService.findByCourse(courseId);
    }

    @PostMapping                                 // POST /courses/1/skills
    @ResponseStatus(HttpStatus.CREATED)
    public CourseSkillResponse add(@PathVariable Long courseId,
                                   @RequestBody CourseSkillRequest request) {
        return courseSkillService.add(courseId, request);
    }

    @DeleteMapping("/{courseSkillId}")           // DELETE /courses/1/skills/5
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long courseId, @PathVariable Long courseSkillId) {
        courseSkillService.remove(courseId, courseSkillId);
    }
}

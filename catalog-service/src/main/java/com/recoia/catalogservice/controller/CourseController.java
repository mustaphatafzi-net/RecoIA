package com.recoia.catalogservice.controller;

import com.recoia.catalogservice.dto.CourseRequest;
import com.recoia.catalogservice.dto.CourseResponse;
import com.recoia.catalogservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController                  // cette classe répond aux requêtes web
@RequestMapping("/courses")      // toutes ses URLs commencent par /courses
@RequiredArgsConstructor         // injection du service par constructeur
public class CourseController {

    private final CourseService courseService;   // l'interface, pas l'implémentation

    @GetMapping                  // GET /courses ou /courses?keyword=java
    public List<CourseResponse> findAll(@RequestParam(required = false) String keyword) {
        return courseService.findAll(keyword);
    }

    @GetMapping("/{id}")         // GET /courses/1
    public CourseResponse findById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @PostMapping                 // POST /courses
    @ResponseStatus(HttpStatus.CREATED)          // renvoie 201
    public CourseResponse create(@RequestBody CourseRequest request) {

        return courseService.create(request);
    }

    @PutMapping("/{id}")         // PUT /courses/1
    public CourseResponse update(@PathVariable Long id, @RequestBody CourseRequest request) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")      // DELETE /courses/1
    @ResponseStatus(HttpStatus.NO_CONTENT)       // renvoie 204
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
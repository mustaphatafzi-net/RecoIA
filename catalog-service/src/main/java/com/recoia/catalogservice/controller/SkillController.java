package com.recoia.catalogservice.controller;

import com.recoia.catalogservice.dto.SkillRequest;
import com.recoia.catalogservice.dto.SkillResponse;
import com.recoia.catalogservice.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public List<SkillResponse> findAll() {
        return skillService.findAll();
    }

    @GetMapping("/{id}")
    public SkillResponse findById(@PathVariable Long id) {
        return skillService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SkillResponse create(@RequestBody SkillRequest request) {
        return skillService.create(request);
    }

    @PutMapping("/{id}")
    public SkillResponse update(@PathVariable Long id, @RequestBody SkillRequest request) {
        return skillService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        skillService.delete(id);
    }
}

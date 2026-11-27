package com.recoia.interactionservice.dto;

/** Une ligne de classement : "le cours 12 a ete consulte 87 fois". */
public record CourseCountResponse(Long courseId, Long total) {
}

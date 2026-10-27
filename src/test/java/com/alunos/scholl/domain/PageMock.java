package com.alunos.scholl.domain;

import java.util.List;

public record PageMock<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int number,
        int size
) {}
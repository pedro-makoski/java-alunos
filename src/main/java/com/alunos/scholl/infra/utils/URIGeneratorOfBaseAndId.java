package com.alunos.scholl.infra.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public class URIGeneratorOfBaseAndId {

    public static URI realize(String basePath, UUID id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path(basePath)
                .buildAndExpand(id)
                .toUri();
    }
}

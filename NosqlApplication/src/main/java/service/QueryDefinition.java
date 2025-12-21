package service;

import java.util.List;

public record QueryDefinition(
        int id,
        String title,
        String description,
        String cypher,
        List<ParamDefinition> params
) {}

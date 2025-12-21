package service;

public record ParamDefinition(
        String name,
        String prompt,
        ParamType type,
        String defaultValue
) {}

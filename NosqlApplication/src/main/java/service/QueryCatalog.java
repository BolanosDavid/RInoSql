package service;

import java.util.List;

import static service.ParamType.INT;
import static service.ParamType.STRING;

public final class QueryCatalog {

    private final List<QueryDefinition> queries;

    public QueryCatalog() {
        this.queries = List.of(
                new QueryDefinition(
                        1,
                        "Alumnos por asignatura (curso académico)",
                        "Cuenta cuántos alumnos hay matriculados en cada asignatura en un curso académico.",
                        CypherQueries.Q1_ALUMNOS_POR_ASIGNATURA,
                        List.of(new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"))
                ),
                new QueryDefinition(
                        2,
                        "Asignaturas relacionadas por un prerrequisito directo",
                        "Lista las asignaturas que tienen un prerrequisito y que asignaturas son.",
                        CypherQueries.Q2_PAREJAS_ASIGNATURAS_PRERREQUISITO,
                        List.of()
                ),
                new QueryDefinition(
                        3,
                        "Asignaturas con mínimo de alumnos",
                        "Devuelve asignaturas con al menos N alumnos matriculados en un curso académico.",
                        CypherQueries.Q3_ASIGNATURAS_MIN_ALUMNOS,
                        List.of(
                                new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"),
                                new ParamDefinition("minAlumnos", "Mínimo de alumnos", INT, "3")
                        )
                ),
                new QueryDefinition(
                         4,
                         "Diferencia: alumnos en SDI sin BD aprobada",
                         "Devuelve alumnos matriculados en SDI (en el curso 24/25) que no tienen BD aprobada de cursos anteriores.",
                         CypherQueries.Q4_SDI_SIN_BD_APROBADA,
                         List.of(new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"))
                 ),
                new QueryDefinition(
                        5,
                        "Asignaturas prerrequisito para RI",
                        "Obtiene prerrequisitos directos e indirectos (y saltos mínimos) para Repositorios de Información.",
                        CypherQueries.Q5_PRERREQUISITOS_RI_CIERRE_TRANSITIVO,
                        List.of()
                ),
                new QueryDefinition(
                        6,
                        "Asignaturas que el alumno UO011 podría cursar en el futuro",
                        "Para el alumno UO011, lista asignaturas que puede cursas siguiendo la cadena de prerrequisitos.",
                        CypherQueries.Q6_ASIGNATURAS_FUTURAS_UO011,
                        List.of()
                )
        );
    }

    public List<QueryDefinition> all() {
        return queries;
    }

    public QueryDefinition byId(int id) {
        return queries.stream()
                .filter(q -> q.id() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe la consulta con id=" + id));
    }
}

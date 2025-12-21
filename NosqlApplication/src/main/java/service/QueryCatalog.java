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
//                new QueryDefinition(
//                        2,
//                        "Asignaturas impartidas por profesor (curso académico)",
//                        "Lista las asignaturas que imparte cada profesor en un curso académico.",
//                        CypherQueries.Q2_ASIGNATURAS_POR_PROFESOR,
//                        List.of(new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"))
//                ),
//                new QueryDefinition(
//                        3,
//                        "Diferencia: alumnos en RI sin BD aprobada",
//                        "Devuelve alumnos matriculados en RI (en un curso académico) que NO tienen BD aprobada.",
//                        CypherQueries.Q3_RI_SIN_BD_APROBADA,
//                        List.of(new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"))
//                ),
                new QueryDefinition(
                        4,
                        "Asignaturas con mínimo de alumnos",
                        "Devuelve asignaturas con al menos N alumnos matriculados en un curso académico.",
                        CypherQueries.Q4_ASIGNATURAS_MIN_ALUMNOS,
                        List.of(
                                new ParamDefinition("cursoAcademico", "Curso académico", STRING, "2024/2025"),
                                new ParamDefinition("minAlumnos", "Mínimo de alumnos", INT, "3")
                        )
                ),
                new QueryDefinition(
                        5,
                        "Asignaturas prerrequisito para RI",
                        "Obtiene prerrequisitos directos e indirectos (y saltos mínimos) para Repositorios de Información.",
                        CypherQueries.Q5_PRERREQUISITOS_RI_CIERRE_TRANSITIVO,
                        List.of()
//                ),
//                new QueryDefinition(
//                        6,
//                        "Avanzada: caminos mínimos en amistad (hasta 2 saltos)",
//                        "Para cada alumno, lista candidatos conectados por amistad con distancia 1 o 2.",
//                        CypherQueries.Q6_CAMINOS_MINIMOS_AMISTAD_HASTA_2,
//                        List.of()
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

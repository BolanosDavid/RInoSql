package service;

public final class CypherQueries {
    private CypherQueries() {}

    public static final String CREATE_SCRIPT_RESOURCE = "scripts/create.cypher";

    // Elemental
    public static final String Q1_ALUMNOS_POR_ASIGNATURA =
            """
            MATCH (:Alumno)-[:MATRICULADO_EN {cursoAcademico:$cursoAcademico}]->(s:Asignatura)
            RETURN s.nombre AS asignatura, count(*) AS num_alumnos
            ORDER BY num_alumnos DESC, asignatura
            """;

    public static final String Q2_ASIGNATURAS_POR_PROFESOR =
            """
            MATCH (p:Profesor)-[r:IMPARTE {cursoAcademico:$cursoAcademico}]->(s:Asignatura)
            RETURN p.nombre AS profesor, s.nombre AS asignatura, r.grupo AS grupo
            ORDER BY profesor, asignatura
            """;

    // Intermediate
    public static final String Q3_RI_SIN_BD_APROBADA =
            """
            MATCH (a:Alumno)-[:MATRICULADO_EN {cursoAcademico:$cursoAcademico}]->(:Asignatura {codigo:'RI'})
            WHERE NOT EXISTS {
              MATCH (a)-[:MATRICULADO_EN {estado:'APROBADA'}]->(:Asignatura {codigo:'BD'})
            }
            RETURN a.uo AS uo, a.nombre AS alumno
            ORDER BY alumno
            """;

    public static final String Q4_ASIGNATURAS_MIN_ALUMNOS =
            """
            MATCH (:Alumno)-[:MATRICULADO_EN {cursoAcademico:$cursoAcademico}]->(s:Asignatura)
            WITH s, count(*) AS num_alumnos
            WHERE num_alumnos >= $minAlumnos
            RETURN s.nombre AS asignatura, num_alumnos
            ORDER BY num_alumnos DESC, asignatura
            """;

    // Advanced
    public static final String Q5_PRERREQUISITOS_RI_CIERRE_TRANSITIVO =
            """
            MATCH (target:Asignatura {codigo:'RI'})
            MATCH p = (pre:Asignatura)-[:ES_PRERREQUISITO_DE*1..]->(target)
            RETURN pre.nombre AS prerequisito, min(length(p)) AS saltos_minimos
            ORDER BY saltos_minimos, prerequisito
            """;

    public static final String Q6_CAMINOS_MINIMOS_AMISTAD_HASTA_2 =
            """
            MATCH (a:Alumno)
            MATCH (b:Alumno)
            WHERE a <> b
            MATCH p = shortestPath( (a)-[:AMIGO_DE*..2]-(b) )
            RETURN a.nombre AS alumno, b.nombre AS candidato, length(p) AS distancia
            ORDER BY alumno, distancia, candidato
            """;
}

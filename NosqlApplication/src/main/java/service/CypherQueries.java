package service;

public final class CypherQueries {
    private CypherQueries() {}

    public static final String CREATE_SCRIPT_RESOURCE = "scripts/create.cypher";

    public static final String Q1_ALUMNOS_POR_ASIGNATURA =
            """
            MATCH (:Alumno)-[:MATRICULADO_EN {cursoAcademico:$cursoAcademico}]->(s:Asignatura)
            RETURN s.nombre AS asignatura, count(*) AS num_alumnos
            ORDER BY num_alumnos DESC, asignatura
            """;
    
    public static final String Q2_PAREJAS_ASIGNATURAS_PRERREQUISITO = 
            """ 
            MATCH (pre:Asignatura)-[:ES_PRERREQUISITO_DE]->(post:Asignatura)
            RETURN pre.nombre  AS asignatura_prerrequisito, post.nombre AS asignatura_posterior
            ORDER BY asignatura_prerrequisito, asignatura_posterior;

            """;
    
    public static final String Q3_ASIGNATURAS_MIN_ALUMNOS =
            """
            MATCH (:Alumno)-[:MATRICULADO_EN {cursoAcademico:$cursoAcademico}]->(s:Asignatura)
            WITH s, count(*) AS num_alumnos
            WHERE num_alumnos >= $minAlumnos
            RETURN s.nombre AS asignatura, num_alumnos
            ORDER BY num_alumnos DESC, asignatura
            """;
    
    public static final String Q4_SDI_SIN_BD_APROBADA = 
            """
            MATCH (a:Alumno)-[:MATRICULADO_EN {cursoAcademico:'2024/2025'}]->(sdi:Asignatura {codigo:'SDI'})
            OPTIONAL MATCH (a)-[mBD:MATRICULADO_EN]->(bd:Asignatura {codigo:'BD'})
            WHERE mBD.estado = 'APROBADA' AND mBD.cursoAcademico < '2024/2025'
            WITH a, sdi, mBD
            WHERE mBD IS NULL
            RETURN a.uo     AS uo_alumno,
                a.nombre AS nombre_alumno,
                sdi.nombre AS asignatura
           ORDER BY uo_alumno;
            """;

    public static final String Q5_PRERREQUISITOS_RI_CIERRE_TRANSITIVO =
            """
        MATCH (target:Asignatura {codigo:'RI'})
		MATCH p = (pre:Asignatura)-[:ES_PRERREQUISITO_DE*1..]->(target)
		WITH pre, target, min(length(p)) AS d
		MATCH p2 = shortestPath( (pre)-[:ES_PRERREQUISITO_DE*..]->(target) )
		WHERE length(p2) = d
		RETURN pre.nombre AS prerequisito, d AS saltos, [n IN nodes(p2) | n.codigo] AS ruta_codigos
		ORDER BY saltos, prerequisito;

            """;

    public static final String Q6_ASIGNATURAS_FUTURAS_UO011 = 
        """
        MATCH (al:Alumno {uo:'UO011'})-[m:MATRICULADO_EN]->(aAprobada:Asignatura)
        WHERE m.estado = 'APROBADA'
        MATCH p = (aAprobada)-[:ES_PRERREQUISITO_DE*1..]->(aFutura:Asignatura)
        WITH al, aAprobada, aFutura, p,
             [n IN nodes(p) | n.codigo] AS ruta_codigos,
             length(p) AS saltos
        OPTIONAL MATCH (al)-[m2:MATRICULADO_EN]->(aFutura)
        WHERE m2 IS NULL
        RETURN al.uo AS alumno,
               aAprobada.codigo AS origen,
               aFutura.codigo AS destino,
               saltos,
               ruta_codigos
        ORDER BY alumno, saltos, origen, destino
        """;


}

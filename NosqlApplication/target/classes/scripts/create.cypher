MATCH (n) DETACH DELETE n;

CREATE CONSTRAINT alumno_uo IF NOT EXISTS
FOR (a:Alumno) REQUIRE a.uo IS UNIQUE;

CREATE CONSTRAINT profesor_id IF NOT EXISTS
FOR (p:Profesor) REQUIRE p.id IS UNIQUE;

CREATE CONSTRAINT asignatura_codigo IF NOT EXISTS
FOR (s:Asignatura) REQUIRE s.codigo IS UNIQUE;

UNWIND [
  {uo:'UO001', nombre:'Ana García',      actualYear:1},
  {uo:'UO002', nombre:'Pablo López',     actualYear:1},
  {uo:'UO003', nombre:'Lucía Fernández', actualYear:1},
  {uo:'UO004', nombre:'Marcos Pérez',    actualYear:2},
  {uo:'UO005', nombre:'Carla Ruiz',      actualYear:2},
  {uo:'UO006', nombre:'Diego Alonso',    actualYear:2},
  {uo:'UO007', nombre:'Sara Gómez',      actualYear:3},
  {uo:'UO008', nombre:'Javier Martín',   actualYear:3},
  {uo:'UO009', nombre:'Elena Díaz',      actualYear:3},
  {uo:'UO010', nombre:'Raúl Sánchez',    actualYear:4},
  {uo:'UO011', nombre:'Noa Vega',        actualYear:4},
  {uo:'UO012', nombre:'Hugo Castro',     actualYear:4}
] AS r
MERGE (a:Alumno {uo:r.uo})
SET a.nombre = r.nombre,
    a.actualYear = r.actualYear;

UNWIND [
  {id:'PR01', nombre:'Dario Álvarez', departamento:'Informática'},
  {id:'PR02', nombre:'Marta Suárez',  departamento:'Matemáticas'}
] AS r
MERGE (p:Profesor {id:r.id})
SET p.nombre = r.nombre,
    p.departamento = r.departamento;

UNWIND [
  {codigo:'IP',  nombre:'Introducción a la Programación',   ects:6, curso:1, cuatrimestre:1},
  {codigo:'MP',  nombre:'Metodología de la Programación',   ects:6, curso:1, cuatrimestre:2},
  {codigo:'ED',  nombre:'Estructuras de Datos',             ects:6, curso:2, cuatrimestre:1},
  {codigo:'BD',  nombre:'Bases de Datos',                   ects:6, curso:2, cuatrimestre:2},
  {codigo:'RI',  nombre:'Repositorios de Información',      ects:6, curso:3, cuatrimestre:1},
  {codigo:'SDI', nombre:'Sistemas Distribuidos e Internet', ects:6, curso:3, cuatrimestre:2}
] AS r
MERGE (s:Asignatura {codigo:r.codigo})
SET s.nombre = r.nombre,
    s.ects = r.ects,
    s.curso = r.curso,
    s.cuatrimestre = r.cuatrimestre;
UNWIND [
  {profId:'PR01', asig:'BD',  cursoAcademico:'2024/2025', grupo:'A'},
  {profId:'PR01', asig:'RI',  cursoAcademico:'2024/2025', grupo:'A'},
  {profId:'PR01', asig:'SDI', cursoAcademico:'2024/2025', grupo:'A'},
  {profId:'PR02', asig:'IP',  cursoAcademico:'2024/2025', grupo:'B'},
  {profId:'PR02', asig:'MP',  cursoAcademico:'2024/2025', grupo:'B'},
  {profId:'PR02', asig:'ED',  cursoAcademico:'2024/2025', grupo:'B'}
] AS r
MATCH (p:Profesor {id:r.profId})
MATCH (s:Asignatura {codigo:r.asig})
MERGE (p)-[:IMPARTE {cursoAcademico:r.cursoAcademico, grupo:r.grupo}]->(s);

UNWIND [
  {pre:'IP',  post:'MP'},
  {pre:'MP',  post:'ED'},
  {pre:'ED',  post:'BD'},
  {pre:'BD',  post:'RI'},
  {pre:'ED',  post:'RI'},
  {pre:'ED',  post:'SDI'},
  {pre:'BD',  post:'SDI'},
  {pre:'RI',  post:'SDI'}
] AS r
MATCH (a:Asignatura {codigo:r.pre})
MATCH (b:Asignatura {codigo:r.post})
MERGE (a)-[:ES_PRERREQUISITO_DE]->(b);

UNWIND [
  {a:'UO001', b:'UO002'},
  {a:'UO001', b:'UO003'},
  {a:'UO002', b:'UO004'},
  {a:'UO003', b:'UO005'},
  {a:'UO004', b:'UO005'},
  {a:'UO006', b:'UO007'},
  {a:'UO007', b:'UO008'},
  {a:'UO008', b:'UO009'},
  {a:'UO009', b:'UO010'},
  {a:'UO011', b:'UO012'}
] AS r
MATCH (x:Alumno {uo:r.a})
MATCH (y:Alumno {uo:r.b})
MERGE (x)-[:AMIGO_DE]->(y);

UNWIND [
  {uo:'UO001', asig:'IP',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO002', asig:'IP',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO003', asig:'IP',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},

  {uo:'UO004', asig:'ED',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO005', asig:'ED',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO006', asig:'BD',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},

  {uo:'UO007', asig:'RI',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO008', asig:'RI',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO009', asig:'SDI', cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},

  {uo:'UO010', asig:'SDI', cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO011', asig:'RI',  cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},
  {uo:'UO012', asig:'SDI', cursoAcademico:'2024/2025', estado:'CURSANDO', nota:null, convocatoria:1},

  {uo:'UO010', asig:'BD',  cursoAcademico:'2023/2024', estado:'APROBADA', nota:7.8, convocatoria:1},
  {uo:'UO010', asig:'ED',  cursoAcademico:'2022/2023', estado:'APROBADA', nota:6.5, convocatoria:2},
  {uo:'UO011', asig:'BD',  cursoAcademico:'2023/2024', estado:'APROBADA', nota:8.0, convocatoria:1},
  {uo:'UO012', asig:'BD',  cursoAcademico:'2023/2024', estado:'SUSPENSA', nota:4.2, convocatoria:1}
] AS r
MATCH (a:Alumno {uo:r.uo})
MATCH (s:Asignatura {codigo:r.asig})
MERGE (a)-[rel:MATRICULADO_EN {cursoAcademico:r.cursoAcademico}]->(s)
SET rel.estado = r.estado,
    rel.nota = r.nota,
    rel.convocatoria = r.convocatoria;
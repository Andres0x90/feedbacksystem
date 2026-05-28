-- ============================================================
-- V2: Datos de prueba mínimos - Sprint 1 (HU-01)
-- ============================================================

-- Rúbrica de prueba: Programación Orientada a Objetos
INSERT INTO rubrica (id, nombre, descripcion, version, activa, fecha_creacion)
VALUES (
    '11111111-0000-0000-0000-000000000001',
    'Rúbrica POO - Trabajo Final',
    'Evaluación de trabajo final del módulo de Programación Orientada a Objetos',
    1, TRUE, NOW()
);

-- Criterios (suman 100%)
INSERT INTO criterio (id, rubrica_id, nombre, descripcion, peso, orden, fecha_creacion) VALUES
(
    '22222222-0000-0000-0000-000000000001',
    '11111111-0000-0000-0000-000000000001',
    'Aplicación de Herencia y Polimorfismo',
    'Uso correcto de herencia, polimorfismo e interfaces',
    30.00, 1, NOW()
),
(
    '22222222-0000-0000-0000-000000000002',
    '11111111-0000-0000-0000-000000000001',
    'Encapsulamiento y Principios SOLID',
    'Correcta aplicación de encapsulamiento y principios de diseño',
    25.00, 2, NOW()
),
(
    '22222222-0000-0000-0000-000000000003',
    '11111111-0000-0000-0000-000000000001',
    'Calidad del Código y Documentación',
    'Legibilidad, comentarios y documentación del código',
    20.00, 3, NOW()
),
(
    '22222222-0000-0000-0000-000000000004',
    '11111111-0000-0000-0000-000000000001',
    'Funcionalidad y Pruebas',
    'El sistema funciona correctamente y tiene pruebas unitarias',
    25.00, 4, NOW()
);

-- Niveles de desempeño para criterio 1
INSERT INTO nivel_desempeno (id, criterio_id, nombre, descripcion, puntaje_min, puntaje_max, fecha_creacion) VALUES
('33333333-0000-0000-0000-000000000001', '22222222-0000-0000-0000-000000000001', 'Excelente', 'Aplica correctamente todos los conceptos de herencia y polimorfismo', 4.5, 5.0, NOW()),
('33333333-0000-0000-0000-000000000002', '22222222-0000-0000-0000-000000000001', 'Bueno', 'Aplica la mayoría de conceptos con errores menores', 3.5, 4.4, NOW()),
('33333333-0000-0000-0000-000000000003', '22222222-0000-0000-0000-000000000001', 'Básico', 'Aplica los conceptos de forma básica con errores relevantes', 3.0, 3.4, NOW()),
('33333333-0000-0000-0000-000000000004', '22222222-0000-0000-0000-000000000001', 'Insuficiente', 'No aplica correctamente los conceptos', 0.0, 2.9, NOW());

-- Niveles para criterio 2
INSERT INTO nivel_desempeno (id, criterio_id, nombre, descripcion, puntaje_min, puntaje_max, fecha_creacion) VALUES
('33333333-0000-0000-0000-000000000005', '22222222-0000-0000-0000-000000000002', 'Excelente', 'Aplica correctamente los principios SOLID y encapsulamiento', 4.5, 5.0, NOW()),
('33333333-0000-0000-0000-000000000006', '22222222-0000-0000-0000-000000000002', 'Bueno', 'Aplica la mayoría con errores menores', 3.5, 4.4, NOW()),
('33333333-0000-0000-0000-000000000007', '22222222-0000-0000-0000-000000000002', 'Básico', 'Encapsulamiento básico, sin principios SOLID', 3.0, 3.4, NOW()),
('33333333-0000-0000-0000-000000000008', '22222222-0000-0000-0000-000000000002', 'Insuficiente', 'No aplica encapsulamiento ni principios', 0.0, 2.9, NOW());

-- Plantilla de retroalimentación
INSERT INTO plantilla_retroalimentacion (id, nombre, objetivo, contenido_base, version, activa, fecha_creacion)
VALUES (
    'aaaaaaaa-0000-0000-0000-000000000001',
    'Plantilla Retroalimentación POO',
    'Orientar al estudiante sobre sus fortalezas y áreas de mejora en POO',
    'Estimado estudiante, a continuación encontrará la retroalimentación sobre su trabajo...',
    1, TRUE, NOW()
);

-- Actividad evaluativa de referencia
INSERT INTO actividad_evaluativa_referencia (id, rubrica_id, plantilla_id, titulo, descripcion, fecha, ponderacion, estado, fecha_creacion)
VALUES (
    'bbbbbbbb-0000-0000-0000-000000000001',
    '11111111-0000-0000-0000-000000000001',
    'aaaaaaaa-0000-0000-0000-000000000001',
    'Trabajo Final - Sistema de Biblioteca POO',
    'Implementación de un sistema de biblioteca usando principios de POO',
    '2026-05-20',
    40.00,
    'ACTIVA',
    NOW()
);

-- Calificaciones de referencia (referenciaEstudiante son seudónimos)
INSERT INTO calificacion_referencia (id, actividad_evaluativa_id, referencia_estudiante, valor_numerico, observacion_general, fecha_registro)
VALUES
(
    'cccccccc-0000-0000-0000-000000000001',
    'bbbbbbbb-0000-0000-0000-000000000001',
    'EST-2026-001',
    4.2,
    'Buen dominio de los conceptos. Mejorar pruebas unitarias.',
    NOW()
),
(
    'cccccccc-0000-0000-0000-000000000002',
    'bbbbbbbb-0000-0000-0000-000000000001',
    'EST-2026-002',
    3.5,
    'Concepto básico aplicado. Requiere refuerzo en principios SOLID.',
    NOW()
),
(
    'cccccccc-0000-0000-0000-000000000003',
    'bbbbbbbb-0000-0000-0000-000000000001',
    'EST-2026-003',
    4.8,
    'Excelente trabajo. Aplicación ejemplar de todos los conceptos.',
    NOW()
);

-- Feedback para EST-2026-001
INSERT INTO feedback (id, calificacion_referencia_id, resumen_general, fecha_publicacion, estado, fecha_creacion)
VALUES (
    'dddddddd-0000-0000-0000-000000000001',
    'cccccccc-0000-0000-0000-000000000001',
    'El estudiante demuestra buen manejo de herencia y polimorfismo. Se recomienda fortalecer la escritura de pruebas unitarias.',
    NOW(),
    'PUBLICADO',
    NOW()
);

-- Comentarios por criterio para el feedback
INSERT INTO comentario_criterio (id, feedback_id, criterio_id, comentario, fortaleza, oportunidad_mejora) VALUES
(
    'eeeeeeee-0000-0000-0000-000000000001',
    'dddddddd-0000-0000-0000-000000000001',
    '22222222-0000-0000-0000-000000000001',
    'Buena aplicación de herencia. El polimorfismo podría mejorarse.',
    'Uso correcto de clases abstractas e interfaces',
    'Implementar más métodos sobrecargados para demostrar polimorfismo'
),
(
    'eeeeeeee-0000-0000-0000-000000000002',
    'dddddddd-0000-0000-0000-000000000001',
    '22222222-0000-0000-0000-000000000002',
    'Encapsulamiento aplicado correctamente. SOLID parcialmente implementado.',
    'Getters y setters bien definidos, atributos privados',
    'Estudiar y aplicar principios de inversión de dependencias'
);

-- Perfil académico básico para EST-2026-001
INSERT INTO perfil_academico_basico (id, referencia_estudiante, fortalezas, brechas, tendencia, fecha_actualizacion)
VALUES (
    'ffffffff-0000-0000-0000-000000000001',
    'EST-2026-001',
    '• Uso correcto de clases abstractas e interfaces' || chr(10) || '• Getters y setters bien definidos, atributos privados',
    '• Implementar más métodos sobrecargados para demostrar polimorfismo' || chr(10) || '• Estudiar y aplicar principios de inversión de dependencias',
    'ESTABLE',
    NOW()
);

-- Recomendación para EST-2026-001
INSERT INTO recomendacion (id, perfil_academico_id, tipo, descripcion, prioridad, estado, fecha_creacion)
VALUES (
    'a1a1a1a1-0000-0000-0000-000000000001',
    'ffffffff-0000-0000-0000-000000000001',
    'MEJORA',
    'Reforzar conocimientos en principios SOLID e implementación de pruebas unitarias con JUnit.',
    'MEDIA',
    'ACTIVA',
    NOW()
);

-- Registro inicial de auditoría
INSERT INTO auditoria_cambio (id, entidad, entidad_id, accion, fecha, detalle) VALUES
('b1b1b1b1-0000-0000-0000-000000000001', 'Rubrica', '11111111-0000-0000-0000-000000000001', 'CREAR', NOW(), 'Datos de prueba iniciales cargados - Sprint 1'),
('b1b1b1b1-0000-0000-0000-000000000002', 'PlantillaRetroalimentacion', 'aaaaaaaa-0000-0000-0000-000000000001', 'CREAR', NOW(), 'Datos de prueba iniciales cargados - Sprint 1'),
('b1b1b1b1-0000-0000-0000-000000000003', 'ActividadEvaluativaReferencia', 'bbbbbbbb-0000-0000-0000-000000000001', 'CREAR', NOW(), 'Datos de prueba iniciales cargados - Sprint 1');

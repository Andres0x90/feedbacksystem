-- ============================================================
-- V1: Esquema inicial - API de Analítica de Aprendizaje TdeA
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Rúbricas de evaluación
CREATE TABLE rubrica (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre           VARCHAR(255) NOT NULL,
    descripcion      TEXT,
    version          INTEGER      NOT NULL DEFAULT 1,
    activa           BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion   TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP
);

-- Criterios de evaluación (N por rúbrica)
CREATE TABLE criterio (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rubrica_id          UUID         NOT NULL REFERENCES rubrica(id) ON DELETE CASCADE,
    nombre              VARCHAR(255) NOT NULL,
    descripcion         TEXT,
    peso                DECIMAL(5,2) NOT NULL CHECK (peso > 0 AND peso <= 100),
    orden               INTEGER      NOT NULL CHECK (orden >= 1),
    fecha_creacion      TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_criterio_rubrica_id ON criterio(rubrica_id);

-- Niveles de desempeño por criterio
CREATE TABLE nivel_desempeno (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    criterio_id         UUID         NOT NULL REFERENCES criterio(id) ON DELETE CASCADE,
    nombre              VARCHAR(255) NOT NULL,
    descripcion         TEXT,
    puntaje_min         DECIMAL(5,2) NOT NULL CHECK (puntaje_min >= 0),
    puntaje_max         DECIMAL(5,2) NOT NULL CHECK (puntaje_max >= 0),
    fecha_creacion      TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP,
    CONSTRAINT chk_puntaje_rango CHECK (puntaje_min <= puntaje_max)
);

CREATE INDEX idx_nivel_criterio_id ON nivel_desempeno(criterio_id);

-- Plantillas de retroalimentación
CREATE TABLE plantilla_retroalimentacion (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre         VARCHAR(255) NOT NULL,
    objetivo       TEXT,
    contenido_base TEXT,
    version        INTEGER      NOT NULL DEFAULT 1,
    activa         BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Actividades evaluativas de referencia
CREATE TABLE actividad_evaluativa_referencia (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rubrica_id     UUID         NOT NULL REFERENCES rubrica(id),
    plantilla_id   UUID         REFERENCES plantilla_retroalimentacion(id),
    titulo         VARCHAR(255) NOT NULL,
    descripcion    TEXT,
    fecha          DATE,
    ponderacion    DECIMAL(5,2) CHECK (ponderacion >= 0 AND ponderacion <= 100),
    estado         VARCHAR(50)  NOT NULL DEFAULT 'ACTIVA',
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP
);

CREATE INDEX idx_actividad_rubrica_id ON actividad_evaluativa_referencia(rubrica_id);
CREATE INDEX idx_actividad_estado ON actividad_evaluativa_referencia(estado);

-- Calificaciones de referencia por estudiante y actividad
CREATE TABLE calificacion_referencia (
    id                   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    actividad_evaluativa_id UUID        NOT NULL REFERENCES actividad_evaluativa_referencia(id),
    referencia_estudiante   VARCHAR(255) NOT NULL,
    valor_numerico          DECIMAL(4,2) NOT NULL CHECK (valor_numerico >= 0 AND valor_numerico <= 5),
    observacion_general     TEXT,
    fecha_registro          TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_calificacion_actividad_estudiante UNIQUE (actividad_evaluativa_id, referencia_estudiante)
);

CREATE INDEX idx_calificacion_estudiante ON calificacion_referencia(referencia_estudiante);
CREATE INDEX idx_calificacion_actividad ON calificacion_referencia(actividad_evaluativa_id);

-- Feedback asociado a cada calificación (relación 1:1)
CREATE TABLE feedback (
    id                        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    calificacion_referencia_id UUID        NOT NULL UNIQUE REFERENCES calificacion_referencia(id),
    resumen_general            TEXT,
    fecha_publicacion          TIMESTAMP,
    estado                     VARCHAR(50) NOT NULL DEFAULT 'BORRADOR',
    fecha_creacion             TIMESTAMP   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_feedback_estado ON feedback(estado);

-- Comentarios por criterio dentro de un feedback
CREATE TABLE comentario_criterio (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    feedback_id      UUID NOT NULL REFERENCES feedback(id) ON DELETE CASCADE,
    criterio_id      UUID NOT NULL REFERENCES criterio(id),
    comentario       TEXT,
    fortaleza        TEXT,
    oportunidad_mejora TEXT
);

CREATE INDEX idx_comentario_feedback_id ON comentario_criterio(feedback_id);
CREATE INDEX idx_comentario_criterio_id ON comentario_criterio(criterio_id);

-- Perfil académico básico del estudiante (identificado externamente)
CREATE TABLE perfil_academico_basico (
    id                    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    referencia_estudiante VARCHAR(255) NOT NULL UNIQUE,
    fortalezas            TEXT,
    brechas               TEXT,
    tendencia             VARCHAR(50),
    fecha_actualizacion   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_perfil_referencia ON perfil_academico_basico(referencia_estudiante);

-- Recomendaciones generadas del perfil
CREATE TABLE recomendacion (
    id                 UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    perfil_academico_id UUID         NOT NULL REFERENCES perfil_academico_basico(id) ON DELETE CASCADE,
    tipo               VARCHAR(100),
    descripcion        TEXT,
    prioridad          VARCHAR(50),
    estado             VARCHAR(50)  DEFAULT 'ACTIVA',
    fecha_creacion     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_recomendacion_perfil ON recomendacion(perfil_academico_id);

-- Reportes básicos generados on-demand
CREATE TABLE reporte_basico (
    id                  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    perfil_academico_id UUID         REFERENCES perfil_academico_basico(id),
    tipo                VARCHAR(100) NOT NULL,
    rango_fechas        VARCHAR(255),
    total_feedbacks     INTEGER      DEFAULT 0,
    indicadores         TEXT,
    fecha_generacion    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Auditoría de cambios (append-only)
CREATE TABLE auditoria_cambio (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    entidad    VARCHAR(100) NOT NULL,
    entidad_id UUID,
    accion     VARCHAR(100) NOT NULL,
    fecha      TIMESTAMP    NOT NULL DEFAULT NOW(),
    detalle    TEXT
);

CREATE INDEX idx_auditoria_entidad ON auditoria_cambio(entidad, entidad_id);
CREATE INDEX idx_auditoria_fecha ON auditoria_cambio(fecha DESC);

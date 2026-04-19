CREATE TABLE usuario(
    id VARCHAR(255) PRIMARY KEY ,
    id_auth0 VARCHAR(255) UNIQUE ,
    nombre VARCHAR(255) ,
    apellido VARCHAR(255)  ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    fecha_registro TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
)
CREATE TABLE revinfo (
    rev SERIAL PRIMARY KEY,
    revtstamp BIGINT
);

CREATE TABLE usuario_aud (
    id VARCHAR(255),
    rev INTEGER,
    revtype SMALLINT,
    id_auth0 VARCHAR(255),
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    email VARCHAR(255),
    fecha_registro TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_usuarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

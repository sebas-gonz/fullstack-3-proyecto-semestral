CREATE TABLE usuario(
    id VARCHAR(255) PRIMARY KEY ,
    id_auth0 VARCHAR(255) NOT NULL UNIQUE ,
    nombre VARCHAR(255) ,
    apellido VARCHAR(255)  ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    rol VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP NOT NULL,
    fecha_modificacion TIMESTAMP
);
CREATE SEQUENCE revinfo_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE revinfo (
    rev INTEGER PRIMARY KEY,
    revtstmp BIGINT
);

CREATE TABLE usuario_aud (
    id VARCHAR(255),
    rev INTEGER,
    revtype SMALLINT,
    id_auth0 VARCHAR(255),
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    email VARCHAR(255),
    rol VARCHAR(255),
    fecha_registro TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_usuarios_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

CREATE TABLE ubicacion (
   id VARCHAR(255) PRIMARY KEY,
   calle VARCHAR(255) NOT NULL ,
   numero VARCHAR(255) NOT NULL ,
   ciudad VARCHAR(255) NOT NULL ,
   pais VARCHAR(255),
   latitude NUMERIC(19, 15) NOT NULL ,
   longitude NUMERIC(19, 15) NOT NULL ,
   fecha_registro TIMESTAMP NOT NULL,
   fecha_modificacion TIMESTAMP,
   usuario_id VARCHAR(255),
   CONSTRAINT fk_direccion_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id)
);

CREATE TABLE direccion_aud (
   id VARCHAR(255),
   rev INTEGER,
   revtype SMALLINT,
   calle VARCHAR(255),
   numero VARCHAR(255),
   ciudad VARCHAR(255),
   pais VARCHAR(255),
   latitude NUMERIC(19, 15),
   longitude NUMERIC(19, 15),
   fecha_registro TIMESTAMP,
   fecha_modificacion TIMESTAMP,
   usuario_id VARCHAR(255),
   PRIMARY KEY (id, rev),
   CONSTRAINT fk_direccion_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);
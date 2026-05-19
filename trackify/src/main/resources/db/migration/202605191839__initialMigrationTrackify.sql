CREATE TABLE contenido
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    titulo      VARCHAR(255) NOT NULL,
    tipo        VARCHAR(255) NOT NULL,
    genero      VARCHAR(255) NOT NULL,
    estado      VARCHAR(255) NOT NULL,
    valoracion  INT NULL,
    descripcion VARCHAR(255) NULL,
    usuario_id  BIGINT       NOT NULL,
    CONSTRAINT pk_contenido PRIMARY KEY (id)
);

CREATE TABLE `role`
(
    id  BIGINT AUTO_INCREMENT NOT NULL,
    rol VARCHAR(255) NOT NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id     BIGINT NOT NULL,
    user_codigo BIGINT NOT NULL
);

CREATE TABLE usuario
(
    codigo         BIGINT AUTO_INCREMENT NOT NULL,
    nombre_usuario VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    fecha_creacion datetime     NOT NULL,
    email          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (codigo)
);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_email UNIQUE (email);

ALTER TABLE usuario
    ADD CONSTRAINT uc_usuario_nombreusuario UNIQUE (nombre_usuario);

ALTER TABLE contenido
    ADD CONSTRAINT FK_CONTENIDO_ON_USUARIO FOREIGN KEY (usuario_id) REFERENCES usuario (codigo);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_usuario FOREIGN KEY (user_codigo) REFERENCES usuario (codigo);
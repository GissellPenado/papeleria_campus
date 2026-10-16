CREATE DATABASE papeleria_campus

CREATE TABLE producto (
                          id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                          codigo VARCHAR(10) NOT NULL UNIQUE,
                          nombre VARCHAR(80) NOT NULL,
                          categoria VARCHAR(20) NOT NULL,
                          precio NUMERIC(10, 2) NOT NULL,
                          stock INTEGER NOT NULL,

                          CONSTRAINT chk_codigo CHECK (
                              char_length(codigo) BETWEEN 1 AND 10
                                  AND codigo = btrim(codigo)
                                  AND codigo = upper(codigo)
                              ),

                          CONSTRAINT chk_nombre CHECK (
                              char_length(btrim(nombre)) BETWEEN 3 AND 80
                              ),

                          CONSTRAINT chk_categoria CHECK (
                              categoria IN ('Papelería', 'Tecnología', 'Accesorios')
                              ),

                          CONSTRAINT chk_precio CHECK (precio > 0),

                          CONSTRAINT chk_stock CHECK (stock >= 0)
);

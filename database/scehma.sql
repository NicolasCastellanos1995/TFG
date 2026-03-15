
CREATE TYPE role_name AS ENUM (
    'admin',
    'supervisor',
    'operario'
);

CREATE TYPE movement_type AS ENUM (
    'IN',
    'OUT',
    'ADJUST',
    'TRANSFER'
);

CREATE TYPE location_criticality AS ENUM (
    'alta',
    'media',
    'baja'
);


CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    role role_name NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);



CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    criticality location_criticality,
    coord_x NUMERIC(10,2),
    coord_y NUMERIC(10,2),
    coord_z NUMERIC(10,2)
);



CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    category_id INT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    min_stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id)
        REFERENCES categories(id)
);


CREATE TABLE stock (
    id SERIAL PRIMARY KEY,
    location_id INT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    quantity INT NOT NULL,
    product_id INT NOT NULL,
    lot_code VARCHAR(50),
    expiration_date DATE,

    FOREIGN KEY (location_id)
        REFERENCES locations(id),

    FOREIGN KEY (product_id)
        REFERENCES products(id)
);


CREATE TABLE inventory_movements (
    id SERIAL PRIMARY KEY,
    type movement_type NOT NULL,
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cancelled_by INT,
    cancelled_at TIMESTAMP,

    FOREIGN KEY (created_by)
        REFERENCES users(id),

    FOREIGN KEY (cancelled_by)
        REFERENCES users(id)
);


CREATE TABLE inventory_movement_lines (
    id SERIAL PRIMARY KEY,
    movement_id INT NOT NULL,
    product_id INT NOT NULL,
    from_location_id INT,
    to_location_id INT,
    qty INT NOT NULL,

    FOREIGN KEY (movement_id)
        REFERENCES inventory_movements(id),

    FOREIGN KEY (product_id)
        REFERENCES products(id),

    FOREIGN KEY (from_location_id)
        REFERENCES locations(id),

    FOREIGN KEY (to_location_id)
        REFERENCES locations(id)
);


CREATE INDEX idx_movements_created
ON inventory_movements(created_at);

CREATE INDEX idx_movement_lines_movement
ON inventory_movement_lines(movement_id);
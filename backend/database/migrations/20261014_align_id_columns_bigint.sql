-- Apply once to an existing database created from the original 01-schema.sql.
-- This preserves row data while aligning SERIAL/INTEGER identifiers with JPA Long.
BEGIN;

ALTER TABLE products DROP CONSTRAINT products_category_id_fkey;
ALTER TABLE stock
    DROP CONSTRAINT stock_location_id_fkey,
    DROP CONSTRAINT stock_product_id_fkey;
ALTER TABLE inventory_movements
    DROP CONSTRAINT inventory_movements_created_by_fkey,
    DROP CONSTRAINT inventory_movements_cancelled_by_fkey;
ALTER TABLE inventory_movement_lines
    DROP CONSTRAINT inventory_movement_lines_movement_id_fkey,
    DROP CONSTRAINT inventory_movement_lines_product_id_fkey,
    DROP CONSTRAINT inventory_movement_lines_from_location_id_fkey,
    DROP CONSTRAINT inventory_movement_lines_to_location_id_fkey;

ALTER TABLE users ALTER COLUMN id TYPE BIGINT;
ALTER TABLE categories ALTER COLUMN id TYPE BIGINT;
ALTER TABLE locations ALTER COLUMN id TYPE BIGINT;
ALTER TABLE products
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN category_id TYPE BIGINT;
ALTER TABLE stock
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN location_id TYPE BIGINT,
    ALTER COLUMN product_id TYPE BIGINT;
ALTER TABLE inventory_movements
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN created_by TYPE BIGINT,
    ALTER COLUMN cancelled_by TYPE BIGINT;
ALTER TABLE inventory_movement_lines
    ALTER COLUMN id TYPE BIGINT,
    ALTER COLUMN movement_id TYPE BIGINT,
    ALTER COLUMN product_id TYPE BIGINT,
    ALTER COLUMN from_location_id TYPE BIGINT,
    ALTER COLUMN to_location_id TYPE BIGINT;

ALTER SEQUENCE users_id_seq AS BIGINT;
ALTER SEQUENCE categories_id_seq AS BIGINT;
ALTER SEQUENCE locations_id_seq AS BIGINT;
ALTER SEQUENCE products_id_seq AS BIGINT;
ALTER SEQUENCE stock_id_seq AS BIGINT;
ALTER SEQUENCE inventory_movements_id_seq AS BIGINT;
ALTER SEQUENCE inventory_movement_lines_id_seq AS BIGINT;

ALTER TABLE products
    ADD CONSTRAINT products_category_id_fkey
    FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE stock
    ADD CONSTRAINT stock_location_id_fkey
        FOREIGN KEY (location_id) REFERENCES locations(id),
    ADD CONSTRAINT stock_product_id_fkey
        FOREIGN KEY (product_id) REFERENCES products(id);
ALTER TABLE inventory_movements
    ADD CONSTRAINT inventory_movements_created_by_fkey
        FOREIGN KEY (created_by) REFERENCES users(id),
    ADD CONSTRAINT inventory_movements_cancelled_by_fkey
        FOREIGN KEY (cancelled_by) REFERENCES users(id);
ALTER TABLE inventory_movement_lines
    ADD CONSTRAINT inventory_movement_lines_movement_id_fkey
        FOREIGN KEY (movement_id) REFERENCES inventory_movements(id),
    ADD CONSTRAINT inventory_movement_lines_product_id_fkey
        FOREIGN KEY (product_id) REFERENCES products(id),
    ADD CONSTRAINT inventory_movement_lines_from_location_id_fkey
        FOREIGN KEY (from_location_id) REFERENCES locations(id),
    ADD CONSTRAINT inventory_movement_lines_to_location_id_fkey
        FOREIGN KEY (to_location_id) REFERENCES locations(id);

COMMIT;

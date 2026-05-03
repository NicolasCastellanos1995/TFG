-- =========================
-- USUARIOS
-- =========================

INSERT INTO users (username, email, password_hash, role, is_active)
VALUES (
    'admin',
    'admin@tfg.com',
    '$2a$10$bRq8l7t3FOhSPQS0dkzp7ulm8wnY2XzyMPRi9JCLtgt0k0S5wgauu',
    'admin',
    true
);

INSERT INTO users (username, email, password_hash, role, is_active)
VALUES (
    'supervisor',
    'supervisor@tfg.com',
    '$2a$10$oxTlbUj9fjLDBJkoYLFeEOKMaEie9OnggRe1Z3XxypsYRTZXPVwNe',
    'supervisor',
    true
);

INSERT INTO users (username, email, password_hash, role, is_active)
VALUES (
    'operario',
    'operario@tfg.com',
    '$2a$10$JhwL3iMztD25xFZXIKDGOeHLqII7vVEUGxYMxKngkdPwyv8IlzgDu',
    'operario',
    true
);

-- =========================
-- CATEGORÍAS
-- =========================
INSERT INTO categories (name) VALUES
('Alimentos'),
('Bebidas'),
('Limpieza'),
('Farmacia'),
('Electrónica')
ON CONFLICT (name) DO NOTHING;


-- =========================
-- UBICACIONES
-- =========================
INSERT INTO locations (code, name, criticality, coord_x, coord_y, coord_z) VALUES
('A01', 'Estantería A01', 'ALTA', 1, 1, 1),
('A02', 'Estantería A02', 'MEDIA', 2, 1, 1),
('B01', 'Zona Refrigerada B01', 'ALTA', 1, 2, 1),
('B02', 'Zona Refrigerada B02', 'MEDIA', 2, 2, 1),
('C01', 'Almacén Seco C01', 'BAJA', 1, 3, 1),
('C02', 'Almacén Seco C02', 'BAJA', 2, 3, 1)
ON CONFLICT (code) DO NOTHING;


-- =========================
-- PRODUCTOS
-- =========================
INSERT INTO products (sku, name, category_id, unit, min_stock, criticality)
VALUES
('SKU-001', 'Leche Entera 1L', (SELECT id FROM categories WHERE name = 'Alimentos'), 'unidad', 20, 'ALTA'),
('SKU-002', 'Yogur Natural', (SELECT id FROM categories WHERE name = 'Alimentos'), 'unidad', 30, 'MEDIA'),
('SKU-003', 'Agua Mineral 1.5L', (SELECT id FROM categories WHERE name = 'Bebidas'), 'unidad', 50, 'BAJA'),
('SKU-004', 'Detergente Líquido', (SELECT id FROM categories WHERE name = 'Limpieza'), 'unidad', 15, 'MEDIA'),
('SKU-005', 'Gel Desinfectante', (SELECT id FROM categories WHERE name = 'Farmacia'), 'unidad', 25, 'ALTA'),
('SKU-006', 'Pilas AA', (SELECT id FROM categories WHERE name = 'Electrónica'), 'pack', 10, 'MEDIA'),
('SKU-007', 'Arroz 1kg', (SELECT id FROM categories WHERE name = 'Alimentos'), 'unidad', 40, 'BAJA'),
('SKU-008', 'Zumo Naranja 1L', (SELECT id FROM categories WHERE name = 'Bebidas'), 'unidad', 20, 'MEDIA')
ON CONFLICT (sku) DO NOTHING;

INSERT INTO users (username, email, password_hash, role, is_active)
VALUES (
    'admin',
    'admin@tfg.com',
    '$2a$10$i4KaynFtsq/AFz/OVd0eouf9P6gHfshUTuawYzNbxwRxJBXqoXPEy',
    'admin',
    true
)
ON CONFLICT (username) DO NOTHING;

INSERT INTO users (email, password, name, role) VALUES
                                                    ('admin@mail.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 'Admin', 'ROLE_ADMIN'),
                                                    ('user@mail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'User One', 'ROLE_USER'),
                                                    ('test@mail.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'Test User', 'ROLE_USER');


INSERT INTO shared_files (original_name, stored_name, file_type, file_size, status, category, download_count, uploaded_at, user_id) VALUES
                                                                                                                                        ('document.pdf', 'doc_001.pdf', 'application/pdf', 204800, 'PUBLIC', 'Documents', 5, '2024-01-15 10:00:00', 2),
                                                                                                                                        ('photo.png', 'photo_001.png', 'image/png', 102400, 'PUBLIC', 'Images', 3, '2024-01-16 11:00:00', 2),
                                                                                                                                        ('archive.zip', 'arch_001.zip', 'application/zip', 512000, 'PRIVATE', 'Archives', 0, '2024-01-17 12:00:00', 2),
                                                                                                                                        ('notes.txt', 'notes_001.txt', 'text/plain', 1024, 'PUBLIC', 'Documents', 10, '2024-01-18 13:00:00', 3),
                                                                                                                                        ('data.csv', 'data_001.csv', 'text/csv', 20480, 'PRIVATE', 'Documents', 0, '2024-01-19 14:00:00', 3),
                                                                                                                                        ('video.mp4', 'video_001.mp4', 'video/mp4', 1048576, 'PUBLIC', 'Videos', 2, '2024-01-20 15:00:00', 3);
CREATE DATABASE IF NOT EXISTS vrtourism;
USE vrtourism;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS destinations (
    destination_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_name VARCHAR(100) NOT NULL,
    description TEXT,
    image_path VARCHAR(255),
    environment_type VARCHAR(50),
    location VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS vr_environments (
    vr_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_id INT,
    model_path VARCHAR(255),
    texture_path VARCHAR(255),
    map_size VARCHAR(50),
    FOREIGN KEY (destination_id) REFERENCES destinations(destination_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS packages (
    package_id INT AUTO_INCREMENT PRIMARY KEY,
    destination_id INT,
    package_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    duration VARCHAR(50),
    hotel_details TEXT,
    FOREIGN KEY (destination_id) REFERENCES destinations(destination_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    package_id INT,
    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES packages(package_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    destination_id INT,
    rating INT CHECK(rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_id) REFERENCES destinations(destination_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS favorites (
    favorite_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    destination_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_id) REFERENCES destinations(destination_id) ON DELETE CASCADE
);

-- Insert destination data
INSERT IGNORE INTO destinations (destination_id, destination_name, description, image_path, environment_type, location) VALUES
(1, 'Taj Mahal', 'Marvel at the breathtaking ivory-white marble mausoleum, a UNESCO World Heritage Site and one of the Seven Wonders of the World.', 'https://images.unsplash.com/photo-1564507592333-c60657eea523?w=800&q=80', 'Monument', 'Agra, India'),
(2, 'Eiffel Tower', 'Experience the iconic iron lattice tower standing tall over the romantic city of Paris, a symbol of French artistry and engineering.', 'https://images.unsplash.com/photo-1511739001486-6bfe10ce65f4?w=800&q=80', 'Monument', 'Paris, France'),
(3, 'Sydney Opera House', 'Explore the stunning architectural masterpiece on Sydney Harbour, one of the most iconic performing arts centres in the world.', 'https://images.unsplash.com/photo-1524293581917-878a6d017c71?w=800&q=80', 'Monument', 'Sydney, Australia'),
(4, 'Colosseum', 'Step inside the legendary Roman amphitheatre where gladiators once battled, an enduring symbol of Imperial Rome.', 'https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=800&q=80', 'Monument', 'Rome, Italy'),
(5, 'Pyramids of Giza', 'Stand before the last remaining wonder of the ancient world, the monumental pyramids rising from the Egyptian desert.', 'https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&q=80', 'Monument', 'Giza, Egypt'),
(6, 'Liberty Island', 'Visit the Statue of Liberty, a colossal neoclassical sculpture symbolizing freedom and democracy.', 'https://images.unsplash.com/photo-1485738422979-f5c462d49f04?w=800&q=80', 'Monument', 'New York, USA'),
(7, 'Space Loop City', 'Explore a futuristic orbital city with neon skywalks and zero-gravity plazas — a sci-fi dream brought to life.', 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&q=80', 'Futuristic', 'Outer Space');

INSERT IGNORE INTO vr_environments (vr_id, destination_id, model_path, texture_path, map_size) VALUES
(1, 1, '/models/taj_mahal.glb', NULL, 'Medium'),
(2, 2, '/models/low_poly_eiffel_tower.glb', NULL, 'Medium'),
(3, 3, '/models/sydney_opera_house.glb', NULL, 'Medium'),
(4, 4, '/models/previz-lowpoly_colosseum_with_concept_textures..glb', NULL, 'Medium'),
(5, 5, '/models/giza_pyramid_complex_from_civilization_vi.glb', NULL, 'Medium'),
(6, 6, '/models/liberty_island.glb', NULL, 'Large'),
(7, 7, '/models/space_loop_city.glb', NULL, 'Large');

INSERT IGNORE INTO packages (package_id, destination_id, package_name, price, duration, hotel_details) VALUES
(1, 1, 'Taj Mahal Heritage Tour', 800.00, '4 Days, 3 Nights', 'Heritage Hotel near Taj'),
(2, 2, 'Paris Romance Package', 1500.00, '5 Days, 4 Nights', 'Boutique Hotel near Eiffel Tower'),
(3, 3, 'Sydney Harbour Experience', 1400.00, '5 Days, 4 Nights', 'Harbour View Hotel'),
(4, 4, 'Roman Holiday', 1200.00, '5 Days, 4 Nights', 'Hotel near Colosseum'),
(5, 5, 'Egyptian Expedition', 1000.00, '6 Days, 5 Nights', 'Desert Luxury Resort'),
(6, 6, 'New York Explorer', 1800.00, '5 Days, 4 Nights', 'Manhattan Suite Hotel'),
(7, 7, 'Space Loop VR Experience', 500.00, '1 Day', 'Virtual Only - No Hotel');

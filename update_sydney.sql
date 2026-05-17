-- Run this in MySQL Workbench to replace Great Wall of China with Sydney Opera House
USE vrtourism;

-- Update destination ID 3: Great Wall → Sydney Opera House
UPDATE destinations SET
    destination_name = 'Sydney Opera House',
    description = 'Explore the stunning architectural masterpiece on Sydney Harbour, one of the most iconic performing arts centres in the world.',
    image_path = 'https://images.unsplash.com/photo-1524293581917-878a6d017c71?w=800&q=80',
    environment_type = 'Monument',
    location = 'Sydney, Australia'
WHERE destination_id = 3;

-- Update VR environment for destination 3: point to Sydney model
UPDATE vr_environments SET
    model_path = '/models/sydney_opera_house.glb',
    map_size = 'Medium'
WHERE destination_id = 3;

-- Update package for destination 3 (if one exists)
UPDATE packages SET
    package_name = 'Sydney Harbour Experience',
    price = 1400.00,
    duration = '5 Days, 4 Nights',
    hotel_details = 'Harbour View Hotel'
WHERE destination_id = 3;

-- Verify the changes
SELECT * FROM destinations WHERE destination_id = 3;
SELECT * FROM vr_environments WHERE destination_id = 3;

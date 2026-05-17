-- SQL Script to fix incorrect images for Eiffel Tower and Liberty Island
USE vrtourism;

-- Update Eiffel Tower (ID 2)
UPDATE destinations SET
    image_path = '/assets/images/eiffel_tower.png'
WHERE destination_id = 2;

-- Update Liberty Island (ID 6)
UPDATE destinations SET
    image_path = '/assets/images/statue_of_liberty.png'
WHERE destination_id = 6;

-- Verify
SELECT destination_id, destination_name, image_path FROM destinations WHERE destination_id IN (2, 6);

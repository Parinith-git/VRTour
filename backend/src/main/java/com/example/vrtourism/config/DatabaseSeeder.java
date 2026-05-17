package com.example.vrtourism.config;

import com.example.vrtourism.entity.Destination;
import com.example.vrtourism.entity.VREnvironment;
import com.example.vrtourism.entity.TravelPackage;
import com.example.vrtourism.repository.DestinationRepository;
import com.example.vrtourism.repository.VREnvironmentRepository;
import com.example.vrtourism.repository.TravelPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final VREnvironmentRepository vrEnvironmentRepository;
    private final TravelPackageRepository travelPackageRepository;

    @Override
    public void run(String... args) throws Exception {
        boolean forceReSeed = false;
        Destination eiffelCheck = destinationRepository.findByDestinationId(2L).orElse(null);
        if (eiffelCheck != null && eiffelCheck.getImagePath().contains("photo-1511739001486-6bfe10ce65f4")) {
            forceReSeed = true;
        }

        if (destinationRepository.findByDestinationId(1L).isEmpty() || forceReSeed) {
            System.out.println("Official destinations not found or need update. Clearing old data and seeding database with official travel experiences...");
            destinationRepository.deleteAll();
            vrEnvironmentRepository.deleteAll();
            travelPackageRepository.deleteAll();

            // 1. Taj Mahal
            Destination tajMahal = new Destination();
            tajMahal.setDestinationId(1L);
            tajMahal.setDestinationName("Taj Mahal");
            tajMahal.setDescription("Marvel at the breathtaking ivory-white marble mausoleum, a UNESCO World Heritage Site and one of the Seven Wonders of the World.");
            tajMahal.setImagePath("https://images.unsplash.com/photo-1564507592333-c60657eea523?w=800&q=80");
            tajMahal.setEnvironmentType("Monument");
            tajMahal.setLocation("Agra, India");
            destinationRepository.save(tajMahal);

            VREnvironment tajVR = new VREnvironment();
            tajVR.setDestinationId(1L);
            tajVR.setModelPath("/models/taj_mahal.glb");
            tajVR.setMapSize("Medium");
            vrEnvironmentRepository.save(tajVR);

            TravelPackage tajPkg = new TravelPackage();
            tajPkg.setPackageId(1L);
            tajPkg.setDestinationId(1L);
            tajPkg.setPackageName("Taj Mahal Heritage Tour");
            tajPkg.setPrice(new BigDecimal("800.00"));
            tajPkg.setDuration("4 Days, 3 Nights");
            tajPkg.setHotelDetails("Heritage Hotel near Taj");
            travelPackageRepository.save(tajPkg);

            // 2. Eiffel Tower
            Destination eiffel = new Destination();
            eiffel.setDestinationId(2L);
            eiffel.setDestinationName("Eiffel Tower");
            eiffel.setDescription("Experience the iconic iron lattice tower standing tall over the romantic city of Paris, a symbol of French artistry and engineering.");
            eiffel.setImagePath("https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=800&q=80");
            eiffel.setEnvironmentType("Monument");
            eiffel.setLocation("Paris, France");
            destinationRepository.save(eiffel);

            VREnvironment eiffelVR = new VREnvironment();
            eiffelVR.setDestinationId(2L);
            eiffelVR.setModelPath("/models/low_poly_eiffel_tower.glb");
            eiffelVR.setMapSize("Medium");
            vrEnvironmentRepository.save(eiffelVR);

            TravelPackage eiffelPkg = new TravelPackage();
            eiffelPkg.setPackageId(2L);
            eiffelPkg.setDestinationId(2L);
            eiffelPkg.setPackageName("Paris Romance Package");
            eiffelPkg.setPrice(new BigDecimal("1500.00"));
            eiffelPkg.setDuration("5 Days, 4 Nights");
            eiffelPkg.setHotelDetails("Boutique Hotel near Eiffel Tower");
            travelPackageRepository.save(eiffelPkg);

            // 3. Sydney Opera House
            Destination sydney = new Destination();
            sydney.setDestinationId(3L);
            sydney.setDestinationName("Sydney Opera House");
            sydney.setDescription("Explore the stunning architectural masterpiece on Sydney Harbour, one of the most iconic performing arts centres in the world.");
            sydney.setImagePath("https://images.unsplash.com/photo-1524293581917-878a6d017c71?w=800&q=80");
            sydney.setEnvironmentType("Monument");
            sydney.setLocation("Sydney, Australia");
            destinationRepository.save(sydney);

            VREnvironment sydneyVR = new VREnvironment();
            sydneyVR.setDestinationId(3L);
            sydneyVR.setModelPath("/models/sydney_opera_house.glb");
            sydneyVR.setMapSize("Medium");
            vrEnvironmentRepository.save(sydneyVR);

            TravelPackage sydneyPkg = new TravelPackage();
            sydneyPkg.setPackageId(3L);
            sydneyPkg.setDestinationId(3L);
            sydneyPkg.setPackageName("Sydney Harbour Experience");
            sydneyPkg.setPrice(new BigDecimal("1400.00"));
            sydneyPkg.setDuration("5 Days, 4 Nights");
            sydneyPkg.setHotelDetails("Harbour View Hotel");
            travelPackageRepository.save(sydneyPkg);

            // 4. Colosseum
            Destination colosseum = new Destination();
            colosseum.setDestinationId(4L);
            colosseum.setDestinationName("Colosseum");
            colosseum.setDescription("Step inside the legendary Roman amphitheatre where gladiators once battled, an enduring symbol of Imperial Rome.");
            colosseum.setImagePath("https://images.unsplash.com/photo-1552832230-c0197dd311b5?w=800&q=80");
            colosseum.setEnvironmentType("Monument");
            colosseum.setLocation("Rome, Italy");
            destinationRepository.save(colosseum);

            VREnvironment colosseumVR = new VREnvironment();
            colosseumVR.setDestinationId(4L);
            colosseumVR.setModelPath("/models/previz-lowpoly_colosseum_with_concept_textures..glb");
            colosseumVR.setMapSize("Medium");
            vrEnvironmentRepository.save(colosseumVR);

            TravelPackage colosseumPkg = new TravelPackage();
            colosseumPkg.setPackageId(4L);
            colosseumPkg.setDestinationId(4L);
            colosseumPkg.setPackageName("Roman Holiday");
            colosseumPkg.setPrice(new BigDecimal("1200.00"));
            colosseumPkg.setDuration("5 Days, 4 Nights");
            colosseumPkg.setHotelDetails("Hotel near Colosseum");
            travelPackageRepository.save(colosseumPkg);

            // 5. Pyramids of Giza
            Destination pyramids = new Destination();
            pyramids.setDestinationId(5L);
            pyramids.setDestinationName("Pyramids of Giza");
            pyramids.setDescription("Stand before the last remaining wonder of the ancient world, the monumental pyramids rising from the Egyptian desert.");
            pyramids.setImagePath("https://images.unsplash.com/photo-1503177119275-0aa32b3a9368?w=800&q=80");
            pyramids.setEnvironmentType("Monument");
            pyramids.setLocation("Giza, Egypt");
            destinationRepository.save(pyramids);

            VREnvironment pyramidsVR = new VREnvironment();
            pyramidsVR.setDestinationId(5L);
            pyramidsVR.setModelPath("/models/giza_pyramid_complex_from_civilization_vi.glb");
            pyramidsVR.setMapSize("Medium");
            vrEnvironmentRepository.save(pyramidsVR);

            TravelPackage pyramidsPkg = new TravelPackage();
            pyramidsPkg.setPackageId(5L);
            pyramidsPkg.setDestinationId(5L);
            pyramidsPkg.setPackageName("Egyptian Expedition");
            pyramidsPkg.setPrice(new BigDecimal("1000.00"));
            pyramidsPkg.setDuration("6 Days, 5 Nights");
            pyramidsPkg.setHotelDetails("Desert Luxury Resort");
            travelPackageRepository.save(pyramidsPkg);

            // 6. Liberty Island
            Destination liberty = new Destination();
            liberty.setDestinationId(6L);
            liberty.setDestinationName("Liberty Island");
            liberty.setDescription("Visit the Statue of Liberty, a colossal neoclassical sculpture symbolizing freedom and democracy.");
            liberty.setImagePath("https://images.unsplash.com/photo-1501594907352-04cda38ebc29?w=800&q=80");
            liberty.setEnvironmentType("Monument");
            liberty.setLocation("New York, USA");
            destinationRepository.save(liberty);

            VREnvironment libertyVR = new VREnvironment();
            libertyVR.setDestinationId(6L);
            libertyVR.setModelPath("/models/liberty_island.glb");
            libertyVR.setMapSize("Large");
            vrEnvironmentRepository.save(libertyVR);

            TravelPackage libertyPkg = new TravelPackage();
            libertyPkg.setPackageId(6L);
            libertyPkg.setDestinationId(6L);
            libertyPkg.setPackageName("New York Explorer");
            libertyPkg.setPrice(new BigDecimal("1800.00"));
            libertyPkg.setDuration("5 Days, 4 Nights");
            libertyPkg.setHotelDetails("Manhattan Suite Hotel");
            travelPackageRepository.save(libertyPkg);

            // 7. Space Loop City
            Destination space = new Destination();
            space.setDestinationId(7L);
            space.setDestinationName("Space Loop City");
            space.setDescription("Explore a futuristic orbital city with neon skywalks and zero-gravity plazas — a sci-fi dream brought to life.");
            space.setImagePath("https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800&q=80");
            space.setEnvironmentType("Futuristic");
            space.setLocation("Outer Space");
            destinationRepository.save(space);

            VREnvironment spaceVR = new VREnvironment();
            spaceVR.setDestinationId(7L);
            spaceVR.setModelPath("/models/space_loop_city.glb");
            spaceVR.setMapSize("Large");
            vrEnvironmentRepository.save(spaceVR);

            TravelPackage spacePkg = new TravelPackage();
            spacePkg.setPackageId(7L);
            spacePkg.setDestinationId(7L);
            spacePkg.setPackageName("Space Loop VR Experience");
            spacePkg.setPrice(new BigDecimal("500.00"));
            spacePkg.setDuration("1 Day");
            spacePkg.setHotelDetails("Virtual Only - No Hotel");
            travelPackageRepository.save(spacePkg);

            System.out.println("Database seeding completed successfully!");
        } else {
            System.out.println("MongoDB database already contains data. Skipping seeder.");
        }
    }
}

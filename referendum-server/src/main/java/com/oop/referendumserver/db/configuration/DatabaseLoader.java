package com.oop.referendumserver.db.configuration;

import com.oop.referendumserver.db.configuration.DataConfiguration.RegionConfiguration;
import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.db.repository.ReferendumRepository;
import com.oop.referendumserver.db.repository.RegionRepository;
import com.oop.referendumserver.db.repository.UserRepository;
import com.oop.referendumserver.tools.ResourceReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

/**
 * Component responsible for loading initial data into the database on application startup.
 */
@Component
public class DatabaseLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);


    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceReader resourceReader;

    @Autowired
    private ReferendumRepository referendumRepository;

    @Autowired
    private DataConfiguration dataConfiguration;


    private final Map<String, Region> regions = new HashMap<>();


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initRegions();
        initializeUsers();
        initializeReferendums();
    }

    /**
     * Initializes the regions in the database.
     */
    private void initRegions() {
        for (RegionConfiguration regionConfig : dataConfiguration.getRegions()) {
            Region region = new Region(regionConfig.getId(), regionConfig.getName(), regionConfig.getDescription());
            regions.put(regionConfig.getId(), regionRepository.save(region));
        }
    }

    /**
     * Initializes the regions in the database and giving them ids
     */
    private void initializeRegions() {
        Properties regionDescriptions = resourceReader.readProperties("region-descriptions.properties");

        log.info(regionDescriptions.get("BA.description").toString());
        regions.put("TT", new Region("TT", "Trnavsky kraj", Collections.emptyList()));
        regions.put("BA", new Region("BA", "Bratislavky kraj", Collections.emptyList()));
        regions.put("TN", new Region("TN", "Trenciansky kraj", Collections.emptyList()));
        regions.put("NR", new Region("NR", "Nitriansky kraj", Collections.emptyList()));
        regions.put("ZA", new Region("ZA", "Zilinsky kraj", Collections.emptyList()));
        regions.put("BB", new Region("BB", "Banskobystricky kraj", Collections.emptyList()));
        regions.put("KE", new Region("KE", "Kosicky kraj", Collections.emptyList()));
        regions.put("PO", new Region("PO", "Presovsky kraj", Collections.emptyList()));

        regionRepository.saveAll(regions.values());
    }

    /**
     * Initializes users in the database.
     */
    private void initializeUsers() {
        TravelVoter travelVoter = new TravelVoter();
        travelVoter.setId("Peter");
        travelVoter.setPassword("heslo");
        Set<Region> regionSet = new HashSet<>();
        regionSet.add(regions.get("BA"));
        regionSet.add(regions.get("TT"));
        travelVoter.setRegions(regionSet);

        userRepository.save(travelVoter);

        LocalVoter voter = new LocalVoter();
        voter.setId("ema");
        voter.setPassword("heslo");
        voter.setRegion(regions.get("TT"));

        userRepository.save(voter);


        LocalVoter voter2 = new LocalVoter();
        voter2.setId("linda");
        voter2.setPassword("heslo");
        voter2.setRegion(regions.get("TT"));

        userRepository.save(voter2);


        Viewer viewer = new Viewer();
        viewer.setId("dominik");
        viewer.setPassword("heslo");

        userRepository.save(viewer);

        Admin admin = new Admin();
        admin.setId("admin");
        admin.setPassword("admin");

        userRepository.save(admin);

    }

    /**
     * Initializes referendums in the database.
     */
    private void initializeReferendums() {
        SimpleReferendum simpleReferendum = new SimpleReferendum();
        simpleReferendum.setTitle("Referendum pre parkovanie");
        simpleReferendum.setDescription("Nove parkovacie miesta na Mikoviniho");
        simpleReferendum.setDate(LocalDate.of(2025, 5, 6));
        simpleReferendum.setPassThreshold(0.1f);
        simpleReferendum.setRegion(regions.get("BA"));

        // create questions
        Question question1 = new Question("1", "blaa");
        Question question2 = new Question("2", "bllaa2");

        // create a list of questions
        List<Question> questions = new ArrayList<>();
        questions.add(question1);
        questions.add(question2);

        // add questions to referendum
        simpleReferendum.setQuestions(questions);

        referendumRepository.save(simpleReferendum);


        ConditionalReferendum conditionalReferendum = new ConditionalReferendum();
        conditionalReferendum.setTitle("Referendum pre nove Bytovky");
        conditionalReferendum.setDescription("Nove bytovky na Vajanskeho ulici");
        conditionalReferendum.setDate(LocalDate.of(2025, 3, 1));
        conditionalReferendum.setLocalVoteQuota(0.2f);
        conditionalReferendum.setMinimalParticipation(10L);
        conditionalReferendum.setRegion(regions.get("TT"));

        Question question3 = new Question("3", "blaa");
        Question question4 = new Question("4", "blaaaaaa");

        List<Question> questions2 = new ArrayList<>();
        questions2.add(question3);
        questions2.add(question4);

        conditionalReferendum.setQuestions(questions2);

        referendumRepository.save(conditionalReferendum);

    }
}

package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.Region;
import com.oop.referendumserver.db.repository.RegionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing regions
 */
@Service
public class RegionService {

    private final RegionRepository regionRepository;

    /**
     * Constructs a new RegionService with the specified repository
     * @param regionRepository the repository for managing regions
     */
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    /**
     * Retrieves a region by its ID
     * @param id the ID of the region to retrieve
     * @return the region with the specified ID, or null if not found
     */
    public Region getRegionById(String id) {
        return regionRepository.findById(id).get();
    }

    /**
     * Retrieves all regions
     * @return a list of all regions
     */
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }
}

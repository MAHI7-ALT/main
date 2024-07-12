package org.imaginnovate.Repository;

import java.util.List;
import java.util.Optional;

import org.imaginnovate.Dto.DivisionDto;
import org.imaginnovate.Entity.Division;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class DivisionRepoTest {

    @Inject
    DivisionRepo divisionRepo;

    @Test
    public void testFindAllDivisions() {
        List<DivisionDto> divisions = divisionRepo.findAllDivisions();
        assert(divisions.size() > 0);
    }

    @Test
    public void testFindDivisionById() {
        Optional<DivisionDto> division = divisionRepo.findDivisionById(1);
        assert(division.isPresent());
    }

    @Test
    public void testFindByName() {
        Division division = divisionRepo.findByName("IMAGINNOVATE");
        assert(division.name.equals("IMAGINNOVATE"));
    }

    @Test
    public void testFindByDivisionId() {
        List<Division> divisions = divisionRepo.findByDivisionId(5);
        assert(divisions.size() > 0);
    }
    

}
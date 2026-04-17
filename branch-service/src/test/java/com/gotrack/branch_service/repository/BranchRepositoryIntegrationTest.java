package com.gotrack.branch_service.repository;

import com.gotrack.branch_service.demo.TestDataUtil;
import com.gotrack.branch_service.domain.Branch;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class BranchRepositoryIntegrationTest {
    private BranchRepository underTest;

    @Autowired
    public BranchRepositoryIntegrationTest(BranchRepository underTest){

        this.underTest= underTest;
    }

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    public void testThatBranchCanBeCreatedAndRecalled(){
        Branch branch = TestDataUtil.createTestBranchA();
        underTest.save(branch);
        Optional<Branch> result = underTest.findById(branch.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(branch.getName());
        assertThat(result.get().getLocation()).isEqualTo(branch.getLocation());
        assertThat(result.get().getPhone()).isEqualTo(branch.getPhone());
    }

    @Test
    public void testThatMultipleBranchesCanBeCreatedAndRecalled(){
        Branch branchA = TestDataUtil.createTestBranchA();
        underTest.save(branchA);
        Branch branchB = TestDataUtil.createTestBranchB();
        underTest.save(branchB);
        Branch branchC = TestDataUtil.createTestBranchC();
        underTest.save(branchC);

        Iterable<Branch> result= underTest.findAll();
        result.forEach(System.out::println);
        assertThat(result)
                .hasSize(3)
                .extracting(Branch::getName)
                .containsExactly(
                        branchA.getName(),
                        branchB.getName(),
                        branchC.getName()
                );
    }

    @Test
    public void TestThatBranchCanBeUpdated(){
        Branch branchA= TestDataUtil.createTestBranchA();
        underTest.save(branchA);
        branchA.setName("Fayoum");
        underTest.save(branchA);
        Optional<Branch> result= underTest.findById(branchA.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Fayoum");

    }

    @Test
    public void testThatBranchCanBeDeleted(){
        Branch branchA= TestDataUtil.createTestBranchA();
        underTest.save(branchA);
        underTest.deleteById(branchA.getId());
        Optional<Branch> result= underTest.findById(branchA.getId());
        assertThat(result).isEmpty();
    }

}

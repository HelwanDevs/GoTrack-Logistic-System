package com.gotrack.branch_service.demo;

import com.gotrack.branch_service.domain.entity.BranchEntity;
import lombok.Builder;

@Builder
public final class TestDataUtil {

    private TestDataUtil(){

    }

    public static BranchEntity createTestBranchA(){
        return BranchEntity.builder()
                .name("Cairo")
                .phone("01552850418")
                .location("Nasr_City")
                .build();
    }

    public static BranchEntity createTestBranchB(){
        return BranchEntity.builder()
                .name("Giza")
                .phone("01252850418")
                .location("Fysal")
                .build();
    }

    public static BranchEntity createTestBranchC(){
        return BranchEntity.builder()
                .name("Alex")
                .phone("01352850418")
                .location("Elalmen")
                .build();
    }
}

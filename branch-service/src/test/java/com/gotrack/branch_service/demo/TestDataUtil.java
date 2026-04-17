package com.gotrack.branch_service.demo;

import com.gotrack.branch_service.domain.Branch;
import lombok.Builder;

@Builder
public final class TestDataUtil {

    private TestDataUtil(){

    }

    public static Branch createTestBranchA(){
        return Branch.builder()
                .name("Cairo")
                .phone("01552850418")
                .location("Nasr_City")
                .build();
    }

    public static Branch createTestBranchB(){
        return Branch.builder()
                .name("Giza")
                .phone("01252850418")
                .location("Fysal")
                .build();
    }

    public static Branch createTestBranchC(){
        return Branch.builder()
                .name("Alex")
                .phone("01352850418")
                .location("Elalmen")
                .build();
    }
}

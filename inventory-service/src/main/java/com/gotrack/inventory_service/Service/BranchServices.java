package com.gotrack.inventory_service.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.gotrack.inventory_service.Dto.BranchResponseDTO;
import com.gotrack.inventory_service.client.feign.BranchClient;
import org.springframework.stereotype.Service;

@Service
public class BranchServices {
    @Autowired
    private BranchClient branchClient;

    public BranchResponseDTO getBranchById(Long id) {
        return branchClient.getBranchById(id);
    }

}

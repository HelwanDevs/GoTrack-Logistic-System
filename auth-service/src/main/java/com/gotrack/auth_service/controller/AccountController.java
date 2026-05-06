package com.gotrack.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gotrack.auth_service.dto.AccountResponse;
import com.gotrack.auth_service.dto.CreateAccountRequest;
import com.gotrack.auth_service.dto.CreateAccountResponse;
import com.gotrack.auth_service.dto.ListAccountsRequest;
import com.gotrack.auth_service.dto.ListAccountsResponse;
import com.gotrack.auth_service.dto.UpdateAccountRequest;
import com.gotrack.auth_service.dto.UpdateDeleteResponse;
import com.gotrack.auth_service.enums.Role;
import com.gotrack.auth_service.services.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/accounts")
    public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        CreateAccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts")
    public ResponseEntity<ListAccountsResponse> listAccounts(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String email,
            @RequestParam(required = false, defaultValue = "false") Boolean includeDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ListAccountsRequest request = new ListAccountsRequest();
        request.setRole(role);
        request.setEmail(email);
        request.setIncludeDeleted(includeDeleted);
        request.setPage(page);
        request.setSize(size);

        ListAccountsResponse response = accountService.listAccounts(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String id) {
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<UpdateDeleteResponse> updateAccount(@PathVariable String id,
            @Valid @RequestBody UpdateAccountRequest request) {
        UpdateDeleteResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<UpdateDeleteResponse> deleteAccount(@PathVariable String id) {
        UpdateDeleteResponse response = accountService.deleteAccount(id);
        return ResponseEntity.ok(response);
    }

}
package com.gotrack.auth_service.services;

import com.gotrack.auth_service.Jwt.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gotrack.auth_service.dto.AccountResponse;
import com.gotrack.auth_service.dto.CreateAccountRequest;
import com.gotrack.auth_service.dto.CreateAccountResponse;
import com.gotrack.auth_service.dto.ListAccountsRequest;
import com.gotrack.auth_service.dto.ListAccountsResponse;
import com.gotrack.auth_service.dto.UpdateAccountRequest;
import com.gotrack.auth_service.dto.UpdateDeleteResponse;
import com.gotrack.auth_service.entity.Account;
import com.gotrack.auth_service.enums.Role;
import com.gotrack.auth_service.Exceptions.AccountNotFoundException;
import com.gotrack.auth_service.Exceptions.EmailAlreadyExistsException;
import com.gotrack.auth_service.Jwt.JwtService;
import com.gotrack.auth_service.repository.AccountRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    AccountRepository accRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtService jwtService;

    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        if (accRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (request.getRole() == Role.ADMIN) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null) {
                throw new AccessDeniedException("No User Found.");
            }

            String currentUserIdString = auth.getDetails().toString();

            Account currentUser = accRepository.findById(currentUserIdString)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found"));

            if (currentUser.getSuperAdmin() == false) {
                System.out.println(currentUser.getSuperAdmin());
                throw new AccessDeniedException("Only Super Admin can create other Admins.");
            }
        }

        Account newUser = new Account();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());

        Account savedUser = accRepository.save(newUser);

        return new CreateAccountResponse(savedUser.getId(), "Account created successfully");

    }

    public AccountResponse getAccountById(String targetId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getDetails() instanceof String)) {
            throw new AccessDeniedException("Authentication required");
        }

        String currentUserId = ((String) auth.getDetails());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        String cleanId = targetId.replaceAll("[{}]", "").trim();
        System.out.println("Searching for ID: [" + targetId + "] with length: " + targetId.length());

        Account user = accRepository.findById(cleanId.trim())
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (!currentUserId.equals(cleanId) && !isAdmin) {
            throw new AccessDeniedException("Access denied: This is not your profile.");
        }

        return new AccountResponse(user.getId(), user.getEmail(), user.getRole().name(), user.getDeleted());
    }

    public UpdateDeleteResponse updateAccount(String targetId, UpdateAccountRequest request) {
        if (request.getEmail() == null && request.getPassword() == null && request.getRole() == null) {
            throw new BadCredentialsException("Invalid data provided");
        }

        String cleanId = targetId.replaceAll("[{}]", "").trim();

        Account targetUser = accRepository.findById(cleanId)
                .orElseThrow(() -> new AccountNotFoundException("Account ID does not exist"));

        String currentUserId = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Account currentUser = accRepository.findById(currentUserId)
                .orElseThrow(() -> new AccountNotFoundException("Your account not found"));

        if (currentUserId.equals(targetId) && request.getEmail() == null && request.getPassword() == null
                && request.getRole() != null) {
            throw new AccessDeniedException("You cannot change your own role.");
        }
        validateAuthorization(currentUser, targetUser, request.getRole());

        checkEmailConflict(targetUser.getEmail(), request.getEmail());
        applyUpdates(targetUser, request);
        accRepository.save(targetUser);

        // make the user re-login
        if (request.getPassword() != null || request.getEmail() != null || request.getRole() != null) {
            refreshTokenService.deleteByUsername(targetUser.getEmail());
        }

        if (currentUser.getRole() == Role.ADMIN && !targetId.equals(currentUserId)) {

            return new UpdateDeleteResponse("Account updated successfully from Admin");
        }

        String newToken = jwtService.generateToken(targetUser);

        return new UpdateDeleteResponse("Account updated successfully", newToken);

    }

    public UpdateDeleteResponse deleteAccount(String targetId) {
        String currentUserIdString = (String) SecurityContextHolder.getContext().getAuthentication().getDetails();

        String cleanId = targetId.replaceAll("[{}]", "").trim();

        Account targetUser = accRepository.findById(cleanId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        Account currentUser = accRepository.findById(currentUserIdString)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (targetUser.getSuperAdmin()) {
            throw new AccessDeniedException("You cannot delete Super Admin.");
        }

        if (targetId.equals(currentUserIdString)) { // ✅
            throw new AccessDeniedException("You cannot delete your own account.");
        }

        if (targetUser.getRole() == Role.ADMIN && currentUser.getSuperAdmin() == false) {
            throw new AccessDeniedException("Only Super Admin can delete other Admins.");
        }

        targetUser.setDeleted(true);
        accRepository.save(targetUser);
        return new UpdateDeleteResponse("Account deactivated");

    }

    public ListAccountsResponse listAccounts(ListAccountsRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AccessDeniedException("Only Admin can list accounts");
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        Page<Account> accountPage = accRepository.findByDynamicFilters(
                request.getRole(),
                request.getEmail(),
                request.getIncludeDeleted(),
                pageable);

        List<AccountResponse> accountResponses = accountPage.getContent().stream()
                .map(account -> new AccountResponse(account.getId(), account.getEmail(), account.getRole().name(),
                        account.getDeleted()))
                .collect(Collectors.toList());

        return new ListAccountsResponse(
                accountResponses,
                accountPage.getTotalElements(),
                request.getPage(),
                request.getSize());
    }

    public boolean isSuperAdmin(String id) {
        Account account = accRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
        return account.getSuperAdmin();
    }

    private void validateAuthorization(Account currentUser, Account targetAccount, Role requestedRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String currentUserId = currentUser.getId();
        String targetId = targetAccount.getId();

        boolean isContextAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!currentUserId.equals(targetId) && !isContextAdmin) {
            throw new AccessDeniedException("Access denied: You can only edit your own profile.");
        }

        if (targetAccount.getSuperAdmin() && !currentUserId.equals(targetId)) {
            throw new AccessDeniedException("Super Admin accounts cannot be modified by others.");
        }

        if (requestedRole == Role.ADMIN || targetAccount.getRole() == Role.ADMIN) {
            if (!currentUser.getSuperAdmin() && !currentUserId.equals(targetId)) {
                throw new AccessDeniedException("Only Super Admin can manage Admin roles.");
            }
        }
    }

    private void applyUpdates(Account user, UpdateAccountRequest request) {
        if (request.getEmail() != null)
            user.setEmail(request.getEmail());
        if (request.getPassword() != null)
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getRole() != null)
            user.setRole(request.getRole());
    }

    private void checkEmailConflict(String currentEmail, String newEmail) {
        if (newEmail != null && !newEmail.equals(currentEmail) && accRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("New email is already taken");
        }
    }
}
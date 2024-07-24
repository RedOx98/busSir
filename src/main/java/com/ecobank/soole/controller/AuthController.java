package com.ecobank.soole.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecobank.soole.models.Account;
import com.ecobank.soole.payload.auth.AccountDTO;
import com.ecobank.soole.payload.auth.AccountVerifiedViewDTO;
import com.ecobank.soole.payload.auth.AccountViewDTO;
import com.ecobank.soole.payload.auth.AuthoritiesDTO;
import com.ecobank.soole.payload.auth.ChangePasswordPayloadDTO;
import com.ecobank.soole.payload.auth.PasswordDTO;
import com.ecobank.soole.payload.auth.PasswordResetPayloadDTO;
import com.ecobank.soole.payload.auth.ProfileDTO;
import com.ecobank.soole.payload.auth.StatsDTO;
import com.ecobank.soole.payload.auth.TokenViewDTO;
import com.ecobank.soole.payload.auth.UpdateAccountPayloadDTO;
import com.ecobank.soole.payload.auth.UpdateAccountViewDTO;
import com.ecobank.soole.payload.auth.UserLoginDTO;
import com.ecobank.soole.payload.auth.VerifiedDTO;
import com.ecobank.soole.services.AccountService;
import com.ecobank.soole.services.EmailService;
import com.ecobank.soole.services.TokenService;
import com.ecobank.soole.util.constants.AccountError;
import com.ecobank.soole.util.constants.AccountSuccess;
import com.ecobank.soole.util.email.EmailDetails;
import com.ecobank.soole.util.email.EmailDetailsWelcome;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Auth Controller", description = "Controller for Account management")
public class AuthController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final TokenService tokenService;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private EmailService emailService;

    @Value("${site.domain}")
    private String siteDomain;

    @Value("${password.token.reset..timeout.minutes}")
    private int passwordTokenTimeout;

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login to get token")
    // @PreAuthorize()
    public ResponseEntity<TokenViewDTO> token(@Valid @RequestBody UserLoginDTO userLogin)
            throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));

            Optional<Account> optionalAccount = accountService.findByEmail(userLogin.getEmail());
            Account account = optionalAccount.get();
            return ResponseEntity
                    .ok(new TokenViewDTO(tokenService.generateToken(authentication), account.getAuthorities(),
                            account.getLevel(), account.getFirstName(), account.getLastName(), account.getUsername()));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PostMapping(value = "/users/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Account added")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Add a new user")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) throws AuthenticationException {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword_hash(accountDTO.getPassword_hash());
            account.setLevel(accountDTO.getLevel());
            account.setTelephone(accountDTO.getTelephone());
            account.setFirstName(accountDTO.getFirstName());
            account.setLastName(accountDTO.getLastName());
            account.setUsername(accountDTO.getUsername());
            accountService.save(account);
            String welcomeMessage = "Welcome to SOOLE APP";
            EmailDetailsWelcome details = new EmailDetailsWelcome(account.getEmail(), welcomeMessage,
                    "Soole just testing", account.getFirstName());
            if (emailService.sendWelcomeEmail(details) == false) {
                return new ResponseEntity<>("Error while sending mail, contact admin", HttpStatus.EXPECTATION_FAILED);
            }
            ;

            return ResponseEntity.ok(AccountSuccess.ACCOUNT_ADDED.toString());
        } catch (Exception e) {
            return ResponseEntity.ok(AccountError.ADD_ACCOUNT_ERROR.toString() + ": " + e.getMessage());
        }
    }

    @GetMapping("/users")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List user API")
    @SecurityRequirement(name = "soole-demo-api")
    public List<AccountViewDTO> users() {

        List<AccountViewDTO> accounts = new ArrayList<>();

        for (Account users : accountService.findAll()) {
            accounts.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }
        return accounts;
    }

    @GetMapping("/stats")
    @ApiResponse(responseCode = "200", description = "Statistics")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Get stats")
    @SecurityRequirement(name = "soole-demo-api")
    public StatsDTO stats() {

        List<AccountViewDTO> accounts = new ArrayList<>();
        for (Account users : accountService.findAll()) {
            accounts.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }

        List<AccountViewDTO> approved = new ArrayList<>();

        for (Account users : accountService.findByVerified("approved")) {
            approved.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }

        List<AccountViewDTO> pending = new ArrayList<>();

        for (Account users : accountService.findByVerified("pending")) {
            pending.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }

        List<AccountViewDTO> rejected = new ArrayList<>();

        for (Account users : accountService.findByVerified("rejected")) {
            rejected.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }

        List<AccountViewDTO> captains = new ArrayList<>();

        for (Account users : accountService.findByAuthorities("CAPTAIN")) {
            captains.add(new AccountViewDTO(users.getId(), users.getEmail(), users.getAuthorities(),
                    users.getCreatedAt(), users.getLevel(), users.getTelephone(), users.getFirstName(),
                    users.getLastName(), users.getUsername(), users.getVerified(), users.getRoute(),
                    users.getDepartment(), users.getAffiliate(), users.getStaff_id()));
        }

        StatsDTO stats = new StatsDTO(accounts.size(), approved.size(), pending.size(), rejected.size(),
                captains.size());
        return stats;
    }

    @GetMapping("/userspaginate")
    @ApiResponse(responseCode = "200", description = "List of users pagination")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users in paginated format")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<List<AccountViewDTO>> allUsers(
            @RequestParam(required = false, name = "sort_by", defaultValue = "createdAt") String sort_by,
            @RequestParam(required = false, name = "per_page", defaultValue = "2") String per_page,
            @RequestParam(required = false, name = "page", defaultValue = "page") String page,
            @RequestParam(required = false, name = "name", defaultValue = "page") String name,
            @RequestParam(required = false, name = "authorities", defaultValue = "page") String authorities) {
        Page<Account> accountsOnPage = accountService.findAccounts(Integer.parseInt(page) - 1,
                Integer.parseInt(per_page), sort_by, name, authorities);
        List<Account> accountList = accountsOnPage.getContent();
        int totalPages = accountsOnPage.getTotalPages();
        List<Integer> pages = new ArrayList<>();
        if (totalPages > 0) {
            pages = IntStream.rangeClosed(0, totalPages - 1).boxed().collect(Collectors.toList());
        }

        if (pages != null) {
            for (int link : pages) {
                // String active = "";
                if (link == accountsOnPage.getNumber()) {
                    // active = "active";
                }
                // Convert Account to AccountViewDTO
                List<AccountViewDTO> accounts = accountList.stream().map(account -> new AccountViewDTO(
                        account.getId(),
                        account.getEmail(),
                        account.getAuthorities(),
                        account.getCreatedAt(),
                        account.getLevel(),
                        account.getTelephone(),
                        account.getFirstName(),
                        account.getLastName(),
                        account.getUsername(),
                        account.getVerified(),
                        account.getRoute(),
                        account.getDepartment(),
                        account.getAffiliate(),
                        account.getStaff_id())).collect(Collectors.toList());
                System.out.println(accounts);
                return ResponseEntity.ok(accounts);
            }
        }
        System.out.println(accountsOnPage);
        return null;
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List user API")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<ProfileDTO> profile(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities(),
                    account.getLevel(), account.getTelephone());
            return ResponseEntity.ok(profileDTO);
        }
        return new ResponseEntity<ProfileDTO>(new ProfileDTO(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/users/reset-password", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Email sent!")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "Forgot password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetPayloadDTO payloadDTO) {
        Optional<Account> optionalAccount = accountService.findByEmail(payloadDTO.getEmail());
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            String resetToken = UUID.randomUUID().toString();
            account.setToken(resetToken);
            account.setPassswordResetTokenExpiry(LocalDateTime.now().plusMinutes(passwordTokenTimeout));
            accountService.save(account);
            String resetMessage = "This is the reset password link: " + siteDomain + "change-password?token="
                    + resetToken;
            EmailDetails details = new EmailDetails(account.getEmail(), resetMessage,
                    ("reset pasword: " + account.getFullName()));
            if (emailService.sendMail(details) == false) {
                return new ResponseEntity<>("Error while sending mail, contact admin", HttpStatus.EXPECTATION_FAILED);
            }
            ;
            return new ResponseEntity<>("Password reset email sent", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No user found with the email supplied!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PostMapping(value = "/users/change-password", produces = "application/json", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "417", description = "Invalid token!")
    @ApiResponse(responseCode = "201", description = "password changed")
    @ApiResponse(responseCode = "200", description = "success")
    @Operation(summary = "User to reset password")
    public ResponseEntity<String> changePassword(@Valid @RequestParam("token") String token,
            @RequestBody ChangePasswordPayloadDTO payloadDTO) {
        Optional<Account> optionalAccount = accountService.findByToken(token);
        if (optionalAccount.isPresent()) {
            Account account = accountService.findById(optionalAccount.get().getId()).get();
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(optionalAccount.get().getPassswordResetTokenExpiry())) {
                return new ResponseEntity<>("Token Expired!!", HttpStatus.EXPECTATION_FAILED);
            }
            account = optionalAccount.get();
            Account accountByid = accountService.findById(account.getId()).get();
            accountByid.setPassword_hash(payloadDTO.getPassword());
            accountByid.setToken("");
            accountService.save(accountByid);
            return new ResponseEntity<>("Password reset successfully!!!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Token Expired!!", HttpStatus.EXPECTATION_FAILED);
        }
    }

    @PutMapping(value = "/profile/update-password", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update profile")
    @SecurityRequirement(name = "soole-demo-api")
    public ProfileDTO updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        account.setPassword_hash(passwordDTO.getPassword());
        accountService.save(account);
        ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities(),
                account.getLevel(), account.getTelephone());

        return profileDTO;
    }

    @PutMapping(value = "/profile/{userId}/update-profile", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "List of users")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update user profile by admin")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<UpdateAccountViewDTO> updateProfile(@Valid @RequestBody UpdateAccountPayloadDTO accountDTO,
            @PathVariable Long userId, Authentication authentication) {
        Optional<Account> optionalAccount = accountService.findById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAffiliate(accountDTO.getAffiliate());
            account.setEmail(accountDTO.getEmail());
            account.setPassword_hash(accountDTO.getPassword_hash());
            account.setDepartment(accountDTO.getDepartment());
            account.setAffiliate(accountDTO.getAffiliate());
            account.setFirstName(accountDTO.getFirstName());
            account.setLastName(accountDTO.getLastName());
            account.setLevel(accountDTO.getLevel());
            account.setTelephone(accountDTO.getTelephone());
            account.setUsername(accountDTO.getUsername());
            account.setVerified(accountDTO.getVerified());
            account.setRoute(accountDTO.getRoute());
            account.setDepartment(accountDTO.getDepartment());
            account.setStatus(accountDTO.getStatus());
            accountService.save(account);
            UpdateAccountViewDTO accountViewDTO = new UpdateAccountViewDTO(account.getId(), account.getEmail(),
                    account.getLevel(),
                    account.getTelephone(), account.getFirstName(), account.getLastName(), account.getUsername(),
                    account.getVerified(), account.getRoute(), account.getDepartment(), account.getAffiliate(),
                    account.getStatus(), account.getStaff_id());
            return ResponseEntity.ok(accountViewDTO);

        } else {
            return ResponseEntity.badRequest().body(null);

        }

    }

    @PutMapping(value = "/users/{userId}/update-authorities", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Updated authorities")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @ApiResponse(responseCode = "400", description = "Invalid user")
    @Operation(summary = "Update authorities")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<AccountViewDTO> updateAuthorities(@Valid @RequestBody AuthoritiesDTO authoritiesDTO,
            @PathVariable Long userId) {
        Optional<Account> optionalAccount = accountService.findById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(account);
            AccountViewDTO accountViewDTO = new AccountViewDTO(
                    account.getId(),
                    account.getEmail(),
                    account.getAuthorities(),
                    account.getCreatedAt(),
                    account.getLevel(),
                    account.getTelephone(),
                    account.getFirstName(),
                    account.getLastName(),
                    account.getUsername(),
                    account.getVerified(),
                    account.getRoute(),
                    account.getDepartment(),
                    account.getAffiliate(),
                    account.getStaff_id());

            return ResponseEntity.ok(accountViewDTO);
        }
        return new ResponseEntity<>(new AccountViewDTO(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value = "/users/{userId}/update-verified", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Updated verified")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @ApiResponse(responseCode = "400", description = "Invalid user")
    @Operation(summary = "Update verified")
    @SecurityRequirement(name = "soole-demo-api")
    public ResponseEntity<AccountVerifiedViewDTO> updateVerified(@Valid @RequestBody VerifiedDTO verifiedDTO,
            @PathVariable Long userId) {
        Optional<Account> optionalAccount = accountService.findById(userId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setVerified(verifiedDTO.getVerified());
            accountService.save(account);
            AccountVerifiedViewDTO accountVerifiedViewDTO = new AccountVerifiedViewDTO(account.getId(),
                    account.getEmail(), account.getAuthorities(), account.getCreatedAt(), account.getLevel(),
                    account.getTelephone(), account.getVerified());

            return ResponseEntity.ok(accountVerifiedViewDTO);
        }
        return new ResponseEntity<>(new AccountVerifiedViewDTO(), HttpStatus.BAD_REQUEST);
    }

    // @DeleteMapping(value = "/profile/delete")
    // @ApiResponse(responseCode = "200", description = "List of users")
    // @ApiResponse(responseCode = "401", description = "Token missing")
    // @ApiResponse(responseCode = "403", description = "Token error")
    // @Operation(summary = "Delete profile")
    // @SecurityRequirement(name = "soole-demo-api")
    // public ResponseEntity<String> deleteUser(Authentication authentication) {
    // String email = authentication.getName();
    // Optional<Account> optionalAccount = accountService.findByEmail(email);
    // if (optionalAccount.isPresent()) {
    // accountService.deleteById(optionalAccount.get().getId());
    // return ResponseEntity.ok("User deleted!");
    // }
    // return new ResponseEntity<String>("Bad request", HttpStatus.BAD_REQUEST);
    // }
}

package com.ecobank.soole.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecobank.soole.models.Account;
import com.ecobank.soole.models.Bus;
import com.ecobank.soole.repositories.AccountRepository;
import com.ecobank.soole.repositories.BusRepository;
import com.ecobank.soole.util.constants.Authority;

@Service
// @RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BusRepository busRepository;

    public Account save(Account account) {

        account.setPassword_hash(passwordEncoder.encode(account.getPassword_hash()));
        if (account.getId() == null) {
            account.setCreatedAt(LocalDateTime.now());
        }
        if (account.getAuthorities() == null) {
            account.setAuthorities(Authority.USER.toString());
        }
        if (account.getVerified() == null) {
            account.setVerified("true");
        }
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(LocalDateTime.now());
        }

        return accountRepository.save(account);
    }

    public Account mapToBus(Account account, Long busId) {

        account.setPassword_hash(passwordEncoder.encode(account.getPassword_hash()));
        if (account.getId() == null) {
            account.setCreatedAt(LocalDateTime.now());
        }
        if (account.getAuthorities() == null) {
            account.setAuthorities(Authority.USER.toString());
        }
        if (account.getVerified() == null) {
            account.setVerified("PENDING");
        }
        if (account.getCreatedAt() == null) {
            account.setCreatedAt(LocalDateTime.now());
        }

        Optional<Bus> optionalBus = busRepository.findById(busId);
        if (optionalBus.isPresent()) {
            Bus bus = optionalBus.get();
            account.setBus(bus);
        } else {
            throw new RuntimeException();
        }

        // account.setSpecial("OKAY");
        // account.setBoard("PENDING");

        return accountRepository.save(account);
    }

    // public Page<Account> findAccounts(int offset, int pageSize, String field) {
    //     return accountRepository.findAll(PageRequest.of(offset, pageSize).withSort(Direction.ASC, field));
    // }

    public Page<Account> findAccounts(int page, int pageSize, String sortBy, String name, String authorities) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, sortBy));
        return accountRepository.findByNameAndDate(name, authorities, pageable);
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public List<Account> findByAuthorities(String authority) {
        return accountRepository.findByAuthorities(authority);
    }

    public List<Account> findByVerified(String status) {
        return accountRepository.findByVerified(status);
    }

    public Optional<Account> findById(Long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findByToken(String token) {
        return accountRepository.findByToken(token);
    }

    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("Account not found!");
        }
        Account account = optionalAccount.get();
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        grantedAuthority.add(new SimpleGrantedAuthority(account.getAuthorities()));
        return new User(account.getEmail(), account.getPassword_hash(), grantedAuthority);
    }

}

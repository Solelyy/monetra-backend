package com.monetra.services;

import com.monetra.exceptions.ClientNotFoundException;
import com.monetra.exceptions.EmailAlreadyExistsException;
import com.monetra.exceptions.MobileNumberAlreadyExistsException;
import com.monetra.exceptions.UnauthorizedException;
import com.monetra.models.Account;
import com.monetra.dto.CurrentUser;
import com.monetra.dto.RegisterRequest;
import com.monetra.models.Address;
import com.monetra.models.Client;
import com.monetra.models.ContactDetail;
import com.monetra.repositories.AccountRepository;
import com.monetra.repositories.AddressRepository;
import com.monetra.repositories.ClientRepository;
import com.monetra.repositories.ContactDetailRepository;
import com.monetra.security.details.CustomUserDetails;
import com.monetra.models.User;
import com.monetra.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ContactDetailRepository contactDetailRepository;
    private final AccountRepository accountRepository;

    //Register
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {
        log.info("Registering a user: {}", registerRequest.getEmail());

        //1. check if email exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            log.warn("Can't create account, email already exists: {}", registerRequest.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        //2. check mobile number exists
        if (contactDetailRepository.findByMobileNumber(registerRequest
                .getMobileNumber())
                .isPresent()) {
            log.warn("Can't create account, mobile number already exists: {}", registerRequest.getMobileNumber());
            throw new MobileNumberAlreadyExistsException("Mobile number already exists");
        }

        //3. create user
        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userRepository.save(user);

        //4. create client
        Client client = Client.builder()
                .user(user)
                .firstName(registerRequest.getFirstName())
                .middleName(registerRequest.getMiddleName())
                .lastName(registerRequest.getLastName())
                .suffix(registerRequest.getSuffix())
                .gender(registerRequest.getGender())
                .dateOfBirth(registerRequest.getDateOfBirth())
                .build();

        clientRepository.save(client);

        //5. create address
        Address address = Address.builder()
                .client(client)
                .addressLine1(registerRequest.getAddressLine1())
                .addressLine2(registerRequest.getAddressLine2())
                .city(registerRequest.getCity())
                .province(registerRequest.getProvince())
                .region(registerRequest.getRegion())
                .country(registerRequest.getCountry())
                .zipCode(registerRequest.getZipcode())
                .build();
        addressRepository.save(address);

        //6. create contact detail
        ContactDetail contactDetail = ContactDetail.builder()
                .client(client)
                .mobileNumber(registerRequest.getMobileNumber())
                .build();
        contactDetailRepository.save(contactDetail);

        //7. create account
        Account account = Account.builder()
                .client(client)
                .accountNumber(generateUniqueAccountNumber())
                .balance(BigDecimal.ZERO)
                .build();
        accountRepository.save(account);
        log.info("User registered successfully: {}", user.getEmail());
        return user;
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do{
            accountNumber = generateAccountNumber();
        } while(accountRepository
                .findByAccountNumber(accountNumber)
                .isPresent());

        return accountNumber;
    }

    private String generateAccountNumber() {
        Random random = new Random();

        StringBuilder accountNumber = new StringBuilder();

        for(int i= 0; i <= 12; i++) {
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }

    public User authenticateUser(String email, String password) {
        log.debug("Authenticating user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Incorrect email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        return user;
    }

    public CurrentUser getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)) {
            log.warn("Unauthorized access attempt");
            throw new UnauthorizedException("Unauthorized");
        }

        var user = userDetails.getUser();

        Client client = clientRepository.findByUser(user)
                .orElseThrow(() -> new ClientNotFoundException("Client not found"));

        return CurrentUser.builder()
                .id(user.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(user.getEmail())
                .build();
    }
}

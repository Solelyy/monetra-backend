package com.monetra.auth.service;

import com.monetra.account.entity.Account;
import com.monetra.auth.dto.CurrentUser;
import com.monetra.auth.dto.RegisterRequest;
import com.monetra.client.entity.Address;
import com.monetra.client.entity.Client;
import com.monetra.client.entity.ContactDetail;
import com.monetra.account.repository.AccountRepository;
import com.monetra.client.repository.AddressRepository;
import com.monetra.client.repository.ClientRepository;
import com.monetra.client.repository.ContactDetailRepository;
import com.monetra.security.details.CustomUserDetails;
import com.monetra.user.entity.User;
import com.monetra.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final ContactDetailRepository contactDetailRepository;
    private final AccountRepository accountRepository;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            AccountRepository accountRepository,
            ContactDetailRepository contactDetailRepository
            ) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.contactDetailRepository = contactDetailRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //Register
    @Transactional
    public User registerUser(RegisterRequest registerRequest) {

        //1. check if email exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        //2. check mobile number exists
        if (contactDetailRepository.findByMobileNumber(registerRequest
                .getMobileNumber())
                .isPresent()) {
            throw new RuntimeException("Mobile number already exists");
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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return user;
    }

    public CurrentUser getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        var user = userDetails.getUser();

        Client client = clientRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return CurrentUser.builder()
                .id(user.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(user.getEmail())
                .build();
    }
}

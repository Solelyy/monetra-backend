package com.monetra.repositories;

import com.monetra.models.ContactDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {
    Optional<ContactDetail> findByMobileNumber(String mobileNumber);
}

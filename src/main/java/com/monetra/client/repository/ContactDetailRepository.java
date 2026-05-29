package com.monetra.client.repository;

import com.monetra.client.entity.ContactDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactDetailRepository extends JpaRepository<ContactDetail, Long> {
    Optional<ContactDetail> findByMobileNumber(String mobileNumber);
}

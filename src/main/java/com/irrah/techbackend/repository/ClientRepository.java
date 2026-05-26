package com.irrah.techbackend.repository;

import com.irrah.techbackend.entity.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByDocumentId(String documentId);

    boolean existsByDocumentId(String documentId);
}

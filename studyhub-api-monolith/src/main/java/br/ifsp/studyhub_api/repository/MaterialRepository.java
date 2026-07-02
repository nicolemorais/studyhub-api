package br.ifsp.studyhub_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.studyhub_api.model.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
}
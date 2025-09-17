package com.example.fedex.repository;

import com.example.fedex.entity.shipment.LabelCreation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelCreationRepository extends JpaRepository<LabelCreation, Long> {
    Optional<LabelCreation> findByFileName(String fileName);

    @Query("SELECT lc FROM LabelCreation lc WHERE lc.labelId = :labelId")
    @EntityGraph(attributePaths = {"pdfContent"})
    Optional<LabelCreation> findByIdWithContent(@Param("labelId") Long labelId);
}

package com.moonwalk.ordereta.repository;

import com.moonwalk.ordereta.entity.EtaExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtaExecutionRepository extends JpaRepository<EtaExecution, Long> {
    List<EtaExecution> findByOrderIdOrderByTimestampAsc(Long orderId);
}

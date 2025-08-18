package com.konecta.internship.Restaurant_POS_System.payment.repositories;

import com.konecta.internship.Restaurant_POS_System.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByOrder_Id(Long orderId);
}

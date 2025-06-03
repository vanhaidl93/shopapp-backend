package com.hainguyen.shop.repositories;

import com.hainguyen.shop.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail,Long> {

   List<OrderDetail> findByOrderId(Long orderId);

   @Modifying
   @Transactional
   void deleteByOrderId(Long orderId);
}

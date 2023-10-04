package com.poly.beeshoes.repository;

import com.poly.beeshoes.entity.Voucher;
import com.poly.beeshoes.infrastructure.request.VoucherRequest;
import com.poly.beeshoes.infrastructure.response.VoucherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoucherRepository extends JpaRepository<Voucher, Long> {
    @Query(value = """
            SELECT v.id AS id,
            ROW_NUMBER() OVER(ORDER BY v.create_at DESC) AS indexs,
            v.code AS code, v.name AS name,
            v.quantity AS quantity, v.percent_reduce AS percentReduce,
            v.min_bill_value AS minBillValue,
            v.status AS status
            FROM voucher v
            WHERE (:#{#req.name} IS NULL OR :#{#req.name} = '' OR v.name LIKE %:#{#req.name}%)
            AND (:#{#req.deleted} IS NULL OR v.deleted = :#{#req.deleted})
            """, nativeQuery = true)
    Page<VoucherResponse> getAllVoucher(@Param("req") VoucherRequest request, Pageable pageable);
}

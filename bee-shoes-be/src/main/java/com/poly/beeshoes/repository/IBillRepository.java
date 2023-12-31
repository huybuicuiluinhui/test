package com.poly.beeshoes.repository;

import com.poly.beeshoes.dto.response.statistic.StatisticBillStatus;
import com.poly.beeshoes.entity.Bill;
import com.poly.beeshoes.dto.request.bill.BillSearchRequest;
import com.poly.beeshoes.dto.response.BillResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBillRepository extends JpaRepository<Bill, Long> {

    Boolean existsByCodeIgnoreCase(String code);

    @Query(value = """
            SELECT  b.id AS id,
            ROW_NUMBER() OVER(ORDER BY b.update_at DESC) AS indexs,
            b.code AS code, b.create_at AS createAt,
            cus.name AS customer,emp.name AS employee,
            b.address AS address,
            b.phone_number AS phoneNumber,
            b.total_money AS totalMoney,
            b.money_ship AS moneyShip,
            b.money_reduce AS moneyReduce,
            b.pay_date AS payDate,
            b.ship_date AS shipDate,
            b.desired_date AS desiredDate,
            b.receive_date AS receiveDate,
            b.type AS type,
            b.status AS status,
            b.note AS note,
            v.code AS voucher
            FROM bill b
            LEFT JOIN account emp ON emp.id = b.account_id
            LEFT JOIN account cus ON cus.id = b.customer_id
            LEFT JOIN voucher v ON v.id = b.voucher_id
            WHERE (:#{#req.code} IS NULL OR b.code LIKE %:#{#req.code}%)
            AND (:#{#req.idStaff} IS NULL OR b.account_id = :#{#req.idStaff})
            AND (:#{#req.status} IS NULL OR b.status = :#{#req.status})
            AND b.deleted = FALSE 
            """, nativeQuery = true)
    Page<BillResponse> getAll(@Param("req") BillSearchRequest request, Pageable pageable);

    @Query(value = """
            SELECT
                       CASE
                           WHEN status = 1 THEN 'Tạo đơn hàng'
                           WHEN status = 2 THEN 'Chờ xác nhận'
                           WHEN status = 3 THEN 'Xác nhận thông tin thanh toán'
                           WHEN status = 4 THEN 'Chờ giao'
                           WHEN status = 5 THEN 'Đang giao'
                           WHEN status = 6 THEN 'Hoàn thành'
                           WHEN status = 7 THEN 'Đã hủy'
                           ELSE 'Chờ thanh toán'
                       END AS statusName,
                       status as status,
                       COUNT(*) AS totalCount
                   FROM
                       bill
                   GROUP BY status
                   ORDER BY status
            """,nativeQuery = true)
    List<StatisticBillStatus> statisticBillStatus();

    @Query("""
            SELECT b
            FROM Bill b
            WHERE (:#{#req.code} IS NULL OR b.code LIKE %:#{#req.code}%)
            AND (:#{#req.idStaff} IS NULL OR b.account.id = :#{#req.idStaff})
            AND (:#{#req.status} IS NULL OR b.status = :#{#req.status})
            AND b.deleted = FALSE 
            ORDER BY b.createAt DESC
            """)
    Page<Bill> getAllBill(@Param("req") BillSearchRequest request, Pageable pageable);

    Boolean existsByCode(String code);

    Page<Bill> findByAccountIdAndStatusAndDeletedFalse(Long idAccount, Integer status, Pageable pageable);
}

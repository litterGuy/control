package com.xinshang.control.dao;

import com.xinshang.control.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperationDao extends JpaRepository<Operation, Integer> {
    @Query(value = "select ip from operation where account_id = :accountId group by ip order by count(1) limit :oftennum", nativeQuery = true)
    List<String> getOftenIpNum(String accountId, int oftennum);

    @Query(value = "select device_type from operation where account_id = :accountId group by device_type order by count(1) limit 1", nativeQuery = true)
    String getOftenDevice(String accountId);

    int countByAccountIdAndOperationTypeAndOperateTimeBetween(String accountId, String operationType, String startTime, String endTime);

    int countByAccountId(String accountId);
}

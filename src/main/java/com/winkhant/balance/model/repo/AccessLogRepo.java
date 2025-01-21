package com.winkhant.balance.model.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.winkhant.balance.model.domain.entity.AccessLog;

public interface AccessLogRepo extends JpaRepository<AccessLog,  Integer>, JpaSpecificationExecutor<AccessLog> {

}

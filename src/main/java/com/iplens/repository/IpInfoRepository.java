package com.iplens.repository;

import com.iplens.data.IpInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IpInfoRepository extends JpaRepository<IpInfo, Long> {
    Optional<IpInfo> findByIp(String ip);
}
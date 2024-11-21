package com.example.statsyncer.repositories;

import com.example.statsyncer.models.SalesAndTrafficByDate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SalesAndTrafficByDateRepository extends MongoRepository<SalesAndTrafficByDate, String> {
    List<SalesAndTrafficByDate> findByDateIn(List<String> dates);
}

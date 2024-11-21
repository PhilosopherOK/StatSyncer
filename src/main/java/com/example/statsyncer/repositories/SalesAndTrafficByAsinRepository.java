package com.example.statsyncer.repositories;

import com.example.statsyncer.models.SalesAndTrafficByAsin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SalesAndTrafficByAsinRepository extends MongoRepository<SalesAndTrafficByAsin, String> {
    List<SalesAndTrafficByAsin> findByParentAsinIn(List<String> parentAsin);
}

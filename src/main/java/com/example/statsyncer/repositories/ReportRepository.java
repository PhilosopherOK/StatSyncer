package com.example.statsyncer.repositories;

import com.example.statsyncer.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {
}


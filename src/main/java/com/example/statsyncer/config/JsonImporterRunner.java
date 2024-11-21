package com.example.statsyncer.config;

import com.example.statsyncer.repositories.UserRepository;
import com.example.statsyncer.services.RedisCacheService;
import com.example.statsyncer.services.ReportService;
import com.example.statsyncer.services.SalesAndTrafficByAsinService;
import com.example.statsyncer.services.SalesAndTrafficByDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JsonImporterRunner implements CommandLineRunner {

    private final ReportService reportService;
    private final RedisCacheService redisCacheService;
    private final SalesAndTrafficByAsinService salesAndTrafficByAsinService;
    private final SalesAndTrafficByDateService salesAndTrafficByDateService;
    private final UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();
    }

}


package com.example.statsyncer.services;

import com.example.statsyncer.models.Report;
import com.example.statsyncer.models.SalesAndTrafficByAsin;
import com.example.statsyncer.models.SalesAndTrafficByDate;
import com.example.statsyncer.repositories.ReportRepository;
import com.example.statsyncer.repositories.SalesAndTrafficByAsinRepository;
import com.example.statsyncer.repositories.SalesAndTrafficByDateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final RedisCacheService redisCacheService;
    private final ReportRepository reportRepository;
    private final SalesAndTrafficByDateRepository salesAndTrafficByDateRepository;
    private final SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository;

    // Конструктор для ініціалізації сервісів і репозиторіїв
    public ReportService(RedisCacheService redisCacheService, ReportRepository reportRepository,
                         SalesAndTrafficByDateRepository salesAndTrafficByDateRepository,
                         SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository) {
        this.redisCacheService = redisCacheService;
        this.reportRepository = reportRepository;
        this.salesAndTrafficByDateRepository = salesAndTrafficByDateRepository;
        this.salesAndTrafficByAsinRepository = salesAndTrafficByAsinRepository;
    }

    // Метод для збереження звіту із JSON файлу в базі даних
    public void saveReportFromJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Читання даних з JSON файлу та перетворення на об'єкт Report
            Report report = mapper.readValue(new File(filePath), Report.class);

            List<SalesAndTrafficByDate> salesAndTrafficByDateList = report.getSalesAndTrafficByDate();
            List<SalesAndTrafficByAsin> salesAndTrafficByAsinList = report.getSalesAndTrafficByAsin();
            report.setSalesAndTrafficByDate(null);
            report.setSalesAndTrafficByAsin(null);
            String reportId = reportRepository.save(report).getId();

            // Збереження даних по SalesAndTrafficByDate і додавання їхніх ID до звіту
            if (salesAndTrafficByDateList != null) {
                salesAndTrafficByDateList.forEach(date -> date.setReportId(reportId));
                List<String> dateIds = salesAndTrafficByDateRepository.saveAll(salesAndTrafficByDateList).stream().map(SalesAndTrafficByDate::getId).collect(Collectors.toList());
                report.setSalesAndTrafficByDateIds(dateIds);
            }

            // Збереження даних по SalesAndTrafficByAsin і додавання їхніх ID до звіту
            if (salesAndTrafficByAsinList != null) {
                salesAndTrafficByAsinList.forEach(asin -> asin.setReportId(reportId));
                List<String> asinIds = salesAndTrafficByAsinRepository.saveAll(salesAndTrafficByAsinList).stream().map(SalesAndTrafficByAsin::getId).collect(Collectors.toList());
                report.setSalesAndTrafficByAsinIds(asinIds);
            }
            // Збереження оновленого звіту з ID записів
            reportRepository.save(report);

        } catch (Exception e) {
            e.printStackTrace(); // Логування помилки
        }
    }

    // Метод для отримання всіх звітів з бази даних
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // Метод для видалення всіх звітів з бази даних
    public void deleteAll() {
        reportRepository.deleteAll();
    }
}

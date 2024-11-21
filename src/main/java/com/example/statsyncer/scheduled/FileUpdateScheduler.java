package com.example.statsyncer.scheduled;

import com.example.statsyncer.services.RedisCacheService;
import com.example.statsyncer.services.ReportService;
import com.example.statsyncer.services.SalesAndTrafficByAsinService;
import com.example.statsyncer.services.SalesAndTrafficByDateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Component
public class FileUpdateScheduler {

    private final ReportService reportService;
    private final RedisCacheService redisCacheService;
    private final SalesAndTrafficByAsinService salesAndTrafficByAsinService;
    private final SalesAndTrafficByDateService salesAndTrafficByDateService;
    private final String filePath;
    private long lastModifiedTime = 0;

    public FileUpdateScheduler(ReportService reportService, RedisCacheService redisCacheService,
                               SalesAndTrafficByAsinService salesAndTrafficByAsinService,
                               SalesAndTrafficByDateService salesAndTrafficByDateService) throws IOException {
        this.salesAndTrafficByAsinService = salesAndTrafficByAsinService;
        this.salesAndTrafficByDateService = salesAndTrafficByDateService;
        String projectRootPath = Paths.get("").toAbsolutePath().toString();
        filePath = projectRootPath + "/test_report.json";
        this.reportService = reportService;
        this.redisCacheService = redisCacheService;
    }

    //Оновлення даних із файлу кожні 5 хвилин
    @Scheduled(fixedRate = 300000)
    public void scheduledUpdateOfDataFromAFile() {
        cleaningAndUpdatingTheDatabaseAndCache();
    }

    //перевірка оновлення файлу, при позитивній перевірці бд оновлюється із файлу кожні 10 секунд
    @Scheduled(fixedRate = 10000)
    public void checkAndUpdateData() throws IOException {
        if (isFileChanged(filePath)) {
            cleaningAndUpdatingTheDatabaseAndCache();
            System.out.println("файл було змінено, база данних оновлена");
        }
    }

    private void cleaningAndUpdatingTheDatabaseAndCache() {
        reportService.deleteAll();
        salesAndTrafficByAsinService.deleteAll();
        salesAndTrafficByDateService.deleteAll();
        reportService.saveReportFromJson(filePath);
        redisCacheService.deleteAll();
    }

    //метод перевірки оновлення файлу
    private boolean isFileChanged(String filePath) {
        File file = new File(filePath);
        long currentModifiedTime = file.lastModified();

        if (currentModifiedTime != lastModifiedTime) {
            lastModifiedTime = currentModifiedTime;
            return true;
        }
        return false;
    }

}

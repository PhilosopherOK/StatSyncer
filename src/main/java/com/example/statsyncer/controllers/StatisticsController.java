package com.example.statsyncer.controllers;

import com.example.statsyncer.models.SalesAndTrafficByAsin;
import com.example.statsyncer.models.SalesAndTrafficByDate;
import com.example.statsyncer.services.RedisCacheService;
import com.example.statsyncer.services.SalesAndTrafficByAsinService;
import com.example.statsyncer.services.SalesAndTrafficByDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контролер для роботи зі статистичними даними продажів та трафіку.
 */
@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticsController {

    private final RedisCacheService redisCacheService;
    private final SalesAndTrafficByDateService salesAndTrafficByDateService;
    private final SalesAndTrafficByAsinService salesAndTrafficByAsinService;

    /**
     * Отримання статистики продажів і трафіку за заданими датами.
     * @param dates Список дат, для яких потрібно отримати дані.
     * @return Відповідь із даними або збереженими у кеші, або отриманими з бази даних.
     */
    @PostMapping("/getByDate")
    public ResponseEntity getSalesAndTrafficByDate(@RequestBody List<String> dates) {
        dates = dates.stream().distinct().collect(Collectors.toList()); // Видалення дублікатів
        StringBuilder sb = new StringBuilder();
        for (String date : dates) {
            sb.append(date);
        }
        if (redisCacheService.existsInCache(sb.toString())) {
            return ResponseEntity.ok(redisCacheService.getListFromCache(sb.toString(), SalesAndTrafficByDate.class));
        } else {
            List<SalesAndTrafficByDate> salesAndTrafficByDate = salesAndTrafficByDateService.findByDate(dates);
            for (SalesAndTrafficByDate salesAndTraffic : salesAndTrafficByDate) {
                redisCacheService.saveListToCache(sb.toString(), salesAndTraffic, SalesAndTrafficByDate.class);
            }
            return ResponseEntity.ok(salesAndTrafficByDate);
        }
    }

    /**
     * Отримання загальної статистики продажів і трафіку за всі дати.
     * @return Загальна статистика, отримана з кешу або з бази даних.
     */
    @GetMapping("/getTotalStatisticByDate")
    public ResponseEntity getTotalSalesAndTrafficByAllDate() {
        if (redisCacheService.existsInCache("totalByDate")) {
            return ResponseEntity.ok(redisCacheService.getFromCache("totalByDate", SalesAndTrafficByDate.class));
        } else {
            SalesAndTrafficByDate summarizedSalesAndTraffic = salesAndTrafficByDateService.getSummarizedSalesAndTraffic();
            redisCacheService.saveToCache("totalByDate", summarizedSalesAndTraffic);
            return ResponseEntity.ok(summarizedSalesAndTraffic);
        }
    }

    /**
     * Отримання статистики продажів і трафіку за заданими ASIN.
     * @param asins Список ASIN, для яких потрібно отримати дані.
     * @return Відповідь із даними або збереженими у кеші, або отриманими з бази даних.
     */
    @PostMapping("/getByAsin")
    public ResponseEntity getSalesAndTrafficByAsin(@RequestBody List<String> asins) {
        asins = asins.stream().distinct().collect(Collectors.toList()); // Видалення дублікатів
        StringBuilder sb = new StringBuilder();
        for (String asin : asins) {
            sb.append(asin);
        }
        if (redisCacheService.existsInCache(sb.toString())) {
            return ResponseEntity.ok(redisCacheService.getListFromCache(sb.toString(), SalesAndTrafficByAsin.class));
        } else {
            List<SalesAndTrafficByAsin> SalesAndTrafficByAsin = salesAndTrafficByAsinService.findByParentAsin(asins);
            for (SalesAndTrafficByAsin salesAndTraffic : SalesAndTrafficByAsin) {
                redisCacheService.saveListToCache(sb.toString(), salesAndTraffic, SalesAndTrafficByAsin.class);
            }
            return ResponseEntity.ok(SalesAndTrafficByAsin);
        }
    }

    /**
     * Отримання загальної статистики продажів і трафіку за всі ASIN.
     * @return Загальна статистика, отримана з кешу або з бази даних.
     */
    @GetMapping("/getTotalStatisticByAsin")
    public ResponseEntity getTotalSalesAndTrafficByAllAsin() {
        if (redisCacheService.existsInCache("totalByAsin")) {
            return ResponseEntity.ok(redisCacheService.getFromCache("totalByAsin", SalesAndTrafficByAsin.class));
        } else {
            SalesAndTrafficByAsin summarizedSalesAndTraffic = salesAndTrafficByAsinService.getSummarizedSalesAndTraffic();
            redisCacheService.saveToCache("totalByAsin", summarizedSalesAndTraffic);
            return ResponseEntity.ok(summarizedSalesAndTraffic);
        }
    }
}


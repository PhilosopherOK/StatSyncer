package com.example.statsyncer.services;

import com.example.statsyncer.models.*;
import com.example.statsyncer.repositories.SalesAndTrafficByDateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAndTrafficByDateService {

    private final SalesAndTrafficByDateRepository salesAndTrafficByDateRepository;

    // Конструктор сервісу, який ініціалізує репозиторій
    public SalesAndTrafficByDateService(SalesAndTrafficByDateRepository salesAndTrafficByDateRepository) {
        this.salesAndTrafficByDateRepository = salesAndTrafficByDateRepository;
    }

    // Метод для пошуку даних за списком дат
    public List<SalesAndTrafficByDate> findByDate(List<String> dates) {
        return salesAndTrafficByDateRepository.findByDateIn(dates);
    }

    // Метод для видалення всіх записів з таблиці
    public void deleteAll() {
        salesAndTrafficByDateRepository.deleteAll();
    }

    // Метод для отримання підсумкових даних продажу та трафіку
    public SalesAndTrafficByDate getSummarizedSalesAndTraffic() {
        List<SalesAndTrafficByDate> salesAndTrafficList = salesAndTrafficByDateRepository.findAll();

        // Створюємо підсумковий об'єкт зберігання сум
        SalesAndTrafficByDate summary = new SalesAndTrafficByDate();
        summary.setDate("Total Summary");

        // Підсумовуємо дані
        SalesByDate totalSales = new SalesByDate();
        TrafficByDate totalTraffic = new TrafficByDate();
        for (SalesAndTrafficByDate item : salesAndTrafficList) {
            // Підсумовуємо SalesByDate
            SalesByDate sales = item.getSalesByDate();
            sumSales(totalSales, sales);

            // Підсумовуємо TrafficByDate
            TrafficByDate traffic = item.getTrafficByDate();
            sumTraffic(totalTraffic, traffic);
        }

        // Встановлюємо підсумкові значення
        summary.setSalesByDate(totalSales);
        summary.setTrafficByDate(totalTraffic);
        return summary;
    }

    // Метод для підсумовування даних по продажам
    private SalesByDate sumSales(SalesByDate totalSales, SalesByDate sales) {
        totalSales.setOrderedProductSales(sumAmounts(totalSales.getOrderedProductSales(), sales.getOrderedProductSales()));
        totalSales.setOrderedProductSalesB2B(sumAmounts(totalSales.getOrderedProductSalesB2B(), sales.getOrderedProductSalesB2B()));
        totalSales.setUnitsOrdered(totalSales.getUnitsOrdered() + sales.getUnitsOrdered());
        totalSales.setUnitsOrderedB2B(totalSales.getUnitsOrderedB2B() + sales.getUnitsOrderedB2B());
        totalSales.setTotalOrderItems(totalSales.getTotalOrderItems() + sales.getTotalOrderItems());
        totalSales.setTotalOrderItemsB2B(totalSales.getTotalOrderItemsB2B() + sales.getTotalOrderItemsB2B());
        totalSales.setAverageSalesPerOrderItem(sumAmounts(totalSales.getAverageSalesPerOrderItem(), sales.getAverageSalesPerOrderItem()));
        totalSales.setAverageSalesPerOrderItemB2B(sumAmounts(totalSales.getAverageSalesPerOrderItemB2B(), sales.getAverageSalesPerOrderItemB2B()));
        totalSales.setAverageUnitsPerOrderItem(totalSales.getAverageUnitsPerOrderItem() + sales.getAverageUnitsPerOrderItem());
        totalSales.setAverageUnitsPerOrderItemB2B(totalSales.getAverageUnitsPerOrderItemB2B() + sales.getAverageUnitsPerOrderItemB2B());
        totalSales.setAverageSellingPrice(sumAmounts(totalSales.getAverageSellingPrice(), sales.getAverageSellingPrice()));
        totalSales.setAverageSellingPriceB2B(sumAmounts(totalSales.getAverageSellingPriceB2B(), sales.getAverageSellingPriceB2B()));
        totalSales.setUnitsRefunded(totalSales.getUnitsRefunded() + sales.getUnitsRefunded());
        totalSales.setRefundRate(totalSales.getRefundRate() + sales.getRefundRate());
        totalSales.setClaimsGranted(totalSales.getClaimsGranted() + sales.getClaimsGranted());
        totalSales.setClaimsAmount(sumAmounts(totalSales.getClaimsAmount(), sales.getClaimsAmount()));
        totalSales.setShippedProductSales(sumAmounts(totalSales.getShippedProductSales(), sales.getShippedProductSales()));
        totalSales.setUnitsShipped(totalSales.getUnitsShipped() + sales.getUnitsShipped());
        totalSales.setOrdersShipped(totalSales.getOrdersShipped() + sales.getOrdersShipped());
        return totalSales;
    }

    // Метод для підсумовування даних по трафіку
    private TrafficByDate sumTraffic(TrafficByDate totalTraffic, TrafficByDate traffic) {
        totalTraffic.setBrowserPageViews(totalTraffic.getBrowserPageViews() + traffic.getBrowserPageViews());
        totalTraffic.setBrowserPageViewsB2B(totalTraffic.getBrowserPageViewsB2B() + traffic.getBrowserPageViewsB2B());
        totalTraffic.setMobileAppPageViews(totalTraffic.getMobileAppPageViews() + traffic.getMobileAppPageViews());
        totalTraffic.setMobileAppPageViewsB2B(totalTraffic.getMobileAppPageViewsB2B() + traffic.getMobileAppPageViewsB2B());
        totalTraffic.setPageViews(totalTraffic.getPageViews() + traffic.getPageViews());
        totalTraffic.setPageViewsB2B(totalTraffic.getPageViewsB2B() + traffic.getPageViewsB2B());
        totalTraffic.setBrowserSessions(totalTraffic.getBrowserSessions() + traffic.getBrowserSessions());
        totalTraffic.setBrowserSessionsB2B(totalTraffic.getBrowserSessionsB2B() + traffic.getBrowserSessionsB2B());
        totalTraffic.setMobileAppSessions(totalTraffic.getMobileAppSessions() + traffic.getMobileAppSessions());
        totalTraffic.setMobileAppSessionsB2B(totalTraffic.getMobileAppSessionsB2B() + traffic.getMobileAppSessionsB2B());
        totalTraffic.setSessions(totalTraffic.getSessions() + traffic.getSessions());
        totalTraffic.setSessionsB2B(totalTraffic.getSessionsB2B() + traffic.getSessionsB2B());
        totalTraffic.setBuyBoxPercentage(totalTraffic.getBuyBoxPercentage() + traffic.getBuyBoxPercentage());
        totalTraffic.setBuyBoxPercentageB2B(totalTraffic.getBuyBoxPercentageB2B() + traffic.getBuyBoxPercentageB2B());
        totalTraffic.setOrderItemSessionPercentage(totalTraffic.getOrderItemSessionPercentage() + traffic.getOrderItemSessionPercentage());
        totalTraffic.setOrderItemSessionPercentageB2B(totalTraffic.getOrderItemSessionPercentageB2B() + traffic.getOrderItemSessionPercentageB2B());
        totalTraffic.setUnitSessionPercentage(totalTraffic.getUnitSessionPercentage() + traffic.getUnitSessionPercentage());
        totalTraffic.setUnitSessionPercentageB2B(totalTraffic.getUnitSessionPercentageB2B() + traffic.getUnitSessionPercentageB2B());
        totalTraffic.setAverageOfferCount(totalTraffic.getAverageOfferCount() + traffic.getAverageOfferCount());
        totalTraffic.setAverageParentItems(totalTraffic.getAverageParentItems() + traffic.getAverageParentItems());
        totalTraffic.setFeedbackReceived(totalTraffic.getFeedbackReceived() + traffic.getFeedbackReceived());
        totalTraffic.setNegativeFeedbackReceived(totalTraffic.getNegativeFeedbackReceived() + traffic.getNegativeFeedbackReceived());
        totalTraffic.setReceivedNegativeFeedbackRate(totalTraffic.getReceivedNegativeFeedbackRate() + traffic.getReceivedNegativeFeedbackRate());

        return totalTraffic;
    }

    // Метод для підсумовування Amount
    private Amount sumAmounts(Amount total, Amount addition) {
        if (total == null) {
            total = new Amount();
            total.setCurrencyCode(addition.getCurrencyCode());
        }
        if (addition == null) {
            addition = new Amount();
        }
        total.setAmount(total.getAmount() + addition.getAmount());
        return total;
    }
}


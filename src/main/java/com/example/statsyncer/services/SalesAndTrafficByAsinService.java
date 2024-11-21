package com.example.statsyncer.services;

import com.example.statsyncer.models.*;
import com.example.statsyncer.repositories.SalesAndTrafficByAsinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesAndTrafficByAsinService {

    private final SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository;

    public SalesAndTrafficByAsinService(SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository) {
        this.salesAndTrafficByAsinRepository = salesAndTrafficByAsinRepository;
    }

    public void deleteAll(){
        salesAndTrafficByAsinRepository.deleteAll();
    }

    public List<SalesAndTrafficByAsin> findByParentAsin(List<String> parentAsin){
        return salesAndTrafficByAsinRepository.findByParentAsinIn(parentAsin);
    }

    public SalesAndTrafficByAsin getSummarizedSalesAndTraffic() {
        List<SalesAndTrafficByAsin> salesAndTrafficList = salesAndTrafficByAsinRepository.findAll();

        SalesAndTrafficByAsin summary = new SalesAndTrafficByAsin();
        summary.setParentAsin("Total Summary");

        SalesByAsin totalSales = new SalesByAsin();
        TrafficByAsin totalTraffic = new TrafficByAsin();

        for (SalesAndTrafficByAsin item : salesAndTrafficList) {
            SalesByAsin sales = item.getSalesByAsin();
            sumSales(totalSales, sales);

            TrafficByAsin traffic = item.getTrafficByAsin();
            sumTraffic(totalTraffic, traffic);
        }

        summary.setSalesByAsin(totalSales);
        summary.setTrafficByAsin(totalTraffic);
        return summary;
    }


    private SalesByAsin sumSales(SalesByAsin totalSales, SalesByAsin sales){
        totalSales.setOrderedProductSales(sumAmounts(totalSales.getOrderedProductSales(), sales.getOrderedProductSales()));
        totalSales.setOrderedProductSalesB2B(sumAmounts(totalSales.getOrderedProductSalesB2B(), sales.getOrderedProductSalesB2B()));
        totalSales.setUnitsOrdered(totalSales.getUnitsOrdered() + sales.getUnitsOrdered());
        totalSales.setUnitsOrderedB2B(totalSales.getUnitsOrderedB2B() + sales.getUnitsOrderedB2B());
        totalSales.setTotalOrderItems(totalSales.getTotalOrderItems() + sales.getTotalOrderItems());
        totalSales.setTotalOrderItemsB2B(totalSales.getTotalOrderItemsB2B() + sales.getTotalOrderItemsB2B());
        return totalSales;
    }

    private TrafficByAsin sumTraffic(TrafficByAsin totalTraffic, TrafficByAsin traffic){
        totalTraffic.setBrowserSessions(totalTraffic.getBrowserSessions() + traffic.getBrowserSessions());
        totalTraffic.setBrowserSessionsB2B(totalTraffic.getBrowserSessionsB2B() + traffic.getBrowserSessionsB2B());
        totalTraffic.setMobileAppSessions(totalTraffic.getMobileAppSessions() + traffic.getMobileAppSessions());
        totalTraffic.setMobileAppSessionsB2B(totalTraffic.getMobileAppSessionsB2B() + traffic.getMobileAppSessionsB2B());
        totalTraffic.setSessions(totalTraffic.getSessions() + traffic.getSessions());
        totalTraffic.setSessionsB2B(totalTraffic.getSessionsB2B() + traffic.getSessionsB2B());
        totalTraffic.setBrowserSessionPercentage(totalTraffic.getBrowserSessionPercentage() + traffic.getBrowserSessionPercentage());
        totalTraffic.setBrowserSessionPercentageB2B(totalTraffic.getBrowserSessionPercentageB2B() + traffic.getBrowserSessionPercentageB2B());
        totalTraffic.setMobileAppSessionPercentage(totalTraffic.getMobileAppSessionPercentage() + traffic.getMobileAppSessionPercentage());
        totalTraffic.setMobileAppSessionPercentageB2B(totalTraffic.getMobileAppSessionPercentageB2B() + traffic.getMobileAppSessionPercentageB2B());
        totalTraffic.setSessionPercentage(totalTraffic.getSessionPercentage() + traffic.getSessionPercentage());
        totalTraffic.setSessionPercentageB2B(totalTraffic.getSessionPercentageB2B() + traffic.getSessionPercentageB2B());
        totalTraffic.setBrowserPageViews(totalTraffic.getBrowserPageViews() + traffic.getBrowserPageViews());
        totalTraffic.setBrowserPageViewsB2B(totalTraffic.getBrowserPageViewsB2B() + traffic.getBrowserPageViewsB2B());
        totalTraffic.setMobileAppPageViews(totalTraffic.getMobileAppPageViews() + traffic.getMobileAppPageViews());
        totalTraffic.setMobileAppPageViewsB2B(totalTraffic.getMobileAppPageViewsB2B() + traffic.getMobileAppPageViewsB2B());
        totalTraffic.setPageViews(totalTraffic.getPageViews() + traffic.getPageViews());
        totalTraffic.setPageViewsB2B(totalTraffic.getPageViewsB2B() + traffic.getPageViewsB2B());
        totalTraffic.setBuyBoxPercentage(totalTraffic.getBuyBoxPercentage() + traffic.getBuyBoxPercentage());
        totalTraffic.setBuyBoxPercentageB2B(totalTraffic.getBuyBoxPercentageB2B() + traffic.getBuyBoxPercentageB2B());
        totalTraffic.setUnitSessionPercentage(totalTraffic.getUnitSessionPercentage() + traffic.getUnitSessionPercentage());
        totalTraffic.setUnitSessionPercentageB2B(totalTraffic.getUnitSessionPercentageB2B() + traffic.getUnitSessionPercentageB2B());
        return totalTraffic;
    }

    private Amount sumAmounts(Amount total, Amount addition) {
        if (total == null){
            total = new Amount();
            total.setCurrencyCode(addition.getCurrencyCode());
        }
        if (addition == null){
            addition = new Amount();
        }
        total.setAmount(total.getAmount() + addition.getAmount());
        return total;
    }
}

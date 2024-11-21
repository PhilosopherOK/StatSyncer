package com.example.statsyncer.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class SalesAndTrafficByDate {
    @Id
    private String id;
    private String date;
    private SalesByDate salesByDate;
    private TrafficByDate trafficByDate;
    private String reportId;

    public SalesAndTrafficByDate(String date, SalesByDate salesByDate, TrafficByDate trafficByDate, String reportId) {
        this.date = date;
        this.salesByDate = salesByDate;
        this.trafficByDate = trafficByDate;
        this.reportId = reportId;
    }
}

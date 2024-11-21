package com.example.statsyncer.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class SalesAndTrafficByAsin {
    @Id
    private String id;
    private String parentAsin;
    private SalesByAsin salesByAsin;
    private TrafficByAsin trafficByAsin;
    private String reportId;

    public SalesAndTrafficByAsin(String parentAsin, SalesByAsin salesByAsin, TrafficByAsin trafficByAsin, String reportId) {
        this.parentAsin = parentAsin;
        this.salesByAsin = salesByAsin;
        this.trafficByAsin = trafficByAsin;
        this.reportId = reportId;
    }
}
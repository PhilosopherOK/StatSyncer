package com.example.statsyncer.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@NoArgsConstructor
@Data
@Document(collection = "reports")
public class Report {

    @Id
    private String id;
    private ReportSpecification reportSpecification;
    private List<SalesAndTrafficByDate> salesAndTrafficByDate;
    private List<SalesAndTrafficByAsin> salesAndTrafficByAsin;
    private List<String> salesAndTrafficByDateIds;
    private List<String> salesAndTrafficByAsinIds;

    public Report(ReportSpecification reportSpecification, List<SalesAndTrafficByDate> salesAndTrafficByDate, List<SalesAndTrafficByAsin> salesAndTrafficByAsin, List<String> salesAndTrafficByDateIds, List<String> salesAndTrafficByAsinIds) {
        this.reportSpecification = reportSpecification;
        this.salesAndTrafficByDate = salesAndTrafficByDate;
        this.salesAndTrafficByAsin = salesAndTrafficByAsin;
        this.salesAndTrafficByDateIds = salesAndTrafficByDateIds;
        this.salesAndTrafficByAsinIds = salesAndTrafficByAsinIds;
    }
}

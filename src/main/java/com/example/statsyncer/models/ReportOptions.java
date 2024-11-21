package com.example.statsyncer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOptions {
    private String dateGranularity;
    private String asinGranularity;
}

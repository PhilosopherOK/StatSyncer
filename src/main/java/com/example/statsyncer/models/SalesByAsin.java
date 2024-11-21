package com.example.statsyncer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesByAsin {
    private int unitsOrdered;
    private int unitsOrderedB2B;
    private Amount orderedProductSales;
    private Amount orderedProductSalesB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
}
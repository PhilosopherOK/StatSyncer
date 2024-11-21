package com.example.statsyncer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesByDate {
    private Amount orderedProductSales;
    private Amount orderedProductSalesB2B;
    private int unitsOrdered;
    private int unitsOrderedB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
    private Amount averageSalesPerOrderItem;
    private Amount averageSalesPerOrderItemB2B;
    private double averageUnitsPerOrderItem;
    private double averageUnitsPerOrderItemB2B;
    private Amount averageSellingPrice;
    private Amount averageSellingPriceB2B;
    private int unitsRefunded;
    private double refundRate;
    private int claimsGranted;
    private Amount claimsAmount;
    private Amount shippedProductSales;
    private int unitsShipped;
    private int ordersShipped;
}
package com.grash.dto.fastSpring;

import lombok.Data;

import java.util.ArrayList;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */

@Data
public class OrderDTO {
    public ArrayList<Event> events;

    @Data
    public static class Withholdings {
        public boolean taxWithholdings;
    }

    @Data
    public static class Address {
        public String country;
        public String display;
    }

    @Data
    public static class Customer {
        public String first;
        public String last;
        public String email;
        public Object company;
        public String phone;
        public boolean subscribed;
    }

    @Data
    public static class Data1 {
        public String order;
        public String id;
        public String reference;
        public Object buyerReference;
        public String ipAddress;
        public boolean completed;
        public long changed;
        public long changedValue;
        public int changedInSeconds;
        public String changedDisplay;
        public String changedDisplayISO8601;
        public String language;
        public boolean live;
        public String currency;
        public String payoutCurrency;
        public Object quote;
        public String invoiceUrl;
        public String account;
        public int total;
        public String totalDisplay;
        public int totalInPayoutCurrency;
        public String totalInPayoutCurrencyDisplay;
        public int tax;
        public String taxDisplay;
        public int taxInPayoutCurrency;
        public String taxInPayoutCurrencyDisplay;
        public int subtotal;
        public String subtotalDisplay;
        public int subtotalInPayoutCurrency;
        public String subtotalInPayoutCurrencyDisplay;
        public int discount;
        public String discountDisplay;
        public int discountInPayoutCurrency;
        public String discountInPayoutCurrencyDisplay;
        public int discountWithTax;
        public String discountWithTaxDisplay;
        public int discountWithTaxInPayoutCurrency;
        public String discountWithTaxInPayoutCurrencyDisplay;
        public String billDescriptor;
        public Payment payment;
        public Customer customer;
        public Address address;
        public ArrayList<Recipient> recipients;
        public Tags tags;
        public ArrayList<Object> notes;
        public ArrayList<Item> items;
    }

    @Data
    public static class Driver {
        public String type;
        public String path;
    }

    @Data
    public static class Event {
        public String id;
        public boolean processed;
        public long created;
        public String type;
        public boolean live;
        public Data1 data;
    }

    @Data
    public static class Tags {
        public long userId;
    }

    @Data
    public static class Fulfillments {
    }

    @Data
    public static class Item {
        public String product;
        public int quantity;
        public String display;
        public Object sku;
        public Object imageUrl;
        public int subtotal;
        public String subtotalDisplay;
        public int subtotalInPayoutCurrency;
        public String subtotalInPayoutCurrencyDisplay;
        public int discount;
        public String discountDisplay;
        public int discountInPayoutCurrency;
        public String discountInPayoutCurrencyDisplay;
        public String subscription;
        public Fulfillments fulfillments;
        public Driver driver;
        public Withholdings withholdings;
    }

    @Data
    public static class Payment {
        public String type;
        public String cardEnding;
    }

    @Data
    public static class Recipient {
        public Recipient recipient;
    }

    @Data
    public static class Recipient2 {
        public String first;
        public String last;
        public String email;
        public Object company;
        public String phone;
        public boolean subscribed;
        public String account;
        public Address address;
    }

}

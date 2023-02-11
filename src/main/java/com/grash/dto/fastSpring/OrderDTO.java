package com.grash.dto.fastSpring;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OrderDTO {
    public ArrayList<Event> events;

    @lombok.Data
    public static class Account {
        public String id;
        public String account;
        public Contact contact;
        public Address address;
        public String language;
        public String country;
        public Lookup lookup;
        public String url;
    }

    @lombok.Data
    public static class Address {
        public Object city;
        public String country;
        public Object postalCode;
        public Object region;
        public Object company;
        public String display;
    }

    @lombok.Data
    public static class Contact {
        public String first;
        public String last;
        public String email;
        public Object company;
        public String phone;
        public boolean subscribed;
    }

    @lombok.Data
    public static class Customer {
        public String first;
        public String last;
        public String email;
        public Object company;
        public String phone;
        public boolean subscribed;
    }

    @lombok.Data
    public static class Data {
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
        public Account account;
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

    @lombok.Data
    public static class Event {
        public String id;
        public boolean processed;
        public long created;
        public String type;
        public boolean live;
        public Data data;
    }

    @lombok.Data
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
        public Subscription subscription;
        public Fulfillments fulfillments;
        public Withholdings withholdings;
    }

    @lombok.Data
    public static class Lookup {
        public String global;
    }

    @lombok.Data
    public static class Payment {
        public String type;
        public String cardEnding;
    }


    @lombok.Data
    public static class Recipient {
        public Recipient recipient;
    }

    @lombok.Data
    public static class Recipient2 {
        public String first;
        public String last;
        public String email;
        public Object company;
        public String phone;
        public boolean subscribed;
        public Account account;
        public Address address;
    }

    @lombok.Data
    public static class Tags {
        public int userId;
    }

    @lombok.Data
    public static class Withholdings {
        public boolean taxWithholdings;
    }


}

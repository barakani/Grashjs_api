package com.grash.dto.fastSpring;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Subscription {
    public String id;
    public Object quote;
    public String subscription;
    public boolean active;
    public String state;
    public long changed;
    public long changedValue;
    public int changedInSeconds;
    public String changedDisplay;
    public String changedDisplayISO8601;
    public boolean live;
    public String currency;
    public String account;
    public String product;
    public Object sku;
    public String display;
    public int quantity;
    public boolean adhoc;
    public boolean autoRenew;
    public int price;
    public String priceDisplay;
    public int priceInPayoutCurrency;
    public String priceInPayoutCurrencyDisplay;
    public int discount;
    public String discountDisplay;
    public int discountInPayoutCurrency;
    public String discountInPayoutCurrencyDisplay;
    public int subtotal;
    public String subtotalDisplay;
    public int subtotalInPayoutCurrency;
    public String subtotalInPayoutCurrencyDisplay;
    public Tags tags;
    public long next;
    public long nextValue;
    public int nextInSeconds;
    public String nextDisplay;
    public String nextDisplayISO8601;
    public Long end;
    public Long endValue;
    public Long endInSeconds;
    public String endDisplay;
    public String endDisplayISO8601;
    public Long canceledDate;
    public Long canceledDateValue;
    public Long canceledDateInSeconds;
    public String canceledDateDisplay;
    public String canceledDateDisplayISO8601;
    public Long deactivationDate;
    public Long deactivationDateValue;
    public Long deactivationDateInSeconds;
    public String deactivationDateDisplay;
    public String deactivationDateDisplayISO8601;
    public int sequence;
    public Long periods;
    public Long remainingPeriods;
    public long begin;
    public long beginValue;
    public int beginInSeconds;
    public String beginDisplay;
    public String beginDisplayISO8601;
    public String intervalUnit;
    public int intervalLength;
    public String nextChargeCurrency;
    public long nextChargeDate;
    public long nextChargeDateValue;
    public int nextChargeDateInSeconds;
    public String nextChargeDateDisplay;
    public String nextChargeDateDisplayISO8601;
    public int nextChargePreTax;
    public String nextChargePreTaxDisplay;
    public int nextChargePreTaxInPayoutCurrency;
    public String nextChargePreTaxInPayoutCurrencyDisplay;
    public int nextChargeTotal;
    public String nextChargeTotalDisplay;
    public int nextChargeTotalInPayoutCurrency;
    public String nextChargeTotalInPayoutCurrencyDisplay;
    public String nextNotificationType;
    public long nextNotificationDate;
    public long nextNotificationDateValue;
    public int nextNotificationDateInSeconds;
    public String nextNotificationDateDisplay;
    public String nextNotificationDateDisplayISO8601;
    public PaymentReminder paymentReminder;
    public PaymentOverdue paymentOverdue;
    public CancellationSetting cancellationSetting;
    public Fulfillments fulfillments;
    public ArrayList<Instruction> instructions;
    public String initialOrderId;
    public String initialOrderReference;


    @lombok.Data
    public static class PaymentOverdue {
        public String intervalUnit;
        public int intervalLength;
        public int total;
        public int sent;
    }

    @lombok.Data
    public static class PaymentReminder {
        public String intervalUnit;
        public int intervalLength;
    }

    @lombok.Data
    public static class CancellationSetting {
        public String cancellation;
        public String intervalUnit;
        public int intervalLength;
    }
}

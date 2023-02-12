package com.grash.dto.fastSpring.payloads;

import com.grash.dto.fastSpring.*;
import lombok.Data;

import java.util.ArrayList;

@Data
public class DeactivatedPayload {
    public String id;
    public String quote;
    public String subscription;
    public boolean active;
    public String state;
    public long changed;
    public long changedValue;
    public int changedInSeconds;
    public String changedDisplay;
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
    public long next;
    public long nextValue;
    public int nextInSeconds;
    public String nextDisplay;
    public long end;
    public long endValue;
    public int endInSeconds;
    public String endDisplay;
    public long canceledDate;
    public long canceledDateValue;
    public int canceledDateInSeconds;
    public String canceledDateDisplay;
    public Long deactivationDate;
    public Long deactivationDateValue;
    public Long deactivationDateInSeconds;
    public String deactivationDateDisplay;
    public int sequence;
    public int periods;
    public int remainingPeriods;
    public long begin;
    public long beginValue;
    public int beginInSeconds;
    public String beginDisplay;
    public String intervalUnit;
    public int intervalLength;
    public PaymentReminder paymentReminder;
    public PaymentOverdue paymentOverdue;
    public CancellationSetting cancellationSetting;
    public Fulfillments fulfillments;
    public ArrayList<Instruction> instructions;
}
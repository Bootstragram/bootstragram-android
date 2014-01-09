package com.bootstragram.demo.currencyrates;

public class CurrencyRate {
    private String currency;
    private String rateString;
    private Double rate;

    public CurrencyRate(String currency, String rateString) {
        super();

        this.currency = currency;
        this.rateString = rateString;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRateString() {
        return rateString;
    }

    public void setRateString(String rateString) {
        this.rateString = rateString;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String toString() {
        return getCurrency() + ": " + getRateString();
    }
}

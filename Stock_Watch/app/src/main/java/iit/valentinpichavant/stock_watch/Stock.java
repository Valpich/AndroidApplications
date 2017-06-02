package iit.valentinpichavant.stock_watch;

import java.io.Serializable;

/**
 * Created by valentinpichavant on 2/28/17.
 */

public class Stock implements Serializable, Comparable<Stock> {

    private String symbol;
    private String company;
    private Double lastTradePrice;
    private Double priceChangeAmmount;
    private Double priceChangePercentage;

    public Stock(String symbol, String company, Double lastTradePrice, Double priceChangeAmmount, Double priceChangePercentage) {
        this.symbol = symbol;
        this.company = company;
        this.lastTradePrice = lastTradePrice;
        this.priceChangeAmmount = priceChangeAmmount;
        this.priceChangePercentage = priceChangePercentage;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public Double getPriceChangeAmmount() {
        return priceChangeAmmount;
    }

    public void setPriceChangeAmmount(Double priceChangeAmmount) {
        this.priceChangeAmmount = priceChangeAmmount;
    }

    public Double getPriceChangePercentage() {
        return priceChangePercentage;
    }

    public void setPriceChangePercentage(Double priceChangePercentage) {
        this.priceChangePercentage = priceChangePercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;
        // Two stock are equals if they have the same symbol
        return symbol != null ? symbol.equals(stock.symbol) : stock.symbol == null;

    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", company='" + company + '\'' +
                ", lastTradePrice=" + lastTradePrice +
                ", priceChangeAmmount=" + priceChangeAmmount +
                ", priceChangePercentage=" + priceChangePercentage +
                '}';
    }

    @Override
    public int compareTo(Stock stock) {
        return symbol.compareTo(stock.getSymbol());
    }
}

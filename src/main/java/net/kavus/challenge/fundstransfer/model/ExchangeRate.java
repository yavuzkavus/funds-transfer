package net.kavus.challenge.fundstransfer.model;

/**
 * Holds rate information for base/counter pair
 * @author yavuz
 *
 */
public class ExchangeRate {
	private final Currency base;
	//aka quote
	private final Currency counter;
	private double rate;

	public ExchangeRate(Currency base, Currency counter, double rate) {
		this.base = base;
		this.counter = counter;
		this.rate = rate;
	}

	public Currency getBase() {
		return base;
	}

	public Currency getCounter() {
		return counter;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}

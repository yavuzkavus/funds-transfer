package net.kavus.challenge.fundstransfer.service;

import static net.kavus.challenge.fundstransfer.model.Currency.BTC;
import static net.kavus.challenge.fundstransfer.model.Currency.DKK;
import static net.kavus.challenge.fundstransfer.model.Currency.EUR;
import static net.kavus.challenge.fundstransfer.model.Currency.GBP;
import static net.kavus.challenge.fundstransfer.model.Currency.INR;
import static net.kavus.challenge.fundstransfer.model.Currency.USD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import net.kavus.challenge.fundstransfer.model.Currency;
import net.kavus.challenge.fundstransfer.model.ExchangeRate;

@Service
public class ExchangeRateService {

	//it can be queried from a real service, but for test purpose we will use this dummy data
	public List<ExchangeRate> getRates() {
		List<ExchangeRate> rates = new ArrayList<>();
		rates.add( new ExchangeRate(EUR, USD, 1.13) );
		rates.add( new ExchangeRate(EUR, GBP, 0.85) );
		rates.add( new ExchangeRate(GBP, USD, 1.32) );
		rates.add( new ExchangeRate(USD, DKK, 6.58) );
		rates.add( new ExchangeRate(USD, INR, 75.16) );
		rates.add( new ExchangeRate(BTC, USD, 57_102.5) );
		return Collections.unmodifiableList(rates);
	}

	public ExchangeRate getRate(Currency base, Currency counter) {
		for (ExchangeRate rate : getRates()) {
			if (rate.getBase()==base && rate.getCounter()==counter ||
					rate.getBase()==counter && rate.getCounter()==base) {
				return rate;
			}
		}
		return null;
	}
}

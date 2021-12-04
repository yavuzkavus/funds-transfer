package net.kavus.challenge.fundstransfer.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.kavus.challenge.fundstransfer.ErrorContainer;
import net.kavus.challenge.fundstransfer.model.Currency;
import net.kavus.challenge.fundstransfer.model.ExchangeRate;
import net.kavus.challenge.fundstransfer.service.ExchangeRateService;

@RestController()
@RequestMapping("/rate")
public class ExchangeRateController {
	public final ExchangeRateService exchangeRateService;

	@Autowired
	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping
	public Object get(	@RequestParam(required = false) String base,
						@RequestParam(required = false) String counter) {
		Currency baseCurrency = null,
				counterCurrency = null;
		if (base!=null) {
			try {
				base = base.toUpperCase(Locale.ENGLISH);
				baseCurrency = Currency.valueOf(base);
			} catch(IllegalArgumentException e) {
				return new ErrorContainer(ErrorContainer.KEY_NOTFOUND, "Currency not supported :" + base);
			}
		}
		if (counter!=null) {
			try {
				counter = counter.toUpperCase(Locale.ENGLISH);
				counterCurrency = Currency.valueOf(counter);
			} catch(IllegalArgumentException e) {
				return new ErrorContainer(ErrorContainer.KEY_NOTFOUND, "Currency not supported :" + counter);
			}
		}
		List<ExchangeRate> rates = exchangeRateService.getRates();
		if (baseCurrency==null && counterCurrency==null)
			return rates;
		if (baseCurrency==null || counterCurrency==null) {
			Currency nonNull = baseCurrency==null ? counterCurrency: baseCurrency;
			return rates.stream().filter(rate->rate.getBase()==nonNull || rate.getCounter()==nonNull).collect(Collectors.toList());
		}
		ExchangeRate rate = exchangeRateService.getRate(baseCurrency, counterCurrency);
		if (rate==null) {
			return new ErrorContainer(ErrorContainer.KEY_NOTFOUND, "A pair not found for " + base + "/" + counter);
		}

		return rate;
	}
}

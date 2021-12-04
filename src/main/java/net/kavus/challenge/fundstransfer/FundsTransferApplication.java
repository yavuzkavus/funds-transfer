package net.kavus.challenge.fundstransfer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import net.kavus.challenge.fundstransfer.model.Account;
import net.kavus.challenge.fundstransfer.model.Currency;
import net.kavus.challenge.fundstransfer.service.AccountService;

@SpringBootApplication
public class FundsTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(FundsTransferApplication.class, args);
	}

	@Bean
	public CommandLineRunner createDumyAccounts(AccountService service) {
		return (args) -> {
			//create some dumy data to test
			Account btcAccount = new Account("Satoshi Nakamoto", Currency.BTC, 1.5),
					usdAccount = new Account("Joe Biden", Currency.USD, 60_000),
					eurAccount = new Account("Angella Merkel", Currency.EUR, 50_000),
					gbpAccount = new Account("Boris Johnson", Currency.GBP, 10_000),
					dkkAccount = new Account("Mette Frederiksen", Currency.DKK, 123_000),
					inrAccount = new Account("Mahatma Gandi", Currency.INR, 68_000);

			service.save(btcAccount);
			service.save(usdAccount);
			service.save(eurAccount);
			service.save(gbpAccount);
			service.save(dkkAccount);
			service.save(inrAccount);

			/*
			System.out.println(btcAccount);
			System.out.println(usdAccount);

			service.transferFunds(usdAccount.getId(), btcAccount.getId(), 28_551.25);

			btcAccount = service.getAccount(btcAccount.getId());
			usdAccount = service.getAccount(usdAccount.getId());
			System.out.println(btcAccount);
			System.out.println(usdAccount);
			*/
		};
	}

}

package net.kavus.challenge.fundstransfer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.kavus.challenge.fundstransfer.model.Account;
import net.kavus.challenge.fundstransfer.model.ExchangeRate;
import net.kavus.challenge.fundstransfer.repository.AccountRepository;

@Service
public class AccountService {
	private final AccountRepository accountRepository;

	@Autowired
	@Lazy
	private ExchangeRateService exchangeRateService;

	@Autowired
	public AccountService(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public Iterable<Account> getAccounts() {
		return accountRepository.findAll();
	}

	public Account getAccount(long id) {
		return accountRepository.findById(id);
	}

	public Account save(Account account) {
		return accountRepository.save(account);
	}

	public void delete(long id) {
		accountRepository.deleteById(id);
	}

	/**
	 * This service is transactional, if one operation fails, all will fail and will be rolled back.
	 *
	 * This service is also syncronized. It will prevent making transfers for same account at same time.
	 * Another approach can be put lock on accounts at database row level
	 */
	@Transactional
	public synchronized boolean transferFunds(long from, long to, double amount) {
		Account fromAccount = accountRepository.findById(from),
				toAccount = accountRepository.findById(to);

		if( fromAccount.getBalance()<amount )
			return false;

		double rate;
		if( fromAccount.getCurrency()==toAccount.getCurrency() )
			rate = 1;
		else {
			ExchangeRate exchangeRate = exchangeRateService.getRate(fromAccount.getCurrency(), toAccount.getCurrency());
			if( exchangeRate==null )
				return false;
			rate = exchangeRate.getBase()==fromAccount.getCurrency() ? exchangeRate.getRate() : 1 / exchangeRate.getRate();
		}

		fromAccount.setBalance(fromAccount.getBalance() - amount);
		toAccount.setBalance(toAccount.getBalance() + amount * rate);
		accountRepository.saveAll( List.of(fromAccount, toAccount) );

		return true;
	}
}

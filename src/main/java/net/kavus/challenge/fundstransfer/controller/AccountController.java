package net.kavus.challenge.fundstransfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.kavus.challenge.fundstransfer.ErrorContainer;
import net.kavus.challenge.fundstransfer.InfoContainer;
import net.kavus.challenge.fundstransfer.model.Account;
import net.kavus.challenge.fundstransfer.model.ExchangeRate;
import net.kavus.challenge.fundstransfer.service.AccountService;
import net.kavus.challenge.fundstransfer.service.ExchangeRateService;

/**
 * CRUD and Transfer Funds services
 * @author yavuz
 *
 */
@RestController
@RequestMapping("/accounts")
public class AccountController {
	private final AccountService accountService;

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}

	/**
	 * @return all accounts
	 */
	@GetMapping
	public Iterable<Account> get() {
		return accountService.getAccounts();
	}

	/**
	 * @return the account matching to id. If no record found, an error is returned
	 */
	@GetMapping("/{id}")
	public Object get(@PathVariable long id) {
		Account account = accountService.getAccount(id);
		if (account==null)
			return new ErrorContainer(ErrorContainer.KEY_NOTFOUND, "An account with specified id not found: " + id);
		return account;
	}

	/**
	 * Deletes the account with specified id
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> delete(@PathVariable long id) {
		accountService.delete(id);
		return InfoContainer.entity("Account deleted successfully");
	}

	/**
	 * creates a new account with posted data
	 * @param account a JSON object in request body
	 */
	@PostMapping
	public ResponseEntity<Account> create(@RequestBody Account account) {
		Account saved = accountService.save(account);
		return ResponseEntity.ok(saved);
	}


	/**
	 * updates an account with posted data. id has to be set in the account object
	 * @param account a JSON object in request body
	 */
	@PutMapping
	public ResponseEntity<Object> update(@RequestBody Account account) {
		Account current = accountService.getAccount(account.getId());
		if (current==null)
			return ErrorContainer.entity(ErrorContainer.KEY_NOTFOUND, "An account with specified id not found: " + account.getId());
		accountService.save(account);
		return ResponseEntity.ok(account);
	}

	/**
	 * @param from id of sender accout
	 * @param to id of receiver account
	 * @param amount desired quantity
	 * @return an error if operation fails, otherwise success information.
	 */
	@PostMapping("/transfer_funds")
	public ResponseEntity<Object> transferFunds(
			@RequestParam long from,
			@RequestParam long to,
			@RequestParam double amount,
			ExchangeRateService exchangeRateService) {
		Account fromAccount = accountService.getAccount(from),
				toAccount = accountService.getAccount(to);
		if( fromAccount==null )
			return ErrorContainer.entity(ErrorContainer.KEY_NOTFOUND, "An account with specified id not found: " + from);
		if( toAccount==null )
			return ErrorContainer.entity(ErrorContainer.KEY_NOTFOUND, "An account with specified id not found: " + to);
		if( from==to )
			return ErrorContainer.entity("From and to accounts have to be different");
		if( from==to )
			return ErrorContainer.entity("From and to accounts have to be different");
		if( fromAccount.getBalance()<amount )
			return ErrorContainer.entity("Insufficient balance");

		if( fromAccount.getCurrency()!=toAccount.getCurrency() ) {
			ExchangeRate exchangeRate = exchangeRateService.getRate(fromAccount.getCurrency(), toAccount.getCurrency());
			if( exchangeRate==null )
				return ErrorContainer.entity("From and to accounts' currencies are not covertible");
		}

		if( accountService.transferFunds(from, to, amount) )
			return InfoContainer.entity("Funds transfered successfully");
		else
			return ErrorContainer.entity("Funds transfer failed");
	}

}

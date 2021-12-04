package net.kavus.challenge.fundstransfer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import net.kavus.challenge.fundstransfer.controller.AccountController;
import net.kavus.challenge.fundstransfer.model.Account;
import net.kavus.challenge.fundstransfer.model.Currency;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class FundsTransferApplicationTests {
	@Autowired
	private AccountController accountController;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private Account testAccount = new Account("Test Account", Currency.EUR, 0);

	@Test
	void contextLoads() {
		assertThat(accountController).isNotNull();
	}

	@Test
	void testGetAccounts() {
		Account beforeAddition[] = restTemplate.getForObject(serviceUrl(), Account[].class);

		Account lastAccount = restTemplate.postForEntity(serviceUrl(), testAccount, Account.class).getBody();

		Account afterAddition[] = restTemplate.getForObject(serviceUrl(), Account[].class);

		assertThat(afterAddition)
			.isNotNull()
			.isNotEmpty()
			.hasSize(beforeAddition.length + 1)
			.contains(lastAccount);


		assertEquals(lastAccount, restTemplate.getForObject(serviceUrl(lastAccount.getId()), Account.class));

	}

	@Test
	void testAddAccount() {
		Account addedAccount = restTemplate.postForEntity(serviceUrl(), testAccount, Account.class).getBody();
		assertEquals(addedAccount, restTemplate.getForObject(serviceUrl(addedAccount.getId()), Account.class));
	}

	@Test
	void testDeleteAccount() {
		Account addedAccount = restTemplate.postForEntity(serviceUrl(), new Account("Last Account", Currency.EUR, 0), Account.class).getBody();
		Account afterAddition[] = restTemplate.getForObject(serviceUrl(), Account[].class);
		assertThat(afterAddition).contains(addedAccount);

		restTemplate.delete(serviceUrl(addedAccount.getId()));
		Account afterDeletion[] = restTemplate.getForObject(serviceUrl(), Account[].class);
		assertThat(afterDeletion).doesNotContain(addedAccount);
	}

	@Test
	void testTransferFunds() {
		Account usdAccount = restTemplate.postForEntity(serviceUrl(), new Account("USD Account", Currency.USD, 100_000), Account.class).getBody();
		Account eurAccount = restTemplate.postForEntity(serviceUrl(), new Account("EUR Account", Currency.EUR, 50_000), Account.class).getBody();
		Account dkkAccount = restTemplate.postForEntity(serviceUrl(), new Account("DKK Account", Currency.DKK, 150_000), Account.class).getBody();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("from", Long.toString(eurAccount.getId()));
		params.add("to", Long.toString(dkkAccount.getId()));
		params.add("amount", Double.toString(100));

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(params, headers);

		String errorResponse = restTemplate.postForObject(
					serviceUrl("transfer_funds"), request, String.class);

		assertThat(errorResponse).contains("error").containsIgnoringCase("not covertible");

		params.set("to", Long.toString(usdAccount.getId()));

		String infoResponse = restTemplate.postForObject(
					serviceUrl("transfer_funds"), request, String.class);

		assertThat(infoResponse).contains("info").containsIgnoringCase("successfully");

		Account usdAccountAfterTransfer = restTemplate.getForObject(serviceUrl(usdAccount.getId()), Account.class);
		assertThat( usdAccount.getBalance() ).isLessThan( usdAccountAfterTransfer.getBalance() );

		Account eurAccountAfterTransfer = restTemplate.getForObject(serviceUrl(eurAccount.getId()), Account.class);
		assertThat( eurAccount.getBalance() ).isGreaterThan( eurAccountAfterTransfer.getBalance() );
	}



	private String serviceUrl() {
		return "http://localhost:" + port + "/accounts";
	}

	private String serviceUrl(Object path) {
		return serviceUrl() + "/" + path;
	}
}

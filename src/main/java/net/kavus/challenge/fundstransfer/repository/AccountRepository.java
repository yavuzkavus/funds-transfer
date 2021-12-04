package net.kavus.challenge.fundstransfer.repository;

import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import net.kavus.challenge.fundstransfer.model.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findById(long id);

	@Lock(LockModeType.PESSIMISTIC_READ)
	@QueryHints({
		@QueryHint(name = "javax.persistence.lock.timeout", value = "60000"),
		@QueryHint(name = "javax.persistence.query.timeout", value = "70000")
	})
	@Query("SELECT a FROM Account a WHERE a.id = ?1 OR a.id=?2")
	List<Account> findAndLockTwoAccounts(long id1, long id2);

	Account findByNameIgnoreCase(String name);
}

package net.kavus.challenge.fundstransfer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private Currency currency;
	@Column(nullable = false)
	private double balance;

	protected Account() { }

	public Account(String name, Currency currency, double balance) {
		this.name = name;
		this.currency = currency;
		this.balance = balance;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Currency getCurrency() {
		return currency;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	@Override
	public String toString() {
		return String.format("{id: %d, name: %s, currency: %s, balance: %.4f}", id, name, currency, balance);
	}

	@Override
	public int hashCode() {
		return this.id>0 ? Long.hashCode(id) : super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (this.getClass() != o.getClass())
			return false;
		Account other = (Account)o;
		return id == other.id && name.equals(other.name) && currency==other.currency && balance==other.balance;
	}
}

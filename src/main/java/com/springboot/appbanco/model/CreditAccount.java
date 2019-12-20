package com.springboot.appbanco.model;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Document(collection = "creditAccount")
@Data
public class CreditAccount {

	@Id
	private String codAccount;

	private String productType; // Cuenta Bancarias o Producto Credito.

	private String accountType; // CRED = Personal, Empresarial, Tarjeta Credito, Adelanto Efectivo

	
	private Integer accountNumber;

	// @JsonSerialize(using = ToStringSerializer.class)

	@JsonFormat(pattern = "dd-MM-yyyy", shape = Shape.STRING)
	private Date openingDate; // Fecha Apertura

	private double balance; // saldo

	private Client customer;
	
	
	//Datos segun Credito:
	
	private double consumption; // consumo
	private double creditLimit;
	

	private char accountstatus; // Activo o Inactivo.

	public String getCodAccount() {
		return codAccount;
	}

	public void setCodAccount(String codAccount) {
		this.codAccount = codAccount;
	}

	public Integer getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Date getOpeningDate() {
		return openingDate;
	}

	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	

	public char getAccountstatus() {
		return accountstatus;
	}

	public void setAccountstatus(char accountstatus) {
		this.accountstatus = accountstatus;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Client getCustomer() {
		return customer;
	}

	public void setCustomer(Client customer) {
		this.customer = customer;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	
	
	
	

}

package com.springboot.appbanco.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Data
public class Transaction {
	@Id
	private String idTransaction;
	
	@JsonFormat(pattern = "dd-MM-yyyy",shape = Shape.STRING)
	private Date date;
	private Integer AccountNumber;
	private double quantity;
	private String TransactionType; // Deposito - Retiro ---- Consumo - Pagos.
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public String getIdTransaction() {
		return idTransaction;
	}

	public void setIdTransaction(String idTransaction) {
		this.idTransaction = idTransaction;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAccountNumber() {
		return AccountNumber;
	}

	public void setAccountNumber(Integer accountNumber) {
		AccountNumber = accountNumber;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}
	
	
	
	
}
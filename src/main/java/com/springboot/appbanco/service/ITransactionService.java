package com.springboot.appbanco.service;

import java.util.Date;

import com.springboot.appbanco.model.ConsultPeriod;
import com.springboot.appbanco.model.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {
	Flux<Transaction> getAll();
	
	Mono<Transaction> save(Transaction transt);
	
	Flux<Transaction> getTranByNroAccount(Integer NumberAcc);
	
	Flux<Transaction> findByAccountNumberByDateBetween(ConsultPeriod consultPeriod);
}

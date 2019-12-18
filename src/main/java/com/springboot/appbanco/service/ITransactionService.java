package com.springboot.appbanco.service;

import com.springboot.appbanco.model.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITransactionService {
	Flux<Transaction> getAll();
	
	Mono<Transaction> save(Transaction transt);
	
}

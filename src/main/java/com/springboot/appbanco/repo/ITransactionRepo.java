package com.springboot.appbanco.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Transaction;

import reactor.core.publisher.Flux;

@Repository
public interface ITransactionRepo extends ReactiveMongoRepository<Transaction, String>{

	
	public Flux<Transaction> findByAccountNumber(Integer NumAcc);
}

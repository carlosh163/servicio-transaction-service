package com.springboot.appbanco.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Transaction;

@Repository
public interface ITransactionRepo extends ReactiveMongoRepository<Transaction, String>{

}

package com.springboot.appbanco.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.ConsultPeriod;
import com.springboot.appbanco.model.Transaction;
import com.springboot.appbanco.repo.ITransactionRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements ITransactionService{

	@Autowired
	ITransactionRepo repo;
	
	@Override
	public Mono<Transaction> save(Transaction trasanc) {
		
		//trasanc.setDate(new Date());
		return repo.save(trasanc);
	}

	@Override
	public Flux<Transaction> getAll() {
		return repo.findAll();
	}

	@Override
	public Flux<Transaction> getTranByNroAccount(Integer NumberAcc) {
		return repo.findByAccountNumber(NumberAcc);
	}

	@Override
	public Flux<Transaction> findByAccountNumberByDateBetween(ConsultPeriod cP) {
		return repo.findByAccountNumberAndDateBetween(cP.getNumberAccount(), cP.getStartDate(), cP.getEndDate())
				.filter(t->	t.getCommission()>0);
	}
	
	
	

}

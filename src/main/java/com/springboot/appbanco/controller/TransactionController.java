package com.springboot.appbanco.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.model.Person;
import com.springboot.appbanco.model.Transaction;
import com.springboot.appbanco.service.ITransactionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	ITransactionService service;
	
	@Autowired
	@Qualifier("cuenta")
	WebClient wCAccount;
	
	@Autowired
	@Qualifier("cliente")
	WebClient wCClient;
	
	@Autowired
	@Qualifier("persona")
	WebClient wCPerson;
	
	@PostMapping("/deposit/{accNumber}/{quantity}")
	public Mono<Transaction> deposit(@PathVariable Integer accNumber, @PathVariable double quantity){
		
		
		Map<String,Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);
		wCClient.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Client.class).subscribe();
		wCPerson.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Person.class).subscribe();
		return wCAccount.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToMono(Account.class)
				
					 
				
				.flatMap(account ->{
					
					Transaction objT = new Transaction();
					
					objT.setDate(new Date());
					objT.setAccountNumber(accNumber);
					objT.setQuantity(quantity);
					objT.setTransactionType("Deposito");
					return service.save(objT);
				});
	}
	
	@PostMapping("/retirement/{accNumber}/{quantity}")
	public Mono<Transaction> retirement(@PathVariable Integer accNumber, @PathVariable double quantity){
		
		
		Map<String,Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);
		
		Account objAcc = new Account();
		objAcc.setAccountstatus('N');
		
		
		
		return wCAccount.put().uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToMono(Account.class)
				
				
				.switchIfEmpty( Mono.just(objAcc))
				
				.flatMap(account ->{
					
					
					
					if(account.getAccountstatus() =='N') {
						return Mono.empty();
					}else {
						wCClient.put().uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Client.class).subscribe();
						wCPerson.put().uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Person.class).subscribe();
						
						Transaction objT = new Transaction();
						
						objT.setDate(new Date());
						objT.setAccountNumber(accNumber);
						objT.setQuantity(quantity);
						objT.setTransactionType("Retiro");
						return service.save(objT);
					}
					
					
				});
	}
	
	
	
	
	
	@PostMapping
	public Mono<Transaction> create(@RequestBody Transaction transt){
		return service.save(transt);
		
	}
	
	@GetMapping
	public Flux<Transaction> list(){
		return service.getAll();
	}
	
}

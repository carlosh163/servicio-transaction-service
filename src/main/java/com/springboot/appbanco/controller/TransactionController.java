package com.springboot.appbanco.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.springboot.appbanco.model.CreditAccount;
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
	
	@Autowired
	@Qualifier("cuentaCredito")
	WebClient wCAccountCredit;
	
	
	@PostMapping("/deposit/{accNumber}/{quantity}/{originMov}")
	public Mono<Transaction> deposit(@PathVariable Integer accNumber, @PathVariable double quantity,@PathVariable String originMov){
		
		
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
					objT.setOriginMov(originMov);
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
						objT.setOriginMov("Efectivo");
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
	
	
	@PostMapping("/consume/{accNumber}/{quantity}")
	public Mono<Transaction> consume(@PathVariable Integer accNumber, @PathVariable double quantity){
		
		
		Map<String,Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);
		
		CreditAccount objAcc = new CreditAccount();
		objAcc.setAccountstatus('N');
		
		
		
		return wCAccountCredit.put().uri("/updateBalanceAccountByAccountNumberConsumer/{accountNumber}/{quantity}",params).retrieve().bodyToMono(CreditAccount.class)
				
				
				.switchIfEmpty( Mono.just(objAcc))
				
				.flatMap(account ->{
					
					
					
					if(account.getAccountstatus() =='N') {
						return Mono.empty();
					}else {
						wCClient.put().uri("/updateBalanceAccountByAccountNumberConsumer/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Client.class).subscribe();
						
						
						Transaction objT = new Transaction();
						
						objT.setDate(new Date());
						objT.setAccountNumber(accNumber);
						objT.setQuantity(quantity);
						objT.setTransactionType("Consumo");
						objT.setOriginMov("Efectivo");
						return service.save(objT);
					}
					
					
				});
		
		
	}
	
	
	@PostMapping("/payment/{accNumber}/{quantity}/{originMov}")
	public Mono<Transaction> payment(@PathVariable Integer accNumber, @PathVariable double quantity,@PathVariable String originMov){
		
		
		Map<String,Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);
		
		CreditAccount objAcc = new CreditAccount();
		objAcc.setAccountstatus('N');
		
		
		
		return wCAccountCredit.put().uri("/updateBalanceAccounByAccountNumberPayment/{accountNumber}/{quantity}",params).retrieve().bodyToMono(CreditAccount.class)
				
				
				.switchIfEmpty( Mono.just(objAcc))
				
				.flatMap(account ->{
					
					
					
					if(account.getAccountstatus() =='N') {
						return Mono.empty();
					}else {
						wCClient.put().uri("/updateBalanceAccounByAccountNumberPayment/{accountNumber}/{quantity}",params).retrieve().bodyToFlux(Client.class).subscribe();
						
						
						Transaction objT = new Transaction();
						
						objT.setDate(new Date());
						objT.setAccountNumber(accNumber);
						objT.setQuantity(quantity);
						objT.setTransactionType("Pago");
						objT.setOriginMov(originMov);
						return service.save(objT);
					}
					
					
				});
		
		
	}
	
	
	// Consult los Movimientos de un CLiente(dni): mostrar transacciones de las cuentas que le pertenecen.
	@GetMapping("/findTransactionByNumberDocuCLient/{numberDocument}")
	public Flux<Transaction> findTransactionByNumberDocuCLient(@PathVariable String numberDocument){
		
		Map<String,Object> params = new HashMap();
		params.put("nroDoc", numberDocument);
		
		//Mono
		return wCClient.get().uri("/BuscarClientePorNroDoc/{nroDoc}",params)
				.retrieve()
				.bodyToMono(Client.class)
				
				.map(objClient->{
					
					List<Account> lstAccBank = objClient.getAccountList();
					//List<CreditAccount> lstAccCred = objClient.getCreditAccountList();
					
					return lstAccBank;
					
				}).flatMapMany(lst -> Flux.fromIterable(lst))
				.flatMap(objB->{
					Integer nroAcc = objB.getAccountNumber();
					
					return service.getTranByNroAccount(nroAcc);
				});
		//return Flux.empty();
		
	}
	
	
	@GetMapping("/prub/{nroAcc}")
	public Flux<Transaction> prub(@PathVariable Integer nroAcc){
		return service.getTranByNroAccount(nroAcc);
	}
	
	
}

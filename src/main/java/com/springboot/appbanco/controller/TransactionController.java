package com.springboot.appbanco.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Transactions")
@RestController
//@RequestMapping("/api/transactions")
public class TransactionController {
	private static Logger log = LoggerFactory.getLogger(TransactionController.class);

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

	@PostMapping("/deposit/{accNumber}/{quantity}")
	public Mono<Transaction> deposit(@PathVariable Integer accNumber, @PathVariable double quantity) {

		Map<String, Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);
		
		return wCClient.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}", params).retrieve()
				.bodyToFlux(Client.class)
				.next()
				.flatMap(o ->{
					return wCPerson.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}", params).retrieve()
							.bodyToFlux(Person.class)
							.next()
							.flatMap(o2 ->{
								return wCAccount.put().uri("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}", params).retrieve()
								.bodyToMono(Account.class)
								.flatMap(account -> {

									Transaction objT = new Transaction();

									objT.setDate(new Date());
									objT.setAccountNumber(accNumber);
									objT.setQuantity(quantity);
									objT.setTransactionType("Deposito");
									objT.setOriginMov("Efectivo");
									return service.save(objT);
								});
							});
				});
		
		 

				
	}

	@PostMapping("/retirement/{accNumber}/{quantity}")
	public Mono<Transaction> retirement(@PathVariable Integer accNumber, @PathVariable double quantity) {

		Map<String, Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);

		Account objAcc = new Account();
		objAcc.setAccountstatus('N');

		return wCAccount.put().uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}", params)
				.retrieve().bodyToMono(Account.class)

				.switchIfEmpty(Mono.just(objAcc))

				.flatMap(account -> {

					if (account.getAccountstatus() == 'N') {
						return Mono.empty();
					} else {
						return wCClient.put()
								.uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}", params)
								.retrieve().bodyToFlux(Client.class)
								.next()
								.flatMap(o ->{
									return wCPerson.put()
											.uri("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}", params)
											.retrieve().bodyToFlux(Person.class)
											.next()
											.flatMap(o2->{
												Transaction objT = new Transaction();

												objT.setDate(new Date());
												objT.setAccountNumber(accNumber);
												objT.setQuantity(quantity);
												objT.setTransactionType("Retiro");
												objT.setOriginMov("Efectivo");
												return service.save(objT);
											});
								});
						

						
					}

				});
	}

	@PostMapping
	public Mono<Transaction> create(@RequestBody Transaction transt) {
		return service.save(transt);

	}

	@GetMapping
	public Flux<Transaction> list() {
		return service.getAll();
	}

	@PostMapping("/consume/{accNumber}/{quantity}")
	public Mono<Transaction> consume(@PathVariable Integer accNumber, @PathVariable double quantity) {

		Map<String, Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);

		CreditAccount objAcc = new CreditAccount();
		objAcc.setAccountstatus('N');

		return wCAccountCredit.put()
				.uri("/updateBalanceAccountByAccountNumberConsumer/{accountNumber}/{quantity}", params).retrieve()
				.bodyToMono(CreditAccount.class)

				.switchIfEmpty(Mono.just(objAcc))

				.flatMap(account -> {

					if (account.getAccountstatus() == 'N') {
						return Mono.empty();
					} else {
						return wCClient.put()
								.uri("/updateBalanceAccountByAccountNumberConsumer/{accountNumber}/{quantity}", params)
								.retrieve().bodyToFlux(Client.class)
								.next()
								.flatMap(ob -> {
									Transaction objT = new Transaction();

									objT.setDate(new Date());
									objT.setAccountNumber(accNumber);
									objT.setQuantity(quantity);
									objT.setTransactionType("Consumo");
									objT.setOriginMov("Efectivo");
									return service.save(objT);
								});

						
					}

				});

	}

	@ApiOperation(value = "(P1)RQ0x-TC payment from cash", notes = "")
	@PostMapping("/paymentCash/{accNumber}/{quantity}")
	public Mono<Transaction> payment(@PathVariable Integer accNumber, @PathVariable double quantity) {

		Map<String, Object> params = new HashMap();
		params.put("accountNumber", accNumber);
		params.put("quantity", quantity);

		// Validando el tipo de Pago::
			return wCAccountCredit
					.put()
					.uri("/updateBalanceAccounByAccountNumberPayment/{accountNumber}/{quantity}", params)
					.retrieve()
					.bodyToMono(CreditAccount.class).switchIfEmpty(Mono.empty()).flatMap(account -> {
						
							return wCClient.put()
									.uri("/updateBalanceAccounByAccountNumberPayment/{accountNumber}/{quantity}", params)
									.retrieve().bodyToFlux(Client.class)
									.next()
									.flatMap(c ->{
										Transaction objT = new Transaction();

										objT.setDate(new Date());
										objT.setAccountNumber(accNumber);
										objT.setQuantity(quantity);
										objT.setTransactionType("Pago");
										objT.setOriginMov("Efectivo");
										return service.save(objT); 
									});

							
						
					});

	}
	
	
	
	
	@ApiOperation(value = "(P2)RQ06-TC payment from any Bank Account", notes = "")
	@PostMapping("/paymentBankAccount/{accNumberCredit}/{quantity}/{numberAccBank}")
	public Mono<Transaction> payment(@PathVariable Integer accNumberCredit, @PathVariable double quantity,@PathVariable Integer numberAccBank) {

		Map<String, Object> params = new HashMap();
		params.put("accNumberCredit", accNumberCredit);
		params.put("quantity", quantity);
		params.put("numberAccBank", numberAccBank);
		
		return  wCAccount.get().uri("/findAccountByNumberAccount/{numberAccBank}", params).retrieve().bodyToMono(Account.class)
				.switchIfEmpty(Mono.empty()).flatMap(objMAcc -> {
					return wCAccountCredit.get().uri("/findAccountByNumberAccount/{accNumberCredit}", params).retrieve().bodyToMono(CreditAccount.class)
					.switchIfEmpty(Mono.empty()).flatMap(objMAccCredt ->{
						
						
						if ( quantity <= objMAcc.getBalance() && objMAccCredt.getConsumption()>= quantity) {
							log.info("Procede con el pago descuento en la cuenta Corriente. -> "+ objMAcc.getBalance());

							return wCAccount.put()
									.uri("/updateBalanceAccountRetireByAccountNumber/{numberAccBank}/{quantity}",params)
									.retrieve().bodyToMono(Account.class)
									.flatMapMany(c ->{
										return wCClient.put()
												.uri("/updateBalanceAccountRetireByAccountNumber/{numberAccBank}/{quantity}",params)
												.retrieve().bodyToFlux(Client.class);
									})
									.next()
									.flatMapMany(c ->{
										
										return wCPerson.put()
												.uri("/updateBalanceAccountRetireByAccountNumber/{numberAccBank}/{quantity}",params)
												.retrieve().bodyToFlux(Person.class);
									})
									.next()
									.flatMap(c->{
										
										return wCAccountCredit
												.put()
												.uri("/updateBalanceAccounByAccountNumberPayment/{accNumberCredit}/{quantity}", params)
												.retrieve()
												.bodyToMono(CreditAccount.class).switchIfEmpty(Mono.empty()).flatMap(account -> {
													
														return wCClient.put()
																.uri("/updateBalanceAccounByAccountNumberPayment/{accNumberCredit}/{quantity}", params)
																.retrieve().bodyToFlux(Client.class)
																.next()
																.flatMap(ce ->{
																	Transaction objT = new Transaction();

																	objT.setDate(new Date());
																	objT.setAccountNumber(accNumberCredit);
																	objT.setQuantity(quantity);
																	objT.setTransactionType("Pago");
																	objT.setOriginMov(String.valueOf(numberAccBank));
																	return service.save(objT); 
																});

														
													
												});
										
										
									});
									
							
							

						} else{
							log.warn("No cuenta con saldo suficiente para la cantidad a Pagar / La cantidad a Pagar es mayor al consumo...");
							return Mono.empty();
						}
						
						
						
					});
					
					
						

					
				});

	}
	
	
	
	

	// Consult los Movimientos de un CLiente(dni): mostrar transacciones de las
	// cuentas que le pertenecen.
	@ApiOperation(value = "RQ11-Check movements of my products", notes = "Returns all movements on all accounts that a customer has by entering their document number")
	@GetMapping("/findTransactionByNumberDocuCLient/{numberDocument}")
	public Flux<Transaction> findTransactionByNumberDocuCLient(@PathVariable String numberDocument) {

		Map<String, Object> params = new HashMap();
		params.put("nroDoc", numberDocument);

		// Mono
		return wCClient.get().uri("/BuscarClientePorNroDoc/{nroDoc}", params).retrieve().bodyToMono(Client.class)

				.map(objClient -> {

					List<Account> lstAccBank = objClient.getAccountList();
					List<CreditAccount> lstAccCred = objClient.getCreditAccountList();

					for (CreditAccount accountC : lstAccCred) {
						Account cred = new Account();
						Integer nr = accountC.getAccountNumber();
						cred.setAccountNumber(nr);
						lstAccBank.add(cred);
					}

					return lstAccBank;

				}).flatMapMany(lst -> Flux.fromIterable(lst)).flatMap(objB -> {
					Integer nroAcc = objB.getAccountNumber();

					return service.getTranByNroAccount(nroAcc);
				});
		// return Flux.empty();

	}

	@GetMapping("/prub/{nroAcc}")
	public Flux<Transaction> prub(@PathVariable Integer nroAcc) {
		return service.getTranByNroAccount(nroAcc);
	}

}

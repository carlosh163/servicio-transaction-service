package com.springboot.appbanco.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ConsultPeriod {

	private Integer numberAccount;
	@JsonFormat(pattern = "dd-MM-yyyy", shape = Shape.STRING)
	private Date startDate;
	@JsonFormat(pattern = "dd-MM-yyyy", shape = Shape.STRING)
	private Date endDate;
	
	
	public ConsultPeriod() {
		// TODO Auto-generated constructor stub
	}


	public Integer getNumberAccount() {
		return numberAccount;
	}


	public void setNumberAccount(Integer numberAccount) {
		this.numberAccount = numberAccount;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
}

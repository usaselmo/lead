package com.allscontracting.tradutor.impl;

import java.io.IOException;
import java.math.BigDecimal;

import com.allscontracting.model.Lead;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LeadHelper {

	public static final Lead localFSFileLineToEntity(String line, Class<Lead> clazz) {
		try {
			Lead lead = new ObjectMapper().readValue(line, clazz);
			return lead;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static final String entityToLocalFSFileLine(Lead entity) {
		try {
			String obj = new ObjectMapper().writeValueAsString(entity);
			return obj+System.lineSeparator();
		} catch (JsonProcessingException e) {
			System.out.println("Erro ao converter Lead pra string : " + entity + " - Erro: " + e.getMessage() );
			return "{}";
		}
	}

	public static final BigDecimal defineCost(String cost) {
		try {
			BigDecimal bd = BigDecimal.valueOf(Float.valueOf(cost.replace("$", "")));
			return bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}
	
}

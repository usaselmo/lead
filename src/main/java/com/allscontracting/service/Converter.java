package com.allscontracting.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.allscontracting.exception.LeadsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Converter {

	public static final String dd_MM_yyyy = "dd/MM/yyyy";
	public static final String MM_dd_yy = "MM/dd/yy";
	public static final String MM_dd_yyyy = "MM/dd/yyyy";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";

	/**
	 * Default pattern dd/MM/yyyy
	 */
	public static LocalDate stringToLocalDate(String dateToConvert) {
		return stringToLocalDate(dateToConvert, dd_MM_yyyy);
	}

	public static LocalDate stringToLocalDate(String date, String pattern) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDate.parse(date, formatter);
	}
	
	public static Date stringToDate(String dateToConvert, String pattern) throws ParseException {
		return new SimpleDateFormat(pattern).parse(dateToConvert); 
	}

	public static void main(String[] args) throws ParseException{
		System.out.println(Converter.stringToDate("2020-05-17", Converter.yyyy_MM_dd));
	}
	
	public static Date convertToDate(String date) throws LeadsException{
		try {
			return Converter.stringToDate(date, MM_dd_yyyy);
		} catch (ParseException e) {
			try {
				return stringToDate(date, MM_dd_yy);
			} catch (ParseException e1) {
				try {
					return stringToDate(date, yyyy_MM_dd);
				} catch (ParseException e2) {
					throw new LeadsException("Could not convert this string to Date: " + date);
				}
			}
		}
	}

	public static LocalDate convertToLocalDate(String date) throws LeadsException {
		try {
			return Converter.stringToLocalDate(date, "MM/dd/yyyy");
		} catch (Exception e) {
			try {
				return Converter.stringToLocalDate(date, "MM/dd/yy");
			} catch (Exception e1) {
				try {
					return Converter.stringToLocalDate(date);
				} catch (Exception e2) {
					log.error("Could not convert this string to Date: " + date);
					throw new LeadsException("Could not convert this string to Date: " + date);
				}
			}
		}
	}
	
}

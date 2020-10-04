package com.allscontracting.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.allscontracting.exception.LeadsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Converter {

	public static final String dd_MM_yyyy = "dd/MM/yyyy";
	public static final String MM_dd_yy = "MM/dd/yy";
	public static final String MM_dd_yy_hh_mm = "MM/dd/yy hh:mm";
	public static final String MM_dd_yyyy = "MM/dd/yyyy";
	public static final String MM_dd_yyyy_hh_mm = "MM/dd/yyyy hh:mm";
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

	public static Date stringToDate(String dateToConvert, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(dateToConvert);
		} catch (Exception e) {
			return null;
		}
	}

	public static String dateToString(Date date) {
		try {
			return new SimpleDateFormat(MM_dd_yy_hh_mm).format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static String dateToString(Date date, String format) {
		try {
			return new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return "";
		}
	}

	public static Date convertToDate(String date) {
		if (StringUtils.isEmpty(date))
			return null;
		return Converter.stringToDate(date, MM_dd_yyyy);
	}

	public static Date convertToDate(String date, String format) {
		if (StringUtils.isEmpty(date))
			return null;
		return Converter.stringToDate(date, format);
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

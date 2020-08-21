package com.allscontracting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.allscontracting.model.Person;

public class PersonTest {

	@Test
	public void testPhoneMask_noSpecialChars() throws Exception {
		Person person = new Person();
		person.setPhone("9999999999");
		assertEquals("999-999-9999", person.getPhone());
	}

	@Test
	public void testPhoneMask_dash() throws Exception {
		Person person = new Person();
		person.setPhone("999-999-9999");
		assertEquals("999-999-9999", person.getPhone());
	}

	@Test
	public void testPhoneMask_dash02() throws Exception {
		Person person = new Person();
		person.setPhone("9-99-999-99-99");
		assertEquals("999-999-9999", person.getPhone());
	}

	@Test
	public void testPhoneMask_parentheses() throws Exception {
		Person person = new Person();
		person.setPhone("(999)9999999");
		assertEquals("999-999-9999", person.getPhone());
	}
	
}

package com.allscontracting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.allscontracting.model.Client;

public class ClientTest {

	@Test
	public void testPhoneMask_noSpecialChars() throws Exception {
		Client client = new Client();
		client.setPhone("9999999999");
		assertEquals("999-999-9999", client.getPhone());
	}

	@Test
	public void testPhoneMask_dash() throws Exception {
		Client client = new Client();
		client.setPhone("999-999-9999");
		assertEquals("999-999-9999", client.getPhone());
	}

	@Test
	public void testPhoneMask_dash02() throws Exception {
		Client client = new Client();
		client.setPhone("9-99-999-99-99");
		assertEquals("999-999-9999", client.getPhone());
	}

	@Test
	public void testPhoneMask_parentheses() throws Exception {
		Client client = new Client();
		client.setPhone("(999)9999999");
		assertEquals("999-999-9999", client.getPhone());
	}
	
}

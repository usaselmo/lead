package com.allscontracting.dto;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.model.Client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

	private String id;
	private String email;
	private String name;
	private String address;
	private String cellPhone;
	private String phone;
	
	public static final ClientDTO of(Client client) {
		if(client==null)
			return ClientDTO.builder().build();
		return ClientDTO.builder()
				.id(String.valueOf(client.getId()))
				.email(client.getEmail())
				.name(client.getName())
				.address(client.getAddress())
				.cellPhone(client.getCellPhone())
				.phone(client.getPhone())
				.build();
	}
	
	public static final Client toClient(ClientDTO cd) {
		Client c = new Client();
		c.setAddress(cd.getAddress());
		c.setCellPhone(cd.getCellPhone());
		c.setEmail(cd.getEmail());
		c.setId(StringUtils.isBlank(cd.getId())?null:Long.valueOf(cd.getId()));
		c.setName(cd.getName());
		c.setPhone(cd.getPhone());
		return c;
	}
}

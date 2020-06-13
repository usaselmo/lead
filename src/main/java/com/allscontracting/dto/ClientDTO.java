package com.allscontracting.dto;

import com.allscontracting.model.Client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO {

	private String id;
	private String email;
	private String name;
	private String address;
	private String cellPhone;
	private String phone;
	
	public static final ClientDTO clientToDTO(Client client) {
		return ClientDTO.builder()
				.id(String.valueOf(client.getId()))
				.email(client.getEmail())
				.name(client.getName())
				.address(client.getAddress())
				.cellPhone(client.getCellPhone())
				.phone(client.getPhone())
				.build();
	}
}
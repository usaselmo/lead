package com.allscontracting.dto;

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
	
}

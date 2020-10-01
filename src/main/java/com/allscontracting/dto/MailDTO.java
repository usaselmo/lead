package com.allscontracting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MailDTO {

	private String[] to;
	private String[] bcc;
	private String text;

}

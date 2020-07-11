package com.allscontracting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventLogDTO {
	private Long id;
	private String objectName;
	private String objectId;
	private String eventType;
	private String eventTime;
	private String message;
}

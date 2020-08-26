package com.allscontracting.dto;

import com.allscontracting.model.EventLog;
import com.allscontracting.service.Converter;

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
	private UserDTO user;
	private String message;
	
	public static EventLogDTO of(EventLog e) {
		if(e==null)
			return null;
		return EventLogDTO.builder()
			.eventTime(Converter.dateToString(e.getEventTime()))
			.eventType(e.getEventType())
			.id(e.getId())
			.message(e.getMessage())
			.objectId(e.getObjectId())
			.objectName(e.getObjectName())
			.user(UserDTO.of(e.getUser()))
			.build();
	}
	
}

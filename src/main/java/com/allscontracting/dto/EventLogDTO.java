package com.allscontracting.dto;

import com.allscontracting.model.EventLog;
import com.allscontracting.service.Converter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EventLogDTO {
	private final Long id;
	private final String objectName;
	private final String objectId;
	private final String eventType;
	private final String eventTime;
	private final UserDTO user;
	private final String message;
	
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

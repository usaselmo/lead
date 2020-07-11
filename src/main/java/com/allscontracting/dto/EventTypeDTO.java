package com.allscontracting.dto;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.event.EventType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventTypeDTO {

	private String name;
	private String status ; 
	private String action ;
	private String abbreviation;
	
	public static EventTypeDTO of(EventType event) {
		return EventTypeDTO.builder()
				.name(event.name())
				.action(event.getAction())
				.status(event.getStatus())
				.abbreviation(event.getAbbreviation())
				.build();
	}
	
	public static EventType toEventType(EventTypeDTO eventTypeDTO) {
		if(eventTypeDTO==null)
			Objects.requireNonNull("Event Type can not be null");
		if(StringUtils.isNotBlank(eventTypeDTO.getAction()))
			return EventType.valueOf(eventTypeDTO.getAction());
		else
			return EventType.valueOf(eventTypeDTO.getStatus());
	}
	
}

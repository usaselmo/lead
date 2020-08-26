package com.allscontracting.dto;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.event.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EventDTO {

	private String name;
	private String status ; 
	private String action ;
	private String abbreviation;
	
	public static EventDTO of(Event event) {
		if(event==null)
			return null;
		return EventDTO.builder()
				.name(event.name())
				.action(event.getAction())
				.status(event.getStatus())
				.abbreviation(event.getAbbreviation())
				.build();
	}
	
	public static Event toEventType(EventDTO eventTypeDTO) {
		if(eventTypeDTO==null)
			return null;
		if(StringUtils.isNotBlank(eventTypeDTO.getAction()))
			return Event.valueOf(eventTypeDTO.getAction());
		else
			return Event.valueOf(eventTypeDTO.getStatus());
	}
	
}

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
public class EventTypeDTO {

	private String name;
	private String status ; 
	private String action ;
	private String abbreviation;
	
	public static EventTypeDTO of(Event event) {
		return EventTypeDTO.builder()
				.name(event.name())
				.action(event.getAction())
				.status(event.getStatus())
				.abbreviation(event.getAbbreviation())
				.build();
	}
	
	public static Event toEventType(EventTypeDTO eventTypeDTO) {
		if(eventTypeDTO==null)
			Objects.requireNonNull("Event Type can not be null");
		if(StringUtils.isNotBlank(eventTypeDTO.getAction()))
			return Event.valueOf(eventTypeDTO.getAction());
		else
			return Event.valueOf(eventTypeDTO.getStatus());
	}
	
}

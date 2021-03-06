package com.allscontracting.dto;

import java.util.stream.Stream;

import org.springframework.util.StringUtils;

import com.allscontracting.event.Event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class EventDTO {

	private final String name;
	private final String status ; 
	private final String action ;
	private final String abbreviation;
	
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
	
	public static Event to(EventDTO eventTypeDTO) {
		if(eventTypeDTO==null) 
			return null;
		if(!StringUtils.isEmpty(eventTypeDTO.getAction()))
			return Stream.of(Event.values()).filter(e -> e.getAction().equals(eventTypeDTO.getAction())).findFirst().orElse(null);
		else
			return Event.valueOf(eventTypeDTO.getStatus());
	}
	
}

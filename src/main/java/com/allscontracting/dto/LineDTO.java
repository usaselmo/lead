package com.allscontracting.dto;

import com.allscontracting.model.Line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LineDTO {
	private final String id;
	private final String description;
	
	public static final LineDTO of(Line line) {
		if(line==null)
			return null;
		return LineDTO.builder()
			.id(String.valueOf(line.getId()))
			.description(line.getDescription())
		.build();
	}
	
	public static final Line toLine(LineDTO lineDTO) {
		if(lineDTO==null)
			return null;
		Line line = new Line();
		line.setDescription(lineDTO.getDescription());
		line.setId(lineDTO.getId()==null?null:Long.valueOf(lineDTO.getId()));
		return line;
	}
	
}

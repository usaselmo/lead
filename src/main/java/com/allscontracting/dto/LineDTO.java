package com.allscontracting.dto;

import com.allscontracting.model.Line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineDTO {
	private String id;
	private String description;
	
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

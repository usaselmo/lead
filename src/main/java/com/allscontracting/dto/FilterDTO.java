package com.allscontracting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilterDTO {
	
  private int pageRange;
  private int lines;
  private String searchText;
  private EventDTO event;

}

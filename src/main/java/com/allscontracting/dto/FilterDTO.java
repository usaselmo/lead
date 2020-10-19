package com.allscontracting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class FilterDTO {
	
  private final int pageRange;
  private final int lines;
  private final String searchText;
  private final EventDTO event;

}

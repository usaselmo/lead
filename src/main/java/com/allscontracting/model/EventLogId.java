package com.allscontracting.model;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLogId implements Serializable{

	private static final long serialVersionUID = -441160984603435547L;
	private String objectName;
	private String objectId;
	private String eventType;
	private Date eventTime;
	
}

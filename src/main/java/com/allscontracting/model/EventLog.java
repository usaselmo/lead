package com.allscontracting.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="event_log")
public class EventLog {
	
	@Id @GeneratedValue private Long id;
	@NotNull private String objectName;
	@NotNull private String objectId;
	@NotNull private String eventType;
	@NotNull private Date eventTime;
	private Long userId;
	private String message;

	public EventLog(String objectName, String objectId, String eventType, Date eventTime, Long userId, String message) {
		super();
		this.objectName = objectName;
		this.objectId = objectId;
		this.eventType = eventType;
		this.eventTime = eventTime;
		this.userId = userId;
		this.message = message;
	}

}

package com.allscontracting.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "event_log")
public class EventLog {

	@Id
	@GeneratedValue
	private Long id;
	private String objectName;
	private String objectId;
	private String eventType;
	private Date eventTime;
	@ManyToOne
	private User user;
	private String message;

	public EventLog() {
		super();
	}

	public EventLog(String objectName, String objectId, String eventType, Date eventTime, User user, String message) {
		super();
		this.objectName = objectName;
		this.objectId = objectId;
		this.eventType = eventType;
		this.eventTime = eventTime;
		this.user = user;
		this.message = message;
	}

}

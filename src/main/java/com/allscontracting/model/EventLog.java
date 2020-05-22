package com.allscontracting.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.allscontracting.event.EventType;

import lombok.Data;

@Data
@Entity(name="event_log")
public class EventLog {

	@javax.persistence.Id @GeneratedValue
	private Long Id;
	
	private String objectName;
	private String objectId;
	
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@Column(name="user_id")
	private Long userId;
	
}

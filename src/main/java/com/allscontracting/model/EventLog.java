package com.allscontracting.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.allscontracting.event.EventType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(EventLogId.class)
@Entity
public class EventLog {
	@Id	private String objectName;
	@Id	private String objectId;
	
	@Id	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Id	private Date eventTime;
	
	private Long userId;

}

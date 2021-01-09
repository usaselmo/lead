package com.allscontracting.type;

import java.io.File;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
public class Attachment {
	
	private final File file;
	private final LocalDateTime time;
	
	
	@RequiredArgsConstructor
	@Getter
	@ToString
	public static class AttachmentId{
		private final String emailId;
		private final String fileId;
	}
	
	

}

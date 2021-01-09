package com.allscontracting.type;

import java.io.File;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class FileUploaded {
	
	private final File file;
	private final LocalDateTime time;
	
	public FileUploaded(@NonNull File file, @NonNull LocalDateTime time) {
		super();
		this.file = file;
		this.time = time;
	}
	
	

}

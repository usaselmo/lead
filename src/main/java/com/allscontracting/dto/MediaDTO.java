package com.allscontracting.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.allscontracting.model.Media;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDTO {
	private Long id;
	private byte[] content;
	private String type;
	private String name;

	public static final MediaDTO of(Media media) {
		if(media== null)
			return null;
		return MediaDTO.builder()
				.id(media.getId())
				//.content(media.getContent())
				.type(media.getType())
				.name(media.getName())
				.build();
	}
	
	public static final List<MediaDTO> of(List<Media> medias){
		if(medias==null || medias.size()==0) return null;
		return medias.stream().map(media-> MediaDTO.of(media)).collect(Collectors.toList());
	}
	
	public static final Media toMedia(MediaDTO mediaDTO) {
		if(mediaDTO==null) return null;
		return new Media(mediaDTO.getId(), mediaDTO.getContent(), mediaDTO.getType(), mediaDTO.getName());
	}
	
	public static final List<Media> toMedia( List<MediaDTO> mediaDTOs){
		if(mediaDTOs==null || mediaDTOs.size()==0) return null;
		return mediaDTOs.stream().map(m->MediaDTO.toMedia(m)).collect(Collectors.toList());
	}
	
}

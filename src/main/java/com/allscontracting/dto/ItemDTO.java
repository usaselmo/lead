package com.allscontracting.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.allscontracting.model.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemDTO {
	private final String id;
	private final String title;
	private final String price;
	private final List<LineDTO> lines;

	public static final ItemDTO of(Item item) {
		if(item==null)
			return null;
		ItemDTO reas = ItemDTO.builder()
			.id(String.valueOf(item.getId()))
			.lines(item.getLines().stream().map(l->LineDTO.of(l)).collect(Collectors.toList()))
			.price(item.getPrice().toString().replaceAll("$",	""))
			.title(item.getTitle())
			.build();
		return reas;
	}
	
	public static final Item toItem(ItemDTO itemDTO) {
		if(itemDTO==null)
			return null;
		Item item = new Item();
		item.setId(StringUtils.isEmpty(itemDTO.getId())?null:Long.valueOf(itemDTO.getId()));
		item.setLines(itemDTO.getLines().stream().map(l->LineDTO.toLine(l)).collect(Collectors.toList()));
		item.setPrice(StringUtils.isEmpty(itemDTO.getPrice())?BigDecimal.ZERO:new BigDecimal(itemDTO.getPrice().replace("$", "")));
		item.setTitle(itemDTO.getTitle());
		return item;
	}
	
}

package com.allscontracting.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.allscontracting.model.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
	private String id;
	private String title;
	private String price;
	private List<LineDTO> lines;

	public static final ItemDTO of(Item item) {
		if(item==null)
			return ItemDTO.builder().build();
		ItemDTO reas = ItemDTO.builder()
			.id(String.valueOf(item.getId()))
			.lines(item.getLines().stream().map(l->LineDTO.of(l)).collect(Collectors.toList()))
			.price(item.getPrice().toString().replaceAll("$",	""))
			.title(item.getTitle())
			.build();
		return reas;
	}
	
	public static final Item toItem(ItemDTO itemDTO) {
		Item item = new Item();
		item.setId(StringUtils.isBlank(itemDTO.getId())?null:Long.valueOf(itemDTO.getId()));
		item.setLines(itemDTO.getLines().stream().map(l->LineDTO.toLine(l)).collect(Collectors.toList()));
		item.setPrice(StringUtils.isBlank(itemDTO.getPrice())?BigDecimal.ZERO:new BigDecimal(itemDTO.getPrice().replace("$", "")));
		item.setTitle(itemDTO.getTitle());
		return item;
	}
	
}

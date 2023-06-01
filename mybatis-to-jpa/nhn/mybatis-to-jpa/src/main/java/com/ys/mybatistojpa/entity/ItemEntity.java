package com.ys.mybatistojpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ys.mybatistojpa.model.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Items")
public class ItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long itemId;

	@Column(name = "item_name")
	private String itemName;

	private Long price;

	public Item toItemDto() {
		Item itemDto = new Item();
		itemDto.setItemId(this.itemId);
		itemDto.setItemName(this.itemName);
		itemDto.setPrice(this.price);

		return itemDto;
	}
}
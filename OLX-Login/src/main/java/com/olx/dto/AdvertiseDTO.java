package com.olx.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdvertiseDTO {
	private long id;
	private String title;
	private String description;
	private double price;
	private long categories;
	private String category;
	private LocalDate createdDate;
	private LocalDate modifiedDate;
	private String active;
	private String username;
	private String postedBy;
	public AdvertiseDTO(long id, String title, String description, double price, long categories, LocalDate createdDate,
			LocalDate modifiedDate, String active, String username, String postedBy,String category) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.price = price;
		this.categories = categories;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.active = active;
		this.username = username;
		this.postedBy = postedBy;
		this.category = category;
	}
	
}

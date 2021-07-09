package com.olx.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="advertises")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AdvertiseEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	private String title;
	private String description;
	private double price;
	private long category;
	@Column(name="created_date")
	private LocalDate createdDate;
	@Column(name="modified_date")
	private LocalDate modifiedDate;
	private String active;
	@Column(name="username")
	private String username;
	public AdvertiseEntity(String title, String description, double price, long category, LocalDate createdDate,
			LocalDate modifiedDate, String active, String username) {
		super();
		this.title = title;
		this.description = description;
		this.price = price;
		this.category = category;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
		this.active = active;
		this.username = username;
	}
	
}
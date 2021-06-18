package com.dfz.model;

import java.io.Serializable;

import lombok.Data;

/**
 * Employee Entity
 * 
 * @author	Felix Du
 * @date	2021-06-14
 */
@Data
public class Employee implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String email;
	private Integer age;
	private Integer deptId;
	
	public Employee() {
	}
	
	public Employee(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Employee(Integer id, String name, String email, Integer age, Integer deptId) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.age = age;
		this.deptId = deptId;
	}
	

}

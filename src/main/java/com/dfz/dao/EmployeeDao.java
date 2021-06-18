package com.dfz.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dfz.model.Employee;

/**
 * Employee Dao
 * 
 * @author	Felix Du
 * @date	2021-06-14
 */
@Mapper
public interface EmployeeDao {
	
	List<Employee> findAll();
	
	Employee findById(Integer id);
	
	int insert(Employee employee);
	
	int update(Employee employee);
	
	void deleteById(Integer id);
	
	void deleteAll();

}

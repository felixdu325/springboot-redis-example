package com.dfz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dfz.dao.EmployeeDao;
import com.dfz.model.Employee;

/**
 * Employee Service
 * 
 * @author	Felix Du
 * @date	2021-06-14
 */
@Service
@CacheConfig(cacheNames = "employee")
public class EmployeeService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	
	//@Cacheable(cacheNames = "employee")
	@Cacheable
	public List<Employee> findAll() {
		System.out.println("查询所有员工......");
		return employeeDao.findAll();
	}
	
	//@Cacheable(cacheNames = "employeeId")
	//@Cacheable	//这里必须指定key,不能使用默认由KeyGenerator生成的key,否则更新,删除等缓存操作不能工作.
	@Cacheable(key = "#id")
	public Employee findById(Integer id) {
		System.out.println("查询员工:"+id);
		return employeeDao.findById(id);
	}
	
	/**
	 * 添加员工,暂不需要加入缓存,等查询时再加
	 */
	public void add(Employee emp) {
		System.out.println("添加员工:"+emp);
		employeeDao.insert(emp);
	}
	
	/**
	 * 将结果缓存，并且该方法不管缓存是否存在，每次都会执行
	 * @param emp
	 * @return
	 * 
	 * public void update(Employee emp)返回值为void会报错.缓存的是方法返回的值!!!
	 */
	//@CachePut	//这里必须指定key
	@CachePut(key = "#emp.id")
	public Employee update(Employee emp) {
		System.out.println("更新员工:"+emp);
		employeeDao.update(emp);
		
		return emp;
	}
	
	/**
	 * 根据指定key移除缓存
	 * @param emp
	 */
	//@CacheEvict	//这里必须指定key
	@CacheEvict(key = "#id")
	public void deleteById(Integer id) {
		System.out.println("删除员工:"+id);
		employeeDao.deleteById(id);
	}
	
	/**
	 * 移除当前cacheName下所有缓存
	 */
	@CacheEvict(allEntries = true)
	public void deleteAll() {
		System.out.println("删除所有员工");
		//employeeDao.deleteAll();
	}
	
}

package com.dfz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.dfz.model.Employee;
import com.dfz.service.EmployeeService;

/**
 * Employee Controller
 * 
 * @author	Felix Du
 * @date	2021-06-14
 */
@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	//[{"id":1,"name":"张三","email":"zhangsan@qq.com","age":20,"deptId":1},{"id":2,"name":"李四","email":"lisi@qq.com","age":18,"deptId":1}]
	@GetMapping("/list")
	public List<Employee> list() {
		return employeeService.findAll();
	}
	
	
	/*	//也可以工作
	public String emp(@PathVariable Integer id) {
		employeeService.selectById(id);
		return "添加缓存 key = " + id;
	}
	*/
	//{"id":1,"name":"张三","email":"zhangsan@qq.com","age":20,"deptId":1}
	@GetMapping("/emp/{id}")
	public Employee emp(@PathVariable Integer id) {
		return employeeService.findById(id);
	}
	
	
	@GetMapping("/add")
	public String add() {
		//Employee emp=new Employee(1, "张三", "zhangsan@qq.com", 19, 1);
		Employee emp=new Employee(3, "王五", "wangwu@qq.com", 22, 2);
		employeeService.add(emp);
		return "添加员工成功! 没有加入缓存";
	}
	
	@GetMapping("/update")
	public Employee update() {
		return employeeService.update(new Employee(1, "三san"));
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		employeeService.deleteById(id);
		return "移除缓存 key = " + id;
	}
	
	@GetMapping("/deleteAll")
	public String deleteAll() {
		employeeService.deleteAll();
		return "移除所有缓存";
	}
	
}

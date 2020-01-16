package com.changgou.config.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * freightTemplate实体类
 * @author 黑马架构师2.5
 *
 */
@Table(name="tb_freight_template")
public class FreightTemplate implements Serializable {

	@Id
	private Integer id;//ID


	
	private String name;//模板名称
	private String type;//计费方式
	private String number; //数量
	private Double startPrice; //起步价
	private String addNumber;  //增加数量
	private Double addPrice;   //加价
	private String region;		//地区(目的地)
	private Date addTime;		//添加事件
	private Integer isEnable;	//是否启用

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public String getAddNumber() {
		return addNumber;
	}

	public void setAddNumber(String addNumber) {
		this.addNumber = addNumber;
	}

	public Double getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(Double addPrice) {
		this.addPrice = addPrice;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
}

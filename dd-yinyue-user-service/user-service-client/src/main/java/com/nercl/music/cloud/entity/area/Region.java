package com.nercl.music.cloud.entity.area;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "regions")
public class Region implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9211335447422640116L;

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;
	
	@Column(name = "city_id")
	private String cityId;

	@ManyToOne
	@JoinColumn(name = "city_id", insertable = false, updatable = false)
	private City city;

	@Column(name = "province_id")
	private String provinceId;

	@ManyToOne
	@JoinColumn(name = "province_id", insertable = false, updatable = false)
	private Province province;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public Province getProvince() {
		return province;
	}

	public void setProvince(Province province) {
		this.province = province;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

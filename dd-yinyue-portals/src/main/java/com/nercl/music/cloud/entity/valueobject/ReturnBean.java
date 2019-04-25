/**
 * 定义普通接口返回Bean
 * */
package com.nercl.music.cloud.entity.valueobject;

import java.io.Serializable;

public class ReturnBean<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5960110766649534327L;
	
	 // 返回码
    private int code;

    // 返回消息
    private String msg;
    
    // 返回内容
    private T data;

	public ReturnBean() {
		super();
	}

	public ReturnBean(T data) {
		super();
		this.data = data;
	}

	public ReturnBean(Throwable e) {
		super();
		this.msg = e.toString();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

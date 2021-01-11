package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.Files;
import com.net.system.model.OrderRefunds;

public class OrderRefundsVo extends OrderRefunds {

	private List<Files> files;

	public List<Files> getFiles() {
		return files;
	}

	public void setFiles(List<Files> files) {
		this.files = files;
	}

}

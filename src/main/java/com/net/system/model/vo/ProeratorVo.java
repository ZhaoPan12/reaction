package com.net.system.model.vo;


import com.net.system.model.Product;

public class ProeratorVo extends Product{

    
    private String[] files;
    
    private String mainFile;
    
    private String labelArray;
    

	public String[] getFiles() {
		return files;
	}

	public void setFiles(String[] files) {
		this.files = files;
	}

	public String getMainFile() {
		return mainFile;
	}

	public void setMainFile(String mainFile) {
		this.mainFile = mainFile;
	}

	public String getLabelArray() {
		return labelArray;
	}

	public void setLabelArray(String labelArray) {
		this.labelArray = labelArray;
	}
	
	

	
}

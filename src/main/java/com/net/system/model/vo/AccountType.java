package com.net.system.model.vo;

public class AccountType {

	private String acc_kind;
	private String accledger_name;
	public String getAcc_kind() {
		return acc_kind;
	}
	public void setAcc_kind(String acc_kind) {
		this.acc_kind = acc_kind;
	}
	public String getAccledger_name() {
		return accledger_name;
	}
	public void setAccledger_name(String accledger_name) {
		this.accledger_name = accledger_name;
	}
	@Override
	public String toString() {
		return "AccountType [acc_kind=" + acc_kind + ", accledger_name=" + accledger_name + "]";
	}
	
	
}

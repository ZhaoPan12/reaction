package com.net.system.model.vo;

public class ConsumptionVo {
	
	//交易前金额
	private String db_bal;
	//优惠金额
	private String discount_amt;
	//终端批次号
	private String tr_batch_no;
	//上送时间
	private String insert_time;
	//交易状态
	private String tr_state;
	//账户类型
	private String accledger_name;
	//流水号
	private String action_no;
	//清分日期
	private String clr_date;
	//交易时间
	private String tr_date;
	//商户
	private String biz_name;
	//卡号
	private String db_card_no;
	//交易金额
	private String db_amt;
	//交易名称
	private String tr_code_name;
	//终端流水号
	private String term_tr_no;
	//终端号
	private String oper_id;
	public String getDb_bal() {
		return db_bal;
	}
	public void setDb_bal(String db_bal) {
		this.db_bal = db_bal;
	}
	public String getDiscount_amt() {
		return discount_amt;
	}
	public void setDiscount_amt(String discount_amt) {
		this.discount_amt = discount_amt;
	}
	public String getTr_batch_no() {
		return tr_batch_no;
	}
	public void setTr_batch_no(String tr_batch_no) {
		this.tr_batch_no = tr_batch_no;
	}
	public String getInsert_time() {
		return insert_time;
	}
	public void setInsert_time(String insert_time) {
		this.insert_time = insert_time;
	}
	public String getTr_state() {
		return tr_state;
	}
	public void setTr_state(String tr_state) {
		this.tr_state = tr_state;
	}
	public String getAccledger_name() {
		return accledger_name;
	}
	public void setAccledger_name(String accledger_name) {
		this.accledger_name = accledger_name;
	}
	public String getAction_no() {
		return action_no;
	}
	public void setAction_no(String action_no) {
		this.action_no = action_no;
	}
	public String getClr_date() {
		return clr_date;
	}
	public void setClr_date(String clr_date) {
		this.clr_date = clr_date;
	}
	public String getTr_date() {
		return tr_date;
	}
	public void setTr_date(String tr_date) {
		this.tr_date = tr_date;
	}
	public String getBiz_name() {
		return biz_name;
	}
	public void setBiz_name(String biz_name) {
		this.biz_name = biz_name;
	}
	public String getDb_card_no() {
		return db_card_no;
	}
	public void setDb_card_no(String db_card_no) {
		this.db_card_no = db_card_no;
	}
	public String getDb_amt() {
		return db_amt;
	}
	public void setDb_amt(String db_amt) {
		this.db_amt = db_amt;
	}
	public String getTr_code_name() {
		return tr_code_name;
	}
	public void setTr_code_name(String tr_code_name) {
		this.tr_code_name = tr_code_name;
	}
	public String getTerm_tr_no() {
		return term_tr_no;
	}
	public void setTerm_tr_no(String term_tr_no) {
		this.term_tr_no = term_tr_no;
	}
	public String getOper_id() {
		return oper_id;
	}
	public void setOper_id(String oper_id) {
		this.oper_id = oper_id;
	}
	@Override
	public String toString() {
		return "ConsumptionVo [db_bal=" + db_bal + ", discount_amt=" + discount_amt + ", tr_batch_no=" + tr_batch_no
				+ ", insert_time=" + insert_time + ", tr_state=" + tr_state + ", accledger_name=" + accledger_name
				+ ", action_no=" + action_no + ", clr_date=" + clr_date + ", tr_date=" + tr_date + ", biz_name="
				+ biz_name + ", db_card_no=" + db_card_no + ", db_amt=" + db_amt + ", tr_code_name=" + tr_code_name
				+ ", term_tr_no=" + term_tr_no + ", oper_id=" + oper_id + "]";
	}

	
}

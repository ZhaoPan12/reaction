package com.net.system.model;

public class Key {
    /**
     * id
     */
    private String id;

    /**
     * 渠道号
     */
    private String channelId;

    /**
     * 私钥
     */
    private String privatekey;

    /**
     * 公钥
     */
    private String publickey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
    
}

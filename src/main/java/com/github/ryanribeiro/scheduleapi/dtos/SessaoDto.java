package com.github.ryanribeiro.scheduleapi.dtos;

public class SessaoDto {
    private String login;
    private String token;
    
    public SessaoDto() {
	}

    public SessaoDto(String login, String token) {
		super();
		this.login = login;
		this.token = token;
	}

	public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


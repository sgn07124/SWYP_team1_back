package com.example.swyp_team1_back.domain.user.dto;

public class SignupRequest {
    private String code;
    private boolean agreePicu;
    private boolean agreeTos;
    private boolean agreeMarketing;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isAgreePicu() {
        return agreePicu;
    }

    public void setAgreePicu(boolean agreePicu) {
        this.agreePicu = agreePicu;
    }

    public boolean isAgreeTos() {
        return agreeTos;
    }

    public void setAgreeTos(boolean agreeTos) {
        this.agreeTos = agreeTos;
    }

    public boolean isAgreeMarketing() {
        return agreeMarketing;
    }

    public void setAgreeMarketing(boolean agreeMarketing) {
        this.agreeMarketing = agreeMarketing;
    }
}

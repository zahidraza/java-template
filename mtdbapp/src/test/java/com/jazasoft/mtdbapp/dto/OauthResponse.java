package com.jazasoft.mtdbapp.dto;

/**
 * Created by mdzahidraza on 04/07/17.
 */
public class OauthResponse {
    private String access_token;
    private String token_type;
    private String resfresh_token;
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getResfresh_token() {
        return resfresh_token;
    }

    public void setResfresh_token(String resfresh_token) {
        this.resfresh_token = resfresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "OauthResponse{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", resfresh_token='" + resfresh_token + '\'' +
                ", scope='" + scope + '\'' +
                '}';
    }
}

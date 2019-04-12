package io.github.v7lin.setup.domain;

import com.beust.jcommander.Strings;

public class Configure {

    public String secretId;

    public String secretKey;

    public String region;

    public Configure() {
    }

    public Configure(String secretId, String secretKey, String region) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.region = region;
    }

    public boolean isLegal() {
        return !Strings.isStringEmpty(secretId) && !Strings.isStringEmpty(secretKey) && !Strings.isStringEmpty(region);
    }
}

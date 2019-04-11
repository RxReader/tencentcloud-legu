package io.github.v7lin.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.Strings;

@Parameters(commandDescription = "configure your profile(eg:secretId, secretKey, region).")
public final class ConfigureCommand extends Command {

    @Parameter(required = true, names = {"-secretId"}, description = "TencentCloud API SecretId")
    private String secretId;

    @Parameter(required = true, names = {"-secretKey"}, description = "TencentCloud API SecretKey")
    private String secretKey;

    @Parameter(required = true, names = {"-region"}, description = "which the region you want to work with belongs[ap-guangzhou, ap-shanghai].")
    private String region;

    @Parameter(names = {"-output"}, description = "TencentCloud API response format[json]", hidden = true)
    private String output;

    @Override
    protected void checkArgs() {
        if (Strings.isStringEmpty(secretId)) {
            throw new IllegalArgumentException("secretId is null or empty");
        }
        if (Strings.isStringEmpty(secretKey)) {
            throw new IllegalArgumentException("secretKey is null or empty");
        }
        if (Strings.isStringEmpty(region)) {
            throw new IllegalArgumentException("region is null or empty");
        }
    }

    @Override
    protected void execute() throws Exception {

    }
}

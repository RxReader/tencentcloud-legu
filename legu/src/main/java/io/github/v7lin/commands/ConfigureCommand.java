package io.github.v7lin.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "configure your profile(eg:secretId, secretKey, region, output).")
public final class ConfigureCommand extends Command {

    @Parameter(required = true, names = {"-secretId"}, description = "TencentCloud API SecretId")
    private String secretId;

    @Parameter(required = true, names = {"-secretKey"}, description = "TencentCloud API SecretKey")
    private String secretKey;

    @Parameter(required = true, names = {"-region"}, description = "which the region you want to work with belongs[ap-guangzhou, ap-shanghai].")
    private String region;

    @Parameter(names = {"-output"}, description = "TencentCloud API response format[json, text]")
    private String output;

    @Override
    protected void checkArgs() {

    }

    @Override
    protected void execute() throws Exception {

    }
}

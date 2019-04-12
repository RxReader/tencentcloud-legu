# tencentcloud-legu

[![Build Status](https://cloud.drone.io/api/badges/v7lin/tencentcloud-legu/status.svg)](https://cloud.drone.io/v7lin/tencentcloud-legu)

### libraries

* [hsiafan/apk-parser](https://github.com/hsiafan/apk-parser)
* [TencentCloud/tencentcloud-sdk-java](https://github.com/TencentCloud/tencentcloud-sdk-java)
* [tencentyun/cos-java-sdk-v5](https://github.com/tencentyun/cos-java-sdk-v5)

### 腾讯云

[控制台-移动应用安全](https://console.cloud.tencent.com/ms/reinforce/list)

### usage

* java

````
java -jar legu/build/libs/legu-all.jar -version
java -jar legu/build/libs/legu-all.jar -help
java -jar legu/build/libs/legu-all.jar configure -secretId $TENCENT_SECRET_ID -secretKey $TENCENT_SECRET_KEY -region $TENCENT_REGION
java -jar legu/build/libs/legu-all.jar legu -in legu/apk/*.apk -out legu/apk/
````

* docker

````
docker run --rm -it -v ${PWD}/legu:/legu alpine:3.9.2 sh -c "mkdir /legu/apk && wget ${TEST_APK_URL} -P /legu/apk"
````

* drone

````
````

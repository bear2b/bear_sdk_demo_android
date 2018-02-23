## Latest BEARSDK build 2.0.0
Detailed explanations can be found at:
https://developer.bear2b.com/docs/sdk-android/

## Installation
```groovy
maven {
    url 's3://mobile-dev.bear2b.com/bearsdk'
    credentials(AwsCredentials) {
        accessKey "AKIAIDDEJY37ZIBU3Q4A"
        secretKey "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"
    }
}

dependencies {
    compile('com.bear:bearsdk:2.0.0@aar') {
        transitive = true
    }
}
```
## Documentation
https://bear2b.github.io/private_bear_sdk_android/

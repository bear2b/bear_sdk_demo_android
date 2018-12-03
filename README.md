## Latest BEARSDK build 2.0.11
Detailed explanations can be found at:
https://developer.bear2b.com/docs/sdk-android/

## Installation
```groovy
maven { url 'https://s3-eu-west-1.amazonaws.com/mobile-dev.bear2b.com/bearsdk' }

dependencies {
    compile('com.bear:bearsdk:2.0.11@aar') {
        transitive = true
    }
}
```
## Documentation
https://bear2b.github.io/private_bear_sdk_android/

## Sample targets
Once you compiled and installed the application on a physical device,
you can scan the markers located in the
https://github.com/bear2b/bear_sdk_demo_android/tree/master/markers folder.

These are examples to show you preview of augmented targets
using https://go.bear2b.com.

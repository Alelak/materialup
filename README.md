MaterialUp Api Wrapper
======================       

Unofficial MaterialUp API Wrapper for Android.

<img src="screenshots/screenshot.png" width="400">

## Usage
```java
MaterialUp.getPosts(this, page, new MaterialUpCallback() {
    @Override
	public void onSuccess(List <Post> posts, Response response) {
		// do stuff with posts!
	}

	@Override
	public void onFailure(Request request, IOException e) {
		// show a toast!

	}
});
```
## Download
Gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    // ... other dependencies here
    compile'com.github.Alelak:materialup:1.0'
}
```

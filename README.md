# CrowdShelf for Android

###Git branching

- Master inneholder alltid siste "stable" release. v0.2, v0.3 etc. 
- Branchen "dev" merges inn i master når en ny versjon er ferdig.
- Feature branch ut av "dev" når ny feature skal implementeres eller bug skal fikses. 
- Lage pull request på feature branch inn i dev når man er ferdig med feature/bug.


### Unit testing
####Robolectric
Robolectric lets us test some classes and methods in the app without running the Android Emulator.

To enable Robolectric, go to "Build variables" in the pane to the left in Android Studio / IntelliJ, and under "Test Artifact" choose "Unit Test".

Test classes should be placed in /src/main/test/java/com/crowdshelf/app/.
Neither the testclasses nor their methods need to follow any specific name convention, but setup methods needs to be tagged with `@Before` and the test methods needs to be tagged with `@Test`.

If you get an error complaining that Robolectric can't find Androidmanifest.xml, go to Run -> Edit configurations -> JUnit -> select test class and append \app (or /app for Mac) to the end of the Working directory path.

For more info see: https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
####Android JUnitRunner
Our database, Realm, *does not support Robolectric and must be run in the Android Emulator.*

To speed up the emulator (and you have an Intel CPU), download Intel HAXM with the SDK manages and install it manually: www.developer.com/ws/android/development-tools/supercharge-your-android-emulator-speed-with-intel-emulation-technologies.html It makes a huge difference.

To set up emulator unit tests:

Set Test Artifact under Build variable to Android Instrument Testing.

Go to Run -> Edit configurations -> Create a new configuration under Android Tests and set instrumentation runner to: android.support.test.runner.AndroidJUnitRunner

Test classes for JUnitRunner should be places in /src/main/androidTest/java/com/crowdshelf/app/

You should also disable animations. In the emulator, go to Settings -> Developer Options -> Drawing
and set Window Animations scale, Transition Animations Scale and Animator Duration Scale to *OFF*.

For testing Fragments you may want to read section 2 here:
http://stackoverflow.com/questions/8199000/unit-test-an-android-fragment/32295976#32295976

### JSON parsing
JSON is parsed to and from `Book`, `Crowd` and `User` -objects using Google's GSON.
GSON tries to match the name fields in the JSON-data with the name of the field variables in the chosen class.
If the names and types matches, a new object is created using the default no-args constructor with all the matching fields set to the values found in the JSON data.

If the fields do not match up, GSON need a serializer to create JSON-data from an object or a deserializer to create an object from JSON-data.

GSON does not use non-default constructors, getters or setters, but the latter two should be used create serializer and deserializer for classes that GSON can't handle automatically.

```java
Gson gson = new GsonBuilder()
            \\ If we need deserialiser for the user class
            .registerTypeAdapter(User.class, new UserDeserializer())
            .setPrettyPrinting()
            .create();
            
Book book = gson.fromJson(jsonString, Book.class);
User user = gson.fromJson(jsonString, User.class);
```

## Structure and data model
The `Book`, `Crowd` and `User` classes are made to match the structure of the objects returned from the API as close as possible with only minor changes to make it easier to use GSON. 
To get or create an object of any of these classes, the rest of the app should use the `MainController` class which 
should take care of retrieving the necessary data from the server, storing it in the databse 
and avoiding duplicate local objects.
## Realm
We use Realm as the database in our App to allow the data we retrieve from the server to be stored perstistently and to avoid deadlocks.

The database cannot hold lists of primitive objects - e.g. List<String>. To store a list of strings, it is necessary to create a wrapper class like this:
```java
class BookId{
	private String bookId;
	// getters and setters
}
```
Then we can make a list which is compatible with the database: RealmList<BookId>.
## Otto
The Otto Event Bus is used to notify UI classes that data is downloaded from the server and put into the database.
The main bus instance is in the MainTabbedActivity class and can be retrieved with MainTabbedActivity.getBus(). 
DBEvents such as BOOK_READY are put onto the bus, and classes on the same thrad as MainTabbedActivity can implement
listeners with the @Subscribe annotation.
Any thread can post to this bus using bus.post().

## Licence
MIT-licence. See the `LICENCE`-file.

# CrowdShelf for Android
### Unit testing
Realm does not work with Robolectric.


Unit testing is done with Robolectric.
This lets us test classes and methods in the app without running the Android Emulator.

To enable Robolectric, go to "Build variables" in the pane to the left in Android Studio / IntelliJ, and under "Test Artifact" choose "Unit Test".

Test classes should be placed in /src/main/test/java/com/crowdshelf/app/.
Neither the testclasses nor their methods need to follow any specific name convention, but setup methods needs to be tagged with `@Before` and the test methods needs to be tagged with `@Test`.

If you get an error complaining that Robolectric can't find Androidmanifest.xml, go to Run -> Edit configurations -> JUnit -> select test class and append \app (or /app for Mac) to the end of the Working directory path.

For more info see: https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
### JSON parsing
JSON is parsed to and from `Book`, `Crowd` and `User` -objects using Google's GSON.
GSON tries to match the name fields in the JSON-data with the name of the field variables in the chosen class.
If the names and types matches, a new object is created using the default no-args constructor with all the matching fields set to the values found in the JSON data.

If the fields do not match up, GSON need a serializer to create JSON-data from an object or a deserializer to create an object from JSON-data.

GSON does not use non-default constructors, getters or setters, but the latter two should be used create serializer and deserializer for classes that GSON can't handle automatically.

```java
Gson gson = new GsonBuilder()
            \\ Need deserialiser for user class, but not book class
            .registerTypeAdapter(User.class, new UserDeserializer())
            .setPrettyPrinting()
            .create();
            
Book book = gson.fromJson(jsonString, Book.class);
User user = gson.fromJson(jsonString, User.class);
```

## Structure and data model
The `Book`, `Crowd` and `User` classes are made to match the structure of the objects returned from the API as close as possible with only minor changes to make it easier to use GSON. To get or create an object of any of these classes, the rest of the app should use the `MainController` class which should take care of retrieving the necessary data from the server and avoiding duplicate local objects.

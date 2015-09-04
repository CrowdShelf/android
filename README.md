# CrowdShelf for Android
### Unit testing
Unit testing is done with Robolectric.
Robolectric lets us test classes and methods in the app without running the Android Emulator.

Test files should be placed in /src/main/test/java/com.crowdshelf.app/.
Neither the testclasses or their methods need to follow a specific name-convention, but setup methods needs to be tagged with @Before and the test methods needs to be tagged with @Test.

For more info see: https://github.com/codepath/android_guides/wiki/Unit-Testing-with-Robolectric
### JSON parsing
JSON is parsed to and from objects using Google's GSON.
GSON tries to match the name-fields in the JSON-data with the name of the field variables of the chosen class.
If a name and type match up, a new object is created using the default no-args constructor with all the matching fields set to the values found in the JSON data.

If the fields do not match up, GSON need a serializer to create JSON-data from an object or a deserializer to create an object from JSON-data.

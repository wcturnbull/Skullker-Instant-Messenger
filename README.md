# CS18000-Project-5

Test Classes Description
To test the three classes that aren't covered in the GUI testing we implemented three different test classes for the Account, Message, and Chat classes.
Within each class there is a comprehensive list of test methods that ensure that all the methods work as intended for the program, all the methods exist with the correct modifier and return types, the instance variables exist with the correct modifier, and the constructors exist with the correct parameters and modifers.

All tests were created with JUnit4 as the mainframe, we used the standard assertEquals/assertTrue methods for most of the testing in regards to actually making sure the methods work as intended. The rest of the work was done utilizing java.lang.reflect which enabled us to grab certain data that previously would have been very tough to verify such as getting methods, modifiers, and fields. With this information it was simple enough to use the methods provided by java.lang.reflect to create a whole and comprehensive test class for each of the three classes that could not be tested through the GUI.

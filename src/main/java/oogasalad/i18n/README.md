# Translator Guide

* This translator guide is meant to explain how to integrate a Translator object into a new feature
  in a project, using the Runner integration as an example.

## Goal:

* Change ALL parts of the Runner view to be adaptable to different languages
* Right now, only the action panel is supported. There are other regions like "In Play", etc that
  should be replaced.

## Rundown of Changes:

* Basically, you need to add a `Translator` object to intercept strings at the last moment to ensure
  that they are translated
    * For the runner team, initialization looks like "translator = new DefaultTranslator(
      AppType.RUNNER, "(language here)"
* Try to only add translator objects at the top level classes, otherwise you'll have to add
  a `Translator` object everywhere with strings and that gets annoying
* If you're adding the translator as an instance variable to a new class, please try to inherit the
  translator from a higher class so everyone has a pointer to the same translator object.
    * We do this so that any call to change the current language can propagate to all parts of the
      Runner at once
* If your intended message is simple and does not need a `String.format()` call, just use
  the `translator.translateToUserSpace(String)` method, where the input is the resource key into the
  language files.
    * Note that these language files can be found in /resources/runner/languages/(specific language)
    * You'll have to add the resource key and corresponding value manually, and make sure that all
      the languages have that same key and adequate translation.
* If your intended message is complex and requires a `String.format()` call, package your
  information into a `TranslationPackage` which takes a resource key and arbitrary number of Strings
  after to represent the inputs.
    * I ended up having to add a `TranslationPackage description()` method to use instead
      of `name()` so that it would return the information I needed.
    * Then, you'll use the `translator.translateToUserSpace(TranslationPackage)` call to fetch your
      translated string.
    * Note that like above, you'll have to add the resource key and corresponding value manually,
      and make sure that all the languages have that same key and adequate translation
* I've gone through and translated all of the current possible action buttons that show up on the
  action panel. Please refer to these for examples, and I'll also walk through my process below.
* You can also change lines 37 and 38 in `RunnerMain` to be different languages (English, Spanish,
  French, German, Portuguese) to see the effects on the action panel for yourself.

---

* Example 1: A simple translation for "End Phase"
    * I saw that the highest level class that was creating the End Phase button was
      in `CardViewManager` so I added a translator there.
    * On line 139, instead of `Button endPhase = new Button("End Phase"));`, I
      used `Button endPhase = new Button(translator.translateToUserSpace("EndPhase"));`
    * Then, I added the mapping `EndPhase=End Phase` in
      /resources/runner/languages/en-us_messages.properties, and translated the values for the other
      languages.

--- 

* Example 2: A complex translation for "Create Group"
    * Like before, I saw that the highest level class that was creating the Create Group button was
      in `CardViewManager` so I added a translator there (was already there as a private instance
      variable).
    * I saw that the button name was being generated on line
      134: `Button button = new Button(action.name());`
    * This is a complex string that needs formatting, so I added a new method to the `PlayerAction`
      interface: `TranslationPackage description();`
        * I did this to avoid colliding with the existing methods, and I didn't want to break
          anything else.
    * Then, in `CreateGroup`, I implemented the `description()` method in the following
      way: `return new TranslationPackage("CreateGroup",
      direction.toString().toLowerCase(), viewType.toString().toLowerCase(), tag);`
        * Note that "CreateGroup" will become the resource key to fetch from the properties file.
        * Also note that any String after the first one ("CreateGroup") is treated as a value to
          shovel in during formatting. You can tack on as many Strings as you need here, in comma
          separated form.
    * Zooming all the way out to line 134 in `CardViewManager`, I wrote a new
      line: `Button button = new Button(translator.translateToUserSpace(action.description()));`
    * Then, I added the following mapping `CreateGroup=Create new %s %s with tag "%s"` to
      /resources/runner/languages/en-us_messages.properties, and translated the values for the other
      languages.

Good luck, and let me know if you have questions!
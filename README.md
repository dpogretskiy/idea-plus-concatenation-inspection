custom-java-inspections-intellij-idea-plugin
==================================

At the moment:

  - Warns when you try to use "+ concatenation" on something different to String and primitive types
  - Warns when you try to call .length() method on java.io.File instance
  - Warns when you try to call .toString() method on subclasses of java.lang.Throwable

Theese inspections were needed for my main job, but still can be an example.

PS: Written is Scala

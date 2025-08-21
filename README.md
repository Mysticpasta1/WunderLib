[![](https://jitpack.io/v/quiqueck/WunderLib.svg)](https://jitpack.io/#quiqueck/WunderLib)

# WunderLib

WunderLib is a library mod mainly focused on UI and Math, MC 1.19

## Importing:

You can easily include WunderLib as an internal Dependency by adding the following to your `build.gradle`:

```
repositories {
    ...
    maven { url 'https://jitpack.io' } 
}
```

```
dependencies {
    ...
    modImplementation "com.github.Mysticpasta1:WunderLib:${project.wunderlib_version}"
    include "com.github.Mysticpasta1:WunderLib:${project.wunderlib_version}"
}
```

```
"depends": {
  ...
  "wunderlib": ["1.0.x", ">1.0.0"]
}
```

In this example `1.0.0` is the WunderLib Version you are building against.

## Building:

* Clone repo
* Run command line in folder: gradlew build
* Mod .jar will be in ./build/libs

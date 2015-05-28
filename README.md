# Self.conference Android

![](http://f.cl.ly/items/13351P0x182Y3l083Q1x/self.conf.avatar.png)

[![Build Status](https://travis-ci.org/Selfconference/selfconf-android.svg?branch=master)](https://travis-ci.org/Selfconference/selfconf-android)

## Setup

- create a `debug.properties` file at the root of the repository
- add your [Parse](https://parse.com/) project credentials

```
parseApplicationId=XXXXXX
parseClientSecret=XXXXXX
```

- sync with Gradle and build the app

## Run the tests

```
./gradlew testDebug
```

## Assemble the app

```
./gradlew assembleDebug
```
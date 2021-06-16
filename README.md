# EnhancedDateTimePicker component for Vaadin Flow

This project is based on [DateTimePicker component for Vaadin Flow](https://github.com/vaadin/vaadin-date-time-picker-flow)
and also in [EnhancedDatePicker component](https://github.com/vaadin-component-factory/enhanced-date-picker).

The idea of this new EnhancedDateTimePicker component is the possibility of adding format and parse patterns for both date & time parts of a DateTimePicker.
Date part uses EnhancedDatePicker component and time part is using an extension of [TimePicker component for Vaadin Flow](https://github.com/vaadin/vaadin-time-picker-flow)
that adds an API for setting formatting and parsing patterns.

As in EnhancedDatePicker, the formatting for the time part is done by JavaScript library [date-fns v2.0.0-beta.2](https://date-fns.org/v2.0.0-beta.2/docs/Getting-Started). More information about supported formatting paterns can be found here:
https://date-fns.org/v2.0.0-beta.2/docs/format

This component is part of Vaadin Component Factory.

## How to use EnhancedDateTimePicker

Formatting patterns can be set using methods: 

- `setDatePattern(String dateFormattingPattern)` for date part 
- `setTimePattern(String timeFormattingPattern)` for time part

or by using constructor:

`EnhancedDateTimePicker(LocalDateTime initialDateTime, String dateFormattingPattern, String timeFormattingPattern)`

Patterns used for parsing user's input can be set using methods 

- `setDateParsers(String ... dateParsers)` for date part
- `setTimeParsers(String ... timeParsers)` for time part

Also, locale can be set for the whole component using method: 

`setLocale(Locale locale)` 

Here's a simple example: 

```java
EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
dateTimePicker.setDatePattern("dd-MM-yyyy");
dateTimePicker.setTimePattern("HH.mm.ss");
dateTimePicker.setDateParsers("dd-MM-yyyy", "dd.MM.yy");
dateTimePicker.setTimeParsers("HH.mm.ss", "HH:mm");
```

## Running the component demo
Run from the command line:
`mvn  -pl enhanced-date-time-picker-demo -Pwar install jetty:run`

Then navigate to `http://localhost:8080`

## Installing the component
Run from the command line:
`mvn clean install -DskipTests`

## Using the component in a Flow application
To use the component in an application using maven,
add the following dependency to your `pom.xml`:
```
<dependency>
    <groupId>com.vaadin.componentfactory</groupId>
    <artifactId>enhanced-date-time-picker</artifactId>
    <version>${component.version}</version>
</dependency>
```

## Flow documentation
Documentation for flow can be found in [Flow documentation](https://github.com/vaadin/flow-and-components-documentation/blob/master/documentation/Overview.asciidoc).

## License

Apache Licence 2
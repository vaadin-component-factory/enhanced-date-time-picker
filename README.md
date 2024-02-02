# EnhancedDateTimePicker component for Vaadin Flow

This project is based on [DateTimePicker component for Vaadin Flow](https://github.com/vaadin/vaadin-date-time-picker-flow)
and also in [EnhancedDatePicker component](https://github.com/vaadin-component-factory/enhanced-date-picker).

The idea of this new EnhancedDateTimePicker component is the possibility of adding format and parse patterns for both date & time parts of a DateTimePicker.
Date part uses EnhancedDatePicker component and time part is using an extension of [TimePicker component for Vaadin Flow](https://github.com/vaadin/vaadin-time-picker-flow)
that adds an API for setting formatting and parsing patterns.

As in EnhancedDatePicker, the formatting for the time part is done by JavaScript library [date-fns v2.0.0-beta.2](https://date-fns.org/v2.0.0-beta.2/docs/Getting-Started). More information about supported formatting paterns can be found here:
https://date-fns.org/v2.0.0-beta.2/docs/format

## Vaadin 24 support :exclamation::exclamation: 

Since version 3.0.0, the component includes support for Vaadin 24.

The new version no longer depends on EnhancedDatePicker for the Date part. It now uses the standard Vaadin [DatePicker](https://vaadin.com/docs/latest/components/date-picker) component.
  
<br/> This component is part of Vaadin Component Factory.

## How to use EnhancedDateTimePicker

##### Formatting patterns can be set using methods: 

- Date part 
	`setDatePattern(String dateFormattingPattern)`  
- Time part 
	`setTimePattern(String timeFormattingPattern)` 

Since version 3.0.0:

- Date part
	```
	DatePickerI18n datePickerI18n = new DatePickerI18n();
	datePickerI18n.setDateFormat("dd-MMM-yyyy");
	dateTimePicker.setDatePickerI18n(datePickerI18n);
	```  
- Time part 
	`setTimePattern(String timeFormattingPattern)`

##### or by using constructor:

`EnhancedDateTimePicker(LocalDateTime initialDateTime, String dateFormattingPattern, String timeFormattingPattern)`

##### Patterns used for parsing user's input can be set using methods 

- Date part
	`setDateParsers(String ... dateParsers)` 
- Time part
	`setTimeParsers(String ... timeParsers)` 

Since version 3.0.0:

- Date part
	```
	DatePickerI18n datePickerI18n = new DatePickerI18n();
	datePickerI18n.setDateFormats("dd-MMM-yyyy", "dd.MM.yy");
	dateTimePicker.setDatePickerI18n(datePickerI18n);
	```  
- Time part 
	`setTimeParsers(String ... timeParsers)`

##### Also, locale can be set for the whole component using method: 

`setLocale(Locale locale)` 

##### Here's a simple example: 

```java
EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
dateTimePicker.setDatePattern("dd-MM-yyyy");
dateTimePicker.setTimePattern("HH.mm.ss");
dateTimePicker.setDateParsers("dd-MM-yyyy", "dd.MM.yy");
dateTimePicker.setTimeParsers("HH.mm.ss", "HH:mm");
```

:exclamation: Since version 3.0.0 
```java
EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
dateTimePicker.setTimePattern("HH.mm.ss");
dateTimePicker.setTimeParsers("HH.mm.ss", "HH:mm");
DatePickerI18n datePickerI18n = new DatePickerI18n();
datePickerI18n.setDateFormats("dd-MM-yyyy", "dd.MM.yy");
dateTimePicker.setDatePickerI18n(datePickerI18n);
```

For more examples see [demo](https://github.com/vaadin-component-factory/enhanced-date-time-picker/blob/master/enhanced-date-time-picker-demo/src/main/java/com/vaadin/componentfactory/demo/EnhancedDateTimePickerDemoView.java).

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
    <groupId>org.vaadin.addons.componentfactory</groupId>
    <artifactId>enhanced-date-time-picker</artifactId>
    <version>${component.version}</version>
</dependency>
```

## Compatibility

- Version 1.x.x supports Vaadin 14+
- Version 2.0.1 supports Vaadin 20
- Version 2.0.2-3 supports Vaadin 21+
- Version 2.0.4 supports Vaadin 23
- Version 3.x.x supports Vaadin 24

## License

Apache Licence 2

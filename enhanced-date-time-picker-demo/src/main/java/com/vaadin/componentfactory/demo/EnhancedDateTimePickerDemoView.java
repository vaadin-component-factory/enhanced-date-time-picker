package com.vaadin.componentfactory.demo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.vaadin.componentfactory.EnhancedDateTimePicker;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;


/**
 * View for {@link EnhancedDateTimePicker} demo.
 */
@Route("")
public class EnhancedDateTimePickerDemoView extends DemoView {

    private static final List<String> TIME_FORMATS = Arrays.asList("hh:mm", "hh:mm:ss", "hhmm", "hh:mm aa", "hh.mm", "hh.mm.ss", "h:mm x");
    private static final List<String> DATE_FORMATS = Arrays.asList("dd-MMM-yyyy", "dd/MM/yy", "dd/MM.yyyy", "dd.MM.yy");
    private static final List<Locale> LOCALE_LIST = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN, Locale.ITALIAN, Locale.TRADITIONAL_CHINESE, Locale.KOREAN);

    @Override
    protected void initView() {
        createSimpleDateTimePicker();
        createSimpleDateTimePickerWithPatterns();
        createOnlyTimePatternDateTimePicker();
        createOnlyTimePatternParsersDateTimePicker();
        createOnlyDatePatternDateTimePicker();        
        createLocaleDateTimePicker();
    }

    private void createSimpleDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Simple date time picker
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker();

        dateTimePicker.addValueChangeListener(
                event -> updateDateTimeMessage(message, dateTimePicker));
        // end-source-example

        dateTimePicker.setId("simple-picker");

        addCard("Simple date time picker", dateTimePicker, message);
    }

    private void createSimpleDateTimePickerWithPatterns() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Simple date time picker with patterns for date & time
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        dateTimePicker.setTimePattern("HH.mm.ss");
        dateTimePicker.setTimeParsers("HH.mm.ss", "HH:mm");
        DatePickerI18n datePickerI18n = new DatePickerI18n();
        datePickerI18n.setDateFormats("dd-MM-yyyy", "dd.MM.yy");
        dateTimePicker.setDatePickerI18n(datePickerI18n);        

        dateTimePicker.addValueChangeListener(
                event -> updateDateTimeMessage(message, dateTimePicker));
        // end-source-example

        dateTimePicker.setId("simple-pattern-picker");

        addCard("Simple date time picker with patterns for date & time", dateTimePicker, message);
    }

    private void createOnlyTimePatternDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern for time only
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        dateTimePicker.setStep(Duration.ofHours(1));
        updateOnlyTimeMessage(message, dateTimePicker);

        dateTimePicker.addValueChangeListener(
                event -> updateOnlyTimeMessage(message, dateTimePicker));

        ComboBox<String> patterns = new ComboBox<>();
        patterns.setLabel("Select a pattern for time");
        patterns.setItems(TIME_FORMATS);
        patterns.addValueChangeListener(e -> {
            dateTimePicker.setTimePattern(e.getValue());
            updateOnlyTimeMessage(message, dateTimePicker);
        });
        patterns.setValue(TIME_FORMATS.get(0));

        Button dropPatternBtn = new Button("Drop time pattern");
        dropPatternBtn.addClickListener(e -> {
            patterns.setValue(null);
            updateOnlyTimeMessage(message, dateTimePicker);
        });
        // end-source-example

        dateTimePicker.setId("time-pattern-picker");

        addCard("Date time picker with pattern for time only", dateTimePicker, message, patterns, dropPatternBtn);
    }

    private void createOnlyTimePatternParsersDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern and parsers for time only
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        dateTimePicker.setTimePattern("hh.mm");        
        updateOnlyTimeMessage(message, dateTimePicker);

        dateTimePicker.addValueChangeListener(
                event -> updateOnlyTimeMessage(message, dateTimePicker));

        TextField parsingPatternOne = new TextField();
        parsingPatternOne.setLabel("Define parsing pattern A");
        parsingPatternOne.setValue("hhmm");
        
        TextField parsingPatternTwo = new TextField();
        parsingPatternTwo.setLabel("Define a parsing pattern B");
        parsingPatternTwo.setValue("hh:mm:ss");
        
        Button setParsingPatternBtn = new Button("Set parsing pattern from text fields A & B");
        setParsingPatternBtn.addClickListener(e -> {
            dateTimePicker.setTimeParsers(parsingPatternOne.getValue(), parsingPatternTwo.getValue());
            updateOnlyTimeMessage(message, dateTimePicker);
        });
        // end-source-example

        dateTimePicker.setId("time-parsers-picker");

        addCard("Date time picker with pattern and parsers for time only", dateTimePicker, message, parsingPatternOne, parsingPatternTwo, setParsingPatternBtn);
    }

    private void createOnlyDatePatternDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern for date only
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        DatePickerI18n datePickerI18n = new DatePickerI18n();
        datePickerI18n.setDateFormat("dd-MMM-yyyy");
        dateTimePicker.setDatePickerI18n(datePickerI18n);
        
        updateOnlyDateMessage(message, dateTimePicker);

        dateTimePicker.addValueChangeListener(
                event -> updateOnlyDateMessage(message, dateTimePicker));

        ComboBox<String> patterns = new ComboBox<>();
        patterns.setLabel("Select a pattern for date");
        patterns.setItems(DATE_FORMATS);
        patterns.addValueChangeListener(e -> {
          datePickerI18n.setDateFormat(e.getValue());
          dateTimePicker.setDatePickerI18n(datePickerI18n);
            updateOnlyDateMessage(message, dateTimePicker);
        });

        Button dropPatternBtn = new Button("Drop date pattern");
        dropPatternBtn.addClickListener(e -> {
            patterns.setValue(null);
            updateOnlyDateMessage(message, dateTimePicker);
        });
        // end-source-example

        dateTimePicker.setId("date-pattern-picker");

        addCard("Date time picker with pattern for date only", dateTimePicker, message, patterns, dropPatternBtn);
    }
    
    private void createLocaleDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Simple date time picker with locale selection
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());

        List<Locale> availableLocales = LOCALE_LIST.stream()
                .sorted(Comparator.comparing(Locale::getDisplayName)).collect(Collectors.toList());
        ComboBox<Locale> locales = new ComboBox<>("Select a locale");
        locales.setItemLabelGenerator(Locale::getDisplayName);
        locales.setWidth("300px");
        locales.setItems(availableLocales);

        locales.addValueChangeListener(event -> {
            Locale value = event.getValue();
            if (value == null) {
                locales.setValue(UI.getCurrent().getLocale());
            } else {
                dateTimePicker.setLocale(event.getValue());
            }
        });

        locales.setValue(UI.getCurrent().getLocale());
        // end-source-example

        dateTimePicker.setId("simple-locale-picker");

        addCard("Simple date time picker with locale selection", locales, dateTimePicker, message);
    }

    // begin-source-example
    // source-example-heading: Additional code used in the demo
    /**
     * Additional code used in the demo
     */           
     private void updateMessage(Div message, EnhancedDateTimePicker dateTimePicker, boolean checkDate, boolean checkTime) {        
        LocalDate selectedDate = dateTimePicker.getDateValue();
        LocalTime selectedTime = dateTimePicker.getTimeValue();
        if (selectedDate != null  || selectedTime != null) {
            message.setText(
                createMessage(selectedDate, selectedTime, checkDate, checkTime, dateTimePicker)
            );        
        } else {
            message.setText("No date-time is selected");
        }
    }

    private void updateOnlyDateMessage(Div message, EnhancedDateTimePicker dateTimePicker){
        this.updateMessage(message, dateTimePicker, true, false);
    }

    private void updateOnlyTimeMessage(Div message, EnhancedDateTimePicker dateTimePicker){
        this.updateMessage(message, dateTimePicker, false, true);
    }

    private void updateDateTimeMessage(Div message, EnhancedDateTimePicker dateTimePicker){
        this.updateMessage(message, dateTimePicker, true, true);
    }

    private String createMessage(LocalDate selectedDate, LocalTime selectedTime, boolean checkDate, boolean checkTime, EnhancedDateTimePicker dateTimePicker){
        String localePart = "Locale: " + dateTimePicker.getLocale();
        
        String dateParsers = ArrayUtils.isEmpty(dateTimePicker.getDateFormats()) ? StringUtils.EMPTY : "\nParsing date pattern: " + Arrays.toString(dateTimePicker.getDateFormats());
        String timeParsers = ArrayUtils.isEmpty(dateTimePicker.getTimeParsers()) ? StringUtils.EMPTY : "\nParsing time pattern: " + Arrays.toString(dateTimePicker.getTimeParsers());
               
        String formattingDatePattern = StringUtils.isNotBlank(dateTimePicker.getDateFormat()) ? "\nFormatting date pattern: " + dateTimePicker.getDateFormat() : StringUtils.EMPTY;
        String formattingTimePattern = StringUtils.isNotBlank(dateTimePicker.getTimePattern()) ? "\nFormatting time pattern: " + dateTimePicker.getTimePattern() : StringUtils.EMPTY;

        String datePart = StringUtils.EMPTY;
        if(checkDate){
        	String day = selectedDate == null ? StringUtils.EMPTY : String.valueOf(selectedDate.getDayOfMonth());
        	String month = selectedDate == null ? StringUtils.EMPTY : String.valueOf(selectedDate.getMonthValue());
        	String year = selectedDate == null ? StringUtils.EMPTY : String.valueOf(selectedDate.getYear());
        	
            datePart = "\nDay: " + day
                     + "\nMonth: " + month
                     + "\nYear: " + year
                     + formattingDatePattern 
                     + dateParsers;
        }

        String timePart = StringUtils.EMPTY;
        if(checkTime){
        	String hour = selectedTime == null ? StringUtils.EMPTY : String.valueOf(selectedTime.getHour());
        	String minute = selectedTime == null ? StringUtils.EMPTY : String.valueOf(selectedTime.getMinute());
        	String seconds = selectedTime == null ?  StringUtils.EMPTY : String.valueOf(selectedTime.getSecond());
        	
            timePart = "\nHour: " + hour
                     + "\nMinutes: " + minute
                     + "\nSeconds: " + seconds
                     + formattingTimePattern
                     + timeParsers;
        }

        return localePart.concat(datePart).concat(timePart);
    }

    private Div createMessageDiv(String id) {
        Div message = new Div();
        message.setId(id);
        message.getStyle().set("whiteSpace", "pre");
        return message;
    }
    // end-source-example
}

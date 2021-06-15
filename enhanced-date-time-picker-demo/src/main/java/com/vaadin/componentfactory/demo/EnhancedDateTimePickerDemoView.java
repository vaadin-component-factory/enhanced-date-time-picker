package com.vaadin.componentfactory.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import com.vaadin.componentfactory.EnhancedDateTimePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * View for {@link EnhancedDateTimePicker} demo.
 */
@Route("")
public class EnhancedDateTimePickerDemoView extends DemoView {

    private static final List<String> TIME_FORMATS = Arrays.asList("HH:mm", "HH:mm:ss", "H", "HH:mm aa", "H:mm z", "H.mm", "HH.mm.ss");

    @Override
    protected void initView() {
        createSimpleDateTimePicker();
        createPatternDatePicker();
        createPatternTimePicker();
        addCard("Additional code used in the demo",
                new Label("These methods are used in the demo."));
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

    private void createPatternDatePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern for date only
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        dateTimePicker.setDatePattern("dd-MMM-yyyy");
        updateOnlyDateMessage(message, dateTimePicker);

        dateTimePicker.addValueChangeListener(
                event -> updateOnlyDateMessage(message, dateTimePicker));

        TextField pattern = new TextField();
        pattern.setLabel("Define a pattern for date");
        pattern.setValue("dd-MMM-yyyy");

        Button setPatternBtn = new Button("Set date pattern from text field");
        setPatternBtn.addClickListener(e -> {
            dateTimePicker.setDatePattern(pattern.getValue());
            updateOnlyDateMessage(message, dateTimePicker);
        });

        Button dropPatternBtn = new Button("Drop date pattern");
        dropPatternBtn.addClickListener(e -> {
            dateTimePicker.setDatePattern(null);
            updateOnlyDateMessage(message, dateTimePicker);
        });

        // end-source-example

        dateTimePicker.setId("pattern-picker");

        addCard("Date time picker with pattern for date only", dateTimePicker, message, pattern, setPatternBtn, dropPatternBtn);
    }

    private void createPatternTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern for time only 
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now());
        dateTimePicker.setTimePattern("H.mm");
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

        Button dropPatternBtn = new Button("Drop time pattern");
        dropPatternBtn.addClickListener(e -> {
            dateTimePicker.setTimePattern(null);
            updateOnlyTimeMessage(message, dateTimePicker);
        });

        // end-source-example

        dateTimePicker.setId("pattern-picker");

        addCard("Date time picker with pattern for time only", dateTimePicker, message, patterns, dropPatternBtn);
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
            message.setText("No date/time is selected");
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
        
        String dateParsers = StringUtils.EMPTY;
        String timeParsers = StringUtils.EMPTY;
        if (ArrayUtils.isNotEmpty(dateTimePicker.getDateParsers()))
            dateParsers = Arrays.toString(dateTimePicker.getDateParsers());
        if (ArrayUtils.isNotEmpty(dateTimePicker.getTimeParsers()))
            timeParsers = Arrays.toString(dateTimePicker.getTimeParsers());

        String datePart = StringUtils.EMPTY;
        if(checkDate){
            datePart = "\nDay: " + selectedDate.getDayOfMonth()
                     + "\nMonth: " + selectedDate.getMonthValue()
                     + "\nYear: " + selectedDate.getYear()
                     + "\nFormatting date pattern: " + dateTimePicker.getDatePattern()
                     + "\nParsing date pattern: " + dateParsers;
        }

        String timePart = StringUtils.EMPTY;
        if(checkTime){
            timePart = "\nHour: " + selectedTime.getHour()
                     + "\nMinutes: " + selectedTime.getMinute()
                     + "\nSeconds: " + selectedTime.getSecond()
                     + "\nFormatting time pattern: " + dateTimePicker.getTimePattern()
                     + "\nParsing time pattern: " + timeParsers;
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

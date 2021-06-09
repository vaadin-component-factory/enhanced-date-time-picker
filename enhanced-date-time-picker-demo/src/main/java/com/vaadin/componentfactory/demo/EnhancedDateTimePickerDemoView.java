package com.vaadin.componentfactory.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import com.vaadin.componentfactory.EnhancedDateTimePicker;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.demo.DemoView;
import com.vaadin.flow.router.Route;


/**
 * View for {@link EnhancedDateTimePicker} demo.
 *
 * @author Paola De Bartolo 
 */
@Route("")
public class EnhancedDateTimePickerDemoView extends DemoView {

    @Override
    protected void initView() {
        createSimpleDateTimePicker();
        createPatternDatePicker();

        addCard("Additional code used in the demo",
                new Label("These methods are used in the demo."));
    }

    private void createSimpleDateTimePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Simple date time picker
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker();

        dateTimePicker.addValueChangeListener(
                event -> updateMessage(message, dateTimePicker));
        // end-source-example

        dateTimePicker.setId("simple-picker");

        addCard("Simple date time picker", dateTimePicker, message);
    }

    private void createPatternDatePicker() {
        Div message = createMessageDiv("simple-picker-message");

        // begin-source-example
        // source-example-heading: Date time picker with pattern for date
        EnhancedDateTimePicker dateTimePicker = new EnhancedDateTimePicker(LocalDateTime.now(), "dd-MMM-yyyy");
        updateMessage(message, dateTimePicker);

        dateTimePicker.addValueChangeListener(
                event -> updateMessage(message, dateTimePicker));

        TextField patten = new TextField();
        patten.setLabel("Define a pattern for date");
        patten.setValue("dd-MMM-yyyy");

        Button setPatternBtn = new Button("Set date pattern from text field");
        setPatternBtn.addClickListener(e -> {
            dateTimePicker.setDatePattern(patten.getValue());
            updateMessage(message, dateTimePicker);
        });

        Button dropPatternBtn = new Button("Drop date pattern");
        dropPatternBtn.addClickListener(e -> {
            dateTimePicker.setDatePattern(null);
            updateMessage(message, dateTimePicker);
        });

        // end-source-example

        dateTimePicker.setId("Pattern-picker");

        addCard("Date time picker with pattern for date", dateTimePicker, message, patten, setPatternBtn, dropPatternBtn);
    }

    // begin-source-example
    // source-example-heading: Additional code used in the demo
    /**
     * Additional code used in the demo
     */
    private void updateMessage(Div message, EnhancedDateTimePicker dateTimePicker) {
        LocalDate selectedDate = dateTimePicker.getDateValue();
        if (selectedDate != null) {
        	String parsers = null;
        	if (dateTimePicker.getDateParsers() != null)
        		parsers = Arrays.toString(dateTimePicker.getDateParsers());
            message.setText(
                    "Day: " + selectedDate.getDayOfMonth()
                            + "\nMonth: " + selectedDate.getMonthValue()
                            + "\nYear: " + selectedDate.getYear()
                            + "\nLocale: " + dateTimePicker.getLocale()
                            + "\nFormatting date pattern: " + dateTimePicker.getDatePattern()
                            + "\nParsing date pattern: " + parsers); 
        } else {
            message.setText("No date is selected");
        }
    }

    private Div createMessageDiv(String id) {
        Div message = new Div();
        message.setId(id);
        message.getStyle().set("whiteSpace", "pre");
        return message;
    }
    // end-source-example
}

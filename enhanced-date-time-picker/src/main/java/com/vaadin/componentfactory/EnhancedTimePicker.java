/*-
 * #%L
 * EnhancedDateTimePicker
 * %%
 * Copyright (C) 2021-2024 Vaadin Ltd
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.vaadin.componentfactory;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.function.SerializableConsumer;
import java.time.LocalTime;

@JsModule("./date-fns-limited.min.js")
@JsModule("./enhancedTimepickerConnector.js")
public class EnhancedTimePicker extends TimePicker {

    private String formattingPattern;
    private String[] parserPatterns;

    /**
     * Default constructor.
     */
    public EnhancedTimePicker() {
        super();
    }

    /**
     * Convenience constructor to create a time picker with a pre-selected time.
     *
     * @param time
     *            the pre-selected time in the picker
     */
    public EnhancedTimePicker(LocalTime time) {
        super(time);
    }

    /**
     * Convenience constructor to create a time picker with a label.
     *
     * @param label
     *            the label describing the time picker
     * @see #setLabel(String)
     */
    public EnhancedTimePicker(String label) {
        super(label);
    }

    /**
     * Convenience constructor to create a time picker with a pre-selected time
     * and a label.
     *
     * @param label
     *            the label describing the time picker
     * @param time
     *            the pre-selected time in the picker
     */
    public EnhancedTimePicker(String label, LocalTime time) {
        super(label, time);
    }

    /**
     * Convenience constructor to create a time picker with a
     * {@link ValueChangeListener}.
     *
     * @param listener
     *            the listener to receive value change events
     * @see #addValueChangeListener(HasValue.ValueChangeListener)
     */
    public EnhancedTimePicker(
            ValueChangeListener<ComponentValueChangeEvent<TimePicker, LocalTime>> listener) {
      super(listener);  
    }

    /**
     * Convenience constructor to create a time picker with a pre-selected time
     * and {@link ValueChangeListener}.
     *
     * @param time
     *            the pre-selected time in the picker
     * @param listener
     *            the listener to receive value change events
     * @see #addValueChangeListener(HasValue.ValueChangeListener)
     */
    public EnhancedTimePicker(LocalTime time,
            ValueChangeListener<ComponentValueChangeEvent<TimePicker, LocalTime>> listener) {
      super(time, listener); 
    }  
    
    /**
     * Convenience constructor to create a time picker with a label, a
     * pre-selected time and a {@link ValueChangeListener}.
     *
     * @param label
     *            the label describing the time picker
     * @param time
     *            the pre-selected time in the picker
     * @param listener
     *            the listener to receive value change events
     * @see #setLabel(String)
     * @see #addValueChangeListener(HasValue.ValueChangeListener)
     */
    public EnhancedTimePicker(String label, LocalTime time,
            ValueChangeListener<ComponentValueChangeEvent<TimePicker, LocalTime>> listener) {
      super(label, time, listener);      
    }
    
    /**
     * Convenience Constructor to create a time picker with pre-selected time
     * and pattern setup.
     *
     * @param time
     *            the pre-selected time in the picker
     * @param formattingPattern
     *            the pattern for formatting value of the time picker
     */
    public EnhancedTimePicker(LocalTime time, String formattingPattern) {
        this(time);
        setPattern(formattingPattern);
    }
    
    /**
     * Convenience Constructor to create a time picker with pre-selected time, formatting 
     * pattern, and parsing patterns.
     *
     * @param initialDate
     *            the pre-selected time in the picker
     * @param formattingPattern
     *            the pattern for formatting value of the time picker
     * @param parserPatterns
     *           the array of patterns used for parsing the time picker's value
     */
    public EnhancedTimePicker(LocalTime time, String formattingPattern, String ... parserPatterns) {
        this(time);
        setPattern(formattingPattern);
        setParsers(parserPatterns);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);       
        initEnhancedConnector(); 
        if (formattingPattern != null) {
            setPattern(formattingPattern);
        }               
    }

    private void initEnhancedConnector() {
        // can't run this with getElement().executeJavaScript(...) since then
        // setLocale might be called before this causing client side error
        runBeforeClientResponse(ui -> ui.getPage().executeJs(
                "window.Vaadin.Flow.enhancedTimepickerConnector.initLazy($0)",
                getElement()));
    }

    /**
     * Setting the patterns for parsing the value of the date-picker.
     * 
     * The parsing will be attempted according to the order of the supplied patterns. If none of these
     * patterns can successfully parse the time-picker's value, the parsing will, first, be attempted using the
     * formatting value (which can be set using @setPattern). If the latter also fails, parsing will be
     * attempted using the Locale (which can be set using @setLocale).
     *
     * @param parserPatterns
     *           the array of patterns used for parsing the date picker's value
     */
    public void setParsers(String... parserPatterns){
    	this.parserPatterns = parserPatterns;
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setParsers", parserPatterns));
    }
    
    /**
     * Gets the parser patterns for this date-picker
     *
     * @return an array of the parser patterns used for formatting value of the date picker
     */
    public String[] getParsers() {
        return parserPatterns;
    }

    /**
     * Setting the Pattern for formatting value of the date picker
     *
     * @param formattingPattern
     *           the pattern for formatting value of the time picker
     *           if set to null or empty string then formatting will be done by Locale
     */
    public void setPattern(String formattingPattern){
        this.formattingPattern = formattingPattern;
        runBeforeClientResponse(ui -> getElement().callJsFunction("$connector.setPattern", formattingPattern));
    }

    /**
     * Gets the Pattern for this date picker
     *
     * @return the pattern for formatting value of the date picker
     */
    public String getPattern() {
        return formattingPattern;
    }

    private void runBeforeClientResponse(SerializableConsumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }

}

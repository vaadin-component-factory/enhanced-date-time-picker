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

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class EnhancedDateTimePicker extends BaseEnhancedDateTimePicker {

  /**
   * Default constructor.
   */
  public EnhancedDateTimePicker() {
    super();
  }

  /**
   * Convenience constructor to create a date time picker with a label.
   *
   * @param label the label describing the date time picker
   * @see #setLabel(String)
   */
  public EnhancedDateTimePicker(String label) {
    super(label);
  }

  /**
   * Convenience constructor to create a date time picker with a pre-selected date and time in
   * current UI locale format and a label.
   *
   * @param label the label describing the date time picker
   * @param initialDateTime the pre-selected date time in the picker
   * @see #setValue(LocalDateTime)
   * @see #setLabel(String)
   */
  public EnhancedDateTimePicker(String label, LocalDateTime initialDateTime) {
    super(label, initialDateTime);
  }

  /**
   * Convenience constructor to create a date time picker with a pre-selected date time in current
   * UI locale format.
   *
   * @param initialDateTime the pre-selected date time in the picker
   */
  public EnhancedDateTimePicker(LocalDateTime initialDateTime) {
    super(initialDateTime);
    setLocale(UI.getCurrent().getLocale());
  }

  /**
   * Convenience constructor to create a date time picker with a {@link ValueChangeListener}.
   *
   * @param listener the listener to receive value change events
   * @see #addValueChangeListener(HasValue.ValueChangeListener)
   */
  public EnhancedDateTimePicker(
      ValueChangeListener<ComponentValueChangeEvent<BaseEnhancedDateTimePicker, LocalDateTime>> listener) {
    super(listener);
  }

  /**
   * Convenience constructor to create a date time picker with a {@link ValueChangeListener} and a
   * label.
   *
   *
   * @param label the label describing the date time picker
   * @param listener the listener to receive value change events
   * @see #setLabel(String)
   * @see #addValueChangeListener(HasValue.ValueChangeListener)
   */
  public EnhancedDateTimePicker(String label,
      ValueChangeListener<ComponentValueChangeEvent<BaseEnhancedDateTimePicker, LocalDateTime>> listener) {
    super(label, listener);
  }

  /**
   * Convenience constructor to create a date time picker with a pre-selected date time in current
   * UI locale format and a {@link ValueChangeListener}.
   *
   * @param initialDateTime the pre-selected date time in the picker
   * @param listener the listener to receive value change events
   * @see #setValue(LocalDateTime)
   * @see #addValueChangeListener(HasValue.ValueChangeListener)
   */
  public EnhancedDateTimePicker(LocalDateTime initialDateTime,
      ValueChangeListener<ComponentValueChangeEvent<BaseEnhancedDateTimePicker, LocalDateTime>> listener) {
    super(initialDateTime, listener);
  }

  /**
   * Convenience constructor to create a date time picker with a pre-selected date and time in
   * current UI locale format, a {@link ValueChangeListener} and a label.
   *
   * @param label the label describing the date time picker
   * @param initialDateTime the pre-selected date time in the picker
   * @param listener the listener to receive value change events
   * @see #setLabel(String)
   * @see #setValue(LocalDateTime)
   * @see #addValueChangeListener(HasValue.ValueChangeListener)
   */
  public EnhancedDateTimePicker(String label, LocalDateTime initialDateTime,
      ValueChangeListener<ComponentValueChangeEvent<BaseEnhancedDateTimePicker, LocalDateTime>> listener) {
    super(label, initialDateTime, listener);
  }

  /**
   * Convenience constructor to create a date time picker with pre-selected date time and locale
   * setup.
   *
   * @param initialDateTime the pre-selected date time in the picker
   * @param locale the locale for the date time picker
   */
  public EnhancedDateTimePicker(LocalDateTime initialDateTime, Locale locale) {
    super(initialDateTime, locale);
  }

  /**
   * Convenience Constructor to create a date time picker with pre-selected date time and patterns
   * setup.
   *
   * @param initialDateTime the pre-selected datetime in the picker
   * @param dateFormattingPattern the pattern for formatting value of the date picker
   * @param timeFormattingPattern the pattern for formatting value of the time picker
   */
  public EnhancedDateTimePicker(LocalDateTime initialDateTime, String dateFormattingPattern,
      String timeFormattingPattern) {
    this(initialDateTime);
    setDateFormat(dateFormattingPattern);
    setTimePattern(timeFormattingPattern);
  }

  private void setDateFormat(String dateFormattingPattern) {
    if (this.getDatePicker().getI18n() == null) {
      this.getDatePicker().setI18n(new DatePickerI18n());
    }
    this.getDatePicker().getI18n().setDateFormat(dateFormattingPattern);
  }

  /**
   * Gets the value of the date picker.
   *
   * @return the current value of the date picker
   */
  public LocalDate getDateValue() {
    return this.getDatePicker().getValue();
  }

  /**
   * Gets the Pattern for the date picker.
   *
   * @return the pattern for formatting value of the date picker
   */
  public String getDateFormat() {
    if (this.getDatePicker().getI18n() == null) {
      return null;
    }
    return this.getDatePicker().getI18n().getDateFormats().get(0);
  }

  /**
   * @see DatePickerI18n#getDateFormats()
   */
  public String[] getDateFormats() {
    if (this.getDatePicker().getI18n() == null) {
      return null;
    }
    return this.getDatePicker().getI18n().getDateFormats().toArray(new String[0]);
  }

  /**
   * Gets the value of the time picker.
   *
   * @return the current value of the time picker
   */
  public LocalTime getTimeValue() {
    return this.getTimePicker().getValue();
  }

  /**
   * Gets the Pattern for the time picker.
   *
   * @return the pattern for formatting value of the time picker
   */
  public String getTimePattern() {
    return this.getTimePicker().getPattern();
  }

  /**
   * Sets the Pattern for formatting the value of the time picker.
   *
   * @param timeFormattingPattern the pattern for formatting value of the time picker
   */
  public void setTimePattern(String timeFormattingPattern) {
    this.getTimePicker().setPattern(timeFormattingPattern);
  }

  /**
   * Gets the parser patterns for the time picker.
   *
   * @return an array of the parser patterns used for formatting value of the time picker
   */
  public String[] getTimeParsers() {
    return this.getTimePicker().getParsers();
  }

  /**
   * Sets the patterns for parsing the value of the time picker.
   * 
   * @param timeParsers the array of patterns used for parsing the time picker's value
   */
  public void setTimeParsers(String... timeParsers) {
    this.getTimePicker().setParsers(timeParsers);
  }

}

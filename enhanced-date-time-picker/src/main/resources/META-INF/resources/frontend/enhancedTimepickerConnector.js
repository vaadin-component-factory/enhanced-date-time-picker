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
import {
	TEST_PM_TIME,
	formatMilliseconds,
	parseMillisecondsIntoInteger,
	parseDigitsIntoInteger,
	getAmString,
	getPmString,
	getSeparator,
	searchAmOrPmToken
} from './helpers.js';

(function() {

	// Execute callback when predicate returns true.
	// Try again later if predicate returns false.
	function when(predicate, callback, timeout = 0) {
		if (predicate()) {
			callback();
		} else {
			setTimeout(() => when(predicate, callback, 200), timeout);
		}
	}

	window.Vaadin.Flow.enhancedTimepickerConnector = {
		initLazy: function(timepicker) {

			timepicker.$connector.pattern;
			timepicker.$connector.parsers = [];

			// detecting milliseconds from input, expects am/pm removed from end, eg. .0 or .00 or .000
			const millisecondRegExp = /[[\.][\d\u0660-\u0669]{1,3}$/;

			// format time based on given pattern
			const formatTimeBasedOnPattern = function(timeToBeFormatted, pattern, language) {
				return DateFns.format(timeToBeFormatted, pattern, { locale: DateFns.locales[language] });
			};

			timepicker.$connector.setLocalePatternAndParsers = function(locale, pattern, parsers) {

				console.log("ENHANCED-setLocalePatternAndParsers")

				let language = locale ? locale.split('-')[0] : 'enUS';

				// capture previous value if any
				let previousValueObject;
				if (timepicker.value && timepicker.value !== '') {
					previousValueObject = timepicker.i18n.parseTime(timepicker.value);
				}

				// 1. 24 or 12 hour clock, if latter then what are the am/pm strings ?
				const pmString = getPmString(locale);
				const amString = getAmString(locale);

				let localeTimeString = TEST_PM_TIME.toLocaleTimeString(locale);
				// since the next regex picks first non-number-whitespace, need to discard possible PM from beginning (eg. chinese locale)
				if (pmString && localeTimeString.startsWith(pmString)) {
					localeTimeString = localeTimeString.replace(pmString, '');
				}

				// 2. What is the separator ?
				const separator = getSeparator(locale);

				// 3. regexp that allows to find the numbers with optional separator and continuing searching after it
				const numbersRegExp = new RegExp('([\\d\\u0660-\\u0669]){1,2}(?:' + separator + ')?', 'g');

				const includeSeconds = function() {
					return timepicker.step && timepicker.step < 60;
				};

				const includeMilliSeconds = function() {
					return timepicker.step && timepicker.step < 1;
				};

				// the web component expects the correct granularity used for the time string,
				// thus need to format the time object in correct granularity by passing the format options
				let cachedStep;
				let cachedOptions;
				const getTimeFormatOptions = function() {
					// calculate the format options if none done cached or step has changed
					if (!cachedOptions || cachedStep !== timepicker.step) {
						cachedOptions = {
							hour: "numeric",
							minute: "numeric",
							second: includeSeconds() ? "numeric" : undefined,
						};
						cachedStep = timepicker.step;
					}
					return cachedOptions;
				};

				let cachedTimeString;
				let cachedTimeObject;

				const parseBasedOnParsers = function(timeString, parsersCopy, language) {
					var date;
					var i;
					for (i in parsersCopy) {
						try {
							date = DateFns.parse(timeString, parsersCopy[i], new Date(), { locale: DateFns.locales[language] });
							if (date != 'Invalid Date') {
								break;
							}
						} catch (err) { }
					}

					cachedTimeObject = date != 'Invalid Date' && {
						hours: date.getHours(),
						minutes: date.getMinutes(),
						seconds: date.getSeconds(),
						milliseconds: date.getMilliseconds()
					};

					cachedTimeString = timeString;
					return cachedTimeObject;
				};

				timepicker.i18n = {
					formatTime: function(timeObject) {
						console.log("ENHANCED-formatTime-original");
						if (timeObject) {
							let timeToBeFormatted = new Date();
							timeToBeFormatted.setHours(timeObject.hours);
							timeToBeFormatted.setMinutes(timeObject.minutes);
							timeToBeFormatted.setSeconds(timeObject.seconds !== undefined ? timeObject.seconds : 0);

							if (pattern) {
								timeToBeFormatted.setMilliseconds(timeObject.milliseconds !== undefined ? timeObject.milliseconds : 0)
								return formatTimeBasedOnPattern(timeToBeFormatted, pattern, language);
							} else {
								let localeTimeString = timeToBeFormatted.toLocaleTimeString(locale, getTimeFormatOptions());
								// milliseconds not part of the time format API
								if (includeMilliSeconds()) {
									localeTimeString = formatMilliseconds(localeTimeString, timeObject.milliseconds, amString, pmString);
								}
								return localeTimeString;
							}
						}
					},

					parseTime: function(timeString) {
						console.log("ENHANCED-parseTime-original");
						if (timeString && timeString === cachedTimeString && cachedTimeObject) {
							return cachedTimeObject;
						}
						if (timeString) {
							timeString = timepicker.$.comboBox.value;

							let parsersCopy = JSON.parse(JSON.stringify(parsers));

							if (pattern) {
								parsersCopy.push(pattern);
							}

							if (parsersCopy.length > 0) {
								return parseBasedOnParsers(timeString, parsersCopy, language);
							}

							const amToken = searchAmOrPmToken(timeString, amString);
							const pmToken = searchAmOrPmToken(timeString, pmString);

							const numbersOnlyTimeString = timeString.replace(amToken, '').replace(pmToken, '').trim();
							// reset regex to beginning or things can explode when the length of the input changes
							numbersRegExp.lastIndex = 0;
							let hours = numbersRegExp.exec(numbersOnlyTimeString);
							if (hours) {
								hours = parseDigitsIntoInteger(hours[0].replace(separator, ''));
								// handle 12 am -> 0
								// do not do anything if am & pm are not used or if those are the same,
								// as with locale bg-BG there is always ч. at the end of the time
								if (amToken !== pmToken) {
									if (hours === 12 && amToken) {
										hours = 0;
									}
									if (hours !== 12 && pmToken) {
										hours += 12;
									}
								}
								const minutes = numbersRegExp.exec(numbersOnlyTimeString);
								const seconds = minutes && numbersRegExp.exec(numbersOnlyTimeString);
								// reset to end or things can explode
								let milliseconds = seconds && includeMilliSeconds() && millisecondRegExp.exec(numbersOnlyTimeString);
								// handle case where last numbers are seconds and . is the separator (invalid regexp match)
								if (milliseconds && milliseconds['index'] <= seconds['index']) {
									milliseconds = undefined;
								}
								// hours is a number at this point, others are either arrays or null
								// the string in [0] from the arrays includes the separator too
								cachedTimeObject = hours !== undefined && {
									hours: hours,
									minutes: minutes ? parseDigitsIntoInteger(minutes[0].replace(separator, '')) : 0,
									seconds: seconds ? parseDigitsIntoInteger(seconds[0].replace(separator, '')) : 0,
									milliseconds: minutes && seconds && milliseconds ? parseMillisecondsIntoInteger(milliseconds[0].replace('.', '')) : 0
								};
								cachedTimeString = timeString;
								return cachedTimeObject;
							}

							// when nothing is returned, the component shows the invalid state for the input
						}
					}
				};

				if (previousValueObject) {
					when(() => timepicker.$, () => {
						const newValue = timepicker.i18n.formatTime(previousValueObject);
						// FIXME works but uses private API, needs fixes in web component
						if (timepicker.inputElement.value !== newValue) {
							timepicker.inputElement.value = newValue;
							timepicker.$.comboBox.value = newValue;
						}
					});
				}
			};

			timepicker.$connector.setLocale = function(locale) {
				try {
					// Check whether the locale is supported by the browser or not
					TEST_PM_TIME.toLocaleTimeString(locale);
				} catch (e) {
					locale = "en-US";
					// FIXME should do a callback for server to throw an exception ?
					throw new Error("vaadin-time-picker: The locale " + locale + " is not supported, falling back to default locale setting(en-US).");
				}

				this.locale = locale;
				this.setLocalePatternAndParsers(this.locale, this.pattern, this.parsers);
			};

			timepicker.$connector.setPattern = function(pattern) {
				this.pattern = pattern;
				this.setLocalePatternAndParsers(this.locale, this.pattern, this.parsers);
			};

			timepicker.$connector.setParsers = function(...parsers) {
				this.parsers = parsers;
				this.setLocalePatternAndParsers(this.locale, this.pattern, this.parsers);
			};
		}
	};
})();    
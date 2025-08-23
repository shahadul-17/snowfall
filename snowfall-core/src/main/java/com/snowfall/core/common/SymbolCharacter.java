package com.snowfall.core.common;

import com.snowfall.core.utilities.StringUtilities;

public enum SymbolCharacter {

	NONE('\0'),
	HYPHEN('-'),
	DASH('—'),
	UNDERSCORE('_'),
	QUESTION_MARK('?'),
	HASH('#'),
	AT_THE_RATE('@'),
	PERCENT('%'),
	COLON(':'),
	SEMICOLON(';'),
	PERIOD('.'),
	COMMA(','),
	EXCLAMATION_MARK('!'),
	ASTERISK('*');

	private final char value;
	private final String valueAsString;

	private static final SymbolCharacter[] SYMBOL_CHARACTERS = SymbolCharacter.values();

	SymbolCharacter(final char value) {
		this.value = value;
		valueAsString = String.valueOf(value);
	}

	public char value() { return value; }

	public String valueAsString() { return valueAsString; }

	public static SymbolCharacter fromName(final String name) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				name, StringUtilities.getEmptyString(), true).toUpperCase();

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		for (final var symbolCharacter : SYMBOL_CHARACTERS) {
			if (sanitizedName.equals(symbolCharacter.name())) { return symbolCharacter; }
		}

		return NONE;
	}

	public static SymbolCharacter fromValue(final char value) {
		// iterating over all the symbol characters...
		for (final var symbolCharacter : SYMBOL_CHARACTERS) {
			// if the value matches the symbol character's value,
			// we shall return the symbol character...
			if (value == symbolCharacter.value()) { return symbolCharacter; }
		}

		// otherwise, we'll return NONE...
		return NONE;
	}

	public static SymbolCharacter fromValue(final String value) {
		// sanitizing the value...
		final var sanitizedValue = StringUtilities.getDefaultIfNullOrWhiteSpace(
				value, StringUtilities.getEmptyString(), true);

		// if the sanitized value is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedValue)) { return NONE; }

		// calling the overloaded method with the first character of the sanitized value...
		return fromValue(sanitizedValue.charAt(0));
	}

	public static SymbolCharacter[] getAll() { return SYMBOL_CHARACTERS; }
}

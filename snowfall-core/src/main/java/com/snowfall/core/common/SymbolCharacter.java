package com.snowfall.core.common;

import com.snowfall.core.utilities.StringUtilities;

import java.util.HashMap;
import java.util.Map;

public enum SymbolCharacter {

	NONE("NONE", '\0'),
	HYPHEN("HYPHEN", '-'),
	DASH("DASH", '—'),
	UNDERSCORE("UNDERSCORE", '_'),
	QUESTION_MARK("QUESTION_MARK", '?'),
	HASH("HASH", '#'),
	AT_THE_RATE("AT_THE_RATE", '@'),
	PERCENT("PERCENT", '%'),
	COLON("COLON", ':'),
	SEMICOLON("SEMICOLON", ';'),
	PERIOD("PERIOD", '.'),
	COMMA("COMMA", ','),
	EXCLAMATION_MARK("EXCLAMATION_MARK", '!'),
	ASTERISK("ASTERISK", '*');

	private final String name;
	private final char value;
	private final String valueAsString;

	private static final SymbolCharacter[] VALUES = SymbolCharacter.values();
	private static final Map<Character, SymbolCharacter> VALUE_MAP;
	private static final Map<String, SymbolCharacter> NAME_MAP;

	static {
		// populates a map from values because every time
		// the static method values() is called, it initializes a new array...
		final var symbolCharacters = getValues();
		// calculating initial capacity of the maps...
		final var initialCapacity = symbolCharacters.length * 2;

		// we neither want to waste memory nor reallocation, so we shall initialize the maps
		// with an initial capacity...
		NAME_MAP = new HashMap<>(initialCapacity);
		VALUE_MAP = new HashMap<>(initialCapacity);

		// puts all the values in the map...
		for (final var symbolCharacter : symbolCharacters) {
			// we shall put the symbol character in the name map if the symbol character
			// does not already exist in the map...
			NAME_MAP.putIfAbsent(symbolCharacter.name.toUpperCase(), symbolCharacter);
			// we shall put the symbol character in the value map if the symbol character
			// does not already exist in the map...
			VALUE_MAP.putIfAbsent(symbolCharacter.value, symbolCharacter);
		}
	}

	SymbolCharacter(final String name, final char value) {
		this.name = name;
		this.value = value;
		valueAsString = String.valueOf(value);
	}

	public String getName() { return name; }

	public char getValue() { return value; }

	public String getValueAsString() { return valueAsString; }

	@Override
	public String toString() { return getValueAsString(); }

	public static SymbolCharacter[] getValues() { return VALUES; }

	public static SymbolCharacter fromValue(final char value) {
		// then we shall look for the symbol character in the value map...
		final var symbolCharacter = VALUE_MAP.get(value);

		// if no symbol character is found for the value, we shall return NONE...
		if (symbolCharacter == null) { return NONE; }

		// otherwise, we'll return the symbol character found for the value...
		return symbolCharacter;
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

	public static SymbolCharacter fromName(final String name) {
		// sanitizing the name...
		final var sanitizedName = StringUtilities.getDefaultIfNullOrWhiteSpace(
				name, StringUtilities.getEmptyString(), true);

		// if the sanitized name is empty, we shall return NONE...
		if (StringUtilities.isEmpty(sanitizedName)) { return NONE; }

		// then we shall look for the name in the name map...
		final var symbolCharacter = NAME_MAP.get(name.toUpperCase());

		// if no symbol character is found for the name, we shall return NONE...
		if (symbolCharacter == null) { return NONE; }

		// otherwise, we'll return the symbol character found for the name...
		return symbolCharacter;
	}
}

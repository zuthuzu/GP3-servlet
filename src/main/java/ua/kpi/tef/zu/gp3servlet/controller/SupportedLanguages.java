package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.controller.filters.LocalizationFilter;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Anton Domin on 2020-02-11
 */

public enum SupportedLanguages {
	ENGLISH("en", "English"),
	RUSSIAN("ru", "Русский");

	private final String code;
	private final String name;

	SupportedLanguages(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public static Locale determineLocale(HttpSession session) {
		try {
			String langCode = (String) session.getAttribute(LocalizationFilter.CURRENT_LANGUAGE);
			return determineLocale(langCode);
		} catch (Exception e) {
			return determineLocale(getDefault());
		}
	}

	public static Locale determineLocale(String code) {
		String codeLC = code.toLowerCase();
		return determineLocale(Arrays.stream(SupportedLanguages.values())
				.filter(lang -> lang.getCode().equals(codeLC))
				.findAny()
				.orElse(getDefault()));
	}

	public static Locale determineLocale(SupportedLanguages lang) {
		return new Locale(lang.getCode());
	}

	public static SupportedLanguages getDefault() {
		return ENGLISH;
	}
}

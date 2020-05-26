package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.controller.filters.LocalizationFilter;
import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;

import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anton Domin on 2020-05-24
 */
public class LocalizationUtility {
	private static final String BUNDLE_NAME = "messages";

	public static Locale determineLocale(HttpSession session) {
		try {
			String langCode = (String) session.getAttribute(LocalizationFilter.CURRENT_LANGUAGE);
			return SupportedLanguages.determineLocale(langCode);
		} catch (Exception e) {
			return SupportedLanguages.determineLocale("default"); //can be any string that's not a language code
		}
	}

	public static void restoreCategoryFromLocalView(OrderDTO order, Locale locale) throws IllegalArgumentException {
		int categoryIndex = getLocalCategories(locale).indexOf(order.getCategory());
		if (categoryIndex == -1)
			throw new IllegalArgumentException("Failed to recognize the category: " + order.getCategory());
		order.setActualCategory(ItemCategory.values()[categoryIndex]);
	}

	public static List<String> getLocalCategories(Locale locale) {
		return Arrays.stream(ItemCategory.values())
				.map((v) -> getLocalizedText(v.toString(), locale))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public static List<OrderDTO> setLocalFields(List<OrderDTO> orders, Locale locale) {
		orders.forEach((o) -> setLocalFields(o, locale));
		return orders;
	}

	public static void setLocalFields(OrderDTO order, Locale locale) {
		DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(locale);
		order.setCreationDate(order.getActualCreationDate().format(dtf));
		order.setCategory(getLocalizedText(order.getActualCategory().toString(), locale));
		order.setStatus(getLocalizedText(order.getActualStatus().toString(), locale));
	}

	public static String getLocalizedText(String token, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
		return bundle.keySet().contains(token) ? bundle.getString(token) : token;
	}
}

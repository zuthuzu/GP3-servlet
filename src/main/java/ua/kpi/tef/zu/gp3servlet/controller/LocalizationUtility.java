package ua.kpi.tef.zu.gp3servlet.controller;

import ua.kpi.tef.zu.gp3servlet.dto.OrderDTO;
import ua.kpi.tef.zu.gp3servlet.entity.ItemCategory;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Anton Domin on 2020-05-24
 */
public class LocalizationUtility {
	private static final String BUNDLE_NAME = "messages";

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

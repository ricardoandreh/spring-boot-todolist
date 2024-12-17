package br.com.rictodolist.todolist.infrastructure.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CustomLocaleResolver extends AcceptHeaderLocaleResolver {

    private static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            new Locale("en"),
            // new Locale("es"),
            new Locale("pt", "BR")
    );

    private Locale currentLocale = Locale.ENGLISH;

    @NonNull
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String langParam = request.getParameter("lang");
        if (langParam != null && !langParam.isEmpty()) {
            Locale queryLocale = Locale.forLanguageTag(langParam);
            if (SUPPORTED_LOCALES.contains(queryLocale)) {
                this.currentLocale = queryLocale;
                return queryLocale;
            }
        }

        String headerLanguage = request.getHeader("Accept-Language");
        if (headerLanguage != null && !headerLanguage.isEmpty()) {
            Locale resolvedLocale = Locale.lookup(Locale.LanguageRange.parse(headerLanguage), SUPPORTED_LOCALES);
            if (resolvedLocale != null) {
                this.currentLocale = resolvedLocale;
                return resolvedLocale;
            }
        }
        return this.currentLocale;
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest request, HttpServletResponse response, Locale locale) {
        if (SUPPORTED_LOCALES.contains(locale)) {
            this.currentLocale = locale;
        }
    }
}

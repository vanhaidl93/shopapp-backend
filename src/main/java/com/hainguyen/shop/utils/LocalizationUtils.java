package com.hainguyen.shop.utils;

import com.hainguyen.shop.dtos.response.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class LocalizationUtils {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    public String getLocalizedMessage(String messageKey, Object... args ) {
        HttpServletRequest currentRequest = getCurrentRequest();
        Locale locale = localeResolver.resolveLocale(currentRequest);

        return messageSource.getMessage(messageKey, args, locale);
    }

    public ResponseEntity<SuccessResponse> getResponseChangeRecord(Boolean result, String failMessage) {
        if (result) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new SuccessResponse(Constants.STATUS_200,
                            getLocalizedMessage("MESSAGE_200")));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new SuccessResponse(Constants.STATUS_417, getLocalizedMessage(failMessage)));
        }
    }
}

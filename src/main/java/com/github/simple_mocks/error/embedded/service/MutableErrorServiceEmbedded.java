package com.github.simple_mocks.error.embedded.service;

import com.github.simple_mocks.content.mutable.api.rq.CreateContentGroupRq;
import com.github.simple_mocks.content.mutable.api.rq.CreateContentRq;
import com.github.simple_mocks.content.mutable.api.rq.CreateSystemRq;
import com.github.simple_mocks.content.mutable.api.rq.DeleteContentRq;
import com.github.simple_mocks.content.mutable.api.service.MutableContentService;
import com.github.simple_mocks.error.embedded.constants.Constants;
import com.github.simple_mocks.error_service.mutable.api.rq.AddLocalizationsRq;
import com.github.simple_mocks.error_service.mutable.api.rq.DeleteLocalizationsRq;
import com.github.simple_mocks.error_service.mutable.api.service.MutableErrorService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author sibmaks
 * @since 0.0.1
 */
@Slf4j
public class MutableErrorServiceEmbedded implements MutableErrorService {
    private final MutableContentService mutableContentService;

    /**
     * Constructor embedded mutable error service
     *
     * @param mutableContentService mutable content service
     */
    public MutableErrorServiceEmbedded(MutableContentService mutableContentService) {
        this.mutableContentService = mutableContentService;
    }

    @Override
    public void addLocalizations(@Nonnull AddLocalizationsRq addLocalizationsRq) {
        var errorSource = addLocalizationsRq.errorSource();

        var systemCode = errorSource.getSystemCode();

        mutableContentService.createSystem(
                CreateSystemRq.builder()
                        .systemCode(systemCode)
                        .build()
        );

        var kindCode = errorSource.getKindCode();
        mutableContentService.createContentGroup(
                CreateContentGroupRq.builder()
                        .systemCode(systemCode)
                        .type(Constants.ERROR_TYPE)
                        .code(kindCode)
                        .build()
        );

        var localizationsToAdd = addLocalizationsRq.localizations();
        for (var localizationEntry : localizationsToAdd.entrySet()) {
            var localizationKey = localizationEntry.getKey();

            var errorCode = localizationKey.errorCode();
            var userLocale = localizationKey.userLocale();
            var iso3Locale = userLocale.getISO3Language();

            var localizations = localizationEntry.getValue();

            for (var localization : localizations) {
                mutableContentService.createContent(
                        CreateContentRq.builder()
                                .systemCode(systemCode)
                                .type(Constants.ERROR_TYPE)
                                .groupCode(kindCode)
                                .code(iso3Locale + ":" + errorCode)
                                .content(localization)
                                .attributes(
                                        Map.of(
                                                Constants.ATTRIBUTE_LOCALE, iso3Locale,
                                                Constants.ATTRIBUTE_CODE, errorCode
                                        )
                                )
                                .build()
                );
            }
        }

    }

    @Override
    public void deleteLocalizations(@Nonnull DeleteLocalizationsRq deleteLocalizationsRq) {
        var errorSource = deleteLocalizationsRq.errorSource();

        var systemCode = errorSource.getSystemCode();
        var kindCode = errorSource.getKindCode();

        var errorLocalizationKeys = deleteLocalizationsRq.localizationKeys();

        for (var errorLocalizationKey : errorLocalizationKeys) {
            mutableContentService.deleteContent(
                    DeleteContentRq.builder()
                            .systemCode(systemCode)
                            .type(Constants.ERROR_TYPE)
                            .groupCode(kindCode)
                            .code(errorLocalizationKey.userLocale() + ":" + errorLocalizationKey.errorCode())
                            .build()
            );
        }
    }
}

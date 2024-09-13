package com.github.simplemocks.error.embedded.service;

import com.github.simplemocks.content.mutable.api.rq.CreateContentGroupRq;
import com.github.simplemocks.content.mutable.api.rq.CreateContentRq;
import com.github.simplemocks.content.mutable.api.rq.CreateSystemRq;
import com.github.simplemocks.content.mutable.api.rq.DeleteContentRq;
import com.github.simplemocks.content.mutable.api.service.MutableContentService;
import com.github.simplemocks.error.embedded.constants.Constants;
import com.github.simplemocks.error_service.mutable.api.rq.AddLocalizationsRq;
import com.github.simplemocks.error_service.mutable.api.rq.DeleteLocalizationsRq;
import com.github.simplemocks.error_service.mutable.api.service.MutableErrorService;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Mutable error service implementation
 *
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
        var sourceId = addLocalizationsRq.sourceId();

        var systemCode = sourceId.getSystemCode();

        mutableContentService.createSystem(
                CreateSystemRq.builder()
                        .systemCode(systemCode)
                        .build()
        );

        var kindCode = sourceId.getKindCode();
        mutableContentService.createContentGroup(
                CreateContentGroupRq.builder()
                        .systemCode(systemCode)
                        .type(Constants.CONTENT_TYPE)
                        .code(kindCode)
                        .build()
        );

        var localizationsToAdd = addLocalizationsRq.localizations();
        for (var localizationEntry : localizationsToAdd.entrySet()) {
            var localizationKey = localizationEntry.getKey();

            var errorCode = localizationKey.errorCode();
            var userLocale = localizationKey.userLocale();
            var iso3Locale = userLocale.getISO3Language();

            var localization = localizationEntry.getValue();

            mutableContentService.createContent(
                    CreateContentRq.builder()
                            .systemCode(systemCode)
                            .type(Constants.CONTENT_TYPE)
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

    @Override
    public void deleteLocalizations(@Nonnull DeleteLocalizationsRq deleteLocalizationsRq) {
        var sourceId = deleteLocalizationsRq.sourceId();

        var systemCode = sourceId.getSystemCode();
        var kindCode = sourceId.getKindCode();

        var errorLocalizationKeys = deleteLocalizationsRq.localizationKeys();

        for (var errorLocalizationKey : errorLocalizationKeys) {
            mutableContentService.deleteContent(
                    DeleteContentRq.builder()
                            .systemCode(systemCode)
                            .type(Constants.CONTENT_TYPE)
                            .groupCode(kindCode)
                            .code(errorLocalizationKey.userLocale() + ":" + errorLocalizationKey.errorCode())
                            .build()
            );
        }
    }
}

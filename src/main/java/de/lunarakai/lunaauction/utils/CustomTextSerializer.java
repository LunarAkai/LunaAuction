package de.lunarakai.lunaauction.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.jetbrains.annotations.NotNull;

public interface CustomTextSerializer extends ComponentSerializer<Component, Component, String> {

    //TODO: Get displayName
    //TODO: Get color of text
    //TODO: Get style of text
    //
    default @NotNull String serialize(@NotNull Component component) {
        String serializedItem = null;

        if(component.hasStyling()) {
            serializedItem = "Item has some styling";
        }
        return serializedItem;
    }

    default @NotNull Component deserialize(@NotNull String input) {

        return null;
    }


}

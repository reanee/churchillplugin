package com.github.manolo8.darkbot.config;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerTag implements eu.darkbot.api.config.types.PlayerTag {
    private static transient final Map<String, PlayerTag> INSTANCES = new HashMap<>();

    public static PlayerTag getTag(String name, Color color) {
        return getTag(new PlayerTag(name, color).toString());
    }

    public static PlayerTag getTag(String strTag) {
        return INSTANCES.computeIfAbsent(strTag, in -> {
            String[] str = in.split(",", 2);
            return new PlayerTag(str[1], Color.decode(str[0]));
        });
    }

    public String name;
    public Color color;

    private PlayerTag(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public boolean has(PlayerInfo playerInfo) {
        return playerInfo != null && playerInfo.hasTag(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerTag playerTag = (PlayerTag) o;
        return Objects.equals(name, playerTag.name) &&
                Objects.equals(color, playerTag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }

    @Override
    public String toString() {
        return String.format("#%02x%02x%02x,%s", color.getRed(), color.getGreen(), color.getBlue(), name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColor() {
        return color;
    }

}

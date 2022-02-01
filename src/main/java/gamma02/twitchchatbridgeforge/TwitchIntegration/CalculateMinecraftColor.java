package gamma02.twitchchatbridgeforge.TwitchIntegration;

import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class CalculateMinecraftColor {
    public static TextFormatting findNearestMinecraftColor(Color color) {
        return Arrays.stream(TextFormatting.values())
                .filter(TextFormatting::isColor)
                .map(TextFormatting -> {
                    Color TextFormattingColor = new Color(TextFormatting.getColor());

                    int distance = Math.abs(color.getRed() - TextFormattingColor.getRed()) +
                            Math.abs(color.getGreen() - TextFormattingColor.getGreen()) +
                            Math.abs(color.getBlue() - TextFormattingColor.getBlue());
                    return new TextFormattingAndDistance(TextFormatting, distance);
                })
                .sorted(Comparator.comparing(TextFormattingAndDistance::getDistance))
                .map(TextFormattingAndDistance::getTextFormatting)
                .findFirst()
                .orElse(TextFormatting.WHITE);
    }


    public static final TextFormatting[] MINECRAFT_COLORS = Arrays.stream(TextFormatting.values()).filter(TextFormatting::isColor).toArray(TextFormatting[]::new);
    // Code gotten from here https://discuss.dev.twitch.tv/t/default-user-color-in-chat/385/2 but a little bit adjusted.
    public static Map<String, TextFormatting> cachedNames = new HashMap<>();
    public static TextFormatting getDefaultUserColor(String username) {
        if (cachedNames.containsKey(username)) {
            return cachedNames.get(username);
        } else {
            // If we don't have the color cached, calculate it.
            char firstChar = username.charAt(0);
            char lastChar = username.charAt(username.length() - 1);

            int n = ((int) firstChar) + ((int) lastChar);
            return MINECRAFT_COLORS[n % MINECRAFT_COLORS.length];
        }
    }

    private static class TextFormattingAndDistance {
        private TextFormatting TextFormatting;

        public TextFormatting getTextFormatting() {
            return TextFormatting;
        }

        private int distance;

        public int getDistance() {
            return distance;
        }

        public TextFormattingAndDistance(TextFormatting TextFormatting, int distance) {
            this.TextFormatting = TextFormatting;
            this.distance = distance;
        }
    }
}
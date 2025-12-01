package gui.style;

import java.awt.Color;

/**
 * Central place for all app colours.
 * Pastel theme: soft purple + peach accents, light gray background.
 */
public final class ColorPalette {

    private ColorPalette() { }

    // App background (around the card)
    public static final Color BACKGROUND = new Color(0xF4, 0xF5, 0xFB);   // very light bluish-gray

    // Main card background
    public static final Color CARD_BG    = new Color(0xFF, 0xFF, 0xFF);   // pure white

    // Text
    public static final Color TEXT_PRIMARY   = new Color(0x23, 0x24, 0x2A); // deep charcoal
    public static final Color TEXT_SECONDARY = new Color(0x6B, 0x72, 0x80); // soft gray

    // Primary accent – pastel purple
    public static final Color ACCENT_PRIMARY       = new Color(0x9B, 0x8C, 0xFF); // lavender / periwinkle
    public static final Color ACCENT_PRIMARY_SOFT  = new Color(0xE6, 0xE3, 0xFF); // very light version

    // Secondary accent – pastel peach
    public static final Color ACCENT_SECONDARY      = new Color(0xFF, 0xC3, 0xA5); // peach
    public static final Color ACCENT_SECONDARY_SOFT = new Color(0xFF, 0xEE, 0xE4); // pale peach

    // Tabs when not selected
    public static final Color TAB_UNSELECTED_BG   = new Color(0xF2, 0xF2, 0xF7); // light gray-violet
    public static final Color TAB_UNSELECTED_TEXT = new Color(0x55, 0x5B, 0x6B); // muted gray

    // Soft border / divider colour
    public static final Color BORDER_SOFT = new Color(0xE2, 0xE3, 0xF0);

    // Convenience colours for checklist buttons
    public static final Color CHECKED_BG     = ACCENT_PRIMARY;
    public static final Color CHECKED_TEXT   = Color.WHITE;
    public static final Color UNCHECKED_BG   = CARD_BG;
    public static final Color UNCHECKED_TEXT = TEXT_PRIMARY;
}

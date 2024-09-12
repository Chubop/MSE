package bsh.mSE.utils;

import io.th0rgal.oraxen.OraxenPlugin;
import io.th0rgal.oraxen.font.FontManager;
import io.th0rgal.oraxen.font.Glyph;

public class OraxenUtils {
    public static Glyph getGlyph(String glyphName){
        FontManager fontManager = OraxenPlugin.get().getFontManager();
        return fontManager.getGlyphFromID(glyphName);
    }

}

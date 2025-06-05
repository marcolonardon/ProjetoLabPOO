package br.unaerp.view.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;

public class LimitDocumentFilter extends DocumentFilter {
    private final int max;

    public LimitDocumentFilter(int max) {
        this.max = max;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (string == null) return;
        int newLength = fb.getDocument().getLength() + string.length();
        if (newLength <= max) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (text == null) return;
        int currentLength = fb.getDocument().getLength();
        int newLength = currentLength - length + text.length();
        if (newLength <= max) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length)
            throws BadLocationException {
        super.remove(fb, offset, length);
    }
}

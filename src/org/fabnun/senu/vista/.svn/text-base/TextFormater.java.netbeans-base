package org.fabnun.senu.vista;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.Format;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import org.fabnun.senu.Constantes;

public class TextFormater extends DefaultStyledDocument {

    private JTextComponent comp;
    private int limit;
    private boolean toUppercase = false;
    private Pattern pattern;
    private Format format;

    public TextFormater(int limit) {
        super();
        this.limit = limit;
    }

    public TextFormater(int limit, boolean upper) {
        super();
        this.limit = limit;
        toUppercase = upper;
    }

    public TextFormater(int limit, boolean upper, String pattern) {
        super();
        this.limit = limit;
        toUppercase = upper;
        this.pattern = Pattern.compile("(" + pattern + ")*");
    }

    public TextFormater(int limit, boolean upper, String pattern, Format format, JTextComponent comp) {
        super();
        this.limit = limit;
        toUppercase = upper;
        this.pattern = Pattern.compile("(" + pattern + ")*");
        final JTextComponent fcomp = comp;
        final Format jformat = format;
        if (fcomp != null && jformat != null) {
            fcomp.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    if (!AutoCompleter.isBlockOthers()) {
                        AutoCompleter.blockOther(true);
                        String s = fcomp.getText();
                        if (jformat instanceof NumberFormat) {
                            try {
                                if (!s.endsWith(",")) {
                                    if (s.length() > 0) {
                                        try {
                                            s = s.replaceAll("\\.", "");
                                            int idx0, idx1;
                                            idx0 = s.indexOf(",");
                                            idx1 = s.lastIndexOf(",");
                                            while (idx0 != idx1) {
                                                s = s.substring(0, idx1) + s.substring(idx1 + 1);
                                                idx1 = s.lastIndexOf(",");
                                            }
                                            s = s.replaceAll(",", ".");
                                            s = Constantes.decimalFormat.format(new Double(s));
                                        } catch (Exception ex) {
                                        }
                                    }
                                    fcomp.setText(s);
                                    fcomp.setForeground(Color.black);
                                } else {
                                    if (fcomp != null) {
                                        fcomp.setForeground(Color.red);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                fcomp.setForeground(Color.red);
                            }
                        }
                        AutoCompleter.blockOther(false);
                    }
                }
            });
        }
    }

    public TextFormater(int limit, boolean upper, String pattern, Format format) {
        super();
        this.limit = limit;
        toUppercase = upper;
        this.pattern = Pattern.compile("(" + pattern + ")*");
        this.format = format;
    }

    public TextFormater(boolean upper, String pattern) {
        this.limit = 0;
        toUppercase = upper;
        this.pattern = Pattern.compile("(" + pattern + ")*");
    }

    @Override
    public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (toUppercase) {
            str = str.toUpperCase();
        }
        if (pattern != null) {
            Matcher m = pattern.matcher(str);
            if (!m.matches()) {
                return;
            }
        }
        if (limit==0 || (getLength() + str.length()) <= limit) {
            super.insertString(offs, str, attribute);
        }
    }
}

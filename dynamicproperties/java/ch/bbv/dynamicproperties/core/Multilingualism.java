/*********************************************************************** 
 * Copyright 2007 BBV Software Services AG (http://www.bbv.ch)
 *  
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * http://www.apache.org/licenses/LICENSE-2.0
 * you may not use this file except in compliance with the License.
 * 
 * $Id$
 * 
 * History:
 * 2007/06/06      Ueli Kurmann     Initial Release
 ***********************************************************************/

package ch.bbv.dynamicproperties.core;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import ch.bbv.dog.DataObjectImpl;
import ch.bbv.dynamicproperties.pmmda.Visitor;

public class Multilingualism extends DataObjectImpl implements Serializable {

    Map<String, String> translations;

    /**
     * Constructor
     * 
     */
    public Multilingualism() {
        translations = new Hashtable<String, String>();
    }

    /**
     * Adds a translation
     * 
     * @param locale
     *            the locale instance
     * @param content
     *            the translation
     */
    public void addTranslation(Locale locale, String content) {
        addTranslation(locale.getLanguage(), content);
    }

    /**
     * Adds a translation
     * 
     * @param code
     *            the language code
     * @param content
     *            the translation
     */
    public void addTranslation(String code, String content) {
        translations.put(code, content);
    }

    /**
     * Returns the translation specified by the default locale instance of the
     * virtual machine
     * 
     * @return the translated word
     */
    public String getTranslation() {
        return getTranslation(Locale.getDefault());
    }

    /**
     * Returns the translation in the language specified by the locale object
     * 
     * @param locale
     *            the locale containing the language code
     * @return the translated word
     */
    public String getTranslation(Locale locale) {
        return getTranslation(locale.getLanguage());
    }

    /**
     * Returns the translation in the language specified by the language code
     * 
     * @param code
     *            the language code
     * @return the translated word
     */
    public String getTranslation(String code) {
        return translations.get(code);
    }

    /**
     * 
     * @param visitor
     */
    public void accept(Visitor visitor) {
        assert (visitor != null);
        visitor.setDepth(visitor.getDepth() + 1);
        visitor.visit(this);
        traverse(visitor);
        visitor.setDepth(visitor.getDepth() - 1);
    }

    private void traverse(Visitor visitor) {
        //
    }
}

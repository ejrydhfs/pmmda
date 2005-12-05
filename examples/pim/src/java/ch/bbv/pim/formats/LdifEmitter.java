/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.io.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Contact;
import ch.bbv.pim.Organization;

public class LdifEmitter extends ContactEmitter {
  /**
   * The velocity template used to generate the representation of contacts.
   */
  private Template template;

  /**
   * The writer of the file to export.
   */
  private BufferedWriter writer;

  /**
   * Default constructor of the class.
   */
  public LdifEmitter() {
    template = loadTemplate("ldif.vm");
  }

  public void emit(AddressBook book, File records) throws IOException {
    assert (book != null)
        && ((records != null) && (!records.exists() || records.canWrite()));
    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
        records)));
    VelocityContext context = new VelocityContext();
    context.put("emitter", this);
    context.put("addressBook", book);
    for (Organization organization : book.getOrganizations()) {
      context.put("organization", organization);
      for (Contact contact : organization.getContacts()) {
        context.put("contact", contact);
        String vcard = processTemplate(template, context);
        writer.write(vcard);
      }
    }
    writer.close();
  }

  public boolean shouldTransform(String text)
      throws UnsupportedEncodingException {
    byte[] bytes = text.getBytes("UTF-8");
    return bytes.length > text.length();
  }

  public String transform(String text) throws UnsupportedEncodingException {
    return new String(Base64.encodeBase64(text.getBytes("UTF-8")));
  }
}

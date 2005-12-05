/* copyright 2000-2005 Marcel Baumann 
 * Licensed under the Apache License, Version 2.0 (the "Licence"); 
 * you may not use this file except in compliance with the License. 
 * $Id$ 
 */
package ch.bbv.pim.formats;

import java.io.*;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Contact;
import ch.bbv.pim.Organization;

/**
 * The VCard emitter implements the VCARD standard as specified in the RFC 2426
 * standard. No new lines are generated between VCARD instances stored in the
 * same file to avoid trouble with the iPod software.
 * @author MarcelBaumann
 * @version $Revision$
 */
public class VCardEmitter extends ContactEmitter {
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
  public VCardEmitter() {
    template = loadTemplate("vcard.vm");
  }

  @Override
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
}

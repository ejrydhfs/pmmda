/* copyright 2000-2005 Marcel Baumann
 * Licensed under the Apache License, Version 2.0 (the "Licence");
 * you may not use this file except in compliance with the License.
 * $Id$
 */

package ch.bbv.mda.generators;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The code extractor retrieves the statements written by the developer in the
 * protected area of generated source code artifacts. The barriers are
 * predefined comments. The extractor only uses the predefined comments to 
 * detect a block and is language independant. It should behave correctly for
 * all programming languages supporting C-style comments. 
 * @author MarcelBaumann
 * @version $Revision$
 */
public class CodeExtractor {
  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String IMPORTS_START = "/* %% USER DEFINED IMPORT START %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String IMPORTS_END = "/* %% USER DEFINED IMPORT END   %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String DECLARATIONS_START = "/* %% USER DEFINED DECLARATION START %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String DECLARATIONS_END = "/* %% USER DEFINED DECLARATION END %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String CONSTRUCTORS_START = "/* %% USER DEFINED CONSTRUCTORS START %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String CONSTRUCTORS_END = "/* %% USER DEFINED CONSTRUCTORS END %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String CODE_START = "/* %% USER DEFINED CODE START %% */";

  /**
   * Code barrier for a block of user defined statements.
   */
  public static final String CODE_END = "/* %% USER DEFINED CODE END %% */";

  /**
   * Start of line regular expression.
   */
  private static final String START_OF_LINE = "^[\t\f ]*";

  /**
   * End of line regular expression.
   */
  private static final String END_OF_LINE = "[\t\f ]*$";

  /**
   * Descriptions of the blocks to retrieve.
   */
  private static final BlockDescription[] descriptions = {
      new BlockDescription(IMPORTS_START, IMPORTS_END),
      new BlockDescription(DECLARATIONS_START, DECLARATIONS_END),
      new BlockDescription(CONSTRUCTORS_START, CONSTRUCTORS_END),
      new BlockDescription(CODE_START, CODE_END) };

  /**
   * Blocks to retrieve.
   */
  private Block[] blocks;

  /**
   * The reader of the file to parsed.
   */
  private BufferedReader reader;

  /**
   * Buffer containing the current input to process.
   */
  private String input;

  /**
   * Default constructor of the class.
   */
  public CodeExtractor() {
    blocks = new Block[descriptions.length];
    for (int i = 0; i < blocks.length; ++i) {
      blocks[i] = new Block(descriptions[i]);
    }
  }

  /**
   * Extracts all user defined statements defined in protected blocks.
   * @param file file to parse.
   * @throws IOException an exception occured when searching for blocks.
   */
  public void extract(File file) throws IOException {
    if (reader != null) {
      reader.close();
    }
    reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(file)));
    input = reader.readLine();
    while (input != null) {
      int match = matchStartPattern(input);
      if (match != -1) {
        readBlock(match);
      }
    }
  }

  /**
   * Returns the statements of the import block.
   * @return list of statements of the block.
   */
  public List getImports() {
    return blocks[0].statements;
  }

  /**
   * Returns the statements of the declarations block.
   * @return list of statements of the block.
   */
  public List getDeclarations() {
    return blocks[1].statements;
  }

  /**
   * Returns the statements of the constructors block.
   * @return list of statements of the block.
   */
  public List getConstructors() {
    return blocks[2].statements;
  }

  /**
   * Returns the statements of the source code block.
   * @return list of statements of the block.
   */
  public List getStatements() {
    return blocks[3].statements;
  }

  /**
   * Matches the start of a protected block based on the block descriptors.
   * @param input input to match against.
   * @return the index of the matched block or -1 if no match was found.
   */
  private int matchStartPattern(String input) {
    for (int i = 0; i < blocks.length; ++i) {
      Matcher matcher = blocks[i].startPattern.matcher(input);
      if (matcher.matches()) {
        return i;
      }
    }
    return -1;
  }

  private void readBlock(int index) throws IOException {
    blocks[index].statements.clear();
    input = reader.readLine();
    if (input == null) {
      throw new IOException("missing end block "
          + blocks[index].description.endBlock);
    }
    while (input != null) {
      Matcher matcher = blocks[index].endPattern.matcher(input);
      if (!matcher.matches()) {
        blocks[index].statements.add(input);
        input = reader.readLine();
        if (input == null) {
          blocks[index].statements.clear();
          throw new IOException("missing end block "
              + blocks[index].description.endBlock);
        }
      } else {
        input = null;
      }
    }
  }

  /**
   * The block description defines the characteristics of a protected block of
   * user defined statements.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class BlockDescription {
    String startBlock;

    String endBlock;

    String startPatternString;

    String endPatternString;

    public BlockDescription(String startBlock, String endBlock) {
      this.startBlock = startBlock;
      this.endBlock = endBlock;
      startPatternString = START_OF_LINE + startBlock + END_OF_LINE;
      endPatternString = START_OF_LINE + endBlock + END_OF_LINE;
    }
  };

  /**
   * The block defines the contents of a protected block of user defined 
   * statements.
   * @author MarcelBaumann
   * @version $Revision$
   */
  private static class Block {
    BlockDescription description;

    Pattern startPattern;

    Pattern endPattern;

    List<String> statements;

    public Block(BlockDescription description) {
      this.description = description;
      startPattern = Pattern.compile(description.startPatternString);
      endPattern = Pattern.compile(description.endPatternString);
      statements = new ArrayList<String>();
    }
  }
}
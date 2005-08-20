package modnlp.tc.parser;
import modnlp.tc.dstruct.*;
import java.io.*;
/**
 *  This class implements the most basic functionality of a tc.parser
 *  object: read the content of a file, assign it a category and a
 *  unique ID. This class will tipically be extended, and the
 *  <code>parse()</code> method will tipically b overridden. 
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Parser.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see NewsParser
 * @see LingspamEmailParser
*/
public class Parser {

  String filename = null;
  ParsedCorpus parsedCorpus = null;

  /**
   * Create a new <code>Parser</code> instance. This will usually be
   * followed by a call to setFilename(). This constructor takes no
   * parameters so that the class can be loaded and instantiated
   * dynamically (as a plugin) by MakeProbabilityModel etc.
   *
   * @see modnlp.tc.induction.MakeProbabilityModel
   */
  public Parser() {
  }

  /** 
   * Set name of file to be parsed 
   */
  public void setFilename (String fn){
    this.filename = fn;
  }

  /**
   * Get parsed corpus, parsing filename if necessary
   */
  public ParsedCorpus getParsedCorpus () {
    if (parsedCorpus == null)
      parse();
    return parsedCorpus;
  }

  /**
   * Process ('parse') input file <code>filename</code> and add its
   * contents literally to parsedCorpus wih ID and category =
   * <code>filename</code>. This method isn't likely to be very
   * useful, so subclasses will tipically  override it
   * (e.g. NewsParser).
   *
   * @see NewsParser
   */
  public void parse() {
    parsedCorpus = new ParsedCorpus();
    ParsedDocument pni = new ParsedDocument();
    try{
      File f = new File(filename);
      pni.setId(filename);
      pni.addCategory(filename);
      BufferedReader in
        = new BufferedReader(new FileReader(filename));
      String line = null;
      while ( (line = in.readLine()) != null )
        {
          pni.addText(line);
        }
      parsedCorpus.addParsedDocument(pni);
    }
    catch (Exception e) 
      {
        System.err.println("Error parsing "+filename);
        e.printStackTrace();
      }
  }
  //abstract void parse();

}

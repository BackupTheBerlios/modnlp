package modnlp.tc.parser;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import java.io.*;

/**
 * Parse an email file and store the results as a ParsedText object.
 * ********* incomplete ***************
 * 
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: EmailParser.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  
*/

public class EmailParser extends Parser
{

  
  /** 
   * parse: Set up parser object, perform parsing
   */
  public void  parse ()
  {
  }
  

  /**
   *  main method for test purposes. 
   */
  public static void main(String[] args) {
    try {
      EmailParser f = new EmailParser();
      f.setFilename(args[0]);
      System.out.println(f.getParsedCorpus());
    }
    catch (Exception e){
      System.err.println("modnlp.tc.parser.EmailParser: ");
      System.err.println("Usage: EmailParser FILENAME");
      //e.printStackTrace();
    } 
  }  


}


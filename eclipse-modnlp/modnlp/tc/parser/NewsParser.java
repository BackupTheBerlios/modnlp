package modnlp.tc.parser;
import modnlp.tc.dstruct.*;
/**
 * Parse a Reuters file and store the results as a ParsedCorpus object.
 * 
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: NewsParser.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  TypeListHandler
*/

public class NewsParser extends Parser
{

  /** 
   * parseNews: Set up parser object, perform parsing
   */
  public void  parse ()
  {
    try {
      XMLHandler xh = new XMLHandler(filename);
      xh.parse();
      System.err.println("xml parsed ");
      parsedCorpus = xh.getParsedCorpus();
    }
    catch (Exception e) 
      {
        System.err.println("Error parsing "+filename);
        e.printStackTrace();
      }
  }

  
  /**
   *  main method for test purposes. 
   */
  public static void main(String[] args) {
    try {
      NewsParser f = new NewsParser();
      f.setFilename(args[0]);
      System.out.println(f.getParsedCorpus());
    }
    catch (Exception e){
      System.err.println("modnlp.tc.parser.NewsParser: ");
      System.err.println("Usage: NewsParser FILENAME");
      //e.printStackTrace();
    } 
  }  

}


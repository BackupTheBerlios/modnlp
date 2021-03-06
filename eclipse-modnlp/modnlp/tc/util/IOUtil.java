package modnlp.tc.util;
import java.io.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.parser.*;
import modnlp.tc.tsr.*;
import modnlp.tc.evaluation.*;
/**
 *  Handle PM serialization, plugin loading, and other IO stuff
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: IOUtil.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  
*/
public class IOUtil {

  public static final String PARSER_PLUGIN_BASE = "modnlp.tc.parser.";
  public static final String TSR_PLUGIN_BASE = "modnlp.tc.tsr.";

  public static void dumpProbabilityModel (TCProbabilityModel pm, String filename) 
  {
    try {
      FileOutputStream out = new FileOutputStream(filename);
      ObjectOutputStream s = new ObjectOutputStream(out);
      s.writeObject(pm);
      s.flush();
    }
    catch (Exception e){
      System.err.println("Error saving Probability Model"); 
      e.printStackTrace();
    }
  }

  public static TCProbabilityModel loadProbabilityModel (String filename)
  {
    try {
      FileInputStream in = new FileInputStream(filename);
      ObjectInputStream s = new ObjectInputStream(in);
      return (TCProbabilityModel)s.readObject();
    }
    catch (Exception e){
      System.err.println("Error reading Probability Model");
      e.printStackTrace();
    }
    return null;
  }


  public static void dumpCSVTable (CSVTable csvt, String filename) 
  {
    try {
      FileOutputStream out = new FileOutputStream(filename);
      ObjectOutputStream s = new ObjectOutputStream(out);
      s.writeObject(csvt);
      s.flush();
    }
    catch (Exception e){
      System.err.println("Error saving CSV table"); 
      e.printStackTrace();
    }
  }

  public static CSVTable loadCSVTable (String filename)
  {
    try {
      FileInputStream in = new FileInputStream(filename);
      ObjectInputStream s = new ObjectInputStream(in);
      return (CSVTable)s.readObject();
    }
    catch (Exception e){
      System.err.println("Error reading CSVTable");
      e.printStackTrace();
    }
    return null;
  }
  

  public static Parser loadParserPlugin(String plugin) throws Exception {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    String pc = PARSER_PLUGIN_BASE+plugin;
    return (Parser)cl.loadClass(pc).newInstance();
  }

  public static TermFilter loadTSRPlugin(String plugin) throws Exception {
    ClassLoader cl = ClassLoader.getSystemClassLoader();
    String fc = TSR_PLUGIN_BASE+plugin;
    return (TermFilter)cl.loadClass(fc).newInstance();
  }

  public static void main(String[] args) {
    try {
      TCProbabilityModel pm = loadProbabilityModel(args[0]);
      System.err.println("loading prob model...");
      //if (args.length > 1){
      //  WordScorePair[] ws = pm.getWordScoreArray();
      //  for (int i = 0; i < ws.length; i++)
      //    System.out.println(ws[i]);
      //}
      System.out.println(pm.getCreationInfo());
    }
    catch (Exception e){
      System.err.println("USAGE:");
      System.err.println(" IOUtil probabilitymodel -d\n");
      System.err.println("SYNOPSIS:\n load probabilitymodel and print creation info.");
    }

  }


}

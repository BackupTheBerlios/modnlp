package modnlp.tc.util;
import modnlp.tc.dstruct.*;
import java.io.*;
import java.util.Vector;
import java.util.Set;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
/**
 *  Manipulate strings for/from ARFF files to be used with the WEKA
 *  toolkit.
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: ARFFUtil.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  http://www.cs.waikato.ac.nz/~ml/weka/index.html the weka toolkit for data mining.
*/
public class ARFFUtil {


  public static String toString(boolean[] ba){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ba.length ; i++)
      sb.append(ba[i]+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(int[] ba){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ba.length ; i++)
      sb.append(ba[i]+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(long[] ba){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ba.length ; i++)
      sb.append(ba[i]+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(double[] ba){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ba.length ; i++)
      sb.append(ba[i]+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(String[] ba){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < ba.length ; i++)
      sb.append(ba[i]+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(WordFrequencyPair[] wfp){
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < wfp.length ; i++)
      sb.append(wfp[i].getWord()+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(Vector v){
    StringBuffer sb = new StringBuffer();
    for (Enumeration e = v.elements() ; e.hasMoreElements() ;)
      sb.append((String)e.nextElement()+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  public static String toString(Set v){
    StringBuffer sb = new StringBuffer();
    for (Iterator e = v.iterator() ; e.hasNext() ;)
      sb.append((String)e.next()+",");
    if (sb.length() < 1)
      return "";
    return sb.substring(0,sb.length()-1);
  }

  /**
   * Convert a <code>TCInvertedIndex</code> into an ARFF file for
   * <code>category</code> (a single category or <>null<>,
   * representing all categories) and prints the ARFF file onto an
   * output stream. The <code>WordFrequencyPair</code> array restricts
   * the entries of this ARFF file to those terms that occur in wfp
   * (i.e. the terms selected by term set reduction.)
   *
   * Documents are represented as vectors of integers whose elements
   * indicate the number of occurrences of terms in a document.
   *
   * @param ii a <code>TCInvertedIndex</code> value
   * @param wfp a <code>WordFrequencyPair[]</code> value
   * @param category a <code>String</code> representing a category or
   *        <code>null</code> representing all categories.
   * @param out a <code>PrintStream</code> value
   */
  public static void printOccurARFF(TCInvertedIndex ii, 
                                      WordFrequencyPair[] wfp, 
                                      String category,
                                      PrintStream out ) 
  {
    Set ds = ii.getDocSet();
    // print header
    out.print("@RELATION text\n\n");
    Set cs = null;
    for (int i = 0; i < wfp.length ; i++)
      out.print("@ATTRIBUTE "+wfp[i].getWord()+" REAL\n");
    if (category == null){
      cs = ii.getCategorySet();
      out.print("@ATTRIBUTE category {"+toString(cs)+"}\n");
    }
    else
      out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    // print data
    int c =  1;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      PrintUtil.printNoMove("Printing ...", c++);
      String id = (String)e.next();
      int[] tabv = new int[wfp.length];
      for (int i = 0; i < wfp.length ; i++)
        tabv[i] = ii.getCount(id, wfp[i].getWord());
      NewsItemAsOccurVector niaov = new NewsItemAsOccurVector(ii.getCategVector(id),
                                                              tabv,
                                                              id);
      String oas = ARFFUtil.toString(niaov.getOccurrenceArray());
      if (category == null)
        for (Iterator i = niaov.getCategVector().iterator(); i.hasNext() ; ){
          out.print(oas);
          out.print(","+i.next()+"\n");
        }
      else {
        out.print(oas);
        out.print(","+niaov.isOfCategory(category)+"\n");
      }
    }
    PrintUtil.donePrinting();
  }

  /**
   * Convert a <code>TCInvertedIndex</code> into an ARFF file for
   * <code>category</code> (a single category or <>null<>,
   * representing all categories) and prints the ARFF file onto an
   * output stream. The <code>WordFrequencyPair</code> array restricts
   * the entries of this ARFF file to those terms that occur in wfp
   * (i.e. the terms selected by term set reduction.)
   *
   * Documents are represented as vectors of Boolean values whose elements
   * indicate the occurrence or non-occurrence of terms a the document.
   *
   * @param ii a <code>TCInvertedIndex</code> value
   * @param wfp a <code>WordFrequencyPair[]</code> value
   * @param category a <code>String</code> representing a category or
   *        <code>null</code> representing all categories.
   * @param out a <code>PrintStream</code> value
   * @see NewsItemAsOccurVector#getBooleanTextArray()
   */
  public static void printBooleanARFF(TCInvertedIndex ii, 
                                      WordFrequencyPair[] wfp, 
                                      String category,
                                      PrintStream out ) 
  {
    Set ds = ii.getDocSet();
    // print header
    out.print("@RELATION text\n\n");
    for (int i = 0; i < wfp.length ; i++)
      out.print("@ATTRIBUTE "+wfp[i].getWord()+" {true,false}\n");
    Set cs = null;
    if (category == null){
      cs = ii.getCategorySet();
      out.print("@ATTRIBUTE category {"+toString(cs)+"}\n");
    }
    else
      out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    // print data
    int c =  1;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      PrintUtil.printNoMove("Printing ...", c++);
      String id = (String)e.next();
      int[] tabv = new int[wfp.length];
      for (int i = 0; i < wfp.length ; i++)
        tabv[i] = ii.getCount(id, wfp[i].getWord());
      NewsItemAsOccurVector niaov = new NewsItemAsOccurVector(ii.getCategVector(id),
                                                              tabv,
                                                              id);
      String oas = ARFFUtil.toString(niaov.getBooleanTextArray());
      if (category == null)
        for (Iterator i = niaov.getCategVector().iterator(); i.hasNext() ; ){
          out.print(oas);
          out.print(","+i.next()+"\n");
        }
      else {
        out.print(oas);
        out.print(","+niaov.isOfCategory(category)+"\n");
      }
    }
    PrintUtil.donePrinting();
  }

  /**
   * Convert a <code>TCInvertedIndex</code> into an ARFF file for
   * <code>category</code> (a single category or <>null<>,
   * representing all categories) and prints the ARFF file onto an
   * output stream. The <code>WordFrequencyPair</code> array restricts
   * the entries of this ARFF file to those terms that occur in wfp
   * (i.e. the terms selected by term set reduction.)
   *
   * Documents are represented as vectors of TFIDF values calculated as follows:
     <pre>
                                                            size_of_corpus  
        tfidf = no_of_occurrences_of_t_in_d * log ---------------------------------- 
                                                  size_of_subcorpus_in_which_t_occurs 
     </pre>
   *
   * @param ii a <code>TCInvertedIndex</code> value
   * @param wfp a <code>WordFrequencyPair[]</code> value
   * @param category a <code>String</code> representing a category or
   *        <code>null</code> representing all categories.
   * @param out a <code>PrintStream</code> value
   * @see NewsItemAsOccurVector#getTFIDFVector(WordFrequencyPair[],int)
   */
  public static void printTFIDFARFF(TCInvertedIndex ii, 
                                    WordFrequencyPair[] wfp, 
                                    String category,
                                    PrintStream out ) 
  {
    Set ds = ii.getDocSet();
    // print header
    out.print("@RELATION text\n\n");
    for (int i = 0; i < wfp.length ; i++)
      out.print("@ATTRIBUTE "+wfp[i].getWord()+" REAL\n");
    Set cs = null;
    if (category == null){
      cs = ii.getCategorySet();
      out.print("@ATTRIBUTE category {"+toString(cs)+"}\n");
    }
    else
      out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    // print data
    int c =  1;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      PrintUtil.printNoMove("Printing ...", c++);
      String id = (String)e.next();
      int[] tabv = new int[wfp.length];
      for (int i = 0; i < wfp.length ; i++)
        tabv[i] = ii.getCount(id, wfp[i].getWord());
      NewsItemAsOccurVector niaov = new NewsItemAsOccurVector(ii.getCategVector(id),
                                                              tabv,
                                                              id);
      String oas = ARFFUtil.toString(niaov.getTFIDFVector(wfp, ii.getCorpusSize()));
      if (category == null)
        for (Iterator i = niaov.getCategVector().iterator(); i.hasNext() ; ){
          out.print(oas);
          out.print(","+i.next()+"\n");
        }
      else {
        out.print(oas);
        out.print(","+niaov.isOfCategory(category)+"\n");
      }
    }
    PrintUtil.donePrinting();
  }

  /**
   * Convert a <code>TCInvertedIndex</code> into an ARFF file for
   * <code>category</code> (a single category or <>null<>,
   * representing all categories) and prints the ARFF file onto an
   * output stream. The <code>WordFrequencyPair</code> array restricts
   * the entries of this ARFF file to those terms that occur in wfp
   * (i.e. the terms selected by term set reduction.)
   *
   * Documents are represented as vectors of proportional term weight
     values calculated as follows: 
    <pre> 
                              1 + log no_occurs_term_i_in_j 
    pweight = round ( 10 x  ------------------------------ )
                              1 + log no_terms_in_j

    </pre> 
    if <code>no_terms_in_j &gt; 0</code>. Otherwise <code>pweight = 0</code>. 
   *
   * @param ii a <code>TCInvertedIndex</code> value
   * @param wfp a <code>WordFrequencyPair[]</code> value
   * @param category a <code>String</code> representing a category or
   *        <code>null</code> representing all categories.
   * @param out a <code>PrintStream</code> value
   * @see NewsItemAsOccurVector#getPWEIGHTVector(int)
   */
  public static void printPWeightARFF(TCInvertedIndex ii, 
                                      WordFrequencyPair[] wfp, 
                                      String category,
                                      PrintStream out ) 
  {
    Set ds = ii.getDocSet();
    // print header
    out.print("@RELATION text\n\n");
    for (int i = 0; i < wfp.length ; i++)
      out.print("@ATTRIBUTE "+wfp[i].getWord()+" REAL\n");
    Set cs = null;
    if (category == null){
      cs = ii.getCategorySet();
      out.print("@ATTRIBUTE category {"+toString(cs)+"}\n");
    }
    else
      out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    // print data
    int c =  1;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      PrintUtil.printNoMove("Printing ...", c++);
      String id = (String)e.next();
      int[] tabv = new int[wfp.length];
      for (int i = 0; i < wfp.length ; i++)
        tabv[i] = ii.getCount(id, wfp[i].getWord());
      NewsItemAsOccurVector niaov = new NewsItemAsOccurVector(ii.getCategVector(id),
                                                              tabv,
                                                              id);
     String oas = ARFFUtil.toString(niaov.getPWEIGHTVector(wfp.length));
      if (category == null)
        for (Iterator i = niaov.getCategVector().iterator(); i.hasNext() ; ){
          out.print(oas);
          out.print(","+i.next()+"\n");
        }
      else {
        out.print(oas);
        out.print(","+niaov.isOfCategory(category)+"\n");
      }
    }
    PrintUtil.donePrinting();
  }

  /**
   * print debug information and all possible ARFF representation this
   * class handles
   */
  public static void printDebug(TCInvertedIndex ii,
                                WordFrequencyPair[] wfp,
                                PrintStream out ) 
  {
    Set ds = ii.getDocSet();
    // print header
    out.print("TEMPLATE: "+ARFFUtil.toString(wfp));
    out.print("\nNo. of DOCS: "+ii.getCorpusSize());
    out.print("\nTERM SET SIZE: "+wfp.length);
    // print data
    int c =  1;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      PrintUtil.printNoMove("Printing ...", c++);
      String id = (String)e.next();
      int[] tabv = new int[wfp.length];
      for (int i = 0; i < wfp.length ; i++)
        tabv[i] = ii.getCount(id, wfp[i].getWord());
      NewsItemAsOccurVector niaov = new NewsItemAsOccurVector(ii.getCategVector(id),
                                                              tabv,
                                                              id);
      out.print("\n================"
                +"\nID: "+niaov.getId()
                +"\nCATEGS: "+ARFFUtil.toString(niaov.getCategVector())
                +"\nOCCUR. VECTOR:\n "+ARFFUtil.toString(niaov.getOccurrenceArray())
                +"\nPWEIGHT VECTOR:\n "+ARFFUtil.toString(niaov.getPWEIGHTVector(wfp.length))
                +"\nTFIDF VECTOR:\n "+ARFFUtil.toString(niaov.getTFIDFVector(wfp, ii.getCorpusSize()))
                );
    }
    PrintUtil.donePrinting();
  }



}

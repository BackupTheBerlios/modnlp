package modnlp.tc.dstruct;
import java.io.*;
import modnlp.tc.util.Maths;
import modnlp.tc.util.ARFFUtil;
import java.util.Vector;
import java.util.Set;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
/**
 *  Store NewsItemAsOccurVector's, handle conversions to a variety
 *  of formats (just plain strings and ARFF, at the moment, actually)
 *  N.B.: TSReducedText is usually BIG! (so use with caution, unless
 *  you have lots of memory to spare). In most situations its preferable
 *  to work with BVProbabilityModel instead.
 *
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: TSReducedText.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see modnlp.tc.util.ARFFUtil
 * @see modnlp.tc.classify.BVProbabilityModel
*/

public class TSReducedText extends  Vector
{

  /////////////////// change this to WordFrequencyPair[]  (so we can generate tfidf scores as well) 
  WordFrequencyPair[] reducedTermSet;

  public TSReducedText ( WordFrequencyPair[] tset) 
  {
    reducedTermSet = tset;
  }


  public TSReducedText (ParsedCorpus pt, WordFrequencyPair[] tset) 
  {
    super(pt.size());
    reducedTermSet = tset;
    for (Enumeration e = pt.elements() ; e.hasMoreElements() ;){
      ParsedDocument pni = (ParsedDocument)e.nextElement();
      BagOfWords bow = new BagOfWords();
      bow.addTokens(pni.getText());
      //      bow.addTypesToFileCount(pni.getText());   // use this if Boolean vectors
      int[] tabv = new int[reducedTermSet.length];
      for (int i = 0; i < reducedTermSet.length ; i++)
        tabv[i] = bow.getCount(getWord(i));
      addNIAV(new NewsItemAsOccurVector(pni.getCategVector(),
                                      tabv,
                                      pni.getId() ));
    }    
  }

  public TSReducedText (TCInvertedIndex ii, WordFrequencyPair[] tset) 
  {
    super( ii.getCorpusSize() );
    Set ds = ii.getDocSet();
    reducedTermSet = tset;
    for (Iterator e = ds.iterator(); e.hasNext() ; ){
      String id = (String)e.next();
      int[] tabv = new int[reducedTermSet.length];
      for (int i = 0; i < reducedTermSet.length ; i++)
        tabv[i] = ii.getCount(id, getWord(i));
      addNIAV(new NewsItemAsOccurVector(ii.getCategVector(id),
                                        tabv,
                                        id));
    }
  }

  public boolean addNIAV ( NewsItemAsOccurVector niav){
    return add(niav);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("TEMPLATE:: "+ARFFUtil.toString(this.reducedTermSet));
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = (NewsItemAsOccurVector)e.nextElement();
      sb.append("\nID: "+niabv.getId()+
                "\nVECTOR: "+ARFFUtil.toString(niabv.getOccurrenceArray())+
                "\nCATEGS: "+ARFFUtil.toString(niabv.getCategVector()));
    }
    return sb+"";
  }

  public void toString(PrintStream out) {
    out.print("TEMPLATE:: "+ARFFUtil.toString(this.reducedTermSet));
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = (NewsItemAsOccurVector)e.nextElement();
      out.print("\nID: "+niabv.getId()+
                "\nVECTOR: "+ARFFUtil.toString(niabv.getOccurrenceArray())+
                "\nCATEGS: "+ARFFUtil.toString(niabv.getCategVector()));
    }
  }


  public String toOccurVectorARFF(String category)
  {
    StringBuffer sb = new StringBuffer("@RELATION text\n\n");
    // headers
    for (int i = 0; i < reducedTermSet.length ; i++) {
      sb.append("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    sb.append("@ATTRIBUTE "+category+"? {true,false}\n");
    sb.append("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      sb.append(ARFFUtil.toString(niabv.getOccurrenceArray()));
      sb.append(","+niabv.isOfCategory(category)+"\n");
    }
    return sb+"";
  }


  public String toBooleanVectorARFF(String category)
  {
    StringBuffer sb = new StringBuffer("@RELATION text\n\n");
    // headers
    for (int i = 0; i < reducedTermSet.length ; i++) {
      sb.append("@ATTRIBUTE "+getWord(i)+" {true,false}\n");
    }
    sb.append("@ATTRIBUTE "+category+"? {true,false}\n");
    sb.append("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      sb.append(ARFFUtil.toString(niabv.getBooleanTextArray()));
      sb.append(","+niabv.isOfCategory(category)+"\n");
    }
    return sb+"";
  }


  public String toPWeightVectorARFF(String category)
  {
    StringBuffer sb = new StringBuffer("@RELATION text\n\n");
    // headers
    for (int i = 0; i < reducedTermSet.length ; i++) {
      sb.append("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    sb.append("@ATTRIBUTE "+category+"? {true,false}\n");
    sb.append("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      sb.append(ARFFUtil.toString(getPWEIGHTVector(niabv)));
      sb.append(","+niabv.isOfCategory(category)+"\n");
    }
    return sb+"";
  }

  public String toTFIDFVectorARFF(String category)
  {
    StringBuffer sb = new StringBuffer("@RELATION text\n\n");
    // headers
    for (int i = 0; i < reducedTermSet.length ; i++) {
      sb.append("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    sb.append("@ATTRIBUTE "+category+"? {true,false}\n");
    sb.append("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      sb.append(ARFFUtil.toString(getTFIDFVector(niabv)));
      sb.append(","+niabv.isOfCategory(category)+"\n");
    }
    return sb+"";
  }


  // Passing gigantic strings around isn't an option when dealing with large corpora 
  public void toBooleanVectorARFF(String category, PrintStream out)
  {
    out.print("@RELATION text\n\n");
    for (int i = 0; i < reducedTermSet.length ; i++) {
      out.print("@ATTRIBUTE "+getWord(i)+" {true,false}\n");
    }
    out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      out.print(ARFFUtil.toString(niabv.getBooleanTextArray()));
      out.print(","+niabv.isOfCategory(category)+"\n");
    }
  }

  public void toOccurVectorARFF(String category, PrintStream out)
  {
    out.print("@RELATION text\n\n");
    // headers
    for (int i = 0; i < reducedTermSet.length ; i++) {
      out.print("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      out.print(ARFFUtil.toString(niabv.getOccurrenceArray()));
      out.print(","+niabv.isOfCategory(category)+"\n");
    }
  }


  public void toPWeightVectorARFF(String category, PrintStream out)
  {
    out.print("@RELATION text\n\n");
    for (int i = 0; i < reducedTermSet.length ; i++) {
      out.print("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      out.print(ARFFUtil.toString(getPWEIGHTVector(niabv)));
      out.print(","+niabv.isOfCategory(category)+"\n");
    }
  }

  public void toTFIDFVectorARFF(String category, PrintStream out)
  {
    out.print("@RELATION text\n\n");
    for (int i = 0; i < reducedTermSet.length ; i++) {
      out.print("@ATTRIBUTE "+getWord(i)+" REAL\n");
    }
    out.print("@ATTRIBUTE "+category+"? {true,false}\n");
    out.print("\n@DATA\n");
    for (Enumeration e = elements() ; e.hasMoreElements() ;){
      NewsItemAsOccurVector niabv = 
        (NewsItemAsOccurVector)e.nextElement();
      out.print(ARFFUtil.toString(getTFIDFVector(niabv)));
      out.print(","+niabv.isOfCategory(category)+"\n");
    }
  }

  /**
   * Return the TFIDF for each vector position. TFIDF calculated as follows:
   *
   * w_ij = no_of_occurrences_of_t_in_d * log ( size_of_corpus / size_of_subcorpus_in_which_t_occurs) 
   * (see notes in mlandtc-4up.pdf)
   */
  private double[] getTFIDFVector(NewsItemAsOccurVector niaov){
    double[] pwv = new double[reducedTermSet.length];
    int [] oca = niaov.getOccurrenceArray();
    int nofdocs = this.getNoOfDocuments();
    for (int i = 0; i < oca.length ; i++ ){
      pwv[i] = oca[i] * Maths.log2(nofdocs / getCount(i));
    }
    return pwv;
  }

  /**
   * Return rounded text-size-proportional weighting of term frequency:
   *
   * w_ij = round ( 10 x (1+ log #_occurs_term_i_in_j / 1 + log #_terms_in_j)). 
   *
   * See Manning & Scutze, p. 580 eq (16.1) (slightly modified here to
   * take only occurrences of terms in the reduced term set into
   * account, rather than ALL occurences)
   */
  private long[] getPWEIGHTVector(NewsItemAsOccurVector niaov){
    // add procedure to calculate tfidf scores
    long[] pwv = new long[reducedTermSet.length];
    int [] oca = niaov.getOccurrenceArray();
    int nofterms = niaov.getNoOfTems();
    for (int i = 0; i < oca.length ; i++ ){
      pwv[i] = oca[i] == 0 ? 0 
        : Math.round( 10*(1+Maths.log2(oca[i]))/(1+Maths.log2(nofterms)));
    }
    return pwv;
  }

  public int getNoOfDocuments ()
  {
    return this.size();
  }

  /**
   * Get the value of wfp.
   * @return value of wfp.
   */
  public WordFrequencyPair[] getReducedTermSet() {
    return reducedTermSet;
  }
  
  /**
   * Set the value of wfp.
   * @param v  Value to assign to wfp.
   */
  public void setReducedTermSet(WordFrequencyPair[]  v) {
    this.reducedTermSet = v;
  }

  private String getWord(int index){
    return reducedTermSet[index].getWord();
  }

  private int getCount(int index){
    return reducedTermSet[index].getCount();
  }

}

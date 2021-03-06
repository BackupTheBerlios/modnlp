package modnlp.tc.dstruct;
import java.util.Set;
import java.util.Vector;
/**
 *  Inverted indices for text categorisation must implement this
 *  interface. It defines methods for storage of and access to
 *  inverted indices of terms and categories with respect to
 *  documents. These indices are 'inverted' in the following sense:
 *  given a term or category, the index points to the documents that
 *  contain that term or category. In the case of terms, the index
 *  should also store the number of occurrences. 
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: TCInvertedIndex.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  BVProbabilityModel
*/
public interface TCInvertedIndex {

  /**
   * Check if the index contains <code>term</code>
   *
   * @param term a term to be looked up.
   * @return true if this index contains <code>term</code>, fals otherwise
   */
  boolean containsTerm(String term);
  
  /**
   * Calculate and return the generality of <code>cat</code> for this
   * model. Generality is given by 
   * <pre>
     G_cat = no_of_docs_classified_as_cat / no_of_docs_in_corpus 
     </pre>
     i.e. (<code>G_cat</code> = p(cat))
 
   * @param cat a <code>String</code> representing a category
   * @return a <code>double</code> value
   */
  double getCatGenerality(String cat);

  /**
   * Return the set of documents used in the generation of this index
   *
   * @return a <code>Set</code> containing the IDs of all documents
   * indexed in this index
   */
  Set getDocSet ();

  /**
   * Find all categories under which document <code>id</code> has been
   * classified
   *
   * @return a <code>Vector</code> containing the the vector of
   * categories (of type <code>String</code>) to which document id
   * belongs
   */
  Vector getCategVector(String id);

  /**
   * Get all categories in this corpus.
   *
   * @return a <code>Set</code> containing all categories that occur in the corpus
   */
  Set getCategorySet ();

  /**
   * Index each term (type) of each <code>ParsedDocument</code> in
   * <code>ParsedCorpus</code>, except those in
   * <code>stopwdlist</code>, on this index.
   *
   * @param pt a <code>ParsedCorpus</code> value
   * @param swlist a <code>StopWordList</code> value
   */
  void addParsedCorpus (ParsedCorpus pt, StopWordList swlist);

  /**
   * Delete all entries for terms not in the reduced term set. (To be
   * used after TSR)
   */
  void trimTermSet(Set rts);

  /**
   * Get the number of terms (types) indexed by this index
   *
   * @return an <code>int</code> value
   */
  int getTermSetSize();

  /**
   * Get the number of files a term occurs in.
   *
   * @param term word (type) to be looked up
   * @return an <code>int</code> value containing the number of files
   * that contain at least one token of type <code>term</>
   */
  int getTermCount(String term);

 /**
   *   Return the number of occurrences of <code>term</code> in
   *   document <code>id</code>
   *
   * @param id a <code>String</code> representing a unique document id
   * @param term a <code>String</code> 
   * @return the number of occurrences
   */
  public int getCount(String id, String term);

  /**
   *   Return the number of occurrences of <code>term</code> in the
   *   corpus
   *
   * @param term a <code>String</code> 
   * @return the number of occurrences
   */
  public int getCount(String term);

  /**
   * Size of the corpus on which this model is based. What this number
   * represents depends on the nature of he model. In Boolean-vector
   * models in which events are sets of documents,
   * <code>corpusSize</code> represents the number of documents.
   */
  public int getCorpusSize();

  /**
   *  make a new <code>WordScorePair</code>, big enough to store all
   *  terms indexed by this index, with scores initialised to zero
   *
   * @return a <code>WordScorePair[]</code> value
   */
  WordScorePair[] getBlankWordScoreArray();

  /**
   *  gets an initialised <code>WordScorePair</code> and populate it
   *  with global term frequency
   *
   * @param wsp a <code>WordScorePair[]</code> value
   * @return a <code>WordScorePair[]</code> value
   */
  public WordScorePair[] setFreqWordScoreArray(WordScorePair[] wsp);

}

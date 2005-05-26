package modnlp.tc.dstruct;
import java.util.Set;
import java.io.Serializable;
/**
 *  Define methodos to be implemented by all probability models for
 *  text categorisation. This is meant as an abstraction over
 *  probability models in which events are described as sets of
 *  documents (e.g. <code>BVProbabilityModel</code>, which implements
 *  the model described in [Sebastiani, 02] and elsewhere), and models
 *  where events are sets of token placeholders (as in [Mitchell, 97],
 *  for instance). It is assumed that <code>TCProbabilityModel</code>
 *  will be serialised and seved to disk for later use (e.g.  by a
 *  classifer), hence the extension of <code>Serializable</code> and
 *  the fields and methods to store and retrieve creation
 *  information. <code>TCProbabilityModel</code>s will be used
 *  typically in conjunction with <code>TCInvertedIndex</code> objects.
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: TCProbabilityModel.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  BVProbabilityModel TCInvertedIndex
*/
public abstract class TCProbabilityModel implements Serializable {

  /**
   * All TC probability models must be based on an inverted index
   *
   */
  public TCInvertedIndex invertedIndex = null;

  /**
   * The program that created this probability model
   */
  String creator = null;

  /**
   * The arguments passed to this model's creator.
   */
  String[] creatorArgs = null;
  
  /**
   * Get a summary of probabilities associated with
   * <code>term</code> and <code>category</code>
   *
   * @param term a <code>String</code> value
   * @param category a <code>String</code> value
   * @return a <code>Probabilities</code> value
   */
  public abstract Probabilities getProbabilities(String term, String category);

  /**
   * Get the value of creationInfo.
   * @return value of creationInfo.
   */
  public abstract String getCreator();
  

  /**
   * Set the value of creationInfo.
   * @param v  Value to assign to creationInfo.
   */
  public abstract void setCreator(String  v);
  
  /**
   * Get the value of creatorArgs.
   * @return value of creatorArgs.
   */
  public abstract String[] getCreatorArgs();

  /**
   * Get the value of creatorArgs as list of comma-separated values.
   * @return value of creatorArgs.
   */
  public abstract String getCreatorArgsCSV();

  /**
   * Get creation information (i.e. command and args that created this PM.)
   * @return value of creationInfo.
   */
  public abstract String getCreationInfo();

}

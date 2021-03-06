/**
 *  � 2006 S Luz <luzs@cs.tcd.ie>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package modnlp.idx.database;

import modnlp.util.LogStream;
import modnlp.dstruct.WordForms;
import modnlp.dstruct.CorpusFile;
import modnlp.idx.query.WordQuery;
import modnlp.idx.inverted.TokenMap;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
 
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
/**
 *  Mediate access to all databases (called Dictionary for
 *  'historical' reasons; see tec-server)
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Dictionary.java,v 1.2 2006/05/22 17:26:02 amaral Exp $</font>
 * @see  
*/
public class Dictionary {

  public static DictProperties dictProps = new DictProperties();
  LogStream logf;
  // main tables 
  //WordPositionTable wPosTable;          // word -> [pos1, pos2, ...]  (one table per fileno; to appear as local variables)
  WordFileTable wFilTable;       // word -> [fileno1, fileno2, ...]
  CaseTable caseTable;           // canonicalform -> [form1, form2, ...]
  FreqTable freqTable;           // word -> noofoccurrences
  FileTable fileTable;           // fileno -> filenameOrUri
  Environment environment;


  /**
   * Open a new <code>Dictionary</code> in read-only mode.
   *
   */
  public Dictionary(){
    init(false);
  }


  /**
   * Open a new <code>Dictionary</code>.
   *
   * @param write a <code>boolean</code> value: false opens the
   * dictionary in read-only mode; true opens it for writing (enabling
   * creation of new tables)
   */
  public Dictionary (boolean write){
    init(write);
  }

  public void init (boolean write){
    try {
      logf = new LogStream(System.err);
      EnvironmentConfig envConfig = new EnvironmentConfig();
      envConfig.setReadOnly(!write);
      envConfig.setAllowCreate(write);
      environment = new Environment(new File(dictProps.getEnvHome()), 
                                    envConfig);
      wFilTable = new WordFileTable(environment, 
                                    dictProps.getWFilTableName(), 
                                    write);
      caseTable = new CaseTable(environment, 
                                dictProps.getCaseTableName(), 
                                write);
      freqTable = new FreqTable(environment, 
                                dictProps.getFreqTableName(), 
                                write);
      fileTable = new FileTable(environment, 
                                dictProps.getFileTableName(), 
                                write);
      
    } catch (Exception e) {
      logf.logMsg("Error opening Dictionaries: "+e);
    }
  }

  
  /**
   * Add each token in tm (extracted from fou) to the index
   *
   * @param tm a <code>TokenMap</code>: multiset of tokens
   * @param fou a <code>String</code>: the file whose <code>TokenMap</code> is tm 
   * @exception AlreadyIndexedException if an error occurs
   */
  public void addToDictionary(TokenMap tm, String fou) throws AlreadyIndexedException {
    // check if file already exists in corpus; if so, quit and warn user
    int founo = fileTable.getKey(fou);
    if (founo >= 0) { // file has already been indexed
      logf.logMsg("Dictionary: file or URI already indexed "+fou);
      throw new AlreadyIndexedException(fou);
    }
    else 
      founo = -1*founo;
    fileTable.put(founo,fou);
    WordPositionTable wPosTable = new WordPositionTable(environment, 
                                      ""+founo,
                                      true);
    for (Iterator e = tm.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        String word = (String)kv.getKey();
        caseTable.put(word);
        wFilTable.put(word,founo);
        //StringIntKey sik = new StringIntKey(word, founo);
        IntegerSet set = (IntegerSet) kv.getValue();
        wPosTable.put(word, set);
        freqTable.put(word,set.size());
      }
    wPosTable.close();
  }
  
  public void removeFromDictionary(String fou) throws NotIndexedException {
    // check if file already exists in corpus; if so, quit and warn user
    int founo = fileTable.getKey(fou);
    if (founo < 0) { // file was not indexed
      logf.logMsg("Dictionary: file or URI not indexed "+fou);
      throw new NotIndexedException(fou);
    }
    WordPositionTable wPosTable = new WordPositionTable(environment, 
                                                        ""+founo,
                                                        true);
    TokenMap tm = wPosTable.removeFile();
    for (Iterator e = tm.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        String word = (String)kv.getKey();
        wFilTable.remove(word,founo);
        IntegerSet set = (IntegerSet) kv.getValue();
        if (freqTable.remove(word,set.size()) == 0)
          caseTable.remove(word);
      }
    fileTable.remove(founo);
  }

  /**
   * Check if file or URI <code>fou</code> is in the index. 
   *
   * @param fou a <code>String</code> value
   * @return a <code>boolean</code> value
   */
  public boolean indexed(String fou) {
    // check if file already exists in corpus; if so, quit and warn user
    int founo = fileTable.getKey(fou);
    if (founo >= 0)  // file has already been indexed
      return true;
    else 
      return false;
  }

  // kwa elements can also be wildcards (e.g. "test*", matching "test", "tested", etc)
  public WordForms getLeastFrequentWord (String [] kwa, boolean cs ){
    //String word = null;
    int freq = 0;
    WordForms wformsout = new WordForms();
    for (int i = 0; i < kwa.length ; i++ ) {
      WordForms wforms = getWordForms(kwa[i], cs);
      int fqaux = getFrequency(wforms);
      if (fqaux == 0)
        return wforms;
      if (freq == 0 || fqaux < freq){
        freq = fqaux;
        wformsout = wforms;
      }
    }
    return wformsout;
  }

	public int getFrequency (WordForms wforms)
	{
		int tf = 0;
    for (Iterator e = wforms.iterator(); e.hasNext() ;)
      {
				String key = (String)e.next();
				tf = tf + freqTable.getFrequency(key);
			}
		return tf;
	}

  

	public WordForms getWordForms (String key, boolean csensitive)
	{
		WordForms wforms = new WordForms(key);
    if ( WordQuery.isWildcard(key) )
      return caseTable.getAllPrefixMatches(key, csensitive);
    
		if (csensitive) {
      wforms.addElement(key);
      return wforms;
    }	
		else
		  return caseTable.getAllCases(key);
	}



	/** Return a vector containing all filenames where KEY 
	 *  occurs in the corpus.
   * @param  key   the keyword to search for 
	 */
	public Vector getAllFileNames (String key){
		Vector filenames = new Vector();
    IntegerSet fset = wFilTable.fetch(key);
    for (Iterator f = fset.iterator(); f.hasNext() ;){
      int fno = ((Integer)f.next()).intValue();
      filenames.addElement(fileTable.getFileName(fno));
    }
		return filenames;
	} 

  public void printSortedFreqList (PrintWriter os) {
    freqTable.printSortedFreqList(os);
  }
  
  public void printCorcordances(WordQuery query, int ctx, boolean ignx, PrintWriter os) {
			WordForms wforms = query.getWordForms();
      String key =  query.getKeyword();
			int frequency = getFrequency(wforms);
      // tell client how many lines we're sending
			os.println(frequency);
      os.flush();
      for (Iterator w = wforms.iterator(); w.hasNext(); ) {
        String word = (String)w.next();
        IntegerSet files =  wFilTable.fetch(word);
        int posl = 0;
        try {
          for (Iterator f = files.iterator(); f.hasNext(); ) {
            Integer fno = (Integer)f.next();
            String fn = fileTable.getFileName(fno.intValue());
            CorpusFile fh = new CorpusFile(fn);
            fh.setIgnoreSGML(ignx);
            WordPositionTable wpt = new WordPositionTable(environment, 
                                                          ""+fno,
                                                          false);
            IntegerSet pos  = wpt.fetch(word);
            for (Iterator p = pos.iterator(); p.hasNext(); ) {
              Integer bp = (Integer)p.next();
              String ot = fh.getWordInContext(bp, key, ctx);
              if (  query.matchConcordance(ot,ctx) )
                {
                  os.println(fn+"|"+bp+"|"+ot);
                  os.flush();
                }

              if (os.checkError()) {
                logf.logMsg("doConcordance: connection closed prematurely by client");
                fh.close();
                return;
              }
            }
            fh.close();
            wpt.close();
          }		
        }
        catch (IOException e) {
          logf.logMsg("Error reading corpus file ", e);
        }
      }
  }



  public void  dump () {
    System.out.println("===========\n FileTable:\n===========");
    fileTable.dump();
    System.out.println("===========\n CaseTable:\n===========");
    caseTable.dump();
    System.out.println("===========\n FreqTable:\n===========");
    freqTable.dump();
    fileTable.dump();
    int fnos [] = fileTable.getKeys();
    for (int i = 0 ; i < fnos.length; i++){
      System.out.println("===========\n WordPositionTable for "+
                         fileTable.getFileName(fnos[i])+":\n=============");
      WordPositionTable wPosTable = new WordPositionTable(environment, 
                                                          ""+fnos[i],
                                                          false);
      wPosTable.dump();
      wPosTable.close();
    }
  }

  public void close () {
    try {
      freqTable.close();
      wFilTable.close();
      //wPosTable.close();
      caseTable.close();
      fileTable.close();
      environment.close();
    } catch(Exception e) {
      logf.logMsg("Error closing environment: "+e);
    }
  }

  public void finalize () {
    close();
  }

}

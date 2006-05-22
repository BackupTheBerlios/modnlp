/**
 *  © 2006 S Luz <luzs@cs.tcd.ie>
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
import modnlp.idx.inverted.TokenMap;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
 
import java.io.File;
import java.util.Iterator;
import java.util.Map;
/**
 *  Mediate access to all databases (called Dictionary for
 *  'historical' reasons; see tec-server)
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Dictionary.java,v 1.1 2006/05/22 16:55:04 amaral Exp $</font>
 * @see  
*/
public class Dictionary {

  public static DictProperties dictProps = new DictProperties();
  LogStream logf;
  // main tables 
  WordPositionTable wPosTable;   // word.fileno -> [pos1, pos2, ...]
  WordFileTable wFilTable;       // word -> [fileno1, fileno2, ...]
  CaseTable caseTable;           // canonicalform -> [form1, form2, ...]
  FreqTable freqTable;           // word -> noofoccurrences
  FileTable fileTable;           // fileno -> filenameOrUri
  Environment environment;

  public Dictionary(){
    init(false);
  }

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
      wPosTable = new WordPositionTable(environment, 
                                        dictProps.getWPosTableName(), 
                                        write);
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
      logf.println("Error opening Dictionaries: "+e);
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
      logf.println("Dictionary: file or URI already indexed "+fou);
      throw new AlreadyIndexedException(fou);
    }
    else 
      founo = -1*founo;
    fileTable.put(founo,fou);
    for (Iterator e = tm.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        String word = (String)kv.getKey();
        caseTable.put(word);
        wFilTable.put(word,founo);
        StringIntKey sik = new StringIntKey(word, founo);
        IntegerSet set = (IntegerSet) kv.getValue();
        wPosTable.put(sik, set);
        freqTable.put(word,set.size());
      }
  }
  
  public void removeFromDictionary(String fou) throws NotIndexedException {
    // check if file already exists in corpus; if so, quit and warn user
    int founo = fileTable.getKey(fou);
    if (founo < 0) { // file was not indexed
      logf.println("Dictionary: file or URI not indexed "+fou);
      throw new NotIndexedException(fou);
    }
    TokenMap tm = wPosTable.removeFile(founo);
    for (Iterator e = tm.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        String word = (String)kv.getKey();
        wFilTable.remove(word,founo);
        StringIntKey sik = new StringIntKey(word, founo);
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

  public void finalize () {
    close();
  }
  
  public void close () {
    try {
      wPosTable.close();
      freqTable.close();
      wFilTable.close();
      wPosTable.close();
      caseTable.close();
      fileTable.close();
      environment.close();
    } catch(Exception e) {
      logf.println("Error closing environment: "+e);
    }
  }

  public void  dump () {
    System.out.println("===========\n FileTable:\n===========");
    fileTable.dump();
    System.out.println("===========\n CaseTable:\n===========");
    caseTable.dump();
    System.out.println("===========\n FreqTable:\n===========");
    freqTable.dump();
    System.out.println("===========\n WordPositionTable:\n=============");
    wPosTable.dump();
  }


}

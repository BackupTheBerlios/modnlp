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

import modnlp.idx.inverted.TokenMap;

import com.sleepycat.je.Environment;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DeadlockException;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;

/**
 *  Store words and the positions in which they occur as follows:
 *  <pre>  
 *       KEY      |  DATA
 *   -------------|-------------------
 *   word  fileno |  [pos1, pos2, ...]
 *  </pre>
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: WordPositionTable.java,v 1.1 2006/05/22 16:55:04 amaral Exp $</font>
 * @see  
*/
public class WordPositionTable extends Table {

  public WordPositionTable (Environment env, String fn, boolean write) {
    super(env,fn,write);
  }

  public void put(StringIntKey sik, IntegerSet set) {
    TupleBinding kb = new StringIntKeyBinding();
    TupleBinding isb = new IntegerSetBinding();
    DatabaseEntry key = new DatabaseEntry();
    DatabaseEntry data = new DatabaseEntry();
    kb.objectToEntry(sik, key);
    isb.objectToEntry(set, data);
    put(key,data);
  }

  /**
   * Remove all entries whose file ids match founo and return a
   * TokenMap of the entries removed
   *
   * @param founo an <code>int</code> value
   */
  public TokenMap removeFile (int founo) {
    TokenMap tm = null;
    Cursor c = null;
    try {
      tm = new TokenMap();
      c = database.openCursor(null, null);
      TupleBinding kb = new StringIntKeyBinding();
      DatabaseEntry key = new DatabaseEntry();
      TupleBinding isb = new IntegerSetBinding();
      DatabaseEntry data = new DatabaseEntry();
      while (c.getNext(key, data, LockMode.DEFAULT) 
             == OperationStatus.SUCCESS) {
        StringIntKey sik = (StringIntKey) kb.entryToObject(key);
        if (sik.getInt() == founo) {
          IntegerSet set  = (IntegerSet) isb.entryToObject(data);
          tm.put(sik.getString(), set);
          c.delete();
        }
      }
      c.close();
    }
    catch (DeadlockException e) {
      logf.println("Deadlock deleting record " + e);
    }
    catch (DatabaseException e) {
      logf.println("Error accessing wordPositionTable" + e);
    }
    finally {
      try{ c.close(); }catch(DatabaseException e){};
      return tm;
    }
  }

  public void dump () {
    try {
      Cursor c = database.openCursor(null, null);
      TupleBinding kb = new StringIntKeyBinding();
      TupleBinding isb = new IntegerSetBinding();
      DatabaseEntry key = new DatabaseEntry();
      DatabaseEntry data = new DatabaseEntry();
      while (c.getNext(key, data, LockMode.DEFAULT) == 
             OperationStatus.SUCCESS) {
        StringIntKey sik = (StringIntKey) kb.entryToObject(key);
        IntegerSet set  = (IntegerSet) isb.entryToObject(data);
        System.out.println(sik+" < "+set+">");
      }
      c.close();
    }
    catch (DatabaseException e) {
      logf.println("Error accessing wordPositionTable" + e);
    }
  }


}

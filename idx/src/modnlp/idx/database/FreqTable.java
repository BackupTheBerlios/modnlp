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

import com.sleepycat.je.Environment;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;

/**
 *  Store a (case-sensitive) word form and the frequency
 *  with which it occurs
 *
 *  <pre>  
 *       KEY        |  DATA
 *   ---------------|-------------------
 *      wordform    | no_of_occurrences
 *  </pre>
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: FreqTable.java,v 1.1 2006/05/22 16:55:04 amaral Exp $</font>
 * @see  
*/
public class FreqTable extends Table {

  public FreqTable (Environment env, String fn, boolean write) {
    super(env,fn,write);
  }

  public int put(String sik, int noccur) {
    DatabaseEntry key = new DatabaseEntry();
    DatabaseEntry data = new DatabaseEntry();
    int freq = noccur;
    StringBinding.stringToEntry(sik, key);
    try {
      if (database.get(null, key, data, LockMode.DEFAULT) ==
          OperationStatus.SUCCESS) {
        freq = IntegerBinding.entryToInt(data)+noccur;
      }
      IntegerBinding.intToEntry(freq, data);
      put(key,data);
    }
    catch (DatabaseException e) {
      logf.println("Error reading FreqTable" + e);
    }
    return freq;
  }

  public int remove(String sik, int noccur) {
    DatabaseEntry key = new DatabaseEntry();
    DatabaseEntry data = new DatabaseEntry();
    int freq = 0;
    StringBinding.stringToEntry(sik, key);
    try {
      if (database.get(null, key, data, LockMode.DEFAULT) ==
          OperationStatus.SUCCESS) {
        freq = IntegerBinding.entryToInt(data)-noccur;
        if (freq == 0)
          remove(key);
        else {
          IntegerBinding.intToEntry(freq, data);
          put(key,data);
        }
      }
    }
    catch (DatabaseException e) {
      logf.println("Error reading FreqTable" + e);
    }
    return freq;
  }

  public void  dump () {
    try {
      Cursor c = database.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry();
      DatabaseEntry data = new DatabaseEntry();
      while (c.getNext(key, data, LockMode.DEFAULT) == 
             OperationStatus.SUCCESS) {
        String sik = StringBinding.entryToString(key);
        int freq  = IntegerBinding.entryToInt(data);
        System.out.println(sik+" = "+freq);
      }
      c.close();
    }
    catch (DatabaseException e) {
      logf.println("Error accessing FreqTable" + e);
    }
  }


}

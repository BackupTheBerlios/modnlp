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
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;

/**
 *  Store all files or URI currently indexed
 *
 *  <pre>  
 *       KEY        |  DATA
 *   ---------------|-------------------
 *      seq_integer    |  filename_or_uri 
 *  </pre>
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: FileTable.java,v 1.1 2006/05/22 16:55:04 amaral Exp $</font>
 * @see  
*/
public class FileTable extends Table {

  public FileTable (Environment env, String fn, boolean write) {
    super(env,fn,write);
  }

  public void put(int sik, String fou) {
    DatabaseEntry key = new DatabaseEntry();
    DatabaseEntry data = new DatabaseEntry();
    IntegerBinding.intToEntry(sik, key);
    StringBinding.stringToEntry(fou, data);
    put(key,data);
  }

  public void remove(int sik) {
    DatabaseEntry key = new DatabaseEntry();
    IntegerBinding.intToEntry(sik, key);
    System.err.println("Removing "+sik);
    remove(key);
  }


  public void  dump () {
    try {
      Cursor c = database.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry();
      DatabaseEntry data = new DatabaseEntry();
      while (c.getNext(key, data, LockMode.DEFAULT) == 
             OperationStatus.SUCCESS) {
        int sik = IntegerBinding.entryToInt(key);
        String fou  = StringBinding.entryToString(data);
        System.out.println(sik+" = "+fou);
      }
      c.close();
    }
    catch (DatabaseException e) {
      logf.println("Error accessing FileTable" + e);
    }
  }


  /**
   * Get key for entry whose data matches fn, or a negative integer
   * indicating the next available key (ie. -1*(no_of_entries+1))
   *
   * @param fn a <code>String</code> value
   * @return an <code>int</code> value
   */
  public int  getKey (String fn) {
    int keynum = 0;
    try {
      Cursor c = database.openCursor(null, null);
      DatabaseEntry key = new DatabaseEntry();
      DatabaseEntry data = new DatabaseEntry();
      while (c.getNext(key, data, LockMode.DEFAULT) == 
             OperationStatus.SUCCESS) {
        int sik = IntegerBinding.entryToInt(key);
        if (sik > keynum)
          keynum = sik;
        String fou = StringBinding.entryToString(data);
        //System.err.println("++++++++++"+fou+"++++++");
        if (fou.equals(fn))  // found matching fou
          return sik;
      }
      c.close();
    }
    catch (DatabaseException e) {
      logf.println("Error accessing FileTable" + e);
    }
    return (-1*(keynum+1));
  }


}

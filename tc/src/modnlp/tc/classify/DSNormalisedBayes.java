package modnlp.tc.classify;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.evaluation.*;
import modnlp.tc.util.*;
import java.util.Set;
import java.util.Enumeration;
import java.util.Iterator;
/**
 *  Document-size normalised Naive Bayes classifier for documents
 *  represented as Boolean vectors. This class uses the CSV described
 *  in (Sebastiani, 2002).  CSV values are not probabilities but the
 *  function is monotonically increasing on the estimated probability
 *  function. In standard BVBayes, larger documents will tend to get
 *  disproportionally large CSV scores. DSNormalisedBayes tackles this
 *  problem by taking into account the size to the document when
 *  computing CSV.
 *
 *  NB: For aggressively reduced term sets, document-size
 *  normalisation (dsn) doesn't seem to yield any improvement in
 *  classification. On the contrary, it seems to worsen things,
 *  slightly. However, for lightly reduced term sets (say, agrr = 100), dsn 
 *  greatly improves accuracy.
 *
 * Categorise each news item in corpus_list according to categ using
 * Boolean Vector Naive Bayes (see lecture notes ctinduction.pdf, p 7)
 *
 * Usage:
 * <pre>
 DSNormalisedBayes corpus_list categ prob_model 

SYNOPSIS:
  Categorise each news item in corpus_list according to categ using
  Boolean Vector Naive Bayes (see lecture notes ctinduction.pdf, p 7)

ARGUMENTS
 corpus_list: list of files to be classified

 categ: target category (e.g. 'acq'.) The classifier will define CSV 
        as CSV_{categ}

 pmfile: file containing a  probability model generated via, say, 
         modnlp.tc.induction.MakeProbabilityModel.

  </pre>
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: DSNormalisedBayes.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  BVProbabilityModel
 * @see NewsParser
*/

public class DSNormalisedBayes
{
  private CorpusList clist = null;
  private BVProbabilityModel pm = null;
  /** 
   *  Set up the main user interface items
   */
  public  DSNormalisedBayes(String clist, String pmfile) {
    super();
    this.clist = new CorpusList(clist);
    this.pm = (BVProbabilityModel)IOUtil.loadProbabilityModel(pmfile);
    return;
  }

 /**
   * pars: Set up a  new parser object of type <code>plugin</code>, perform
   * parsing, and return a ParsedCorpus
   *
   * @param filename the file to be parsed 
   * @param plugin the parser class name
   * @return a <code>ParsedCorpus</code>
   * @exception Exception if an error occurs
   */
  public ParsedCorpus parse (String filename, String plugin) throws Exception
  {
    Parser np = IOUtil.loadParserPlugin(plugin);
    np.setFilename(filename);
    return np.getParsedCorpus();
  }


  /** CSV_i(d_j) = \sum_0^T tkj log p(t|c) * (1 - p(t|�c) / p(t|�c) * (1 - p(t|c) 
   *
   * (where tkj \in {0, 1} is the binary weight at position k in
   * vector d_j; multiplying by it causes terms that do not occur in
   * the document to be ignored)
   
   */
  public double computeCSV(String cat, ParsedDocument pni){
    double csv = 0;
    boolean barcat = false;
    if ( Tokenizer.isBar(cat) ){
      cat = Tokenizer.disbar(cat);
      barcat = true;
    }
    SetOfWords sov = new SetOfWords(pni.getText());
    for (Iterator e = sov.iterator(); e.hasNext() ; ) {
      String term = (String)e.next();
      if (! pm.containsTerm(term) )
        continue;
      Probabilities p = pm.getProbabilities(term, cat);
      // traversing the set of terms that occur in the docment, so no need to multiply by tkj
      // first compute CSV based on p(d|c)
      csv += Maths.safeLog2((p.getPTgivenC() * (1 - p.getPTgiven_C()))
                            / (p.getPTgiven_C() * (1 - p.getPTgivenC())));
    }
    if ( barcat )
      // CSV based on p(d|�c):
      // if  pcsv =def  p(t|c) * (1 - p(t|�c)) /  p(t|�c) * (1 - p(t|c))  
      // then that's simply log( 1 / pcsv ) = -log(pcsv)
      return -1 * csv/(double) sov.size();
    return csv/(double) sov.size();
  }
  
  public static void main(String[] args) {
    try {
      String clistfn = args[0];
      String category = args[1];
      String pmfile = args[2];
      String tmethod = args[3];
      String parser = args.length > 4 ? args[4] : "NewsParser";
      System.err.println("Loading probability model...");
      DSNormalisedBayes f = new DSNormalisedBayes(clistfn, pmfile);
      CSVTable csvt = new CSVTable(category);
      for (Enumeration e = f.clist.elements(); e.hasMoreElements() ;)
			{
        String fname = (String)e.nextElement();
        System.err.print("\n----- Processing: "+fname+" ------\n");
        ParsedCorpus pt = f.parse(fname,parser);
        int i = 1;
        for (Enumeration g = pt.elements(); g.hasMoreElements() ;){
          PrintUtil.printNoMove("Classifying ...", i++);
          ParsedDocument pni = (ParsedDocument)g.nextElement();
          double csv = f.computeCSV(category, pni);
          csvt.setCSV(pni.getId(), csv);
          if (pni.isOfCategory(category))
            csvt.addToTargetDocSet(pni.getId());
        }
        PrintUtil.donePrinting();
      }

      System.out.println("TARGET DOCS for "+category+": "+csvt.getTargetDocSet());
      System.out.println("CSV RESULTS:\n"+csvt);
      Set selected = csvt.applyThreshold(tmethod, f.pm.getCatGenerality(category));
      System.out.println("SELECTED DOCS:\n"+selected);
      System.out.println("EFFECTIVENESS:\n"
                         +"  accuracy = "+csvt.getAccuracy()
                         +"  precision = "+csvt.getPrecision()
                         +"  recall = "+csvt.getRecall()
                         );
    }
    catch (Exception e){
      System.err.println("USAGE:");
      System.err.println(" DSNormalisedBayes corpus_list categ prob_model threshold\n");
      System.err.println("SYNOPSIS:");
      System.err.println("  Categorise each news item in corpus_list according to categ using");
      System.err.println("  Boolean Vector Naive Bayes (see lecture notes ctinduction.pdf, p 7)\n");
      System.err.println("ARGUMENTS:");
      System.err.println(" corpus_list: list of files to be classified\n");
      System.err.println(" categ: target category (e.g. 'acq'.) The classifier will define CSV ");
      System.err.println("     as CSV_{categ}\n");
      System.err.println(" pmfile: file containing a  probability model generated via, say, ");
      System.err.println("     modnlp.tc.induction.MakeProbabilityModel.\n");
      System.err.println(" threshold: a real number (for RCut thresholding) or the name of a"); 
      System.err.println("     thresholding strategy. Currently supported strategies:");
      System.err.println("      - 'proportional': choose threshold s.t. that g_Tr(ci) is");
      System.err.println("         closest to g_Tv(ci). [DEFAULT]");
      System.err.println("      - ...");
      System.err.println(" PARSER: parser to be used [default: 'news']");
      System.err.println("   see modnlp.tc.parser.* for other options ");
      e.printStackTrace();
    } 
  }
}

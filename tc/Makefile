SRCCLASSPATH=.:../lib/MinML2.jar
TOPCLASSPATH=src:lib/MinML2.jar
SOURCES=modnlp/tc/util/*.java modnlp/tc/dstruct/*.java modnlp/tc/parser/*.java modnlp/tc/tsr/*.java modnlp/tc/evaluation/*.java modnlp/tc/induction/*.java modnlp/tc/classify/*.java 
DOCS=doc
SOURCEDIR=src
DISTDIR=tc-`cat VERSION`
DISTFILES=BUGS COPYING ChangeLog INSTALL Makefile README TODO VERSION AUTHORS\
	compile.sh exclude.txt run.sh doc lib src testdata
#JAVA=jamvm
#JAVAC=jikes-classpath
JAVA=java
JAVAC=javac
JAVAFLAGS=-classpath $(TOPCLASSPATH)
JAVACFLAGS= -classpath $(SRCCLASSPATH)

build:
	cd $(SOURCEDIR) && $(JAVAC) $(JAVACFLAGS) $(SOURCES)

test:
	@echo "Testing TSR..."
	$(JAVA) $(JAVAFLAGS) modnlp.tc.tsr.MakeReducedTermSet testdata/tinycorpus.lst testdata/stopwds.txt 10 InfoGain acq  > /tmp/tinycorpus.tsr
	@echo "** TSR scores saved to /tmp/tinycorpus.tsr"
	@echo "Testing ARFF file generation..."
	$(JAVA) $(JAVAFLAGS) -classpath $(TOPCLASSPATH) modnlp.tc.util.MakeARFF testdata/tinycorpus.lst testdata/stopwds.txt 10 InfoGain acq tfidf > /tmp/tinycorpus.arff
	@echo "** ARFF file (of TFIDF vectors) saved to /tmp/tinycorpus.arff"
	@echo "Testing Probability Model Induction..."
	$(JAVA) $(JAVAFLAGS) -classpath $(TOPCLASSPATH) modnlp.tc.induction.MakeProbabilityModel testdata/tinycorpus.lst testdata/stopwds.txt 10 InfoGain acq /tmp/tinycorpus.pm
	@echo "Probability model saved to /tmp/tinycorpus.pm"
	@echo "Testing Classification..."
	$(JAVA) $(JAVAFLAGS) -classpath $(TOPCLASSPATH) modnlp.tc.classify.BVBayes testdata/tinycorpus.lst acq /tmp/tinycorpus.pm proportional >/tmp/tinycorpus.log
	@echo "** Classification results saved to /tmp/tinycorpus.log"
	@echo "Testing completed. See lines starting with '**' for location of log files."

docs:
	javadoc -d $(DOCS) -sourcepath $(SOURCEDIR) -subpackages modnlp.tc

dist: docs
	mkdir $(DISTDIR)
	cp -a $(DISTFILES) $(DISTDIR)
	tar cfvz /tmp/$(DISTDIR).tar.gz $(DISTDIR) --exclude-from=exclude.txt
	rm -rf $(DISTDIR)

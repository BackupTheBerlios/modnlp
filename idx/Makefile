SRCCLASSPATH=.:../lib/gnu-regexp-1.1.4.jar:../lib/je.jar
TOPCLASSPATH=src:lib/gnu-regexp-1.1.4.jar:lib/je.jar
SOURCES=modnlp/util/*.java modnlp/dstruct/*.java modnlp/idx/database/*.java modnlp/idx/inverted/*.java modnlp/idx/query/*.java modnlp/idx/*.java 
TARGETS=$(subst .java,.class,$(SOURCES))
DOCS=doc
SOURCEDIR=src
DISTDIR=idx-`cat VERSION`
DISTFILES=BUGS COPYING ChangeLog INSTALL Makefile README TODO VERSION AUTHORS\
	exclude.txt doc lib src data
#JAVA=jamvm
#JAVAC=jikes-classpath
JAVA=java
JAVAC=javac
JAR=jar
JAVAFLAGS=-classpath $(TOPCLASSPATH)
JAVACFLAGS= -classpath $(SRCCLASSPATH)

build:
	cd $(SOURCEDIR) && $(JAVAC) $(JAVACFLAGS) $(SOURCES)

jar:
	cd $(SOURCEDIR) && $(JAR) cfv /tmp/idx.jar $(TARGETS)

test:
	@echo "Testing Indexer..."
	-rm -rf /tmp/dbv2/ ; mkdir /tmp/dbv2 
	@echo "** Indexing data/short.lst"
	$(JAVA) $(JAVAFLAGS) modnlp.idx.MakeTECIndex data/short.lst 
	@echo "** Dumping index..."
	$(JAVA) $(JAVAFLAGS) modnlp.idx.Dump 
	@echo "** Testing completed"

docs:
	javadoc -d $(DOCS) -sourcepath $(SOURCEDIR) -subpackages modnlp.idx

dist: docs
	mkdir $(DISTDIR)
	cp -a $(DISTFILES) $(DISTDIR)
	tar cfvz /tmp/$(DISTDIR).tar.gz $(DISTDIR) --exclude-from=exclude.txt
	rm -rf $(DISTDIR)


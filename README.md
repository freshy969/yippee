Yippee!
=========

## A fully distributed search engine

To   say    the   world   wide    web   is   enormous   would    be   an
understatement:)  In  the  last  month  (March  2012),  [Google's  index
size](http://www.worldwidewebsize.com)  was  estimated   to  be  between
forty-five and fifty-five billion pages, only a part of the existing web
universe.

The Yippee  Search Engine  is a distributed  application comprised  of 4
main  components:  a  web  crawler  augmenting  [the  Mercator  pattern]
(http://www.cindoc.csic.es/cybermetrics/pdf/68.pdf) the  web, an indexer
to index  crawled web pages,  our implementation of  [Google's PageRank]
(http://infolab.stanford.edu/~backrub/google.html)  algorithm  ,  and  a
search  module to  retrieve documents  based on  user-provided keywords.
Crawler,  indexer   and  search  components  are   distributed  via  the
FreePastry  peer-to-peer substrate,  while  the  PageRank components  is
centralized. All  parts of  the project are  deployed to  Amazon Elastic
Cloud Computing (EC2) instances.

[![Yippee's
architecture](http://www.seas.upenn.edu/~nvas/architecture.png)]

Contributors:

* Chris Imbriano    -  imbriano@seas.upenn.edu
* Margarita Miranda -  mmiran@seas.upenn.edu
* Nikos Vasilakis   -  nvas@seas.upenn.edu
* TJ Du             -  tdu2@seas.upenn.edu

RestFixtureExtensions
=====================

Contributed extensions of RestFixture.

SlimRestFixtureWithSeq
----------------------

An extension of RestFixture that generates a sequence diagrams for a table fixture. 

Sequence diagrams are generated as SVG files using Patternity Graphic (http://cyrille.martraire.com/2008/12/new-java-api-uml-diagrams/).
 
Each picture can then be transcoded into either PNG or JPG format (via Batik transcoder API: http://xmlgraphics.apache.org/batik/using/transcoder.html) and embedded in the FitNesse page to enhance the content.

RestStriptFixture
-----------------

A different take on RestFixture to format fixtures as Slim Script tables, with an eye to BDD.
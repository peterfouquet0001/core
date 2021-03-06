/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

/* Generated By:JavaCC: Do not edit this line. SPARQLParser.java */
/*
 * (c) Copyright 2004, 2005, 2006, 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 */

package eu.trowl.query.sparql;

import eu.trowl.rdf.*;
import eu.trowl.query.*;
import eu.trowl.vocab.*;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ed
 */
public class SPARQLParser extends SPARQLParserBase implements SPARQLParserConstants {

    /**
     *
     * @throws ParseException
     * @throws QueryTypeException
     * @throws QuerySyntaxException
     */
    final public void CompilationUnit() throws ParseException, QueryTypeException, QuerySyntaxException {
    Query();
    jj_consume_token(0);
  }

    /**
     *
     * @throws ParseException
     * @throws QueryTypeException
     * @throws QuerySyntaxException
     */
    final public void Query() throws ParseException, QueryTypeException, QuerySyntaxException {
    Prologue();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SELECT:
      SelectQuery();
      break;
    case CONSTRUCT:
      ConstructQuery();
      break;
    case DESCRIBE:
      DescribeQuery();
      break;
    case ASK:
      AskQuery();
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  /**
   *
   * @throws ParseException
   */
  final public void Prologue() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BASE:
      BaseDecl();
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PREFIX:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_1;
      }
      PrefixDecl();
    }
  }

  /**
   *
   * @throws ParseException
   */
  final public void BaseDecl() throws ParseException {
                    Node n ;
    jj_consume_token(BASE);
    n = IRI_REF();
    getQuery().setBaseURI(n.getURI()) ;
  }

  /**
   *
   * @throws ParseException
   */
  final public void PrefixDecl() throws ParseException {
                      Token t ; Node n ;
    jj_consume_token(PREFIX);
    t = jj_consume_token(PNAME_NS);
    n = IRI_REF();
        setFixedPrefix(t.image, n.getURI()) ;
  }

// ---- Query type clauses
  /**
   *
   * @throws ParseException
   * @throws QueryTypeException
   * @throws QuerySyntaxException
   */
  final public void SelectQuery() throws ParseException, QueryTypeException, QuerySyntaxException {
                                                                       Node v ;
    jj_consume_token(SELECT);
      getQuery().setQuerySelectType() ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DISTINCT:
    case REDUCED:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DISTINCT:
        jj_consume_token(DISTINCT);
                 getQuery().setDistinct(true);
        break;
      case REDUCED:
        jj_consume_token(REDUCED);
                getQuery().setReduced(true);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR1:
    case VAR2:
      label_2:
      while (true) {
        v = Var();
                  getQuery().getSelectClause().addVariable(v) ;
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case VAR1:
        case VAR2:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_2;
        }
      }
      getQuery().setQueryResultStar(false) ;
      break;
    case STAR:
      jj_consume_token(STAR);
             getQuery().setQueryResultStar(true) ;
      break;
    default:
      jj_la1[6] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FROM:
        ;
        break;
      default:
        jj_la1[7] = jj_gen;
        break label_3;
      }
      DatasetClause();
    }
    WhereClause();
  }

  /**
   *
   * @throws ParseException
   * @throws QueryTypeException
   */
  final public void ConstructQuery() throws ParseException, QueryTypeException {
    jj_consume_token(CONSTRUCT);
      {if (true) throw new QueryTypeException("Construct queries are not currently supported");}
  }

  /**
   *
   * @throws ParseException
   * @throws QueryTypeException
   */
  final public void DescribeQuery() throws ParseException, QueryTypeException {
    jj_consume_token(DESCRIBE);
      {if (true) throw new QueryTypeException("Describe queries are not currently supported");}
  }

  /**
   *
   * @throws ParseException
   * @throws QueryTypeException
   * @throws QuerySyntaxException
   */
  final public void AskQuery() throws ParseException, QueryTypeException, QuerySyntaxException {
    jj_consume_token(ASK);
          getQuery().setQueryAskType() ;
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FROM:
        ;
        break;
      default:
        jj_la1[8] = jj_gen;
        break label_4;
      }
      DatasetClause();
    }
    WhereClause();
  }

// ----
  /**
   *
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void DatasetClause() throws ParseException, QuerySyntaxException {
    jj_consume_token(FROM);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
      DefaultGraphClause();
      break;
    case NAMED:
      NamedGraphClause();
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  /**
   *
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void DefaultGraphClause() throws ParseException, QuerySyntaxException {
                                                          Node n ;
    n = SourceSelector();
    getQuery().getFromClause().addGraphURI(n.getURI()) ;
  }

  /**
   *
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void NamedGraphClause() throws ParseException, QuerySyntaxException {
                                                        Node n ;
    jj_consume_token(NAMED);
    n = SourceSelector();

  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node SourceSelector() throws ParseException, QuerySyntaxException {
                                                      Node n ;
    n = IRIref();
    if ( ! n.isResource() )
        {if (true) throw new QuerySyntaxException("Not a URI: "+n.toString(),
                                      token.beginLine, token.beginColumn) ;}
    {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void WhereClause() throws ParseException, QuerySyntaxException {
                                                   GraphPattern bgp ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case WHERE:
      jj_consume_token(WHERE);
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
    bgp = GroupGraphPattern();
                                          getQuery().getWhereClause().setBasicGraphPattern(bgp) ;
  }

// ---- General Graph Pattern
  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public GraphPattern GroupGraphPattern() throws ParseException, QuerySyntaxException {
                                                                 GraphPattern bgp = null ;
    jj_consume_token(LBRACE);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case BLANK_NODE_LABEL:
    case VAR1:
    case VAR2:
    case TRUE:
    case FALSE:
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
    case STRING_LITERAL1:
    case STRING_LITERAL2:
    case STRING_LITERAL_LONG1:
    case STRING_LITERAL_LONG2:
    case NIL:
    case LBRACKET:
    case ANON:
      bgp = TriplesBlock(null);
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
    jj_consume_token(RBRACE);
      {if (true) return bgp;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @param bgp
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public GraphPattern TriplesBlock(GraphPattern bgp) throws ParseException, QuerySyntaxException {
    if ( bgp == null )
      bgp = new GraphPattern() ;
    TriplesSameSubject(bgp);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case DOT:
      jj_consume_token(DOT);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IRIref:
      case PNAME_NS:
      case PNAME_LN:
      case BLANK_NODE_LABEL:
      case VAR1:
      case VAR2:
      case TRUE:
      case FALSE:
      case INTEGER:
      case DECIMAL:
      case DOUBLE:
      case INTEGER_POSITIVE:
      case DECIMAL_POSITIVE:
      case DOUBLE_POSITIVE:
      case INTEGER_NEGATIVE:
      case DECIMAL_NEGATIVE:
      case DOUBLE_NEGATIVE:
      case STRING_LITERAL1:
      case STRING_LITERAL2:
      case STRING_LITERAL_LONG1:
      case STRING_LITERAL_LONG2:
      case NIL:
      case LBRACKET:
      case ANON:
        TriplesBlock(bgp);
        break;
      default:
        jj_la1[12] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
      {if (true) return bgp ;}
    throw new Error("Missing return statement in function");
  }

// -------- Triple lists with property and object lists
  /**
   *
   * @param acc
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void TriplesSameSubject(GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                          Node s ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case BLANK_NODE_LABEL:
    case VAR1:
    case VAR2:
    case TRUE:
    case FALSE:
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
    case STRING_LITERAL1:
    case STRING_LITERAL2:
    case STRING_LITERAL_LONG1:
    case STRING_LITERAL_LONG2:
    case NIL:
    case ANON:
      s = VarOrTerm();
      PropertyListNotEmpty(s, acc);
      break;
    case LBRACKET:
      // Any of the triple generating syntax elements
        s = TriplesNode(acc);
      PropertyList(s, acc);
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  /**
   *
   * @param s
   * @param acc
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void PropertyListNotEmpty(Node s, GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                                    Node p ;
    p = Verb();
    ObjectList(s, p, acc);
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SEMICOLON:
        ;
        break;
      default:
        jj_la1[15] = jj_gen;
        break label_5;
      }
      jj_consume_token(SEMICOLON);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case IRIref:
      case PNAME_NS:
      case PNAME_LN:
      case VAR1:
      case VAR2:
      case KW_A:
        p = Verb();
        ObjectList(s, p, acc);
        break;
      default:
        jj_la1[16] = jj_gen;
        ;
      }
    }
  }

  /**
   *
   * @param s
   * @param acc
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void PropertyList(Node s, GraphPattern acc) throws ParseException, QuerySyntaxException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case VAR1:
    case VAR2:
    case KW_A:
      PropertyListNotEmpty(s, acc);
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
  }

  /**
   *
   * @param s
   * @param p
   * @param acc
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void ObjectList(Node s, Node p, GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                                 Node o ;
    Object(s, p, acc);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[18] = jj_gen;
        break label_6;
      }
      jj_consume_token(COMMA);
      Object(s, p, acc);
    }
  }

  /**
   *
   * @param s
   * @param p
   * @param acc
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public void Object(Node s, Node p, GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                             Node o ;
    o = GraphNode(acc);
    insert(acc, s, p, o) ;
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node Verb() throws ParseException, QuerySyntaxException {
                                           Node p ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case VAR1:
    case VAR2:
      p = VarOrIRIref();
      break;
    case KW_A:
      jj_consume_token(KW_A);
                                 p = Node.fromURI(RDF.TYPE);
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return p ;}
    throw new Error("Missing return statement in function");
  }

// -------- Triple expansions

// Anything that can stand in a node slot and which is
// a number of triples
  /**
   *
   * @param acc
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node TriplesNode(GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                    Node n ;
    n = BlankNodePropertyList(acc);
                                   {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @param acc
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node BlankNodePropertyList(GraphPattern acc) throws ParseException, QuerySyntaxException {
    jj_consume_token(LBRACKET);
      Node n = createBNode() ;
    PropertyListNotEmpty(n, acc);
    jj_consume_token(RBRACKET);
      {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

// -------- Nodes in a graph pattern or template
  /**
   *
   * @param acc
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node GraphNode(GraphPattern acc) throws ParseException, QuerySyntaxException {
                                                                  Node n ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case BLANK_NODE_LABEL:
    case VAR1:
    case VAR2:
    case TRUE:
    case FALSE:
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
    case STRING_LITERAL1:
    case STRING_LITERAL2:
    case STRING_LITERAL_LONG1:
    case STRING_LITERAL_LONG2:
    case NIL:
    case ANON:
      n = VarOrTerm();
                    {if (true) return n ;}
      break;
    case LBRACKET:
      n = TriplesNode(acc);
                         {if (true) return n ;}
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node VarOrTerm() throws ParseException, QuerySyntaxException {
                                                Node n = null ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR1:
    case VAR2:
      n = Var();
      break;
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
    case BLANK_NODE_LABEL:
    case TRUE:
    case FALSE:
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
    case STRING_LITERAL1:
    case STRING_LITERAL2:
    case STRING_LITERAL_LONG1:
    case STRING_LITERAL_LONG2:
    case NIL:
    case ANON:
      n = GraphTerm();
      break;
    default:
      jj_la1[21] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

// Property (if no bNodes) + DESCRIBE
  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node VarOrIRIref() throws ParseException, QuerySyntaxException {
                                                  Node n = null ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR1:
    case VAR2:
      n = Var();
      break;
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
      n = IRIref();
      break;
    default:
      jj_la1[22] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node Var() throws ParseException, QuerySyntaxException {
                                            Token t ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR1:
      t = jj_consume_token(VAR1);
      break;
    case VAR2:
      t = jj_consume_token(VAR2);
      break;
    default:
      jj_la1[23] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      {if (true) return createVariable(t.image, t.beginLine, t.beginColumn) ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node GraphTerm() throws ParseException, QuerySyntaxException {
                                                 Node n ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
    case PNAME_NS:
    case PNAME_LN:
      n = IRIref();
                 {if (true) return n ;}
      break;
    case STRING_LITERAL1:
    case STRING_LITERAL2:
    case STRING_LITERAL_LONG1:
    case STRING_LITERAL_LONG2:
      n = RDFLiteral();
                     {if (true) return n ;}
      break;
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
      n = NumericLiteral();
                         {if (true) return n ;}
      break;
    case TRUE:
    case FALSE:
      n = BooleanLiteral();
                         {if (true) return n ;}
      break;
    case BLANK_NODE_LABEL:
    case ANON:
      n = BlankNode();
                    {if (true) return n ;}
      break;
    case NIL:
      jj_consume_token(NIL);
          {if (true) return Node.fromURI(RDF.NIL) ;}
      break;
    default:
      jj_la1[24] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node RDFLiteral() throws ParseException, QuerySyntaxException {
                                                  Token t ; String lex = null ;
    lex = String();
    String lang = null ; Node uri = null ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LANGTAG:
    case DATATYPE:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LANGTAG:
        t = jj_consume_token(LANGTAG);
                      lang = t.image.substring(1) ;
        break;
      case DATATYPE:
        jj_consume_token(DATATYPE);
        uri = IRIref();
        break;
      default:
        jj_la1[25] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[26] = jj_gen;
      ;
    }
      {if (true) return makeNode(lex, lang, uri) ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node NumericLiteral() throws ParseException {
                          Node n ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
    case DECIMAL:
    case DOUBLE:
      n = NumericLiteralUnsigned();
      break;
    case INTEGER_POSITIVE:
    case DECIMAL_POSITIVE:
    case DOUBLE_POSITIVE:
      n = NumericLiteralPositive();
      break;
    case INTEGER_NEGATIVE:
    case DECIMAL_NEGATIVE:
    case DOUBLE_NEGATIVE:
      n = NumericLiteralNegative();
      break;
    default:
      jj_la1[27] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return n ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node NumericLiteralUnsigned() throws ParseException {
                                  Token t ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER:
      t = jj_consume_token(INTEGER);
                  {if (true) return makeNodeInteger(t.image) ;}
      break;
    case DECIMAL:
      t = jj_consume_token(DECIMAL);
                  {if (true) return makeNodeDecimal(t.image) ;}
      break;
    case DOUBLE:
      t = jj_consume_token(DOUBLE);
                 {if (true) return makeNodeDouble(t.image) ;}
      break;
    default:
      jj_la1[28] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node NumericLiteralPositive() throws ParseException {
                                  Token t ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_POSITIVE:
      t = jj_consume_token(INTEGER_POSITIVE);
                           {if (true) return makeNodeInteger(t.image) ;}
      break;
    case DECIMAL_POSITIVE:
      t = jj_consume_token(DECIMAL_POSITIVE);
                           {if (true) return makeNodeDecimal(t.image) ;}
      break;
    case DOUBLE_POSITIVE:
      t = jj_consume_token(DOUBLE_POSITIVE);
                          {if (true) return makeNodeDouble(t.image) ;}
      break;
    default:
      jj_la1[29] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node NumericLiteralNegative() throws ParseException {
                                  Token t ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INTEGER_NEGATIVE:
      t = jj_consume_token(INTEGER_NEGATIVE);
                           {if (true) return makeNodeInteger(t.image) ;}
      break;
    case DECIMAL_NEGATIVE:
      t = jj_consume_token(DECIMAL_NEGATIVE);
                           {if (true) return makeNodeDecimal(t.image) ;}
      break;
    case DOUBLE_NEGATIVE:
      t = jj_consume_token(DOUBLE_NEGATIVE);
                          {if (true) return makeNodeDouble(t.image) ;}
      break;
    default:
      jj_la1[30] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node BooleanLiteral() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TRUE:
      jj_consume_token(TRUE);
           {if (true) return Node.fromURI(XSD.TRUE);}
      break;
    case FALSE:
      jj_consume_token(FALSE);
            {if (true) return Node.fromURI(XSD.FALSE);}
      break;
    default:
      jj_la1[31] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public String String() throws ParseException {
                    Token t ; String lex ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRING_LITERAL1:
      t = jj_consume_token(STRING_LITERAL1);
                            lex = stripQuotes(t.image) ;
      break;
    case STRING_LITERAL2:
      t = jj_consume_token(STRING_LITERAL2);
                            lex = stripQuotes(t.image) ;
      break;
    case STRING_LITERAL_LONG1:
      t = jj_consume_token(STRING_LITERAL_LONG1);
                                 lex = stripQuotes3(t.image) ;
      break;
    case STRING_LITERAL_LONG2:
      t = jj_consume_token(STRING_LITERAL_LONG2);
                                 lex = (t.image) ;
      break;
    default:
      jj_la1[32] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
      lex = unescapeString(lex, t.beginLine, t.beginColumn) ;
      {if (true) return lex ;}
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node IRIref() throws ParseException, QuerySyntaxException {
                                              Node n ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IRIref:
      n = IRI_REF();
                  {if (true) return n ;}
      break;
    case PNAME_NS:
    case PNAME_LN:
      n = PrefixedName();
                       {if (true) return n ;}
      break;
    default:
      jj_la1[33] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   * @throws QuerySyntaxException
   */
  final public Node PrefixedName() throws ParseException, QuerySyntaxException {
                                                    Token t ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PNAME_LN:
      t = jj_consume_token(PNAME_LN);
      {if (true) return createNodeFromPrefixedName(t.image, t.beginLine, t.beginColumn) ;}
      break;
    case PNAME_NS:
      t = jj_consume_token(PNAME_NS);
      {if (true) return createNodeFromPrefixedName(t.image, t.beginLine, t.beginColumn) ;}
      break;
    default:
      jj_la1[34] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node BlankNode() throws ParseException {
                     Token t = null ;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case BLANK_NODE_LABEL:
      t = jj_consume_token(BLANK_NODE_LABEL);
      {if (true) return createBNode(t.image, t.beginLine, t.beginColumn) ;}
      break;
    case ANON:
      jj_consume_token(ANON);
           {if (true) return createBNode() ;}
      break;
    default:
      jj_la1[35] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /**
   *
   * @return
   * @throws ParseException
   */
  final public Node IRI_REF() throws ParseException {
                   Token t ;
    t = jj_consume_token(IRIref);
    {if (true) try {
                return createNodeFromURI(t.image, t.beginLine, t.beginColumn);
            } catch (QuerySyntaxException ex) {
                Logger.getLogger(SPARQLParser.class.getName()).log(Level.SEVERE, null, ex);
            }}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public SPARQLParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[36];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static private int[] jj_la1_3;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
      jj_la1_init_2();
      jj_la1_init_3();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x3900000,0x40000,0x80000,0x600000,0x600000,0x3000,0x3000,0x0,0x0,0x700,0x0,0x3f00,0x3f00,0x0,0x3f00,0x0,0x23700,0x23700,0x0,0x23700,0x3f00,0x3f00,0x3700,0x3000,0xf00,0x4000,0x4000,0x0,0x0,0x0,0x0,0x0,0x0,0x700,0x600,0x800,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2,0x2,0x1,0x4,0x7fd80000,0x7fd80000,0x0,0x7fd80000,0x0,0x0,0x0,0x0,0x0,0x7fd80000,0x7fd80000,0x0,0x0,0x7fd80000,0x0,0x0,0x7fc00000,0x1c00000,0xe000000,0x70000000,0x180000,0x0,0x0,0x0,0x0,};
   }
   private static void jj_la1_init_2() {
      jj_la1_2 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x80000000,0x0,0x0,0x0,0x0,0x5278,0x5278,0x20000,0x5278,0x8000,0x0,0x0,0x10000,0x0,0x5278,0x4278,0x0,0x0,0x4278,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x78,0x0,0x0,0x4000,};
   }
   private static void jj_la1_init_3() {
      jj_la1_3 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x2,0x2,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,};
   }

   /** Constructor with InputStream.
    * @param stream
    */
  public SPARQLParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding
   * @param stream
   * @param encoding
   */
  public SPARQLParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new JavaCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new SPARQLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  /** Reinitialise.
   * @param stream
   */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise.
   * @param stream
   * @param encoding
   */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  /** Constructor.
   * @param stream
   */
  public SPARQLParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new SPARQLParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  /** Reinitialise.
   * @param stream
   */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager.
   * @param tm
   */
  public SPARQLParser(SPARQLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  /** Reinitialise.
   * @param tm
   */
  public void ReInit(SPARQLParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 36; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


  /** Get the next Token.
   * @return
   */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  /** Get the specific Token.
   * @param index
   * @return
   */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List jj_expentries = new java.util.ArrayList();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException.
   * @return
   */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[106];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 36; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
          if ((jj_la1_2[i] & (1<<j)) != 0) {
            la1tokens[64+j] = true;
          }
          if ((jj_la1_3[i] & (1<<j)) != 0) {
            la1tokens[96+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 106; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}

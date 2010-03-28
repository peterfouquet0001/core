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
 * along with TrOWL .  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 University of Aberdeen
 */

package eu.trowl.owl.rel.reasoner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLDataRange;
import org.semanticweb.owl.model.OWLDescription;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.OWLSubClassAxiom;

import eu.trowl.owl.rel.classify.ELCClassifier;
import eu.trowl.owl.rel.factory.ELCOntologyFactory;
import eu.trowl.owl.rel.model.Atomic;
import eu.trowl.owl.rel.model.Basic;
import eu.trowl.owl.rel.model.Description;
import eu.trowl.owl.rel.model.ELCQOntology;
import eu.trowl.owl.rel.model.Individual;
import eu.trowl.owl.rel.model.Role;
import eu.trowl.owl.rel.model.Singleton;


/** 
 * @author Yuan Ren
 * @version 2010-03-27
 */
public class RELReasoner implements OWLReasoner{
	private ELCQOntology elcontology;
	private final OWLOntologyManager manager;
	private final OWLDataFactory factory;
	private ELCOntologyFactory elcfactory = null;
	public ArrayList<OWLIndividual> inconsistentIndividuals = new ArrayList<OWLIndividual>();
	ELCClassifier classifier = new ELCClassifier();
	private Set<OWLOntology> ontologies = null;

	public RELReasoner(OWLOntologyManager manager) {
		super();
		this.manager = manager;
		this.factory = manager.getOWLDataFactory();
	}

	public void loadOntology()
	{
		elcfactory = new ELCOntologyFactory(ontologies, manager);
		elcfactory.createbuilder();
		this.elcontology = elcfactory.createELOntology();	
	}

	public ELCQOntology getOntology()
	{
		return elcontology;
	}

	private Description getDescription(OWLDescription desc){
		if(elcontology.classID.get(desc)!=null)
			return elcontology.descriptions.get(elcontology.classID.get(desc));
		return null;
	}

	private Role getRole(OWLObjectPropertyExpression role){
		if(elcontology.roleID.get(role)!=null)
			return elcontology.roles.get(elcontology.roleID.get(role));
		return null;
	}

	private Role getRole(OWLDataPropertyExpression role){
		OWLObjectProperty objrole = factory.getOWLObjectProperty(role.asOWLDataProperty().getURI());
		if(elcontology.roleID.get(objrole)!=null)
			return elcontology.roles.get(elcontology.roleID.get(objrole));
		return null;
	}

	public Individual getIndividual(OWLIndividual indi){
		if(elcontology.individualID.get(indi)!=null)
			return elcontology.individuals.get(elcontology.individualID.get(indi));
		return null;
	}


	public HashSet<OWLClass> getsubsumer(OWLClass concept) {
		// TODO Auto-generated method stub
		HashSet<OWLClass> subsumers = new HashSet<OWLClass>();
		Atomic elpconcept = (Atomic) elcontology.descriptions.get(elcontology.classID.get(concept));
		Atomic bot = (Atomic) elcontology.descriptions.get(0);
		if(elpconcept.subsumers.contains(bot)){
			for(Basic subsumer:elcontology.allconcepts)
				if(subsumer.original && subsumer instanceof Atomic)
					subsumers.add(factory.getOWLClass(((Atomic) subsumer).uri));
			subsumers.add(factory.getOWLNothing());
		}
		else
			for(Basic subsumer:elpconcept.subsumers)
				if(subsumer.original && subsumer instanceof Atomic)
					subsumers.add(factory.getOWLClass(((Atomic) subsumer).uri));
		return subsumers;
	}

	public HashSet<OWLClass> getClasses(OWLIndividual individual){
		HashSet<OWLClass> classes = new HashSet<OWLClass>();
		Individual indi = elcontology.individuals.get(elcontology.individualID.get(individual));
		Atomic bot = (Atomic) elcontology.descriptions.get(0);
		Singleton single = indi.singleton;
		if(single.subsumers.contains(bot)){
			for(Basic classifier:elcontology.allconcepts)
				if(classifier.original && classifier instanceof Atomic)
					classes.add(factory.getOWLClass(((Atomic) classifier).uri));				
			classes.add(factory.getOWLNothing());
		}
		else for(Basic classifier:single.subsumers)
			if(classifier.original && classifier instanceof Atomic)
				classes.add(factory.getOWLClass(((Atomic) classifier).uri));
		return classes;
	}

	public HashSet<OWLIndividual> getIndividuals(OWLClass concept){
		HashSet<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		Atomic elpconcept = (Atomic) elcontology.descriptions.get(elcontology.classID.get(concept));
		Atomic bot = (Atomic) elcontology.descriptions.get(0);
		for(Individual indi:elcontology.individuals.values())
		{
			Singleton single = indi.singleton;
			if(single.subsumers.contains(bot) || single.subsumers.contains(elpconcept))
				individuals.add(factory.getOWLIndividual(indi.uri));
		}
		return individuals;
	}

	public boolean isoriginal(OWLClass concept) {
		// TODO Auto-generated method stub
		Basic elpconcept = (Basic) getDescription(concept);
		if(elpconcept != null)
			return elpconcept.original;
		return false;
	}

	public boolean entail(OWLSubClassAxiom axiom){
		Description sub = getDescription(axiom.getSubClass());
		Description sup = getDescription(axiom.getSuperClass());
		Atomic bot = (Atomic) elcontology.descriptions.get(0);

		ELCClassifier classifier = new ELCClassifier();
		classifier.ontology = elcontology;
		boolean answer = false;
		if(sub != null && sup != null && sub instanceof Basic && sup instanceof Basic)
		{
			Basic bsub = (Basic) sub;
			Basic bsup = (Basic) sup;
			answer = (bsub.subsumers.contains(bsup) || bsub.subsumers.contains(bot));
		}
		return answer;
	}


	public int getrelations(OWLObjectPropertyExpression property) {
		// TODO Auto-generated method stub
		int num = 0;
		Role role = elcontology.roles.get(elcontology.roleID.get(property));
		for(Entry<Basic, HashSet<Basic>> entry:role.Relations.entrySet())
			if(entry.getKey() instanceof Singleton)
				for(Basic basic:entry.getValue())
					if(basic instanceof Singleton)
						num++;
		return num;
	}



	@Override
	public boolean isConsistent(OWLOntology arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		return elcontology.consistency;
	}

	@Override
	public void classify() throws OWLReasonerException {
		// TODO Auto-generated method stub
		classifier.ontology = elcontology;
		classifier.tBoxCompletion();
	}

	@Override
	public boolean isClassified() throws OWLReasonerException {
		// TODO Auto-generated method stub
		return elcontology.classified;
	}

	@Override
	public boolean isDefined(OWLClass owlclass) throws OWLReasonerException {
		// TODO Auto-generated method stub
		if(getDescription(owlclass) == null)
			return false;
		return true;
	}

	@Override
	public boolean isDefined(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		if(getRole(arg0) == null)
			return false;
		return true;
	}

	@Override
	public boolean isDefined(OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		if(getRole(arg0) != null)
			return true;
		return false;
	}

	@Override
	public boolean isDefined(OWLIndividual arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		if(getIndividual(arg0) != null)
			return true;
		return false;
	}

	@Override
	public boolean isRealised() throws OWLReasonerException {
		// TODO Auto-generated method stub
		return false;
		// todo
	}

	@Override
	public void realise() throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
	}

	@Override
	public Set<Set<OWLClass>> getAncestorClasses(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		Set<Set<OWLClass>>	ancestors = new HashSet<Set<OWLClass>>();
		Description desc = getDescription(arg0);
		if(desc != null)
		{
			if(desc instanceof Atomic)
			{
				HashSet<Basic> subsumers = new HashSet<Basic>();
				for(Basic sub:((Atomic)desc).subsumers)
					subsumers.add(sub);
				while(subsumers.size()>0)
				{
					Basic sub = subsumers.iterator().next();
					subsumers.removeAll(sub.equivalence);
					subsumers.remove(sub);
					HashSet<OWLClass> ancestor = new HashSet<OWLClass>();
					for(Basic eq:sub.equivalence)
						if(eq instanceof Atomic && ((Atomic)eq).original)
							ancestor.add(factory.getOWLClass(((Atomic)eq).uri));							
					if(ancestor.size() > 0)
						ancestors.add(ancestor);
				}
			}
		}
		return ancestors;
	}

	@Override
	public Set<Set<OWLClass>> getDescendantClasses(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		Set<Set<OWLClass>>	descendants = new HashSet<Set<OWLClass>>();
		Description desc = getDescription(arg0);
		if(desc != null)
		{
			if(desc instanceof Atomic)
			{
				HashSet<Basic> subsumees = new HashSet<Basic>();
				for(Basic sub:elcontology.allconcepts)
					if(sub.subsumers.contains((Atomic)desc))
						subsumees.add(sub);
				while(subsumees.size()>0)
				{
					Basic sub = subsumees.iterator().next();
					subsumees.removeAll(sub.equivalence);
					HashSet<OWLClass> descendant = new HashSet<OWLClass>();
					for(Basic eq:sub.equivalence)
						if(eq instanceof Atomic && ((Atomic)eq).original)
							descendant.add(factory.getOWLClass(((Atomic)eq).uri));							
					if(descendant.size() > 0)
						descendants.add(descendant);
				}
			}
		}
		return descendants;
	}

	@Override
	public Set<OWLClass> getEquivalentClasses(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		HashSet<OWLClass> equivalence = new HashSet<OWLClass>();
		Description desc = getDescription(arg0);
		if(desc != null)
			if(desc instanceof Atomic)
				for(Basic eq:((Atomic)desc).equivalence)
					if(eq instanceof Atomic && eq.original)
						equivalence.add(factory.getOWLClass(((Atomic)eq).uri));
		return equivalence;
	}

	@Override
	public Set<OWLClass> getInconsistentClasses() throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<Set<OWLClass>> getSubClasses(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLClass>>	descendants = new HashSet<Set<OWLClass>>();
		Description desc = getDescription(arg0);
		if(desc != null)
		{
			if(desc instanceof Atomic)
			{
				Atomic atom = (Atomic)desc;
				HashSet<Basic> subsumees = new HashSet<Basic>();
				for(Basic sub:elcontology.allconcepts)
					if(!subsumees.contains(sub) && sub.subsumers.contains(atom) && sub.subsumers.size() == atom.subsumers.size()+1)
						subsumees.addAll(sub.equivalence);
				while(subsumees.size()>0)
				{
					Basic sub = subsumees.iterator().next();
					subsumees.removeAll(sub.equivalence);
					HashSet<OWLClass> descendant = new HashSet<OWLClass>();
					for(Basic eq:sub.equivalence)
						if(eq instanceof Atomic && ((Atomic)eq).original)
							descendant.add(factory.getOWLClass(((Atomic)eq).uri));							
					if(descendant.size() > 0)
						descendants.add(descendant);
				}
			}
		}
		return descendants;
	}

	@Override
	public Set<Set<OWLClass>> getSuperClasses(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		Set<Set<OWLClass>>	ancestors = new HashSet<Set<OWLClass>>();
		Description desc = getDescription(arg0);
		if(desc != null)
		{
			if(desc instanceof Atomic)
			{
				Atomic atom = (Atomic)desc;
				HashSet<Basic> subsumers = new HashSet<Basic>();
				for(Basic sub:atom.subsumers)
					if(!subsumers.contains(sub) && sub.subsumers.size()+1 == atom.subsumers.size())
						subsumers.addAll(sub.equivalence);
				while(subsumers.size()>0)
				{
					Basic sub = subsumers.iterator().next();
					subsumers.removeAll(sub.equivalence);
					HashSet<OWLClass> ancestor = new HashSet<OWLClass>();
					for(Basic eq:sub.equivalence)
						if(eq instanceof Atomic && ((Atomic)eq).original)
							ancestor.add(factory.getOWLClass(((Atomic)eq).uri));							
					if(ancestor.size() > 0)
						ancestors.add(ancestor);
				}
			}
		}
		return ancestors;
	}

	@Override
	public boolean isEquivalentClass(OWLDescription arg0, OWLDescription arg1)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		OWLSubClassAxiom axiom1 = factory.getOWLSubClassAxiom(arg0, arg1);
		OWLSubClassAxiom axiom2 = factory.getOWLSubClassAxiom(arg1, arg0);
		return entail(axiom1) && entail(axiom2);
	}

	@Override
	public boolean isSubClassOf(OWLDescription arg0, OWLDescription arg1)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		OWLSubClassAxiom axiom1 = factory.getOWLSubClassAxiom(arg0, arg1);
		return entail(axiom1);
	}

	@Override
	public boolean isSatisfiable(OWLDescription arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		OWLSubClassAxiom axiom1 = factory.getOWLSubClassAxiom(arg0, factory.getOWLNothing());
		return !entail(axiom1);
	}

	@Override
	public Map<OWLDataProperty, Set<OWLConstant>> getDataPropertyRelationships(
			OWLIndividual arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<OWLIndividual> getIndividuals(OWLDescription arg0, boolean arg1)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// now only work for named concepts
		// todo
		if(arg0 instanceof OWLClass)
			return getIndividuals(arg0.asOWLClass());
		return null;
	}

	@Override
	public Map<OWLObjectProperty, Set<OWLIndividual>> getObjectPropertyRelationships(
			OWLIndividual arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<OWLIndividual> getRelatedIndividuals(OWLIndividual arg0,
			OWLObjectPropertyExpression arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<OWLConstant> getRelatedValues(OWLIndividual arg0,
			OWLDataPropertyExpression arg1) throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	//	@Override
	public Set<Set<OWLClass>> getTypes(OWLIndividual arg0, boolean arg1)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Individual indi = getIndividual(arg0);
		if(indi == null)
			return null;
		Singleton single = indi.singleton;
		Set<Set<OWLClass>> types = new HashSet<Set<OWLClass>>();
		Atomic bot = (Atomic) elcontology.descriptions.get(0);
		HashSet<Basic> classes = new HashSet<Basic>();
		if(single.subsumers.contains(bot))
			classes = elcontology.allconcepts;
		else
			classes = single.subsumers;
		while(classes.size()>0)
		{
			Basic sub = classes.iterator().next();
			classes.removeAll(sub.equivalence);
			HashSet<OWLClass> type = new HashSet<OWLClass>();
			for(Basic eq:sub.equivalence)
				if(eq instanceof Atomic && ((Atomic)eq).original)
					type.add(factory.getOWLClass(((Atomic)eq).uri));							
			if(type.size() > 0)
				types.add(type);
		}
		return types;
	}

	@Override
	public boolean hasDataPropertyRelationship(OWLIndividual arg0,
			OWLDataPropertyExpression arg1, OWLConstant arg2)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean hasObjectPropertyRelationship(OWLIndividual arg0,
			OWLObjectPropertyExpression arg1, OWLIndividual arg2)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean hasType(OWLIndividual arg0, OWLDescription arg1, boolean arg2)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		OWLSubClassAxiom axiom = factory.getOWLSubClassAxiom(factory.getOWLObjectOneOf(arg0), arg1);
		return entail(axiom);
	}

	@Override
	public Set<Set<OWLObjectProperty>> getAncestorProperties(
			OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLObjectProperty>>	ancestors = new HashSet<Set<OWLObjectProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumers = new HashSet<Role>();
			for(Role sub:role.subsumers)
				subsumers.add(sub);
			while(subsumers.size()>0)
			{
				Role sub = subsumers.iterator().next();
				subsumers.removeAll(sub.equivalence);
				HashSet<OWLObjectProperty> ancestor = new HashSet<OWLObjectProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLObjectProperty(eq.uri));							
				if(ancestor.size() > 0)
					ancestors.add(ancestor);
			}
		}
		return ancestors;
	}

	@Override
	public Set<Set<OWLDataProperty>> getAncestorProperties(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLDataProperty>>	ancestors = new HashSet<Set<OWLDataProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumers = new HashSet<Role>();
			for(Role sub:role.subsumers)
				subsumers.add(sub);
			while(subsumers.size()>0)
			{
				Role sub = subsumers.iterator().next();
				subsumers.removeAll(sub.equivalence);
				HashSet<OWLDataProperty> ancestor = new HashSet<OWLDataProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLDataProperty(eq.uri));							
				if(ancestor.size() > 0)
					ancestors.add(ancestor);
			}
		}
		return ancestors;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getDescendantProperties(
			OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLObjectProperty>>	descendants = new HashSet<Set<OWLObjectProperty>>();
		Role desc = getRole(arg0);
		if(desc != null)
		{
			HashSet<Role> subsumees = new HashSet<Role>();
			for(Role sub:elcontology.roles.values())
				if(sub.subsumers.contains(desc))
					subsumees.add(sub);
			while(subsumees.size()>0)
			{
				Role sub = subsumees.iterator().next();
				subsumees.removeAll(sub.equivalence);
				HashSet<OWLObjectProperty> descendant = new HashSet<OWLObjectProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						descendant.add(factory.getOWLObjectProperty(eq.uri));							
				if(descendant.size() > 0)
					descendants.add(descendant);
			}
		}
		return descendants;
	}

	@Override
	public Set<Set<OWLDataProperty>> getDescendantProperties(
			OWLDataProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLDataProperty>>	descendants = new HashSet<Set<OWLDataProperty>>();
		Role desc = getRole(arg0);
		if(desc != null)
		{
			HashSet<Role> subsumees = new HashSet<Role>();
			for(Role sub:elcontology.roles.values())
				if(sub.subsumers.contains(desc))
					subsumees.add(sub);
			while(subsumees.size()>0)
			{
				Role sub = subsumees.iterator().next();
				subsumees.removeAll(sub.equivalence);
				HashSet<OWLDataProperty> descendant = new HashSet<OWLDataProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						descendant.add(factory.getOWLDataProperty(eq.uri));							
				if(descendant.size() > 0)
					descendants.add(descendant);
			}
		}
		return descendants;
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<Set<OWLDescription>> getDomains(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<OWLObjectProperty> getEquivalentProperties(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		HashSet<OWLObjectProperty> equivalence = new HashSet<OWLObjectProperty>();
		Role desc = getRole(arg0);
		if(desc != null)
		{
			for(Role eq:desc.equivalence)
				if(eq.original)
					equivalence.add(factory.getOWLObjectProperty(eq.uri));
		}
		return equivalence;
	}

	@Override
	public Set<OWLDataProperty> getEquivalentProperties(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		HashSet<OWLDataProperty> equivalence = new HashSet<OWLDataProperty>();
		Role desc = getRole(arg0);
		if(desc != null)
		{
			for(Role eq:desc.equivalence)
				if(eq.original)
					equivalence.add(factory.getOWLDataProperty(eq.uri));
		}
		return equivalence;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getInverseProperties(
			OWLObjectProperty arg0) throws OWLReasonerException {
		// TODO Auto-generated method stub
		Role role = getRole(arg0);
		if(role != null)
		{
			Set<Set<OWLObjectProperty>> inverse = new HashSet<Set<OWLObjectProperty>>();;
			Set<OWLObjectProperty> inv = new HashSet<OWLObjectProperty>();
			for(Role invrole:role.inverse.equivalence)
				if(invrole.original)
					inv.add(factory.getOWLObjectProperty(invrole.uri));
			if(inv.size() > 0)
				inverse.add(inv);
			return inverse;
		}
		return null;
	}

	@Override
	public Set<OWLDescription> getRanges(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<OWLDataRange> getRanges(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return null;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSubProperties(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLObjectProperty>>	descendants = new HashSet<Set<OWLObjectProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumees = new HashSet<Role>();
			for(Role sub:elcontology.roles.values())
				if(!subsumees.contains(sub) && sub.subsumers.contains(role) && sub.subsumers.size() == role.subsumers.size()+1)
					subsumees.addAll(sub.equivalence);
			while(subsumees.size()>0)
			{
				Role sub = subsumees.iterator().next();
				subsumees.removeAll(sub.equivalence);
				HashSet<OWLObjectProperty> descendant = new HashSet<OWLObjectProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						descendant.add(factory.getOWLObjectProperty(eq.uri));							
				if(descendant.size() > 0)
					descendants.add(descendant);
			}
		}
		return descendants;
	}

	@Override
	public Set<Set<OWLDataProperty>> getSubProperties(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLDataProperty>>	descendants = new HashSet<Set<OWLDataProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumees = new HashSet<Role>();
			for(Role sub:elcontology.roles.values())
				if(!subsumees.contains(sub) && sub.subsumers.contains(role) && sub.subsumers.size() == role.subsumers.size()+1)
					subsumees.addAll(sub.equivalence);
			while(subsumees.size()>0)
			{
				Role sub = subsumees.iterator().next();
				subsumees.removeAll(sub.equivalence);
				HashSet<OWLDataProperty> descendant = new HashSet<OWLDataProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						descendant.add(factory.getOWLDataProperty(eq.uri));							
				if(descendant.size() > 0)
					descendants.add(descendant);
			}
		}
		return descendants;
	}

	@Override
	public Set<Set<OWLObjectProperty>> getSuperProperties(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLObjectProperty>>	ancestors = new HashSet<Set<OWLObjectProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumers = new HashSet<Role>();
			for(Role sub:role.subsumers)
				if(!subsumers.contains(sub) && sub.subsumers.size()+1 == role.subsumers.size())
					subsumers.add(sub);
			while(subsumers.size()>0)
			{
				Role sub = subsumers.iterator().next();
				subsumers.removeAll(sub.equivalence);
				HashSet<OWLObjectProperty> ancestor = new HashSet<OWLObjectProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLObjectProperty(eq.uri));							
				if(ancestor.size() > 0)
					ancestors.add(ancestor);
			}
		}
		return ancestors;
	}

	@Override
	public Set<Set<OWLDataProperty>> getSuperProperties(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Set<Set<OWLDataProperty>>	ancestors = new HashSet<Set<OWLDataProperty>>();
		Role role = getRole(arg0);
		if(role != null)
		{
			HashSet<Role> subsumers = new HashSet<Role>();
			for(Role sub:role.subsumers)
				if(!subsumers.contains(sub) && sub.subsumers.size()+1 == role.subsumers.size())
					subsumers.add(sub);
			while(subsumers.size()>0)
			{
				Role sub = subsumers.iterator().next();
				subsumers.removeAll(sub.equivalence);
				HashSet<OWLDataProperty> ancestor = new HashSet<OWLDataProperty>();
				for(Role eq:sub.equivalence)
					if(eq.original)
						ancestor.add(factory.getOWLDataProperty(eq.uri));							
				if(ancestor.size() > 0)
					ancestors.add(ancestor);
			}
		}
		return ancestors;
	}

	@Override
	public boolean isAntiSymmetric(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean isFunctional(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Role role = getRole(arg0);
		if(role != null)
			return role.functional;
		return false;
	}

	@Override
	public boolean isFunctional(OWLDataProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Role role = getRole(arg0);
		if(role != null)
			return role.functional;
		return false;
	}

	@Override
	public boolean isInverseFunctional(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Role role = getRole(arg0);
		if(role != null && role.inverse != null)
			return role.inverse.functional;
		return false;
	}

	@Override
	public boolean isIrreflexive(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean isReflexive(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean isSymmetric(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
		return false;
	}

	@Override
	public boolean isTransitive(OWLObjectProperty arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		Role role = getRole(arg0);
		if(role != null)
			return role.transitive;
		return false;
	}

	@Override
	public void clearOntologies() throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
	}

	@Override
	public void dispose() throws OWLReasonerException {
		// TODO Auto-generated method stub
		// todo
	}

	@Override
	public Set<OWLOntology> getLoadedOntologies() {
		// TODO Auto-generated method stub
		return ontologies;
	}

	@Override
	public void loadOntologies(Set<OWLOntology> ontologies)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		this.ontologies = ontologies;
		loadOntology();
	}

	@Override
	public void unloadOntologies(Set<OWLOntology> arg0)
	throws OWLReasonerException {
		// TODO Auto-generated method stub
		ontologies.removeAll(arg0);
		loadOntologies(ontologies);
	}


}

package com.company.invocation;

import com.company.entities.Personne;
import com.company.dao.DataManager;
import com.company.exception.UncallableMethodException;

import java.lang.reflect.Method;

/**
 * Objet "invocateur" qu'on associe � un objet, objet sur lequel on
 * appelera dynamiquement des m�thodes � partir de leurs nom et
 * param�tres.
 */
public class Invocation {

    /**
     * L'objet sur lequel on appelera les m�thodes
     */ 
    protected Object cible;

    /**
     * Invoque dynamiquement une m�thode sur l'objet cible
     * @param nomMethode le nom de la m�thode
     * @param types la liste des types des param�tres. Note : on ne
     * d�termine pas automatiquement la liste des types � partir
     * de la liste des param�tres puisqu'il faut pouvoir
     * explicitement diff�rencier, par exemple, le cas entre la
     * classe "Integer" et le type primitif "int" (repr�sent�
     * alors par la valeur Integer.TYPE) et qu'on aura dans les 2 cas
     * une instance de la classe Integer en tant que param�tre.
     * @param params la liste des param�tres
     * @exception Exception deux cas sont � diff�rencier : <ol>
     * <li>Un probl�me a eu lieu pour appeler la m�thode
     * (param�tres non corrects, acc�s interdit, pas de
     * m�thode de ce nom l� ...) une exception
     * <code>UncallableMethodException</code> est alors lev�e</li>
     * <li>L'appel a bien eu lieu correctement mais la m�thode a
     * lev� une exception, cette exception est alors lev�e �
     * nouveau (d'o� le type g�n�rique <code>Exception</code>
     * dans la signature de la m�thode puisqu'on ne peut lever
     * n'importe quelle exception)</li></ol>.
     */
    public Object invoquer(String nomMethode, Class types[], Object params[]) throws Exception
    {
	
	Method met = null;
	Object result = null;
	Class cl;
	
	// recupere la classe de l'objet a invoquer
	cl = cible.getClass();

	// recupere la methode a appeler si elle existe
	// sinon renvoie une exception
	try {    
	    met = cl.getMethod(nomMethode, types);
	}
	catch (NoSuchMethodException e) {
	    throw new UncallableMethodException("Methode inexistante ["+e.getMessage()+"]");
	}

	try {
	    // la m�thode est trouv�e, on l'invoque dynamiquement
	    result =  met.invoke(cible, params);
	}
	catch(IllegalAccessException e) {
	    throw new UncallableMethodException("Acces interdit a la methode ["+
						e.getMessage()+"]");
	}
	catch(java.lang.reflect.InvocationTargetException e) {
	    // la methode a leve une exception, on la leve 
	    //System.out.println("Exception levee : "+e.getCause()+" type = "+e.getCause().getClass());
	    throw (Exception)e.getCause();	    
	}
	
	return result;	
    }

    /**
     * @param cible l'objet sur lequel on appelera les m�thodes
     */ 
    public Invocation(Object cible)
    {
	this.cible = cible;
    }

    // petit programme de test
    public static void main(String argv[])
    {
	try {
	    Invocation invoc = new Invocation(new DataManager());
	    String nom = "addPersonne";
	    Personne p = new Personne(14, "toto");
	    Object param[] = { p };
	    Class types[] = { p.getClass() };
	    
	    Object res = invoc.invoquer(nom, types, param);
	    System.out.println(" Resultat : "+ res);
	    
	    param = new Object[1];
	    param[0] = new Integer(2);
	    types = new Class[1];
	    types[0] = Integer.TYPE;
	    res = invoc.invoquer("getPersonne", types, param);
	    System.out.println(" Resultat : "+ res);   
	}
	catch (Exception e) {
	    System.err.println("Erreur : "+e);
	}
    }
}

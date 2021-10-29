package com.company.rmi.server;

import com.company.dao.DataManager;
import com.company.entities.Personne;
import com.company.invocation.Invocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DataServImpl extends UnicastRemoteObject implements DataServInterface{

    private Invocation invocation;

    protected DataServImpl() throws RemoteException {
        super();
        this.invocation = new Invocation( new DataManager());
    }

    @Override
    public String ajouterPersonne(Personne p) throws Exception, RemoteException {
        try{
            String s1 = "La personne a bien été ajoutée et son id est ";
            Object param[] = { p };
            Class types[] = { p.getClass() };
            Object res = this.invocation.invoquer("addPersonne", types, param);
            return (s1 + res);
        }catch (Exception e){
            return "Une erreur s'est produite";
        }
    }

    @Override
    public String rechercheParId(int id) throws RemoteException {
        try{
            String s1 = "La personne existe et ses informations sont ";
            String s2 = "La personne n'existe pas";

            Object param[] = { id };
            Class types[] = { Integer.TYPE };
            Personne res = (Personne) this.invocation.invoquer("getPersonne", types, param);

            return s1 + " nom : " + res.getNom() + " age : " + res.getAge();

        }catch (Exception e){
            return "Une erreur s'est produite";
        }
    }

    @Override
    public String rechercheParPersonne(Personne p) throws RemoteException {
        String s1 = "La personne existe et son id est ";
        String s2 = "La personne n'existe pas";
        return null;
    }
}

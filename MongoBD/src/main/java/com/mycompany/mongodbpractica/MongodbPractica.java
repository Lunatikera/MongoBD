package com.mycompany.mongodbpractica;

import dao.PersonaDAO;
import modelo.Persona;


public class MongodbPractica {

    public static void main(String[] args) {
        
        PersonaDAO personaDAO = new PersonaDAO();
        
        for(Persona persona : personaDAO.ObtenerPersonas())
        {
            System.out.println(persona.toString());
        }

        for(Persona persona : personaDAO.ordernarPersonas())
        {
            System.out.println(persona.toString());
        }
        

        Persona nuevaPersona = new Persona("roberto", 19, "masculino");
        
        personaDAO.crearPersona(nuevaPersona);
        
        for(Persona persona : personaDAO.ObtenerPersonas())
        {
            System.out.println(persona.toString());
        }
        

        Persona nuevaPersona2 = new Persona("marcos", 25, "masculino");
        
        personaDAO.actualizarPersona("ruben", nuevaPersona2);
        
        for(Persona persona : personaDAO.ObtenerPersonas())
        {
            System.out.println(persona.toString());
        }
        
        //Elimina una persona espeficada por su nombre en la coleccion
        personaDAO.borrarPersona("jose");
        
        for(Persona persona : personaDAO.ObtenerPersonas())
        {
            System.out.println(persona.toString());
        }
    }
}

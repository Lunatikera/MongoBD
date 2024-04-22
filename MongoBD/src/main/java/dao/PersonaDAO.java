package dao;

import modelo.Persona;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

public class PersonaDAO {

    private final MongoDatabase dataBase;
    private MongoCollection<Persona> collection;

    public PersonaDAO() {
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        ConnectionString cadenaConexion = new ConnectionString("mongodb://localhost/27017");

        MongoClientSettings clientsSettings = MongoClientSettings.builder().applyConnectionString(cadenaConexion).codecRegistry(codecRegistry).build();

        MongoClient dbServer = MongoClients.create(clientsSettings);

        this.dataBase = dbServer.getDatabase("MongoBD");
        this.collection = dataBase.getCollection("Personas", Persona.class);
    }

    public List<Persona> ObtenerPersonas() {
        List<Persona> personas = new ArrayList<>();

        // Obtener todas las personas de la base de datos
        for (Document personaDoc : personaCollection.find()) {
            String id = personaDoc.getString("_id");
            String nombre = personaDoc.getString("nombre");
            int edad = personaDoc.getInteger("edad");
            String sexo = personaDoc.getString("sexo");

            personas.add(new Persona(id, nombre, edad, sexo));
        }

        return personas;
    }

    public List<Persona> obtenerPersonasEdad(int edad) {
         List<Persona> personas = new ArrayList<>();

        // Filtrar personas por edad en la base de datos
        Document query = new Document("edad", edad);
        for (Document personaDoc : personaCollection.find(query)) {
            String id = personaDoc.getString("_id");
            String nombre = personaDoc.getString("nombre");
            String sexo = personaDoc.getString("sexo");
            // Puedes agregar más campos según tu esquema de base de datos

            personas.add(new Persona(id, nombre, edad, sexo));
        }

        return personas;
    }

    public List<Persona> ordernarPersonas() {
        List<Persona> personas = new ArrayList<>();
        Bson orderBySort = orderBy(descending("edad"), descending("nombre"));
        collection.find().sort(orderBySort).into(personas);

        return personas;
    }

    public void crearPersona(Persona persona) {
        collection.insertOne(persona);
    }

    public void actualizarPersona(String nombre, Persona persona) 
    {
        Bson filter = Filters.eq("nombre", nombre);
        Bson newData = new Document("$set", new Document()
                .append("edad", persona.getEdad())
                .append("nombre", persona.getNombre())
                .append("sexo", persona.getSexo())
        );

        collection.findOneAndUpdate(filter, newData);
    }
    
    public void borrarPersona(String nombre)
    {
        Bson filter = Filters.eq("nombre", nombre);
        collection.deleteOne(filter);
    }

}

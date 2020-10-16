package data;

import com.thoughtworks.xstream.XStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database 
{
    private final String DATABASE = "db";
    private final String XML_EXT = ".xml";
    private HashMap<String, Class> classes = new HashMap<>();
    private String baseDirectory;
    private static Database database = null;
    
    private Database(String directory){
        this.baseDirectory = directory;
    }
    
    private Database(){
        String p = "";
        try {
            p = pathComponent(Database.class.getProtectionDomain().getCodeSource()
                              .getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        //ClassLoader.getSystemClassLoader().getResource(".").getPath();
        this.baseDirectory = p+File.separator+DATABASE+File.separator;
        (new File(baseDirectory)).mkdirs();
        System.out.println(this.baseDirectory);
    }
    
    private String pathComponent(String filename) {
      int i = filename.lastIndexOf(File.separator);
      return (i > -1) ? filename.substring(0, i) : filename;
    }
    
    public static Database getInstance()
    {
        if(database == null)
            database = new Database();
        
        return database;
    }
    
    public HashMap<String, Class> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, Class> classes) {
        this.classes = classes;
    }

    public void loadClasses()
    {
        try
        {
           File dir = new File(baseDirectory);
           System.out.println( dir.listFiles().toString());
            
            XStream xstream = new XStream();
            
            for (File fileEntry : dir.listFiles()) {
                
                if (!fileEntry.isDirectory()
                     && fileEntry.getCanonicalPath().endsWith(XML_EXT)) {
                    
                    FileInputStream input = new FileInputStream(fileEntry);        
                    Class _class = (Class)xstream.fromXML(input);
                    classes.put(_class.getName(), _class);
                } 
            }
            
        }catch(IOException ex){
            System.err.println(ex.getMessage());
        }
    }
    
    public boolean saveClass(Class newClass)
    {
        String fileName = baseDirectory + File.separator 
                          + newClass.getName() + XML_EXT;
        
        try
        {
            newClass.sortStudents();
            XStream xstream = new XStream();
            String xml = xstream.toXML(newClass);
            FileWriter fw = new FileWriter(new File(fileName));
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(xml);
            bw.close();
            
            classes.put(newClass.getName(), newClass);
            return true; 
            
        }catch(IOException ex){
            System.err.println(ex.getMessage());
            return false;
        }
    }
    
    public void updateClass(String name)
    {
        if(classes.containsKey(name))
        {
            saveClass(classes.get(name));
        }
    }
    
    public boolean removeClass(String name)
    {
        if(classes.containsKey(name))
        {
            data.Class currentClasss = classes.get(name);
            String fileName = baseDirectory + File.separator 
                          + currentClasss.getName() + XML_EXT;
            
            try
            {
                classes.remove(name);
                File file = new File(fileName);
                file.delete();
                return true;
                
            }catch(Exception ex){
                System.err.println(ex.getMessage());
                return false;
            }
            
        }
        
        return false;
    }
}

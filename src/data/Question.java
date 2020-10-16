package data;

import java.io.File;

public class Question implements Comparable<Question>{
    
    private Integer id;
    private String file;
    
    public Question(Integer id, String file)
    {
        this.id = id;
        this.file = file;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
    
    @Override
    public String toString()
    {
        String[] splitPath = file.split(File.separator);
        String string = "";
        if(splitPath.length > 2)
        {
            int last = splitPath.length-1;
            int beforeLast = splitPath.length-2;
            string = splitPath[beforeLast]
                          +File.separator
                          +splitPath[last];
        }
        else
        {
            string = file;
        }
        
        return string;
    }

    @Override
    public int compareTo(Question t) {
        return this.file.compareTo(t.file);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author hermano
 */
public class QuestionParser {
    
    private final String  DOUBLE_SLASH = "\\\\";
    private final String  START_QUESTION = "\\\\question";
    private final String  _START_QUESTION = "\\question";
    private final String  START_CHOICES = "\\\\begin\\{choices\\}";
    private final String  _START_CHOICES = "\\begin{choices}";
    private final String  END_CHOICES = "\\\\end\\{choices\\}";
    private final String  _END_CHOICES = "\\end{choices}";
    private final String  CHOICE_TAG = " \\\\choice";
    private final String  LINE_BREAK = "\n";
    private final String  TAB = "\t";
    
   
    
    private String readFile(String file) throws IOException {
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } 
        finally {
            reader.close();
        }
    }
    
    private Integer getAnswer(String fileName)
    {
        Integer len = fileName.length();
        String auxFileName = fileName.toLowerCase();
        Character r = auxFileName.charAt(len-5);
        
        switch(r)
        {
            case 'a' : return 1;
            case 'b' : return 2;
            case 'c' : return 3;
            case 'd' : return 4;
            case 'e' : return 5;
            default : return -1;
        }
        
    }
    
    public SelectedQuestion parser(Question question)
    {
        SelectedQuestion selectedQuestion = null;
        try
        {
            String fileContent = readFile(question.getFile());
            String alternatives = "";
            String questionString = "";
            
            Pattern findQuestion = Pattern.compile(START_QUESTION
                                        +"(.+?)"+START_CHOICES, Pattern.DOTALL);
            Matcher matcher = findQuestion.matcher(fileContent);
            if (matcher.find()) {
                questionString += matcher.group();
            }
            else{
                return selectedQuestion;
            }
            
            Pattern findAlternatives = Pattern.compile(START_CHOICES
                                          +"(.+?)"+END_CHOICES, Pattern.DOTALL);
            matcher = findAlternatives.matcher(fileContent);
            if (matcher.find()) {
                alternatives += matcher.group();
            }
            else{
                return selectedQuestion;
            }
                
            questionString = questionString.replace(_START_QUESTION, "");
            questionString = questionString.replace(_START_CHOICES, "");
            questionString = questionString.replace(LINE_BREAK, "  ");
            questionString = questionString.replace(DOUBLE_SLASH, "  ");
            questionString = questionString.trim();
                    
            alternatives = alternatives.replace(_START_CHOICES, "");
            alternatives = alternatives.replace(_END_CHOICES, "");
            alternatives = alternatives.replace(LINE_BREAK, "  ");
            alternatives = alternatives.replace(TAB, "  ");
            alternatives = alternatives.replace(DOUBLE_SLASH, "  ");
            
            String[] splitAlternatives = alternatives.split(CHOICE_TAG);
            ArrayList<String> sAlternatives = new ArrayList<>();
            
            for(String s : splitAlternatives)
            {
               s = s.trim();
               if(!s.isEmpty())
                    sAlternatives.add(s);
            }
            
            Integer answer = getAnswer(question.getFile());
            
            if(answer != -1)
            {
                selectedQuestion = new SelectedQuestion();
                selectedQuestion.setQuestion(questionString);
                selectedQuestion.setAlternatives(sAlternatives);
                selectedQuestion.setAnswer(answer);
                selectedQuestion.setId(question.getId());
                selectedQuestion.setFile(question.getFile());
            }
            
        }
        catch(IOException ex)
        {
            System.err.println(ex.getMessage());
        }
        
        return selectedQuestion;
        
    }
}

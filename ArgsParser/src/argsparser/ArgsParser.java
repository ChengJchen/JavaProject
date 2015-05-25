/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package argsparser;

import java.util.HashMap;

/**
 *
 * @author ccj
 */
public class ArgsParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
    }
    
    String errInfo="[Error]:\n";
    String helpInfo="[Usage]:\n";
    HashMap<String,Argument> argsStore=new HashMap<>();
    

    public String getValue(String name){
        return argsStore.get(name).getValue();
    }
    public void setValue(String name,String value){
        argsStore.get(name).setValue(value);
    }
    
    public void addArgs(String name,String description){
        Argument argN=new Argument();
        argN.setName(name);
        argN.setDescription(description);
        argsStore.put(name, argN);
    }
    public void addArgs(String name,String descritption,String value){
        Argument argN=new Argument();
        argN.setName(name);
        argN.setValue(value);
        argN.setDescription(descritption);
        argsStore.put(name, argN);
    }
    
    
    
    public String getErrInfo(){
        return errInfo;
    }
    public String getHelpinfo(){
        
        for (String name:argsStore.keySet()){
            if(!argsStore.get(name).isSeted()){
                helpInfo=helpInfo+"\t"+name+"\t"+argsStore.get(name).getDescription()+"[Required]\n";
            }else{
                helpInfo=helpInfo+"\t"+name+"\t"+argsStore.get(name).getDescription()+"[Default:"+argsStore.get(name).getValue()+"]\n";
            }
        }
        
        return helpInfo;
    }   
    
    public boolean parseArgs(String[] args){
        int agrsLen=args.length;
        if(agrsLen%2!=0){
            errInfo+="Please provided arguments in correct format";
            return false;
        }else{
            for(int index=0;index<agrsLen;index+=2){
                String name=args[index];
                String value=args[index+1];
                if (argsStore.containsKey(name)){
                    this.setValue(name, value);
                }else{
                    errInfo+=" invalid aruguments "+name;
                    return false;
                }
                
            }
        }
        // if all options and arguments is setted, then test
        for (String name:argsStore.keySet()){
            if(!argsStore.get(name).isSeted()){
                errInfo=errInfo+"\tArgument ["+name+"] Should be Setted";
                return false;
            }
        }
        
        // if no problem were found, then return ture
        return true;
    }
    
    public void printAllArgs(){
        System.out.println("[All Args Info]:");
        for (String name:argsStore.keySet()){
            System.out.println("\t"+name+"\t"+argsStore.get(name).getValue());
        }
    }
    
}

class Argument {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        // if value is given, then isSet is ture;
        isSet=true;
        this.value = value;
    }

    public boolean isSeted() {
        return isSet;
    }

    public void setIsSeted(boolean isSet) {
        this.isSet = isSet;
    }
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public boolean getIsRequired() {
//        return isRequired;
//    }
//
//    public void setIsRequired(boolean isRequired) {
//        this.isRequired = isRequired;
//    }
    
    private String name;
    private String value;
    private String description;
//    boolean isRequired;
    // if value is given than isSet is ture;
    private boolean isSet;
    public Argument(){
        name="";
        value="[Unsetted]";
        description="";
        isSet=false;
    }
    
}

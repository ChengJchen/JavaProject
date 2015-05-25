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
public class testArgsParser {
    public static void main (String[] args){
        
        args=new String[]{"--in","ooo"};
        
        ArgsParser ap = new ArgsParser();
        ap.addArgs("--in", "provided the inputfile");
        ap.addArgs("--out","provided the outputfile","pp");
        ap.addArgs("--cutoff", "provided the cutoff value");
        if(!ap.parseArgs(args)){
            System.out.println(ap.getErrInfo());
            System.out.println("[Example]:\n\tjava -jar this.jar --in infile --cutoff number");
            System.out.println(ap.getHelpinfo());
        };
        ap.printAllArgs();
    }
}
